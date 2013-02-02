package co.uk.jagw.andtorch;

import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;

public class TorchPreferences extends PreferenceActivity {

	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.preferences);
	    }
	  
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.back_menu, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle item selection
			switch (item.getItemId()) {
			
			case R.id.menu_back:
				finish();
				return true;
			case R.id.menu_help:
				Intent helpIntent = new Intent(TorchPreferences.this, Help.class);
				TorchPreferences.this.startActivity(helpIntent);       	
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
}