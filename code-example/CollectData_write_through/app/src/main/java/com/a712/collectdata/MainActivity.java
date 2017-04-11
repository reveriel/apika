package com.a712.collectdata;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100;
    private static final int READ_PHONE_STATE_REQUEST_CODE = 101;
    private static final int RANDOM_REQUEST_CODE = 102;
    private static final String TAG = "CollectData";
    ImageButton startButton;
    ImageButton stopButton;
    TextView time_lasted;
    String DEVICE_ID;
    CountingThread ct;
    int current_time;
    Handler handler;
    private PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ArrayList<String> notAcquiredPermissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            notAcquiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            notAcquiredPermissions.add(Manifest.permission.READ_PHONE_STATE);
        } else {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            DEVICE_ID = tm.getDeviceId();
        }
        if (!notAcquiredPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, notAcquiredPermissions.toArray(new String[notAcquiredPermissions.size()]), RANDOM_REQUEST_CODE);
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");

        PowerManager.WakeLock wl2 = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "second wake lock");
        wl2.acquire();


        wl2.release();


        startButton = (ImageButton) findViewById(R.id.startButton);
        stopButton = (ImageButton) findViewById(R.id.stopButton);
        time_lasted = (TextView) findViewById(R.id.time_lasted);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        handler = new Handler();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Log.i(TAG, permissions[i] + " permission denied");
                System.exit(0);
            } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, permissions[i] + " permission granted");
            }
            if (permissions[i].equals(Manifest.permission.READ_PHONE_STATE)) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    DEVICE_ID = tm.getDeviceId();
                }
            }
        }
    }

    private void onReallyStart(String engStr) {
        wl.acquire();

        Intent intent = new Intent(MainActivity.this, MyService.class);
        intent.putExtra("type", engStr);
        intent.putExtra("deviceID", DEVICE_ID);
        startService(intent);

        ct = new CountingThread();
        new Thread(ct).start();

        startButton.setEnabled(false);
        stopButton.setEnabled(true);

    }

    public void onStartButtonClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("选择你现在需要的模式");

        /**
         * 设置内容区域为简单列表项
         */
        final String[] Items = {"平放设备", "日常走路", "随意数据"};
        final String[] engItems = {"Flat", "Walk", "Random"};
        builder.setItems(Items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "You clicked " + Items[i], Toast.LENGTH_SHORT).show();
                onReallyStart(engItems[i]);
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void onStopButtonClick(View view) {
        ct.stopThread();

        Intent intent = new Intent(MainActivity.this, MyService.class);
        stopService(intent);

        Toast.makeText(this, "Saved to file!", Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time_lasted.setText(time_lasted.getText() + "s has been saved to file");
            }
        }, 500);


        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        wl.release();


    }

    private final class CountingThread implements Runnable {
        boolean vRun = true;

        void stopThread() {
            vRun = false;
        }

        public void run() {
            current_time = 0;
            while (vRun) {
                current_time++;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        time_lasted.setText(Integer.toString(current_time));
                    }
                }, 0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
