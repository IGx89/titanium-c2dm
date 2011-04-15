var senderId = 'youraddress@gmail.com';

var c2dm = require('com.findlaw.titanium.c2dm');
Ti.API.info("module is => " + c2dm);

Ti.API.info('Registering...');
c2dm.register(senderId, {
	success:function(e)
	{
		Ti.API.info('JS registration success event: ' + e.registrationId);
	
		// TODO for you: send e.registrationId to your server-side database
	},
	error:function(e)
	{
		Ti.API.error("Error during registration: "+e.error);
		
		var message;
		if(e.error == "ACCOUNT_MISSING") {
			message = "No Google account found; you'll need to add one (in Settings/Accounts) in order to activate notifications";
		} else {
			message = "Error during registration: "+e.error
		}
		
		Titanium.UI.createAlertDialog({
			title: 'Push Notification Setup',
			message: message,
			buttonNames: ['OK']
		}).show();
	},
	callback:function(e) // called when a push notification is received
	{
		Ti.API.info('JS message event: ' + JSON.stringify(e.data));
		
		var intent = Ti.Android.createIntent({
			action: Ti.Android.ACTION_MAIN,
			flags: Ti.Android.FLAG_ACTIVITY_NEW_TASK | Ti.Android.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED,
			className: 'com.company.app.YourActivity',
			packageName: 'com.company.app'
		});
		intent.addCategory(Ti.Android.CATEGORY_LAUNCHER);
		  
		// This is fairly static: Not much need to be altered here
		var pending = Ti.Android.createPendingIntent({
			activity: Ti.Android.currentActivity,
			intent: intent,
			type: Ti.Android.PENDING_INTENT_FOR_ACTIVITY,
		});
		
		var notification = Ti.Android.createNotification({
			contentIntent: pending,
			contentTitle: 'New message',
			contentText: e.data.message,
			tickerText: "New message"
		});
		 
		Ti.Android.NotificationManager.notify(1, notification);
	}
});