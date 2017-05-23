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
var end = 1; // including

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
console.log("Activity count, avg = ", activityCntAvg);
console.log("Service count, avg = ", servicesCntAvg);
console.log("Provider count, avg = ", providersCntAvg);
console.log("Receiver count, avg = ", receiverCntAvg);
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

    // console.log(obj.manifest.activities);
    // console.log(obj.manifest.activities.length);
    activityCntAvg += (obj.manifest.activities.length - activityCntAvg) / appCnt;

    // console.log(obj.manifest.services.length);
    servicesCntAvg += (obj.manifest.services.length - servicesCntAvg) / appCnt;

    providersCntAvg += (obj.manifest.providers.length - providersCntAvg) / appCnt;

    receiverCntAvg += (obj.manifest.receiver.length - receiverCntAvg) / appCnt;

    if (activityDistribute[obj.manifest.activities.length] == undefined) {
        activityDistribute[obj.manifest.activities.length] = 1;
    } else {
        activityDistribute[obj.manifest.activities.length]++;
    }

    // console.log(obj.manifest.features);
    if (obj.manifest.features.some((f) => {return f.includes("sensor")})) {
        featuresHasSensorCnt++;
    }

    console.log(obj.callsite);
    if (obj.callsite.length > 0) {
        hasCalledTargetFunCnt++;

        if (obj.callsite.some((c) => {return c.sensorType != undefined})) {
            canGetSensorTypeCnt++;
        }

        for (let callsite of obj.callsite) {
            if (callsite.sensorType != undefined) {
                if (sensorTypeCnt[callsite.sensorType] == undefined) {
                    sensorTypeCnt[callsite.sensorType] = 0;
                }
                sensorTypeCnt[callsite.sensorType]++;
            }
        }
    }

    // console.log(obj.callsite);
    // console.log(obj);
}

