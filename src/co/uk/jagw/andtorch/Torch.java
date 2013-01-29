package co.uk.jagw.andtorch;

import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.millennialmedia.android.MMAdViewSDK;
import com.mopub.mobileads.MoPubView;

public class Torch extends Activity {
	
	// Create private variables
	private Camera camera;
	private int flashFlag = 0;
	private int stickFlash = 0;
	private Parameters params;

	// MoPub
	private MoPubView mAdView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torch);
		
		// MoPub Code
		  mAdView = (MoPubView) findViewById(R.id.adview);
		  mAdView.setAdUnitId("aa8476646a2a11e281c11231392559e4"); // Enter your Ad Unit ID from www.mopub.com
		  mAdView.loadAd();
		  MMAdViewSDK.logLevel = MMAdViewSDK.LOG_LEVEL_VERBOSE;
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
		}
		
		// Call the rest of the onPause
		super.onPause();
	}
	
	@Override
	public void onResume(){
		try{
			camera = Camera.open();
		
		
		
		//Call the rest of the onResume
		super.onResume();
		}
		catch(Exception e){
			Log.d("OnResume", "Camera is killing me!");
		}
	}
	
	@Override
	public void onDestroy(){
		mAdView.destroy();
		super.onDestroy();
	}
	
	// Method to toggle to flash, logic check if flash is on.
	public void toggleFlash(View view){
		Log.d("toggleFlash", "Got to toggleFlash method");
		
		if (camera == null) {
			camera = Camera.open();
		}
		
		// Remove the sticky toggle
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
			camera = Camera.open();
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

}
