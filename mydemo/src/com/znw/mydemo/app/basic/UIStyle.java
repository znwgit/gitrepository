package com.znw.mydemo.app.basic;

import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

public class UIStyle
{ 
	public static void setFullWindowStyle(android.app.Activity activity)
	{
		 activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 activity.requestWindowFeature(Window.FEATURE_PROGRESS);
		 activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		 activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
