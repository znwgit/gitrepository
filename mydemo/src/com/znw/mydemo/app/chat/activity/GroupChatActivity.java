package com.znw.mydemo.app.chat.activity;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.znw.mydemo.R;
import com.znw.mydemo.app.basic.BasicActivity;
import com.znw.mydemo.app.chat.entity.MessageEntity;
import com.znw.mydemo.third.myself.CustomDialog;
import com.znw.mydemo.third.openfile.ChatService;
import com.znw.mydemo.third.openfile.ChatService.ChatBinder;
import com.znw.mydemo.third.openfile.ChatService.OnNetConnectedListener;
import com.znw.mydemo.third.openfile.XmppConnection;
import com.znw.mydemo.third.xlistview.XListView;
import com.znw.mydemo.third.xlistview.XListView.IXListViewListener;
import com.znw.mydemo.utils.date.DateUtil;
import com.znw.mydemo.utils.debug.DebugUtils;
import com.znw.mydemo.utils.debug.LogUtils;
import com.znw.mydemo.utils.string.StringUtils;

public class GroupChatActivity extends BasicActivity implements OnClickListener,
		IXListViewListener {
	public static final String AT_GROUP = "@groupchat";
	private String  xmppGroupName;
	private Button btnSend;
	private EditText etSendMessage;
	protected String response = "";
	private XListView xlvGroupMessgae;
	private int currentPageIndex = 1;
	private String gid;//群组jid
	private MessageAdapter messageAdapter;
	private List<MessageEntity> chatMessageList = new ArrayList<MessageEntity>();
	private LayoutInflater mInflater;

	// 监听发送消息：文本、语音、图片
	private PacketListener packetListener;
	private Chat chat;
	private ViewHolder holder;

	private String openFireUid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_chat);
		Intent intent = getIntent();
		xmppGroupName = intent.getStringExtra("groupName");
		init();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MessageEntity entity = (MessageEntity) msg.obj;
			chatMessageList.add(entity);
			if (messageAdapter == null) {
				messageAdapter = new MessageAdapter();
				xlvGroupMessgae.setAdapter(messageAdapter);
			}
			messageAdapter.notifyDataSetChanged();
			xlvGroupMessgae.setSelection(xlvGroupMessgae.getCount()-1);
		}
	};

	@Override
	protected void initVariable() {
		initTopBarForLeft(xmppGroupName);
		mInflater = LayoutInflater.from(getContext());
		btnSend = (Button) findViewById(R.id.btn_send);
		etSendMessage = (EditText) findViewById(R.id.et_sendmessage);
		xlvGroupMessgae = (XListView) findViewById(R.id.xlv_group_message_list);
		gid = xmppGroupName;
	}

	@Override
	protected void initContent() {
		openFireUid = spHelper.getUserName();
		mApplication.getInstance().getChatBinder()
				.setOnNetConnectedListener(new OnNetConnectedListener() {

					@Override
					public void onNetConnected() {
						if (packetListener != null) {
							chat = XmppConnection.getConnection(false)
									.getChatManager()
									.createChat(xmppGroupName+AT_GROUP, null);
							PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
							XmppConnection.getConnection(false)
									.removePacketListener(packetListener);
							XmppConnection.getConnection(false)
									.addPacketListener(packetListener, filter);
						}
					}
				});
		chat = XmppConnection.getConnection(false).getChatManager()
				.createChat(xmppGroupName+AT_GROUP, null);
		xlvGroupMessgae.setPullRefreshEnable(false);
		xlvGroupMessgae.setPullLoadEnable(false);
		xlvGroupMessgae.setXListViewListener(this);
	}

	@Override
	protected void initEvent() {
		btnSend.setOnClickListener(this);
	}

	@Override
	protected Context getContext() {
		return this;
	}

	@Override
	protected Activity getActivity() {
		return this;
	}
	@Override
	protected void onResume() {
		super.onResume();
		mApplication.setCurrentGroupName(xmppGroupName);
		clearUnReadMsg();
		// 创建一个packet过滤器来监听来自一个特定用户的新的消息
		//PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		PacketFilter filter = new AndFilter(
				new PacketTypeFilter(Message.class), new FromContainsFilter(
						"同学"+AT_GROUP));
		if (packetListener == null) {
			packetListener = new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					final Message message = (Message) packet;
					if (message != null && message.getFrom() != null
							&& xmppGroupName != null
							&& !message.getFrom().contains(mApplication.getOpenFireUid())) {
						final String msgType = message.getMsgType();
						if (!StringUtils.isEmpty(msgType) && isNumeric(msgType)) {
							if (msgType.equals(Message.TYPE_TEXT)) {
								DebugUtils.print("msgBody====="
										+ message.getBody());
								MessageEntity messageEntity = new MessageEntity();
								messageEntity
										.setXmppUserImage("http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg");
								messageEntity.setMessageContent(message
										.getBody());
								messageEntity.setPosition("1");
								messageEntity.setMsgType(Message.TYPE_TEXT);
								messageEntity
										.setDate(StringUtils.isEmpty(message
												.getMsgTime()) == true ? DateUtil
												.getCurrentDateTime() : message
												.getMsgTime());
								showChatMsgEntity(messageEntity);
								String uid = spHelper.getUserName();
								appDataBaseHelper.saveGroupChatRecordInfo(db,xmppGroupName, uid,
										message.getFrom().split("@")[0],
										Message.TYPE_TEXT,
										messageEntity.getDate(), "",
										message.getBody(), "1", "",
										DateUtil.getCurrentDateTime(), "");
							} else if (msgType.equals(Message.TYPE_PICTURE)) {

							} else if (msgType.equals(Message.TYPE_VOICE)) {

							}
						}
					}
				}
			};
		}
		XmppConnection.getConnection(false).addPacketListener(packetListener,
				filter);
		getGroupChatMsgEntityHistroyList();
		messageAdapter = new MessageAdapter();
		xlvGroupMessgae.setAdapter(messageAdapter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mApplication.setCurrrentChatFriendId("");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			try {
				
				Message msg = new Message();
				msg.setBody(etSendMessage.getText().toString());
				// 定义成normal，在对象不在线时发送离线消息，消息存放在数据表ofoffline中
				msg.setType(Message.Type.groupchat);
				msg.setMsgType(Message.TYPE_TEXT);
				msg.setMsgTime(DateUtil.getCurrentDateTime());
				msg.setUsername(spHelper.getUserName());
				msg.setNickname(spHelper.getUserName());
				msg.setHeadurl("http://img0.bdstatic.com/img/image/shouye/mingxing0720.jpg");
				chat.sendMessage(msg);
				LogUtils.info("xmpp", "发送成功！" + "_____"
						+ etSendMessage.getText().toString());
				MessageEntity message = new MessageEntity();
				message.setXmppUserImage("http://img0.bdstatic.com/img/image/shouye/mingxing0720.jpg");
				message.setMessageContent(etSendMessage.getText().toString());
				message.setPosition("0");
				message.setMsgType("1");
				message.setDate(DateUtil.getCurrentDateTime());
				chatMessageList.add(message);
				if (messageAdapter == null) {
					messageAdapter = new MessageAdapter();
					xlvGroupMessgae.setAdapter(messageAdapter);
				} 
					messageAdapter.notifyDataSetChanged();
				xlvGroupMessgae.setSelection(xlvGroupMessgae.getCount() - 1);
				String uid = spHelper.getUserName();
				appDataBaseHelper.saveChatRecordInfo(db, uid,
						"", Message.TYPE_TEXT,
						message.getDate(), etSendMessage.getText().toString(),
						"", "0", "", DateUtil.getCurrentDateTime(), "");
				etSendMessage.setText("");
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@SuppressWarnings("rawtypes")
	class MessageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return chatMessageList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MessageEntity messageEntity = chatMessageList.get(position);
			LogUtils.info("messageEntity", messageEntity.toString());
			//if (convertView == null) {
				if ("0".equals(messageEntity.getPosition().trim())) {
					convertView = mInflater.inflate(R.layout.item_my_message,
							null);
				} else {
					convertView = mInflater.inflate(
							R.layout.item_friend_message, null);
				}
				holder = new ViewHolder();
				holder.myImage = (ImageView) convertView
						.findViewById(R.id.civ_my_user_image);
				holder.myMessage = (TextView) convertView
						.findViewById(R.id.tv_my_message);
				holder.friendImage = (ImageView) convertView
						.findViewById(R.id.civ_friend_user_image);
				holder.friendMessage = (TextView) convertView
						.findViewById(R.id.tv_friend_message);
				//convertView.setTag(holder);
			//} else {
			//	holder = (ViewHolder) convertView.getTag();
			//}
			if ("0".equals(messageEntity.getPosition().trim())) {
				ImageLoader.getInstance().displayImage(
						messageEntity.getXmppUserImage(),
						holder.myImage,
						com.znw.mydemo.utils.image.ImageLoadOptions
								.getOptions());
				holder.myMessage.setText(messageEntity.getMessageContent());
			} else {
				ImageLoader.getInstance().displayImage(
						messageEntity.getXmppUserImage(),
						holder.friendImage,
						com.znw.mydemo.utils.image.ImageLoadOptions
								.getOptions());
				holder.friendMessage.setText(messageEntity.getMessageContent());
			}
			return convertView;
		}
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
	}
	
	private static OnReadNewMsgListener onReadNewMsgListener;
	public interface OnReadNewMsgListener
	{
		public abstract void onReadNewMsg(boolean bool);
	}
	public static OnReadNewMsgListener getOnReadNewMsgListener()
	{
		return onReadNewMsgListener;
	}

	public static void setOnReadNewMsgListener(OnReadNewMsgListener onReadNewMsgListener)
	{
		GroupChatActivity.onReadNewMsgListener = onReadNewMsgListener;
	}

	private String getAccountId(String from) {
		if (StringUtils.isEmpty(from)) {
			return null;
		}

		if (from.contains("@")) {
			String[] split = from.split("\\@");
			return split[0];
		}
		return null;
	}

	class ViewHolder {
		TextView myMessage;
		TextView friendMessage;
		ImageView myImage;
		ImageView friendImage;
	}

	// 获取历史聊天记录
	private void getGroupChatMsgEntityHistroyList() {
		CustomDialog dialog = new CustomDialog(this, R.layout.layout_dialog,
				R.style.custom_dialog);
		dialog.show();
		if (!StringUtils.isEmpty(openFireUid)
				&& !StringUtils.isEmpty(xmppGroupName)) {
			List<MessageEntity> messageList = appDataBaseHelper
					.getGroupChatMsgEntityListFromDB(db, openFireUid,
							xmppGroupName);
			android.os.Message msg = secondHandler.obtainMessage(3,
					messageList);
			secondHandler.sendMessage(msg);
		}
		dialog.cancel();
	}

	Handler secondHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 3:// 显示历史记录
				List<MessageEntity> chatMsgEntityList = (List<MessageEntity>) msg.obj;
				showChatMsgEntityHistroyList(chatMsgEntityList);

				break;
			}
		}
	};

	private void showChatMsgEntityHistroyList(
			List<MessageEntity> chatMsgEntityList) {
		if (chatMsgEntityList == null) {
			return;
		}
		if (chatMessageList != null) {
			chatMessageList.clear();
		}
		for (MessageEntity chatMsgEntity : chatMsgEntityList) {
			showChatMsgEntity(chatMsgEntity);
		}
	}
	
	private void showChatMsgEntity(MessageEntity entity) {
		android.os.Message msg = handler.obtainMessage();
		msg.obj = entity;
		msg.sendToTarget();
	}
	
	/**
	 * 清除未读消息
	 */
	private void clearUnReadMsg() {
		DebugUtils.print("clearUnReadMsg fid="+gid);
		appDataBaseHelper.clearFriendInfoUnReadMsgCount(db,openFireUid,gid);
		ChatBinder chatBinder = mApplication.getChatBinder();
		if(chatBinder != null){
			chatBinder.clearSingleNotification(ChatService.NOTIFY_TYPE_NEW_MSG);
		}
		if(onReadNewMsgListener != null){
			onReadNewMsgListener.onReadNewMsg(true);
		}
	}
}
