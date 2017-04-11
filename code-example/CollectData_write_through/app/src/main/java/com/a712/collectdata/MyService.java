package com.a712.collectdata;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service implements SensorEventListener {
    File accelerometerFile;
    PrintWriter accelerometerPrintWriter;
    File gyroscopeFile;
    PrintWriter gyroscopePrintWriter;
    String deviceID;
    Long recording_start_time;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String typeString = intent.getStringExtra("type");
        deviceID = intent.getStringExtra("deviceID");
        startRecording(typeString);
        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        String dataString = sensorDataToString(event.timestamp, event.values[0], event.values[1], event.values[2]);
        if (event.sensor == accelerometer) {
            accelerometerPrintWriter.println(dataString);
        } else if (event.sensor == gyroscope) {
            gyroscopePrintWriter.println(dataString);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }

    public void stopRecording() {
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, gyroscope);

        accelerometerPrintWriter.flush();
        accelerometerPrintWriter.close();
//        accelerometerPrintWriter = null;
//        accelerometerFile = null;
        gyroscopePrintWriter.flush();
        gyroscopePrintWriter.close();
//        gyroscopePrintWriter = null;
//        gyroscopeFile = null;

        Toast.makeText(this, "Saved to file!", Toast.LENGTH_SHORT).show();


    }

    private String sensorDataToString(long t, float x, float y, float z) {
        return String.format("%d, %f, %f, %f", t, x, y, z);
    }

    public void startRecording(String engStr) {
        File rootDir = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(rootDir.getAbsolutePath() + "/CollectedSensorData");
        try {
            dir.mkdir();
            Date startTime = new java.util.Date();
            String fileName = String.format("%s-A-%s-%s.txt", engStr, (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(startTime), deviceID);
            accelerometerFile = new File(dir, fileName);
            accelerometerPrintWriter = new PrintWriter(accelerometerFile);

            fileName = String.format("%s-G-%s-%s.txt", engStr, (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(startTime), deviceID);
            gyroscopeFile = new File(dir, fileName);
            gyroscopePrintWriter = new PrintWriter(gyroscopeFile);
        } catch (IOException e) {
            Toast.makeText(this, "Can not create file!", Toast.LENGTH_LONG);
            return;
        }


        Toast.makeText(this, "Now start recording!", Toast.LENGTH_LONG).show();

        int frequency = 100;//Hz
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, accelerometer, (int) (1e6 / frequency));
        sensorManager.registerListener(this, gyroscope, (int) (1e6 / frequency));
        recording_start_time = System.currentTimeMillis();

    }
}
