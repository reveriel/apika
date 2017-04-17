# apk analysis

## prepare
copy `config.properties.template` to `config.properties`

## usage

(if `gradle` is not installed, use `./gradlew`)

run test :
`$ gradle test`

run on an apk:
`$ gradle jar`
`$ ./apika.sh  <apk-file>`

output:  (directory `output` needs to be created manually)
`output/<apk-name>.json`

pretty print output: (need tool `json_pp`)
`cat output/<apk-name>.json | json_pp`
 







