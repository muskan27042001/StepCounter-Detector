package com.example.stepdetector;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
private TextView textViewStepCounter,textViewStepDetector;
private SensorManager sensorManager;
private Sensor mStepCounter;  // step counter object
private Sensor mStepDetector;  // step detector object
private boolean isCounterSensorPresent;
private boolean isDetectorSensorPresent;
int stepCount=0;
int stepDetect=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)== PackageManager.PERMISSION_DENIED)
        {
            // ask for permision
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION},0);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textViewStepCounter=findViewById(R.id.textViewStepCounter);
        textViewStepDetector=findViewById(R.id.textViewStepDetector);

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);


        // checking if step counter sensor is available or not
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            mStepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent=true;
        }
        else
        {
            textViewStepCounter.setText("Counter sensor is not present");
            isCounterSensorPresent=false;
        }

        // checking if step detector sensor is available or not
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null)
        {
            mStepDetector=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            isDetectorSensorPresent=true;
        }
        else
        {
            textViewStepDetector.setText("Detector sensor is not present");
            isDetectorSensorPresent=false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor==mStepCounter)
        {
            stepCount= (int) event.values[0];
            textViewStepCounter.setText(String.valueOf(stepCount));
        }
        else if(event.sensor==mStepDetector)
        {
            stepDetect= (int) (stepDetect+event.values[0]);
            textViewStepDetector.setText(String.valueOf(stepDetect));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // register sensor
    @Override
    protected void onResume() {
        super.onResume();
        // for step counter sensor
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            sensorManager.registerListener(this,mStepCounter,SensorManager.SENSOR_DELAY_NORMAL);
        }

        // for step detector sensor
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null)
        {
            sensorManager.registerListener(this,mStepDetector,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // unregister sensor
    @Override
    protected void onPause() {
        super.onPause();
        // for step counter sensor
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            sensorManager.unregisterListener(this,mStepCounter);
        }

        // for step detector sensor
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null)
        {
            sensorManager.unregisterListener(this,mStepDetector);
        }
    }
}