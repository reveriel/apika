var fs = require('fs');


var fileName = "output/src.test.resources.app-debug.apk.json";


var obj;

fs.readFile(fileName, 'utf8', (err, data) => {
    if (err) throw err;
    obj = JSON.parse(data);
});


console.log(obj);
console.log(obj.Services);


