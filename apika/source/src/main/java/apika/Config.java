package apika;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by guoxing on 19/4/2017.
 */


public class Config {


    /**
     * apk name, including path
     */
    static String apkName;
    static String jsonFileName;
    static String outputDir;

//    static ArrayList<String> methodsToCollect = new ArrayList<>();
    /**
     * map signature to sensor type index in argument list
     */
    public static HashSet<String> sensorManagerListener = new HashSet<>();
    public static HashMap<String, Integer> sensorMangerGetSensor = new HashMap<>();

    //by default we collect SensorManager.(un)registerListener

    static {
        sensorManagerListener.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorListener,int)>");
        sensorManagerListener.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorListener,int,int)>");
        sensorManagerListener.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorListener)>");
        sensorManagerListener.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorListener,int)>");
        sensorManagerListener.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorEventListener,android.hardware.Sensor)>");
        sensorManagerListener.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorEventListener)>");
        sensorManagerListener.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int)>");
        sensorManagerListener.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,int)>");
        sensorManagerListener.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,android.os.Handler)>");
        sensorManagerListener.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,int,android.os.Handler)>");
        // dynamic
        sensorManagerListener.add("<android.hardware.SensorManager: void registerDynamicSensorCallback(android.hardware.SensorManager$DynamicSensorCallback)>");
        sensorManagerListener.add("<android.hardware.SensorManager: void registerDynamicSensorCallback(android.hardware.SensorManager$DynamicSensorCallback,android.os.Handler)>");
        sensorManagerListener.add("<android.hardware.SensorManager: void unregisterDynamicSensorCallback(android.hardware.SensorManager$DynamicSensorCallback)>");


        sensorMangerGetSensor.put("<android.hardware.SensorManager: android.hardware.Sensor getDefaultSensor(int)>", 0);
        sensorMangerGetSensor.put("<android.hardware.SensorManager: android.hardware.Sensor getDefaultSensor(int,boolean)>", 0);
        sensorMangerGetSensor.put("<android.hardware.SensorManager: java.util.List getSensorList(int)>", 0);
        // dynamic
        sensorMangerGetSensor.put("<android.hardware.SensorManager: java.util.List getDynamicSensorList(int)>", 0);
    }



    public static String getApkName() {
        return apkName;
    }

    public static void setApkName(String apkName) {
        Config.apkName = apkName;
    }

    public static String getOutputDir() {
        return outputDir;
    }

    public static void setOutputDir(String outputDir) {
        Config.outputDir = outputDir;
    }

    /**
     * also create the outputDir if not exists
     * @return the output json file name
     */
    public static String getJsonFileName() throws IOException {
        if (jsonFileName != null) {
            return jsonFileName;
        }

        // replace '/' in path name, and remove leading '.'
        String apk = apkName.replace(File.separator.charAt(0),'.').replaceAll("^\\.+", "");

        File outDir = new File(outputDir);
        if (outDir.isFile()) {
            throw new IOException("output directory: " + outputDir + "is a file");
        } else if (!outDir.exists()) {
            boolean created = outDir.mkdirs();
            if (!created) {
                throw new IOException("Cannot create directory :" + outDir);
            }
        }

        jsonFileName = outputDir + File.separator + apk + ".json";
        return jsonFileName;
    }


    public static void reset() {
        apkName = null;
        jsonFileName = null;
        outputDir = null;
    }

}


