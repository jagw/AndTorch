package co.uk.jagw.andtorch;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.NotificationManager;
import android.content.Context;
import android.app.Notification;
import android.app.PendingIntent;

//import com.millennialmedia.android.MMAdViewSDK;
//import com.mopub.mobileads.MoPubView;

public class Torch extends Activity {

	// Create private variables
	private Camera camera;
	private int flashFlag = 0;
	private int stickFlash = 0;
	private Parameters params;
	private boolean hasFlash;
	public String tag;
	public Notification mNotification;

	// ADVERTISING MoPub - create a private
//	private MoPubView mAdView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torch);

//		// ADVERTISING
//		// MoPub Code
//		mAdView = (MoPubView) findViewById(R.id.adview);
//		mAdView.setAdUnitId("aa8476646a2a11e281c11231392559e4");
//		mAdView.loadAd();
//		// Set the Millennial SDK to LOG_LEVEL_VERBOSE  
//		MMAdViewSDK.logLevel = MMAdViewSDK.LOG_LEVEL_VERBOSE;

		// Determine whether the phone has a camera with an LED
		camera = Camera.open();
		params = camera.getParameters();
		if(checkIfFlash(params) == true){
			hasFlash = true;
		} else {
			hasFlash = false;
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_torch, menu);
		return true;
	}


	@Override
	public void onPause(){

		if(stickFlash == 0){
			camera.release();
		} else if (stickFlash ==1){
			// TODO: Warn the user that they cannot use other camera apps.
		}

		// Call the rest of the onPause
		super.onPause();
	}

	@Override
	public void onResume(){

		if(stickFlash == 0){
			try{
				camera = Camera.open();
			} catch(Exception e){
				// TODO: this. If the stickFlash flag isn't set, but the camera is open, we get here.
			}
		} else {
			//Fire a reminder to the user that the torch is STILL stuck on.
		}

		// Call the rest of the onResume method.
		super.onResume();
	}

	@Override
	public void onDestroy(){

		// ADVERTISING - Destroy the MoPub ad unit.
//		mAdView.destroy();

		super.onDestroy();
	}

	//
	public boolean checkIfFlash(Parameters params){
		List<String> pList = params.getSupportedFlashModes();
		if (pList.contains(Parameters.FLASH_MODE_TORCH)){
			return true;
		} else {
			return false;
		}
	}

	// Method to toggle to flash, logic check if flash is on.
	public void toggleFlash(View view){
		Log.d("toggleFlash", "Got to toggleFlash method");

		// Check the preferences!
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean screenPref = sharedPref.getBoolean("pref_screenflash", false);


		if(hasFlash == true & screenPref == false){
			// If there's no open camera unit; let's open one!
			if (camera == null) {
				try{
					camera = Camera.open();
				} catch(Exception e){
					// TODO: If we aren't able to open the camera, we should try and use the screen.
				}
			}

			// Remove the sticky toggle if we've hit the first button.
			stickFlash = 0;

			if(flashFlag == 0){
				try{
					camera.reconnect();
				} catch(Exception e){
					// Do nothing - camera is already connected
				}

				params = camera.getParameters();
				flashOn(params);

			} else if(flashFlag == 1){
				params = camera.getParameters();
				flashOff(params);

			}
		} else {
			// The device doesn't have a flash, or the user wants to use the screen
			flashScreen();
		}
	}

	public void stickFlash(View view){
		Log.d("stickFlash", "Got to stickFlash method");

		if (camera == null) {
			try{
				camera = Camera.open();
			} catch(Exception e){
				// TODO: If we can't open the camera.
			}
		}


		if(flashFlag == 0){
			try{
				camera.reconnect();
			} catch(Exception e){
				// Do nothing - camera is already connected
			}

			params = camera.getParameters();
			flashOn(params);
			stickFlash = 1;

		} else if(flashFlag == 1){
			params = camera.getParameters();
			flashOff(params);
			stickFlash = 0;

		}

	}

	// Method to turn the flash on, accepts camera parameters
	public void flashOn(Parameters params){
		Log.d("flashOn", "Got to flashOn method");
		// Get supported flash modes so we can work out what is possible.
		List<String> pList = params.getSupportedFlashModes();

		// Check if we can use the rear LED
		if (pList.contains(Parameters.FLASH_MODE_TORCH)){
			Log.d("FlashMode", "FLASH_MODE_TORCH available");
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);

			// Save the parameters back to the camera and set a flag.
			camera.setParameters(params);
			flashFlag = 1;		
			createNotification();
			
		} else {
			// We shouldn't get here - but in case we do, call the frontFlash activity
			Log.d("FlashMode", "FLASH_MODE_TORCH not available");
			flashScreen();

		}
	}

	// Method to turn the flash off, accepts camera parameters
	public void flashOff(Parameters params){
		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(params);
		flashFlag = 0;

		// Give the camera back to the OS.
		camera.unlock();

	}
	// Method to make the current background white and turn brightness to maximum.
	// Turning the front screen into a flashlight instead!

	public void flashScreen(){
		Intent helpIntent = new Intent(Torch.this, FrontFlash.class);
		Torch.this.startActivity(helpIntent);
		createNotification();
	}

	// Options menu handling
	
	public void createNotification(){
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("AndTorch")
		        .setContentText("Click to return to app.");
	
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Torch.class);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotification = mBuilder.build();
		// Make sure the notification only comes up once, and goes on click.
		mNotification.flags |= Notification.FLAG_ONLY_ALERT_ONCE |Notification.FLAG_AUTO_CANCEL;
		// When we click the notification, make sure it comes back to a new intent of Torch, although it's only a single launch activity.
		Intent notificationIntent = new Intent(this.getApplicationContext(), Torch.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, notificationIntent, 0);
		mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(1, mNotification);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent settingIntent = new Intent(Torch.this, TorchPreferences.class);
			Torch.this.startActivity(settingIntent);
			return true;
		case R.id.menu_about:
			Intent aboutIntent = new Intent(Torch.this, About.class);
			Torch.this.startActivity(aboutIntent);
			return true;
		case R.id.menu_help:
			Intent helpIntent = new Intent(Torch.this, Help.class);
			Torch.this.startActivity(helpIntent);
			return true;
		case R.id.menu_exit:
			finish();	        	
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
