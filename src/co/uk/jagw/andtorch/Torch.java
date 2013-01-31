package co.uk.jagw.andtorch;

import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.millennialmedia.android.MMAdViewSDK;
import com.mopub.mobileads.MoPubView;

public class Torch extends Activity {
	
	// Create private variables
	private Camera camera;
	private int flashFlag = 0;
	private int stickFlash = 0;
	private Parameters params;
	private RelativeLayout backgroundView;
	private List<String> plist;
	private boolean hasFlash;
	
	// TODO: BACKGROUND COLOUR FOR NO FLASH.
	private int bgColor = 0;

	// ADVERTISING MoPub - create a private
	private MoPubView mAdView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torch);
		
		// ADVERTISING
		// MoPub Code
		  mAdView = (MoPubView) findViewById(R.id.adview);
		  mAdView.setAdUnitId("aa8476646a2a11e281c11231392559e4");
		  mAdView.loadAd();
		// Set the Millennial SDK to LOG_LEVEL_VERBOSE  
		  MMAdViewSDK.logLevel = MMAdViewSDK.LOG_LEVEL_VERBOSE;
		
		// Determine whether the phone has a camera with an LED
		  camera = Camera.open();
		  params = camera.getParameters();
		  
		  
		  // TODO: SORT THIS OUT
		  if(checkIfFlash(params) == true){
			  hasFlash = true;
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
			camera = Camera.open();
		} else {
			//Fire a reminder to the user that the torch is STILL stuck on.
		}
		
		// Call the rest of the onResume method.
		super.onResume();
	}
	
	@Override
	public void onDestroy(){
		
		// ADVERTISING - Destroy the MoPub ad unit.
		mAdView.destroy();
		
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
		
		// Save the parameters back to the camera.
		
		camera.setParameters(params);
		flashFlag = 1;
		
		flashScreen();
		
		
		} else {
			Log.d("FlashMode", "FLASH_MODE_TORCH not available");
			//TODO - We should engage make a WHITE screen with maximum brightness.
			
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
		
		// Make phone white.
		backgroundView = (RelativeLayout) findViewById(R.id.mainView);
		//TODO: make another way for older phones!
		//backgroundView.setBackground(null);
		backgroundView.setBackgroundColor(bgColor);
		
		// Set brightness of current window to maximum.
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 1;
		getWindow().setAttributes(lp);
		
		
		
	}

}
