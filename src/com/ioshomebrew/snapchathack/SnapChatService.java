package com.ioshomebrew.snapchathack;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.ioshomebrew.snapchathack.MainActivity;

public class SnapChatService extends IntentService {
	public SnapChatService() {
		super("SnapChatService");
		// TODO Auto-generated constructor stub
	}

	// declarations
	private boolean displayed = false;
	private MainActivity activity;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//TODO do something useful
	    return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		//TODO for communication return IBinder implementation
		return null;
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		activity = new MainActivity();
		Bundle extras = arg0.getExtras();
		
		// create the message
	    Messenger messenger = (Messenger) extras.get("MESSENGER");
	    Message msg = Message.obtain();
	    msg.what = 2;
	    msg.obj = "Service started";
	    
	    // send the message
	    try {
			messenger.send(msg);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    while(true)
	    {
			if(activity.checkForSnapChat())
			{
		    	// declarations
				msg.what = 2;
				msg.obj = "Please start your screen recording software now";
				
				// if user has not already been told
				if(!displayed)
					try {
						messenger.send(msg);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				// mark displayed as true
				displayed = true;
				break;
			}	
	    }
	}
} 