package com.znw.mydemo.app.chat.entity;

public class MessageEntity {
	private String xmppUserName;
	private String xmppUserJid;
	private String xmppUserImage;
	private String date;//日期
	private String position;// 0自己1好友
	private String messageContent;
	private String msgType;// 0文本1图片2语音
	public String getXmppUserName() {
		return xmppUserName;
	}
	public void setXmppUserName(String xmppUserName) {
		this.xmppUserName = xmppUserName;
	}
	public String getXmppUserJid() {
		return xmppUserJid;
	}
	public void setXmppUserJid(String xmppUserJid) {
		this.xmppUserJid = xmppUserJid;
	}
	public String getXmppUserImage() {
		return xmppUserImage;
	}
	public void setXmppUserImage(String xmppUserImage) {
		this.xmppUserImage = xmppUserImage;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	@Override
	public String toString() {
		return "MessageEntity [xmppUserName=" + xmppUserName + ", xmppUserJid="
				+ xmppUserJid + ", xmppUserImage=" + xmppUserImage + ", date="
				+ date + ", position=" + position + ", messageContent="
				+ messageContent + ", msgType=" + msgType + "]";
	}
	
}
