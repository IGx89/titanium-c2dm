/*
    titanium-c2dm
    Copyright (C) 2010 by Matthew Lieder

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/

package com.findlaw.titanium.c2dm;

import java.io.IOException;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.util.Log;

import com.google.android.c2dm.C2DMBaseReceiver;

import android.content.Context;
import android.content.Intent;

public class C2DMReceiver extends C2DMBaseReceiver {

	private static final String LCAT = "C2DMReceiver";
	
	private static final String REGISTER_EVENT = "register";
	private static final String UNREGISTER_EVENT = "unregister";
	private static final String MESSAGE_EVENT = "message";
	private static final String ERROR_EVENT = "error";

	public void onRegistrered(Context context, String registrationId) throws IOException {
		Log.d(LCAT, "Registered: " + registrationId);
		
		KrollDict data = new KrollDict();
		data.put("registrationId", registrationId);
		C2dmModule.getInstance().fireEvent(REGISTER_EVENT, data);
	}
	
    public void onUnregistered(Context context) {
    	Log.d(LCAT, "Unregistered");
    	
    	C2dmModule.getInstance().fireEvent(UNREGISTER_EVENT, new KrollDict());
    }

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.d(LCAT, "Message received: " + intent.getExtras().getString("data.message"));
		
		KrollDict data = new KrollDict();
		for(String key: intent.getExtras().keySet()) {
			String eventKey = key.startsWith("data.") ? key.substring(5) : key;
			data.put(eventKey, intent.getExtras().getString(key));
		}
		
		C2dmModule.getInstance().fireEvent(MESSAGE_EVENT, data);
		
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.e(LCAT, "Error: " + errorId);
		
		KrollDict data = new KrollDict();
		data.put("errorId", errorId);
		C2dmModule.getInstance().fireEvent(ERROR_EVENT, data);
	}

}
