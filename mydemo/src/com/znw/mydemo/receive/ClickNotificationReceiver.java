package com.znw.mydemo.receive;


import java.util.List;

import org.jivesoftware.smack.packet.Message;

import com.znw.mydemo.app.chat.activity.ChatActivity;
import com.znw.mydemo.application.SoftApplication;
import com.znw.mydemo.third.openfile.ChatService;
import com.znw.mydemo.utils.string.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ClickNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent data) {
		String actionName=	data.getAction();
		int type = data.getIntExtra("type", -1);
		int friendFromCount = data.getIntExtra("friendFromCount", -1);
		if("com.broadcast.clickNotification".equals(actionName)){
			if(type==ChatService.NOTIFY_TYPE_NEW_MSG){//未读消息-私聊
				if(friendFromCount == 1){//消息來自一个人
					Intent intent1 = new Intent(context, ChatActivity.class);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					List<Message> messageList = SoftApplication.softApplication.getMessageList();
					if(messageList != null && !messageList.isEmpty()){
						intent1.putExtra("xmppFriendCode", messageList.get(0).getFrom());
						intent1.putExtra("xmppFriendName", StringUtils.getAccountId(messageList.get(0).getFrom()));
					}
					context.startActivity(intent1);
				}else if(friendFromCount > 1){//消息來自多个人
					
				}
			}
		}
	}
}

