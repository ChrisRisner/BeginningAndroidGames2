package com.badlogic.androidgames;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.FROYO)
public class CompassTest extends Activity implements SensorEventListener {
    TextView textView;
    StringBuilder builder = new StringBuilder();
    Sensor accelerometer;
    Sensor magnetometer;
    SensorManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        setContentView(textView);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0) {
            textView.setText("No accelerometer installed");
        } else {
            accelerometer = manager.getSensorList(
                    Sensor.TYPE_ACCELEROMETER).get(0);
            magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//            if (!manager.registerListener(this, accelerometer,
//                    SensorManager.SENSOR_DELAY_GAME)) {
//                textView.setText("Couldn't register sensor listener");
//            }
        }
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }
    
    
    
    public void onResume() {
    		super.onResume();
    		if (!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME))
    			textView.setText("Couldn't register sensor listener accelerometer");
    		if (!manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME))
    			textView.setText("Couldn't register sensor listener magneometer");
    }
    
    @Override
    protected void onPause() {
    	
    		super.onPause();
    		manager.unregisterListener(this);
    }
    
	
    float[] mGravity;
    float[] mGeomagnetic;
    float azimut;
    
	public void onSensorChanged(SensorEvent event) {
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		      mGravity = event.values;
		    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		      mGeomagnetic = event.values;
		    if (mGravity != null && mGeomagnetic != null) {
		      float R[] = new float[9];
		      float I[] = new float[9];
		      boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
		      if (success) {
		        float orientation[] = new float[3];
		        SensorManager.getOrientation(R, orientation);
		        azimut = orientation[0]; // orientation contains: azimut, pitch and roll
		        
		        float[] values = new float[3];
		        values[0] = orientation[0] * -1 * 360 / (2 * 3.14159f);
		        values[1] = orientation[1] * -1 * 360 / (2 * 3.14159f);
		        values[2] = orientation[2] * -1 * 360 / (2 * 3.14159f);
		        
		        builder.setLength(0);
		        builder.append("azimut: ");
		        builder.append(values[0]);
		        builder.append(", pitch: ");
		        builder.append(values[1]);
		        builder.append(", roll: ");
		        builder.append(values[2]);
		        textView.setText(builder.toString());
		      }
		    }
		    
		
//		final int[] as = ACCELEROMETER_AXIS_SWAP[screenRotation]; 
//		float screenX = (float)as[0] * event.values[as[2]];
//		float screenY = (float)as[1] * event.values[as[3]];
//		float screenZ = event.values[2];
		    // use screenX, screenY, and screenZ as your accelerometer values now!
		
//		float[] rotation = new float[9];
//		float[] values = new float[3];
//		SensorManager.getOrientation(rotation, values);
//		float screenX = values[0];
//		float screenY = values[1];
//		float screenZ = values[2];
//		
//		builder.setLength(0);
//        builder.append("x: ");
//        builder.append(screenX);
//        builder.append(", y: ");
//        builder.append(screenY);
//        builder.append(", z: ");
//        builder.append(screenZ);
//        textView.setText(builder.toString());
	}
} 
