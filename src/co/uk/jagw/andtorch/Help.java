package co.uk.jagw.andtorch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

	}
	
	public void sendEmail(View view){
		/* Create the Intent */
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		/* Fill it with Data */
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"andtorch@jagw.co.uk"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndTorch: User Help Required");
		String str = "Model " + android.os.Build.MODEL + " | Manufacturer " + android.os.Build.MANUFACTURER + " | Version " + android.os.Build.VERSION.RELEASE;
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, str);

		/* Send it off to the Activity-Chooser */
		this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
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
