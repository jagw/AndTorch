package co.uk.jagw.andtorch;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class TorchPreferences extends PreferenceActivity {

	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.preferences);
	    }
}