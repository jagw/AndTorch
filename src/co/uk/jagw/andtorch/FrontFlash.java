package co.uk.jagw.andtorch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class FrontFlash extends Activity{
	
	private LinearLayout linearLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frontflash);
		
		// Check the preferences!
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);	    
		String screenColour = sharedPref.getString("pref_colourPicker", "#FFFFFF");
		
		linearLayout = (LinearLayout) findViewById(R.id.frontFlashBackground);
		Log.d("FrontFlash", screenColour);
		
		linearLayout.setBackgroundColor(Color.parseColor(screenColour));
		
		
		
		// Set brightness of current window to maximum.
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 1;
		getWindow().setAttributes(lp);
	}
	
	// Closes this activity
	public void closeScreenFlash(View view){
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.back_menu, menu);
		return true;
	}
	
	// TODO: TEST THIS! Expecially Exit
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		
		case R.id.menu_back:
			finish();
			return true;
		case R.id.menu_help:
			Intent helpIntent = new Intent(FrontFlash.this, Help.class);
		FrontFlash.this.startActivity(helpIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
		Log.d("dispatchTouchEvent", "Touch intercepted");
		finish();
		super.dispatchTouchEvent(ev);
		return true;
	}

}
