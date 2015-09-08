package com.znw.mydemo.app.chat.entity;

import java.io.Serializable;

import com.znw.mydemo.third.openfile.XmppConnection;

import android.text.TextUtils;

@SuppressWarnings("serial")
public class FriendInfo implements Serializable {
	public String friendId;
	public String nickname;
	public String cname;
	public String mood;
	public String imageUrl;
	public int unreadMsgCount;// 未读消息数
	public String lastChatTime;// 私聊好友最后一次聊天的时间
	public String group_name;

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getNickname() {
		if (TextUtils.isEmpty(nickname))
			return friendId;
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getJid() {
		if (friendId == null)
			return null;
		return friendId + "@" + XmppConnection.SERVER_HOST;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

	public String getLastChatTime() {
		return lastChatTime;
	}

	public void setLastChatTime(String lastChatTime) {
		this.lastChatTime = lastChatTime;
	}

	@Override
	public String toString() {
		return "FriendInfo [friendId=" + friendId + ", nickname=" + nickname
				+ ", cname=" + cname + ", mood=" + mood + ", imageUrl="
				+ imageUrl + ", unreadMsgCount=" + unreadMsgCount
				+ ", lastChatTime=" + lastChatTime + ", group_name="
				+ group_name + "]";
	}

}
