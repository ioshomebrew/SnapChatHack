package com.ioshomebrew.snapchathack;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	// start the service
	public void Start(View view)
	{
		intent.putExtra("MESSENGER", messenger);
		startService(intent);
	}
	
	// stop the service
	public void Stop(View view)
	{	
		// stop the service
		stopService(intent);
		
		// tell user service was stopped
		Toast.makeText(this, "Service was stopped", Toast.LENGTH_LONG).show();
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
}
