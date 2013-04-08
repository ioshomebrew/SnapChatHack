package com.ioshomebrew.snapchathack;

import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
	// declarations
	public Handler toastTeller;
	private Intent intent;
	private Messenger messenger;
	static Context context;
	private boolean threadStarted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        toastTeller = new Handler() {
           public void handleMessage(Message msg) {
             if (msg.what == 2)
            	 Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
             super.handleMessage(msg);
           }
        };
        intent = new Intent(this, SnapChatService.class);
        messenger = new Messenger(toastTeller);
        context = this;
        setContentView(R.layout.activity_main);
	}
	
	// app checking thread
	Thread thread = new Thread()
	{
		public void run()
		{
			while(threadStarted)
			{
				if(checkForSnapChat())
				{
					// tell user to start screen recording software
					showToast("Please start your screen recording software now");
					
					// pause for 30 seconds before restarting service
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	// start the service
	public void Start(View view)
	{
		// start the service
		if(!isMyServiceRunning())
		{
			intent.putExtra("MESSENGER", messenger);
			startService(intent);
		}
	}
	
	// stop the service
	public void Stop(View view)
	{	
		// stop the service
		if(isMyServiceRunning())
		{
			stopService(intent);
			
			// tell user service was stopped
			Toast.makeText(this, "Service was stopped", Toast.LENGTH_LONG).show();
		}
	}
	
	// start the thread
	public void startThread(View view)
	{
		// mark thread started as true
		threadStarted = true;
		
		// start the thread
		thread.start();
	}
	
	// stop the thread
	public void stopThread(View view)
	{
		// mark thread started as false
		threadStarted = false;
	}
	
	// function to check for snapchat
	public boolean checkForSnapChat()
	{
		ActivityManager activityManager = (ActivityManager) context.getSystemService( ACTIVITY_SERVICE );
		List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
		for(int i = 0; i < procInfos.size(); i++){
			if(procInfos.get(i).processName.equals("com.snapchat.android")) {
				return true;
		    }
		}
		    
		return false;
	}
	
	// check if SnapChatHack service is running
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if ("com.example.MyService".equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void showToast(final String toast)
	{
	    runOnUiThread(new Runnable() {
	        public void run()
	        {
	            Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
}
