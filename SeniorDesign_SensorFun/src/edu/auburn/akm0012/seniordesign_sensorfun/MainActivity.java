package edu.auburn.akm0012.seniordesign_sensorfun;


import orbotix.robot.base.ConfigureLocatorCommand;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import orbotix.robot.sensor.AccelerometerData;
import orbotix.robot.sensor.AttitudeSensor;
import orbotix.robot.sensor.BackEMFData;
import orbotix.robot.sensor.DeviceSensorsData;
import orbotix.robot.sensor.LocatorData;
import orbotix.sphero.ConnectionListener;
import orbotix.sphero.LocatorListener;
import orbotix.sphero.SensorFlag;
import orbotix.sphero.SensorListener;
import orbotix.sphero.Sphero;
import orbotix.view.connection.SpheroConnectionView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	// The connection view
	private SpheroConnectionView mSpheroConnectionView;

	// The Shpero
	private Sphero mSphero = null; 
	
	// Called when sensor data is changed 
	private final SensorListener mSensorListener = new SensorListener() {
        @Override
        public void sensorUpdated(DeviceSensorsData datum) {
            //Show attitude data
//            AttitudeSensor attitude = datum.getAttitudeData();
//            if (attitude != null) {
////                mImuView.setPitch(String.format("%+3d", attitude.pitch));
////                mImuView.setRoll(String.format("%+3d", attitude.roll));
////                mImuView.setYaw(String.format("%+3d", attitude.yaw));
//            	((TextView) findViewById(R.id.edit_pitch)).setText(String.format("%+3d", attitude.pitch));
//            	((TextView) findViewById(R.id.edit_roll)).setText(String.format("%+3d", attitude.roll));
//            	((TextView) findViewById(R.id.edit_yaw)).setText(String.format("%+3d", attitude.yaw));
//
//            }

            //Show accelerometer data
//            AccelerometerData accel = datum.getAccelerometerData();
//            if (accel != null) {
//                mAccelerometerFilteredView.setX(String.format("%+.4f", accel.getFilteredAcceleration().x));
//                mAccelerometerFilteredView.setY(String.format("%+.4f", accel.getFilteredAcceleration().y));
//                mAccelerometerFilteredView.setZ(String.format("%+.4f", accel.getFilteredAcceleration().z));
//            	((TextView) findViewById(R.id.edit_x_accel)).setText("" + accel.getFilteredAcceleration().x);
//            	((TextView) findViewById(R.id.edit_y_accel)).setText("" + accel.getFilteredAcceleration().y);
//            	((TextView) findViewById(R.id.edit_z_accel)).setText("" + accel.getFilteredAcceleration().z);
//            }
            
//            BackEMFData emf = datum.getBackEMFData();
//            if (emf != null) {
            	
//            	Log.i("Sphero_emf", "Left motor value FILTERED: " + emf.getEMFFiltered().leftMotorValue);
//            	Log.i("Sphero_emf", "Right motor value FILTERED: " + emf.getEMFFiltered().rightMotorValue);
//            	Log.i("Sphero_emf", "Filtered toString: " + emf.getEMFFiltered().toString());
            	
//            	((TextView) findViewById(R.id.edit_filter_left_motor)).setText("" + emf.getEMFFiltered().leftMotorValue);
//            	((TextView) findViewById(R.id.edit_filter_right_motor)).setText("" + emf.getEMFFiltered().leftMotorValue);
//            	((TextView) findViewById(R.id.edit_filter_toString_motor)).setText("" + emf.getEMFFiltered().toString());
            	
//            	Log.i("Sphero_emf", "Left motor value RAW: " + emf.getEMFRaw().leftMotorValue);
//            	Log.i("Sphero_emf", "Right motor value RAW: " + emf.getEMFRaw().rightMotorValue);           	
//            	Log.i("Sphero_emf", "Filtered toString: " + emf.getEMFRaw().toString());
//            	
//            	((TextView) findViewById(R.id.edit_raw_left_motor)).setText("" + emf.getEMFRaw().leftMotorValue);
//            	((TextView) findViewById(R.id.edit_raw_right_motor)).setText("" + emf.getEMFRaw().leftMotorValue);
//            	((TextView) findViewById(R.id.edit_raw_toString_motor)).setText("" + emf.getEMFRaw().toString());
            	
//            }
        }
    };
	
	// Called when location has changed 
	 private LocatorListener mLocatorListener = new LocatorListener() {
	        @Override
	        public void onLocatorChanged(LocatorData locatorData) {
	            Log.i("Sphero_Locate", locatorData.toString());
	            if (locatorData != null) {
	                ((TextView) findViewById(R.id.edit_x)).setText(locatorData.getPositionX() + " cm");
	                ((TextView) findViewById(R.id.edit_y)).setText(locatorData.getPositionY() + " cm");
	                ((TextView) findViewById(R.id.edit_x_vel)).setText(locatorData.getVelocityX() + " cm/s");
	                ((TextView) findViewById(R.id.edit_y_vel)).setText(locatorData.getVelocityY() + " cm/s");
	            }
	        }
	    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Find Sphero Connection View from layout file
		mSpheroConnectionView = (SpheroConnectionView) findViewById(R.id.sphero_connection_view);

		// This event listener will notify you when these events occur, it is up
		// to you what you want to do during them
		ConnectionListener mConnectionListener = new ConnectionListener() {
			
			@Override
			// The method to run when a Sphero is connected
			public void onConnected(Robot sphero) {
				// Hides the Sphero Connection View
				mSpheroConnectionView.setVisibility(View.INVISIBLE);
				
				
				// Cache the Sphero so we can send commands to it later
				mSphero = (Sphero) sphero;
				
				// You can add commands to set up the ball here, these are some example
				// Set the back LED brightness to full
				mSphero.setBackLEDBrightness(1.0f);
				// Set the main LED color to green at full brightness
				mSphero.setColor(0, 255, 0);

				// End examples
				
				// Set the locaton listener and the rate it will sample the sphero
				mSphero.getSensorControl().addLocatorListener(mLocatorListener);
				//TODO: May have to play around with the EMF flags
//				
				mSphero.getSensorControl().addSensorListener(mSensorListener, SensorFlag.MOTOR_BACKEMF_RAW, SensorFlag.MOTOR_BACKEMF_NORMALIZED);
//				mSphero.getSensorControl().addSensorListener(mSensorListener, 
//						SensorFlag.ACCELEROMETER_NORMALIZED, 
//						SensorFlag.ATTITUDE,
//						SensorFlag.MOTOR_BACKEMF_NORMALIZED); 
//						SensorFlag.MOTOR_BACKEMF_RAW);
				// Set to 20 Hz, this should work on every device
				mSphero.getSensorControl().setRate(20);

				
			}

			// The method to run when a connection fails
			@Override
			public void onConnectionFailed(Robot sphero) {
				// let the SpheroConnectionView handle or hide it and do
				// something here...
				// Good place to ask the user if the sphero is turned on, charged and near by
			}

			// Ran when a Sphero connection drops, such as when the battery runs
			// out or Sphero sleeps
			@Override
			public void onDisconnected(Robot sphero) {
				// Starts looking for robots
				mSpheroConnectionView.startDiscovery();
			}
		};
		
		// Add the listener to the Sphero Connection View
		mSpheroConnectionView.addConnectionListener(mConnectionListener);

	}
	
	@Override
	protected void onResume() {
	    // Required by android, this line must come first
	    super.onResume();
	    // This line starts the discovery process which finds Sphero's which can be connected to
	    mSpheroConnectionView.startDiscovery();
	}
	
	// You must ensure that the robot is cleaned up properly by ensuring discovery is cancelled, and disconnecting the robot. 
	// This is best done in the onPause() method in your activity. 
	// Do not forget to stop discovery as this consumes a lot of resources on the device!
	@Override
	protected void onPause() {
	    super.onPause();
	    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
	    if (mSphero != null) {
	    	
//	    	mSphero.getSensorControl().removeSensorListener(mSensorListener);
		    RobotProvider.getDefaultProvider().disconnectControlledRobots();
	        mSphero.disconnect(); // Disconnect Robot properly
	    }
	    
	    
	} 
	
	public void reset_location(View view) {
		// Position
		((TextView) findViewById(R.id.edit_x)).setText("0 cm");
        ((TextView) findViewById(R.id.edit_y)).setText("0 cm");
        
        // Vel
        ((TextView) findViewById(R.id.edit_y_vel)).setText("0 cm/s");
        ((TextView) findViewById(R.id.edit_x_vel)).setText("0 cm/s");
        
        // Accel
        ((TextView) findViewById(R.id.edit_x_accel)).setText("0");
        ((TextView) findViewById(R.id.edit_y_accel)).setText("0");
        ((TextView) findViewById(R.id.edit_z_accel)).setText("0");
        
        // Pitch / roll / yaw
        ((TextView) findViewById(R.id.edit_pitch)).setText("0");
        ((TextView) findViewById(R.id.edit_roll)).setText("0");
        ((TextView) findViewById(R.id.edit_yaw)).setText("0");
        
        // EMF Filtered 
        ((TextView) findViewById(R.id.edit_filter_left_motor)).setText("0");
        ((TextView) findViewById(R.id.edit_filter_right_motor)).setText("0");
        ((TextView) findViewById(R.id.edit_filter_toString_motor)).setText("0");
        
        // EMF Raw 
        ((TextView) findViewById(R.id.edit_raw_left_motor)).setText("0");
        ((TextView) findViewById(R.id.edit_raw_right_motor)).setText("0");
        ((TextView) findViewById(R.id.edit_raw_toString_motor)).setText("0");
		
        ConfigureLocatorCommand.sendCommand(mSphero, 0, 0, 0, 0);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
