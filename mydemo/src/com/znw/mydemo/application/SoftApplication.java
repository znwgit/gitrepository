package com.znw.mydemo.application;

import java.io.File;
import java.util.List;
import java.util.Stack;

import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.znw.mydemo.third.openfile.ChatService.ChatBinder;
import com.znw.mydemo.third.openfile.XmppConnection;
import com.znw.mydemo.utils.debug.DebugUtils;

public class SoftApplication extends Application {

	public static Stack<Activity> activityStack = null;
	public static SoftApplication softApplication;
	private ChatBinder chatBinder;
	private static boolean isXmppLogin;// 判断是否已经登录
	public String openFireUid;
	private String currentChatFid, currentGroupName;
	private List<Message> messageList;
	public UMSocialService mController;

	public static ImageLoader imageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		softApplication = this;
		initImageLoader(getApplicationContext());
		// JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		// JPushInterface.init(this); // 初始化 JPush
		initUmeng();
	}

	/**
	 * 初始化友盟：微信，朋友圈，新浪微博，qq,qq空间
	 */
	private void initUmeng() {
		// 获取友盟服务成员变量
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");

		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.TENCENT);
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.SINA);
	}

	public UMSocialService getUMSocialService() {
		return this.mController;
	}

	/** 初始化ImageLoader */
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"bmobim/Cache");// 获取到缓存的目录地址
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				// 线程池内加载的数量
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(new WeakMemoryCache())
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);// 全局初始化此配置
	}

	/** 返回本类实例 */
	public static SoftApplication getInstance() {
		return softApplication;
	}

	/**
	 * 返回当前应用程序的版本号
	 * 
	 * @return
	 */
	public String getVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// can't reach
			return "";
		}
	}

	/**
	 * add Activity 添加Activity到栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * get current Activity 获取当前Activity（栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				// LogUtils.info("activity",
				// activityStack.get(i).getClass().getName());
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用
	 */
	public void quit() {
		try {
			finishAllActivity();
		} catch (Exception e) {
		}
		imageLoader.stop();
	}

	/**
	 * 注销帐号
	 */
	public void logout() {
		XmppConnection.closeConnection();
		isXmppLogin = false;
	}

	public ChatBinder getChatBinder() {
		return chatBinder;
	}

	public void setChatBinder(ChatBinder chatBinder) {
		this.chatBinder = chatBinder;
	}

	public boolean isLogin() {
		return isXmppLogin;
	}

	public void setCurrrentChatFriendId(String fid) {
		this.currentChatFid = fid;
	}

	public String getCurrrentChatFriendId() {
		return this.currentChatFid;
	}

	public void setCurrentGroupName(String groupName) {
		this.currentGroupName = groupName;
	}

	public String getCurrentGroupName() {
		return this.currentGroupName;
	}

	public void setOpenFireUid(String openFireUid) {
		this.openFireUid = openFireUid;
	}

	public String getOpenFireUid() {
		DebugUtils.print(this.getClass(), "openFireUid=" + openFireUid);
		return this.openFireUid;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public List<Message> getMessageList() {
		return this.messageList;
	}
}
