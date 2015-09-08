package com.znw.mydemo.utils.sp;

import com.znw.mydemo.application.SoftApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {
	/**
	 * SharedPreferences的名字
	 */
	private static final String SP_FILE_NAME = "APPLICATION_SP";
	private static SharedPrefHelper sharedPrefHelper = null;
	private static SharedPreferences sharedPreferences;

	public static synchronized SharedPrefHelper getInstance() {
		if (null == sharedPrefHelper) {
			sharedPrefHelper = new SharedPrefHelper();
		}
		return sharedPrefHelper;
	}

	private SharedPrefHelper() {
		sharedPreferences = SoftApplication.softApplication
				.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
	}

	// cookie
	public String getCookie() {
		return sharedPreferences.getString("SHARED_KEY_COOKIE", null);
	}

	public void setCookie(String cookie) {
		sharedPreferences.edit().putString("SHARED_KEY_COOKIE", cookie)
				.commit();
	}
	public boolean getIsLogin() {
		return sharedPreferences.getBoolean("LOGIN", false);
	}

	public void setIsLogin(boolean isLogin) {
		sharedPreferences.edit().putBoolean("LOGIN", isLogin).commit();
	}

	public boolean getIsXmppLogin() {
		return sharedPreferences.getBoolean("XMPP_LOGIN", false);
	}

	public void setIsXmppLogin(boolean xmppIsLogin) {
		sharedPreferences.edit().putBoolean("XMPP_LOGIN", xmppIsLogin).commit();
	}

	public String getUserName() {
		return sharedPreferences.getString("SHARED_USER_NAME", null);
	}

	public void setUserName(String userName) {
		sharedPreferences.edit().putString("SHARED_USER_NAME", userName)
				.commit();
	}

	public String getUserPassword() {
		return sharedPreferences.getString("SHARED_USER_PASSWORD", null);
	}

	public void setUserPassword(String userPassword) {
		sharedPreferences.edit()
				.putString("SHARED_USER_PASSWORD", userPassword).commit();
	}

	public int getCurrentNetState() {
		return sharedPreferences.getInt("netState", -1);// 默认
	}

	// 网络状态
	public void setCurrentNetState(int netState) {
		sharedPreferences.edit().putInt("netState", netState).commit();
	}
	
	public void setVoiceReminder(boolean isChecked) {
		sharedPreferences.edit().putBoolean("voiceReminder", isChecked).commit();
	}
	public boolean getVoiceReminder() {
		return sharedPreferences.getBoolean("voiceReminder",true);
	}

	public void setVibrationReminder(boolean isChecked) {
		sharedPreferences.edit().putBoolean("vibrationReminder", isChecked).commit();
	}
	public boolean getVibrationReminder() {
		return sharedPreferences.getBoolean("vibrationReminder",true);
	}
	
}
