package edu.auburn.akm0012.seniordesign_sensorfun;

import orbotix.robot.base.Robot;
import orbotix.sphero.ConnectionListener;
import orbotix.sphero.Sphero;
import orbotix.view.connection.SpheroConnectionView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	// The connection view
	private SpheroConnectionView mSpheroConnectionView;

	// The Shpero
	private Sphero mSphero = null; 
	
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
				// You can add commands to set up the ball here, these are some
				// examples

				// Set the back LED brightness to full
				mSphero.setBackLEDBrightness(1.0f);
				// Set the main LED color to green at full brightness
				mSphero.setColor(0, 255, 0);

				// End examples
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
	        mSphero.disconnect(); // Disconnect Robot properly
	    }
	} 

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
