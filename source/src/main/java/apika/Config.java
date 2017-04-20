package apika;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by guoxing on 19/4/2017.
 */


public class Config {
    public static String apkName;



//    static ArrayList<String> methodsToCollect = new ArrayList<>();
    /**
     * map signature to sensor type index in argument list
     */
    static HashSet<String> sensorManagerListener = new HashSet<>();


    static HashMap<String, Integer> sensorMangerGetSensor = new HashMap<>();


    //by defalut we collect SensorManager.(un)registerListener

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



    }
}


