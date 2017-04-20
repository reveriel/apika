const fs = require('fs');


// const fileName = "output/src.test.resources.app-debug.apk.json";

const dirName = "output/";
fs.readdir(dirName, (err, files) => {
    files.forEach(parseOneFile);
});



var appCnt = 0;
function parseOneFile(fileName) {
    appCnt++;
    var obj = JSON.parse(fs.readFileSync(dirName + fileName, 'utf8'));
    console.log(obj.features);
    // console.log(obj);
}


