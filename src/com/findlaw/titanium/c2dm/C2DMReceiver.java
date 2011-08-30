package com.findlaw.titanium.c2dm;

import java.io.IOException;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.util.Log;

import com.google.android.c2dm.C2DMBaseReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class C2DMReceiver extends C2DMBaseReceiver {

	private static final String LCAT = "C2DMReceiver";

	private static final String REGISTER_EVENT = "register";
	private static final String UNREGISTER_EVENT = "unregister";
	private static final String MESSAGE_EVENT = "message";
	private static final String ERROR_EVENT = "error";

	public void onRegistrered(Context context, String registrationId)
			throws IOException {
		Log.d(LCAT, "Registered: " + registrationId);

		C2dmModule.getInstance().sendSuccess(registrationId);
	}

	public void onUnregistered(Context context) {
		Log.d(LCAT, "Unregistered");

		C2dmModule.getInstance().fireEvent(UNREGISTER_EVENT, new KrollDict());
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.d(LCAT, "Message received");

		KrollDict data = new KrollDict();
		for (String key : intent.getExtras().keySet()) {
			Log.d(LCAT, "Message key: " + key + " value: "
					+ intent.getExtras().getString(key));

			String eventKey = key.startsWith("data.") ? key.substring(5) : key;
			data.put(eventKey, intent.getExtras().getString(key));
		}
		if (C2dmModule.getInstance() == null) {
			int icon = 0x7f020002; // get this from R.java
			CharSequence tickerText = "You got a new message";
			long when = System.currentTimeMillis();

			CharSequence contentTitle = "Hello"; // expanded message title
			CharSequence contentText = "World"; // expanded
																			// message
																			// text

			Intent notificationIntent = new Intent(this, C2DMReceiver.class);

			Intent launcherintent = new Intent("android.intent.action.MAIN");
			launcherintent
					.setComponent(ComponentName
							.unflattenFromString("your.app.id/your.app.id.yourappActivity"));
			launcherintent.addCategory("android.intent.category.LAUNCHER");
			
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					launcherintent, 0);

			// the next two lines initialize the Notification, using the
			// configurations above

			Notification notification = new Notification(icon, tickerText, when);
			
			notification.setLatestEventInfo(context, contentTitle, contentText,
					contentIntent);
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
			mNotificationManager.notify(1, notification);
		} else
			C2dmModule.getInstance().sendMessage(data);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.e(LCAT, "Error: " + errorId);

		C2dmModule.getInstance().sendError(errorId);
	}

}
