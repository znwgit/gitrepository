package com.znw.mydemo.third.openfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;

import com.znw.mydemo.MainActivity;
import com.znw.mydemo.R;
import com.znw.mydemo.app.chat.activity.ChatActivity;
import com.znw.mydemo.app.chat.activity.ChatActivity.OnReadNewMsgListener;
import com.znw.mydemo.application.SoftApplication;
import com.znw.mydemo.utils.date.DateUtil;
import com.znw.mydemo.utils.db.AppDataBaseHelper;
import com.znw.mydemo.utils.debug.LogUtils;
import com.znw.mydemo.utils.device.NetUtil;
import com.znw.mydemo.utils.sp.SharedPrefHelper;
import com.znw.mydemo.utils.string.StringUtils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ChatService extends Service {
	private static final String MYTAG="ChatService";
	private Timer timer;
	private MyTimerTask task;
	private int currentNetState;
	private boolean isXmppLogining = false;
	private ChatBinder binder;
	private OnNetConnectedListener onNetConnectedListener;
	
	private AppDataBaseHelper appDataBaseHelper;
	private SQLiteDatabase db;
	private PacketListener packetListener;
	private NotificationManager mNotificationManager;
	private long lastTime;
	
	public static final int NOTIFY_TYPE_NEW_MSG = 3;//未读新消息私聊
	private static final String COM_BROADCAST_BOXIN = "com.broadcast.boxin";
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(currentNetState == 2 && SharedPrefHelper.getInstance().getCurrentNetState() == 1){
					Toast.makeText(ChatService.this, "网络已连接", Toast.LENGTH_SHORT).show();
					
					if(!isXmppLogining){
						doRelogin();
					}
				}
				break;
			case 2:
				if(currentNetState == 1 && SharedPrefHelper.getInstance().getCurrentNetState() == 2){
					Toast.makeText(ChatService.this, "您已断开网络连接，请开启网络连接", Toast.LENGTH_SHORT).show();
				}
				break;
			case 3://成功链接至服务器
				Toast.makeText(ChatService.this, "您已成功连接通讯服务器", Toast.LENGTH_SHORT).show();
				if(ChatService.this.onNetConnectedListener != null){
					ChatService.this.onNetConnectedListener.onNetConnected();
				}
				break;
			case 4://链接至服务器失败
				Toast.makeText(ChatService.this, "连接通讯服务器失败", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(ChatService.this, "您的帐号在其他地方登录，请重新登录或修改密码", Toast.LENGTH_LONG).show();
				List<Activity> activityList = SoftApplication.activityStack;
				if(activityList != null){
					for (Activity activity : activityList) {
						if (null != activity) {
							activity.finish();
						}
					}
					activityList.clear();
				}
				
				Intent intent2 = new Intent();
				intent2.setAction("unbind_chat_service");
				sendBroadcast(intent2);
				ChatBinder chatBinder = SoftApplication.softApplication.getChatBinder();
				if(chatBinder != null){
					//chatBinder.clearNotification();
				}
				SharedPrefHelper.getInstance().setUserPassword("");//清除密码
				SoftApplication.softApplication.logout();
				Intent intent = new Intent(ChatService.this,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(intent);
				break;
			case 6:
				Toast.makeText(ChatService.this, "连接超时，与服务器断开连接", Toast.LENGTH_SHORT).show();
				break;
			
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		appDataBaseHelper = AppDataBaseHelper.getInstance(this);
		db = appDataBaseHelper.getWritableDatabase();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		binder = new ChatBinder();
		initMessageTypeFilter();
		// 定时任务：监听网络状态
		timer = new Timer();
		task = new MyTimerTask();
		timer.schedule(task, 0, 1000);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new ChatBinder();
	}

	public class ChatBinder extends Binder {
		
		public void updatePresenceAvailable(){
			ChatService.this.updatePresenceAvailable();
		}
		
		public OnNetConnectedListener getOnNetConnectedListener() {
			return ChatService.this.onNetConnectedListener;
		}

		// 设置网络监听
		public void setOnNetConnectedListener(
				OnNetConnectedListener onNetConnectedListener) {
			ChatService.this.onNetConnectedListener = onNetConnectedListener;
		}

		public void clearSingleNotification(int id){
			try {
				if(mNotificationManager != null){
					mNotificationManager.cancel(id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public interface OnNetConnectedListener {
		public abstract void onNetConnected();
	}

	/**
	 * 网络监听异步类
	 */
	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			currentNetState = SharedPrefHelper.getInstance()
					.getCurrentNetState();
			if (NetUtil.isNetDeviceAvailable(SoftApplication.softApplication)) {
				SharedPrefHelper.getInstance().setCurrentNetState(1);// 网络好
				handler.sendEmptyMessage(1);
			} else {
				SharedPrefHelper.getInstance().setCurrentNetState(2);// 断网
				handler.sendEmptyMessage(2);
			}
		}
	}
	
	/**
	 * 重新登录
	 */
	private void doRelogin() {
		if(!NetUtil.isNetDeviceAvailable(ChatService.this)){
			return;
		}
		
//		boolean isGettingFriendList = SharedPrefHelper.getInstance().isGettingFriendList();
//		if(isGettingFriendList){//通讯录列表正在刷新的时候，不执行重新登录的代码
//			return;
//		}
		
		isXmppLogining = true;
		
		if(SharedPrefHelper.getInstance().getIsLogin()){//已登录状态
			//重新登陆openFire
			final String account_String = SharedPrefHelper.getInstance().getUserName();
			final String password_String = SharedPrefHelper.getInstance().getUserPassword();
			
			new Thread(){

				@Override
				public void run() {
					super.run();
					try {
						XmppConnection.closeConnection();//断开连接
						LogUtils.info("重新登录",account_String+"@"+XmppConnection.SERVER_NAME+"_"+ password_String);
						 XmppConnection.login(account_String+"@"+XmppConnection.SERVER_NAME, password_String);
						//重新设置服务的监听
						/*ChatBinder chatBinder = SoftApplication.softApplication.getChatBinder();
						if(chatBinder != null){
							chatBinder.addMessageTypeFilter();
						}*/
						initMessageTypeFilter();
						handler.sendEmptyMessage(3);
					} catch (Exception e) {
						XmppConnection.closeConnection();
						e.printStackTrace();
						handler.sendEmptyMessage(4);
					} finally{
						isXmppLogining = false;
					}
				}
			}.start();
		}
	}
	/**
	 * 实例化消息过滤器
	 */
	private void initMessageTypeFilter() {
		
		new Thread(){

			private XMPPConnection connection;

			@Override
			public void run() {
				try {
					connection = XmppConnection.getConnection(false);
					if(connection != null){
						//本地连接的监听，是否被挤掉线
						addLocalConnectionListener(connection);
						//过滤会话消息的监听器
						initPacketListener(connection);
						//监听加好友/同意加好友的消息
						//initSubscribeListener(connection);
						//监听离线消息的监听器
						initOfflineManager(connection);
						//登录成功之后用户是不在线的，用来接收离线消息，接收完离线消息之后就改变在线状态。
						updatePresenceAvailable();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}.start();
	}
	
	/**
	 * 本地连接的监听
	 * @param connection
	 */
	protected void addLocalConnectionListener(XMPPConnection connection) {
		if(connection == null){
			return;
		}
		if(NetUtil.isNetDeviceAvailable(ChatService.this)){
			connection.addConnectionListener(new ConnectionListener() {
				
				@Override
				public void reconnectionSuccessful() {
					Log.e("connection","来自连接监听,conn重连成功");
				}
				
				@Override
				public void reconnectionFailed(Exception e) {
					Log.e("connection","来自连接监听,conn重连失败");
				}
				
				@Override
				public void reconnectingIn(int seconds) {
					Log.e("connection","来自连接监听,conn重连中..." + seconds);
				}
				
				@Override
				public void connectionClosedOnError(Exception e) {
					Log.e("connection","来自连接监听,conn关闭出现错误");
					Log.e("connection",e.getMessage());
					if (e.getMessage().contains("conflict")) { // 被挤掉线
						// 关闭连接，由于是被人挤下线，可能是用户自己，所以关闭连接，让用户重新登录是一个比较好的选择
						XmppConnection.closeConnection();//断开连接
						handler.sendEmptyMessage(5);
						// 接下来你可以通过发送一个广播，提示用户被挤下线，重连很简单，就是重新登录
					} else if (e.getMessage().contains("Connection timed out")) {// 连接超时
						// 不做任何操作，会实现自动重连
						handler.sendEmptyMessage(6);
					}
				}
				
				@Override
				public void connectionClosed() {
					Log.e("connection","来自连接监听,conn正常关闭");
				}
			});
		}
	}
	
	/**
	 * 过滤会话消息的监听器
	 * @param connection
	 */
	private void initPacketListener(XMPPConnection connection) {
		if(connection == null){
			return ;
		}
		
		try {
			// 创建一个packet过滤器来监听来自一个特定用户的新的消息
			// 监听器被注册时还加了一个过滤器，这个过滤器的目的是监听器只接收自己感兴趣的内容
//		PacketFilter filter = new AndFilter(
//			new PacketTypeFilter(Message.class), new FromContainsFilter(
//						"wds@www.feijizhe.com"));
		PacketFilter filter = new MessageTypeFilter(Message.Type.normal);
			//PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			if(packetListener == null){
				packetListener = new PacketListener() {
					List<Message > messageList =  new ArrayList<Message>();
					List<Message > tmpMsgList =  new ArrayList<Message>();
					Set<String> userSet = new HashSet<String>();
					Map<String,Integer> privateChatMap = new HashMap<String,Integer>();
					Map<String,Integer> groupChatMap = new HashMap<String,Integer>();
					List<Message > messageList2 =  new ArrayList<Message>();
					List<Message > tmpMsgList2 =  new ArrayList<Message>();
					Set<String> userSet2 = new HashSet<String>();
					
					@Override
					public void processPacket(Packet packet) {
						
						String uid = SoftApplication.getInstance().getOpenFireUid();
						if(StringUtils.isEmpty(uid)){
							return;
						}
						
						Message message = (Message) packet;
						if (message != null && message.getBody() != null) {
							// TODO 处理私聊信息通知
							if(StringUtils.isEmpty(message.getSubject())){
								String fid = getAccountId(message.getFrom());
								LogUtils.debug("fid", "fid="+fid);
									messageList.add(message);
									tmpMsgList.add(message);
									if(!fid.equals(SoftApplication.softApplication.getCurrrentChatFriendId()) && !messageList.isEmpty()){
										for (Message message2 : messageList) {
											if(message2 != null){
												String accountId = getAccountId(message2.getFrom());
												if(!StringUtils.isEmpty(accountId)){
													userSet.add(accountId);
												}
											}
										}
										
										//从数据库中取出未读消息进行赋值
										Map<String,Integer> map = appDataBaseHelper.getFriendInfoUnReadMsgMap(db,uid);
										if(map != null){
											privateChatMap = map;
										}
										
										if(userSet.size() > 0){//至少有1个人发来了消息
											for (final Message message2 : messageList) {
												privateChatMap.put(getAccountId(message2.getFrom()), (privateChatMap.get(getAccountId(message2.getFrom())) == null ? 0 : privateChatMap.get(getAccountId(message2.getFrom()))) + 1);
												LogUtils.debug(MYTAG, "privateChatMap.put("+getAccountId(message2.getFrom())+","+(privateChatMap.get(getAccountId(message2.getFrom())) == null ? 0 : privateChatMap.get(getAccountId(message2.getFrom()))) + 1+")");
											 if(Message.TYPE_TEXT.equals(message2.getMsgType())){
													appDataBaseHelper.saveChatRecordInfo(db,uid,getAccountId(message2.getFrom()), message2.getMsgType(), message2.getMsgTime(), "", message2.getBody(), "1", "",DateUtil.getCurrentDateTime(),"");
											 }
											}
											
											Iterator<Entry<String, Integer>> iter = privateChatMap.entrySet().iterator();
											while (iter.hasNext()) {
												Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iter.next();
												String key = entry.getKey();
												Integer unReadMsgCount = entry.getValue();
												LogUtils.debug(MYTAG, "key="+key+",value="+unReadMsgCount);
												//更新某个好友发来的N条消息
												appDataBaseHelper.updateFriendInfounReadMsgCount(db,uid,key,unReadMsgCount,DateUtil.getCurrentDateTime());
											}
											
											sendChatMessageNotify("消息通知", userSet.size() + "个联系人发来" + tmpMsgList.size() + "条消息", NOTIFY_TYPE_NEW_MSG,tmpMsgList,userSet.size(),false);
											Intent intent = new Intent(COM_BROADCAST_BOXIN);
											sendBroadcast(intent);
											messageList.clear();
										}
									}else{
										privateChatMap.clear();
										messageList.clear();
										userSet.clear();
										tmpMsgList.clear();
									}
									
									ChatActivity.setOnReadNewMsgListener(new OnReadNewMsgListener() {
										
										@Override
										public void onReadNewMsg(boolean bool) {
											if(bool){
												messageList.clear();
												userSet.clear();
												tmpMsgList.clear();
											}
										}
									});
							} 
						}		
					}
				};
			}
			connection.removePacketListener(packetListener);
			connection.addPacketListener(packetListener,filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 监听离线消息的监听器
	 * @param connection
	 */
	private void initOfflineManager(XMPPConnection connection){
		try {
			OfflineMessageManager offlineManager = new OfflineMessageManager(connection);
			//消息数  offlineManager.getMessageCount()里面和有可能出现空指针异常
			int messageCount = offlineManager.getMessageCount();
			LogUtils.info("离线消息数目",messageCount+"");
			Set<String> userSet = new HashSet<String>();
			Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager.getMessages();
			List<org.jivesoftware.smack.packet.Message> messageList = new ArrayList<org.jivesoftware.smack.packet.Message>();
			while (it.hasNext()) {
				org.jivesoftware.smack.packet.Message message = it.next();
				messageList.add(message);
				if (message != null) {
					String accountId = getAccountId(message.getFrom());
					if(!StringUtils.isEmpty(accountId)){
						userSet.add(accountId);
						LogUtils.info("离线消息",accountId);
					}
				}
			}
			//删除服务器的离线消息
			offlineManager.deleteMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getAccountId(String from){
		if(StringUtils.isEmpty(from)){
			return null;
		}
		
		if(from.contains("@")){
			String[] split = from.split("\\@");
			return split[0];
		}
		return null;
	}
	
	/**
	 * 更改在线状态，
	 * 登录成功之后用户是不在线的，用来接收离线消息，接收完离线消息之后就改变在线状态。
	 */
	private void updatePresenceAvailable(){
		XMPPConnection connection = XmppConnection.getConnection(false);
		if(connection == null){
			return;
		}
		//连接服务器成功，更改在线状态
		Presence presence = new Presence(Presence.Type.available);
		connection.sendPacket(presence);
	}
	
	@SuppressWarnings("deprecation")
	private void sendChatMessageNotify(String title,String content,int type,List<Message> messageList,int friendFromCount,boolean isGroupMessage){
		// 第一个参数为图标，第二个参数为标题，第三个参数为通知时间
		Notification notification = new Notification(R.drawable.ic_launcher, "mydemo",System.currentTimeMillis());
		boolean voiceReminder = SharedPrefHelper.getInstance().getVoiceReminder();
		boolean vibrationReminder = SharedPrefHelper.getInstance().getVibrationReminder();
		
		if(voiceReminder){//声音提醒
			if(System.currentTimeMillis() - lastTime > 2000){
				notification.defaults |= Notification.DEFAULT_SOUND;// 发出默认声音
			}
		}
		if(vibrationReminder){//震动提醒
			if(System.currentTimeMillis() - lastTime > 2000){
				notification.defaults |= Notification.DEFAULT_VIBRATE;// 震动
			}
		}
		
		lastTime = System.currentTimeMillis();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//发广播
		Intent intent = new Intent("com.broadcast.clickNotification");
		intent.putExtra("type", type);
		intent.putExtra("friendFromCount", friendFromCount);
		
		if(isGroupMessage){
			//SoftApplication.softApplication.setGroupMessageList(messageList);
		}else{
			SoftApplication.softApplication.setMessageList(messageList);
		}
		PendingIntent contentIntent = PendingIntent.getBroadcast(this, type, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, title,content, contentIntent);
		mNotificationManager.notify(type, notification);// 发送通知
	}
	
	
}
