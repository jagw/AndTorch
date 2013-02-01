package co.uk.jagw.andtorch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.back_menu, menu);
		return true;
	}

	// Options menu handling

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_back:
			finish();
			return true;
		case R.id.menu_help:
			Intent helpIntent = new Intent(Help.this, Help.class);
			Help.this.startActivity(helpIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
