package co.uk.jagw.andtorch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class TorchWidget extends AppWidgetProvider{
	
	//TODO: implement widget functionality
	
	//TODO: Fix IllegalStateExceptions
	
	
	// This onUpdate just opens the Torch activity, and triggers the onNewIntent() method
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	                     int[] appWidgetIds) {
	    for (int i = 0; i < appWidgetIds.length; i++) {
	        int appWidgetId = appWidgetIds[i];

	        Intent intent = new Intent(context, Torch.class);
	        intent.putExtra("methodName", "toggleFlash");
	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.andtorch_appwidget);
	        views.setOnClickPendingIntent(R.id.torchWidgetButton, pendingIntent);
	        
	        appWidgetManager.updateAppWidget(appWidgetId, views);
	    }
	}
	
}


