"use strict";

const fs = require('fs');

// const fileName = "output/src.test.resources.app-debug.apk.json";

// const dirName = "output/";


const rawDataDir = "/mnt/ta/apks/elite/output/part";
var dirName = "/mnt/ta/apks/elite/output/part1/";


var appCnt = 0;
var timeAvg = 0;
var activityCntAvg = 0;
var servicesCntAvg = 0;
var providersCntAvg = 0;
var receiverCntAvg = 0;
var featuresHasSensorCnt = 0;


var activityDistribute = []; // number of activity : number of apk 

var hasCalledTargetFunCnt = 0;

var sensorTypeCnt = {};
var canGetSensorTypeCnt = 0;

var start = 1;
var end = 10; // including

var useStartActCnt = 0;
var useStartActApkCnt = 0;

var callSiteClassDic = {};
var registerClassDic = {};
var unregisterClassDic = {};

for (let i = start; i <= end; i++ ) {
    dirName = rawDataDir + i + "/";
    console.log(dirName);

    var files =  fs.readdirSync(dirName);

    for (let f of files) {
        parseOneFile(f, dirName);
    }
}

console.log("apk count = " + appCnt);
console.log("time per apk, avg = " + timeAvg + "ms");
console.log("Activity count, avg = ", activityCntAvg,
            Math.round(appCnt * activityCntAvg) ,"/", appCnt);
console.log("Service count, avg = ", servicesCntAvg,
            Math.round(appCnt * servicesCntAvg) ,"/", appCnt);
console.log("Provider count, avg = ", providersCntAvg,
            Math.round(appCnt * providersCntAvg) ,"/", appCnt);
console.log("Receiver count, avg = ", receiverCntAvg,
            Math.round(appCnt * receiverCntAvg) ,"/", appCnt);
console.log("features has sensor in manifest, percent = " + featuresHasSensorCnt / appCnt +
            "(" + featuresHasSensorCnt + "/" +  appCnt + ")");



for (var i = 0; i < activityDistribute.length; i++) {
    if (activityDistribute[i] == undefined) {
        activityDistribute[i] = 0;
    }
    activityDistribute[i] /= appCnt;
}
console.log("activity distribute");
console.log(activityDistribute);
obj2csv("activityDist.csv", activityDistribute);

console.log("has Called Target function, percent = " + hasCalledTargetFunCnt / appCnt +
            "(" + hasCalledTargetFunCnt + "/" +  appCnt + ")");

console.log("can get sensor type from argument, percent = " + canGetSensorTypeCnt / appCnt +
            "(" + canGetSensorTypeCnt + "/" +  appCnt + ")");
console.log("sensor types:");
console.log(sensorTypeCnt);


console.log(" callSiteClassDic  = ");
console.log(callSiteClassDic);

console.log(" registerClassDic  = ");
console.log(registerClassDic);

console.log(" unregisterClassDic  = ");
console.log(unregisterClassDic);

console.log("useStartActCnt = " + useStartActCnt );
console.log("useStartActApkCnt = " + useStartActApkCnt );


function obj2csv(fileName, obj) {
    var output = "letter,  frequency\n"; // : string
    for (var i = 0; i < obj.length; i++) {
        output += i + "," + (obj[i] == undefined ? 0 : obj[i]) + "\n";
    }
    fs.writeFileSync(fileName, output);
    console.log("file written");
}

function parseOneFile(fileName, dir) {
    var obj = JSON.parse(fs.readFileSync(dir + fileName, 'utf8'));
    appCnt++;
    // console.log(obj.sootTime);
    timeAvg += (obj.sootTime - timeAvg) / appCnt;
    forAvg(obj, appCnt);

    if (activityDistribute[obj.manifest.activities.length] == undefined) {
        activityDistribute[obj.manifest.activities.length] = 1;
    } else {
        activityDistribute[obj.manifest.activities.length]++;
    }

    // console.log(obj.manifest.features);
    if (obj.manifest.features.some((f) => {return f.includes("sensor")})) {
        featuresHasSensorCnt++;
    }
    forCallSites(obj);
}

function forAvg(obj, appCnt) {
    activityCntAvg +=
        (obj.manifest.activities.length - activityCntAvg) / appCnt;
    servicesCntAvg +=
        (obj.manifest.services.length - servicesCntAvg) / appCnt;
    providersCntAvg +=
        (obj.manifest.providers.length - providersCntAvg) / appCnt;
    receiverCntAvg +=
        (obj.manifest.receiver.length - receiverCntAvg) / appCnt;
}


function forCallSites(obj) {
    if (!(obj.callSites && obj.callSites.length > 0))
        return;

    hasCalledTargetFunCnt++;
    if (obj.callSites.some(
        (c) => {return c.sensorType != undefined})) {
        canGetSensorTypeCnt++;
    }
    // count sensor type
    for (let callSite of obj.callSites) {
        if (callSite.sensorType != undefined) {
            if (sensorTypeCnt[callSite.sensorType] == undefined) {
                sensorTypeCnt[callSite.sensorType] = 0;
            }
            sensorTypeCnt[callSite.sensorType]++;
        }

        // the first superclass started with android
        let fsaClass = "others";
        for (let superclass of callSite.superClasses) {
            if (superclass.startsWith("android")) {
                fsaClass = superclass;
                break;
            }
        }

        if (callSite.contextClass.includes("unity3d")) {
            continue;
        }

        if (callSiteClassDic[fsaClass] == undefined) {
            callSiteClassDic[fsaClass]=1;
        } else {
            callSiteClassDic[fsaClass]++;
        }

        reg_unreg(callSite, fsaClass);

        if (callSite.func.startsWith("<android.hardware.SensorManager: android.hardware.Sensor getDefaultSensor(int,boolean)>")
            || callSite.func.includes("getDefaultSensor")
            || callSite.func.includes("isWakeUpSensor")) {
            // console.log(callSite.func);
        }
    }

    var call_start_act = false;
    for (let callSite of obj.callSites) {
        if (callSite.func.includes("startActivity")) {
            // console.log(callSite);
            useStartActCnt++;
            call_start_act = true;
        }
    }
    if (call_start_act == true) {
        useStartActApkCnt++;
    }
}


function reg_unreg(callSite, fsaClass) {
    if (callSite.func.includes(" register")) {
        registerClassDic[fsaClass] = (registerClassDic[fsaClass] == undefined) ?
            1 : registerClassDic[fsaClass]+1;
    }

    if (callSite.func.includes("unregister")) {
        unregisterClassDic[fsaClass] = (unregisterClassDic[fsaClass] == undefined) ?
            1 : unregisterClassDic[fsaClass]+1;
    }
}
