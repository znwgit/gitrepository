package com.znw.mydemo.third.openfile;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import android.os.Handler;

public class MyChatManagerListener implements ChatManagerListener {

	private Handler handler;
	
	public MyChatManagerListener(Handler handler) {
		this.handler=handler;
	}

	@Override
	public void chatCreated(Chat chat, boolean arg1) {
		chat.addMessageListener(new MessageListener() {
			public void processMessage(Chat arg0, Message msg) {
				/** 通过handler转发消息 */
				
				android.os.Message m = handler.obtainMessage();
				m.obj = msg;
				m.sendToTarget();
			}
		});
	}

}
