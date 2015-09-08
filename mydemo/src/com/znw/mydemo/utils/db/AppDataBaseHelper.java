package com.znw.mydemo.utils.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.znw.mydemo.app.chat.entity.FriendInfo;
import com.znw.mydemo.app.chat.entity.MessageEntity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDataBaseHelper extends SQLiteOpenHelper {

	// 数据库名称，开启注释把数据库放到Sdcard上
	private static final String DB_NAME = "financial.db";
	/**
	 * 数据库版本，升级时修改
	 */
	private static final int DB_VERSION = 1;

	private AppDataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	private static AppDataBaseHelper dbOpenHelper = null;

	/**
	 * 得到数据库实例
	 * 
	 * @param context
	 * @return 数据库的SQLiteOpenHelper对象
	 */
	public static synchronized AppDataBaseHelper getInstance(Context context) {
		if (dbOpenHelper == null) {
			dbOpenHelper = new AppDataBaseHelper(context);
			return dbOpenHelper;
		} else {
			return dbOpenHelper;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建好友表friend_info
		db.execSQL("create table if not exists friend_info(friend_id TEXT,uid TEXT,nickname TEXT,jid TEXT,mood TEXT,imageUrl TEXT,isSended TEXT,unreadMsgCount INTEGER,lastChatTime TEXT,group_name TEXT)");
		// 创建一个表，用来存储和当前帐号已经聊过的好友.
		db.execSQL("create table if not exists friend_info_chatting(friend_id TEXT,uid TEXT,nickname TEXT,jid TEXT,mood TEXT,imageUrl TEXT,isSended TEXT,unreadMsgCount INTEGER,lastChatTime TEXT,group_name TEXT)");
		// 创建私聊记录的表
		db.execSQL("create table if not exists chat_record(id INTEGER PRIMARY KEY autoincrement, uid TEXT,friend_id TEXT,msgType TEXT,msgTime TEXT,mineMsg TEXT,friendMsg TEXT,isComeMsg TEXT,nativePath TEXT,saveTime TEXT,voiceTime TEXT)");
		// 创建群聊记录的表
		db.execSQL("create table if not exists group_chat_record(id INTEGER PRIMARY KEY autoincrement, group_name TEXT,uid TEXT,friend_id TEXT,msgType TEXT,msgTime TEXT,mineMsg TEXT,friendMsg TEXT,isComeMsg TEXT,nativePath TEXT,saveTime TEXT,voiceTime TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * 
	 * @param db
	 * @param uid
	 *            登陆者本人的ID
	 * @param friend_id
	 *            好友的ID
	 * @param msgType
	 *            消息类型0文本，1语音，2图片
	 * @param msgTime
	 *            发送消息的时间
	 * @param mineMsg
	 *            我的消息
	 * @param friendMsg
	 *            朋友发的消息
	 * @param isComeMsg
	 *            是否是朋友的消息 0:我的消息，1:朋友发的消息
	 * @param nativePath
	 *            语音或图片的本地路径
	 */
	public synchronized int saveChatRecordInfo(SQLiteDatabase db, String uid,
			String friend_id, String msgType, String msgTime, String mineMsg,
			String friendMsg, String isComeMsg, String nativePath,
			String saveTime, String voiceTime) {
		int id = 0;
		db.beginTransaction();
		try {
			String[] object = new String[] { uid, friend_id, msgType, msgTime,
					mineMsg, friendMsg, isComeMsg, nativePath, saveTime,
					voiceTime };
			String sql = "insert into chat_record(uid, friend_id, msgType, msgTime,mineMsg, friendMsg, isComeMsg, nativePath, saveTime, voiceTime) values(?,?,?,?,?,?,?,?,?,?)";
			db.execSQL(sql, object);

			String sql2 = "select * from chat_record where uid = ? and friend_id = ? and msgType = ? and msgTime = ? and mineMsg = ? and friendMsg = ? and isComeMsg = ? and nativePath = ? and saveTime = ?";
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql2, new String[] { uid, friend_id,
						msgType, msgTime, mineMsg, friendMsg, isComeMsg,
						nativePath, saveTime });
				while (cursor.moveToNext()) {
					id = cursor.getInt(cursor.getColumnIndex("id"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return id;
	}

	/**
	 * 保存群聊信息
	 * 
	 * @param db
	 * @param groupName
	 *            群组名字
	 * @param uid
	 *            登陆者本人的ID
	 * @param friend_id
	 *            好友的ID
	 * @param msgType
	 *            消息类型0文本，1语音，2图片
	 * @param msgTime
	 *            发送消息的时间
	 * @param mineMsg
	 *            我的消息
	 * @param friendMsg
	 *            朋友发的消息
	 * @param isComeMsg
	 *            是否是朋友的消息 0:我的消息，1:朋友发的消息
	 * @param nativePath
	 *            语音或图片的本地路径
	 */
	public synchronized int saveGroupChatRecordInfo(SQLiteDatabase db,
			String groupName, String uid, String friend_id, String msgType,
			String msgTime, String mineMsg, String friendMsg, String isComeMsg,
			String nativePath, String saveTime, String voiceTime) {
		int id = 0;
		db.beginTransaction();
		try {
			String[] object = new String[] { groupName,uid, friend_id, msgType, msgTime,
					mineMsg, friendMsg, isComeMsg, nativePath, saveTime,
					voiceTime };
			String sql = "insert into group_chat_record(group_name,uid, friend_id, msgType, msgTime,mineMsg, friendMsg, isComeMsg, nativePath, saveTime, voiceTime) values(?,?,?,?,?,?,?,?,?,?)";
			db.execSQL(sql, object);

			String sql2 = "select * from group_chat_record where group_name= ? and  uid = ? and friend_id = ? and msgType = ? and msgTime = ? and mineMsg = ? and friendMsg = ? and isComeMsg = ? and nativePath = ? and saveTime = ?";
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql2, new String[] { groupName,uid, friend_id,
						msgType, msgTime, mineMsg, friendMsg, isComeMsg,
						nativePath, saveTime });
				while (cursor.moveToNext()) {
					id = cursor.getInt(cursor.getColumnIndex("id"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return id;
	}

	/**
	 * 根据自己的id和别人的id号获取聊天记录
	 * 
	 * @param db
	 * @param uid
	 * @param fid
	 * @return
	 */

	public List<MessageEntity> getChatMsgEntityListFromDB(SQLiteDatabase db,
			String openFireUid, String string) {
		List<MessageEntity> list = new ArrayList<MessageEntity>();
		String sql = "select * from chat_record where uid = ? and friend_id = ? order by saveTime asc";// 根据saveTime进行降序显示
		Cursor cursor = db.rawQuery(sql, new String[] { openFireUid, string });
		try {
			while (cursor.moveToNext()) {
				MessageEntity message = new MessageEntity();
				message.setDate(cursor.getString(cursor
						.getColumnIndex("msgTime")));
				String position = cursor.getString(cursor
						.getColumnIndex("isComeMsg"));
				message.setPosition(position);
				if ("0".equals(position)) {
					// 自己的消息
					message.setMessageContent(cursor.getString(cursor
							.getColumnIndex("mineMsg")));
					message.setXmppUserImage("http://img0.bdstatic.com/img/image/shouye/mingxing0720.jpg");
				} else {
					// 好友的消息
					message.setMessageContent(cursor.getString(cursor
							.getColumnIndex("friendMsg")));
					message.setXmppUserImage("http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg");
				}
				list.add(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 根据自己的id和群组名字获取聊天记录
	 * 
	 * @param db
	 * @param uid 我的openfile id
	 * @param groupName 群组名字
	 * @return
	 */

	public List<MessageEntity> getGroupChatMsgEntityListFromDB(SQLiteDatabase db,
			String openFireUid, String groupName) {
		List<MessageEntity> list = new ArrayList<MessageEntity>();
		String sql = "select * from group_chat_record where uid = ? and group_name = ? order by saveTime asc";// 根据saveTime进行降序显示
		Cursor cursor = db.rawQuery(sql, new String[] { openFireUid, groupName });
		try {
			while (cursor.moveToNext()) {
				MessageEntity message = new MessageEntity();
				message.setDate(cursor.getString(cursor
						.getColumnIndex("msgTime")));
				String position = cursor.getString(cursor
						.getColumnIndex("isComeMsg"));
				message.setPosition(position);
				if ("0".equals(position)) {
					// 自己的消息
					message.setMessageContent(cursor.getString(cursor
							.getColumnIndex("mineMsg")));
					message.setXmppUserImage("http://img0.bdstatic.com/img/image/shouye/mingxing0720.jpg");
				} else {
					// 好友的消息
					message.setMessageContent(cursor.getString(cursor
							.getColumnIndex("friendMsg")));
					message.setXmppUserImage("http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg");
				}
				list.add(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return list;
	}
	

	/**
	 * 取出数据库中未读的消息数，以map返回
	 * 
	 * @param db
	 * @param uid
	 * @return
	 */
	public Map<String, Integer> getFriendInfoUnReadMsgMap(SQLiteDatabase db,
			String uid) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			db.beginTransaction();
			String sql2 = "select * from friend_info where uid = ? and unreadMsgCount > 0";
			Cursor cursor = db.rawQuery(sql2, new String[] { uid });
			try {
				while (cursor.moveToNext()) {
					map.put(cursor
							.getString(cursor.getColumnIndex("friend_id")),
							cursor.getInt(cursor
									.getColumnIndex("unreadMsgCount")));
				}
			} finally {
				cursor.close();
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return map;
	}

	/**
	 * 
	 * @param db
	 * @param uid
	 *            用户的openFireId
	 * @param key
	 *            朋友的openFireId(手机号)
	 * @param unReadMsgCount
	 *            未读消息数
	 * @param lastChatTime
	 *            最后一次交谈的时间
	 */
	public void updateFriendInfounReadMsgCount(SQLiteDatabase db, String uid,
			String key, Integer unReadMsgCount, String lastChatTime) {
		try {
			db.beginTransaction();
			String sql1 = "select * from friend_info where friend_id = ? and uid = ?";
			Cursor cursor = db.rawQuery(sql1, new String[] { key, uid });
			try {
				if (cursor.moveToNext()) {
					String sql = "update friend_info SET unreadMsgCount = ?,lastChatTime = ? WHERE friend_id = ? and uid = ?";
					Object[] bindArgs = new Object[] { unReadMsgCount,
							lastChatTime, key, uid };
					db.execSQL(sql, bindArgs);
				}
			} finally {
				cursor.close();
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * 将该好友的未读消息清空为0
	 * 
	 * @param db
	 * @param openFireUid
	 * @param fid
	 */
	public void clearFriendInfoUnReadMsgCount(SQLiteDatabase db,
			String openFireUid, String fid) {
		try {
			db.beginTransaction();
			String sql1 = "select * from friend_info where friend_id = ? and uid = ?";
			Cursor cursor = db
					.rawQuery(sql1, new String[] { fid, openFireUid });
			try {
				if (cursor.moveToNext()) {
					String sql = "update friend_info SET unreadMsgCount = 0 WHERE friend_id = ? and uid = ?";
					Object[] bindArgs = new Object[] { fid, openFireUid };
					db.execSQL(sql, bindArgs);
				}
			} finally {
				cursor.close();
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		// clearFriendInfoChatUnReadMsgCount(db,openFireUid,fid);
	}

	/**
	 * 保存好友的个人信息，没有好友就创建好友，有好友就更新好友信息
	 * 
	 * @param friendInfo
	 */
	public void saveFriendInfo(SQLiteDatabase db, FriendInfo friendInfo,
			String openFireUid) {
		if (friendInfo == null)
			return;
		try {
			db.beginTransaction();
			String sql1 = "select * from friend_info where friend_id = ? and uid = ?";

			Cursor cursor = db.rawQuery(sql1,
					new String[] { friendInfo.getFriendId(), openFireUid });
			try {
				if (!cursor.moveToNext()) {// 没有这个好友，则添加好友
					String sql = "insert into friend_info(friend_id,uid,nickname,jid,mood,imageUrl,isSended,unreadMsgCount,lastChatTime,group_name) values(?,?,?,?,?,?,?,?,?,?)";
					Object[] bindArgs = new Object[] {
							friendInfo.getFriendId(), openFireUid,
							friendInfo.getNickname(), friendInfo.getJid(),
							friendInfo.getMood(), friendInfo.getImageUrl(),
							"0", 0, "" + System.currentTimeMillis(),
							friendInfo.getGroup_name() };
					db.execSQL(sql, bindArgs);
				} else {// 有这个好友，则更新好友的信息
					String sql = "update friend_info SET nickname = ?,jid = ?,mood = ?,imageUrl = ?,group_name = ? WHERE friend_id = ? and uid = ?";
					Object[] bindArgs = new Object[] {
							friendInfo.getNickname(), friendInfo.getJid(),
							friendInfo.getMood(), friendInfo.getImageUrl(),
							friendInfo.getGroup_name(),
							friendInfo.getFriendId(), openFireUid };
					db.execSQL(sql, bindArgs);
				}
			} finally {
				cursor.close();
			}
			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * 删除好友信息表 清空表数据
	 * 
	 * @param db
	 */
	public void deleteFriendInfoTabdle(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			String sql1 = "delete from friend_info";
			db.execSQL(sql1);
			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
}
