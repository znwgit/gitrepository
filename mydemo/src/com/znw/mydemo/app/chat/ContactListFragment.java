package com.znw.mydemo.app.chat;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.RosterEntry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.znw.mydemo.R;
import com.znw.mydemo.app.basic.BaseListAdapter;
import com.znw.mydemo.app.basic.BasicFragment;
import com.znw.mydemo.app.basic.ViewHolder;
import com.znw.mydemo.app.chat.activity.ChatActivity;
import com.znw.mydemo.app.chat.activity.groupActivity;
import com.znw.mydemo.app.chat.entity.FriendInfo;
import com.znw.mydemo.app.task.UserJio.UserEntity;
import com.znw.mydemo.third.openfile.XmppConnection;
import com.znw.mydemo.third.openfile.XmppService;
import com.znw.mydemo.third.xlistview.XListView;
import com.znw.mydemo.third.xlistview.XListView.IXListViewListener;
import com.znw.mydemo.utils.sp.SharedPrefHelper;

public class ContactListFragment extends BasicFragment implements
		IXListViewListener {
	protected String response = "";
	private XListView xlvUser;
	private RelativeLayout rlgroup;
	UserAdapter userAdapter;
	List<UserEntity> list;
	List<FriendInfo> friendlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list_contact, container,
				false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();

	}

	@Override
	protected void initVariable() {
		xlvUser = (XListView) findViewById(R.id.xlv_xmpp_user_list);
		rlgroup=(RelativeLayout) findViewById(R.id.rl_group);
		list = new ArrayList<UserEntity>();
		friendlist=new ArrayList<FriendInfo>();
	}

	@Override
	protected void initContent() {
		rlgroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getContext(),groupActivity.class);
				startActivity(intent);
			}
		});
		xlvUser.setPullRefreshEnable(true);
		xlvUser.setPullLoadEnable(false);
		xlvUser.setXListViewListener(this);
		xlvUser.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getContext(), ChatActivity.class);
				intent.putExtra("xmppFriendName", list.get(position - 1).cname);
				intent.putExtra("xmppFriendCode",
						list.get(position - 1).nameCode);
				startActivity(intent);
			}

		});
		getXmppUserList();
	}

	@Override
	protected void initEvent() {

	}

	@Override
	protected Context getContext() {
		return getActivity();
	}

	@SuppressWarnings("rawtypes")
	class UserAdapter extends BaseListAdapter {

		public UserAdapter(Context context, List<UserEntity> list) {
			super(context, list);
		}

		@Override
		public View bindView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_user, null);
			}
			ImageView userImage = ViewHolder.get(convertView,
					R.id.civ_user_image);
			TextView userName = ViewHolder.get(convertView, R.id.tv_user_name);
			UserEntity userEntity = (UserEntity) getList().get(position);
			ImageLoader.getInstance().displayImage(userEntity.avatar,
					userImage,
					com.znw.mydemo.utils.image.ImageLoadOptions.getOptions());
			userName.setText(userEntity.cname);

			return convertView;
		}

	}

	private void onLoad() {
		xlvUser.stopRefresh();
		xlvUser.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		xlvUser.setPullLoadEnable(false);
		onLoad();
		list.clear();
		getXmppUserList();
	}

	@Override
	public void onLoadMore() {
	}

	// 获取所有用户
	public void getXmppUserList() {
		if (SharedPrefHelper.getInstance().getIsXmppLogin()) {
			List<RosterEntry> rosterEntryList=	XmppService.getAllEntries(XmppConnection.getConnection(false).getRoster());
			for (RosterEntry re : rosterEntryList) {
				UserEntity userEntity = new UserEntity();
				userEntity.cname = re.getName();
				userEntity.avatar = "http://image.photophoto.cn/nm-6/018/030/0180300244.jpg";
				userEntity.nameCode = re.getUser();
				list.add(userEntity);
				FriendInfo friendEntity = new FriendInfo();
				friendEntity.setCname(re.getName());
				friendEntity.cname = re.getName();
				friendEntity.setImageUrl("http://image.photophoto.cn/nm-6/018/030/0180300244.jpg");
				friendEntity.setFriendId(re.getUser()) ;
				friendlist.add(friendEntity);
				Log.i("好友信息：", re.getUser() + "-------------------------------"
						+ re.getName());
			}
			//保存好友信息到数据库
			saveFriendInfo(friendlist);
			userAdapter = new UserAdapter(getContext(), list);
			xlvUser.setAdapter(userAdapter);
			
//			List<RosterGroup> grouplist =XmppService.getGroups(XmppConnection.getConnection(false).getRoster());
//			List<RosterEntry> rosterEntryList1 = XmppService.getEntriesByGroup(XmppConnection.getConnection(false).getRoster(), "大学部");
//			for (int i = 0; i < grouplist.size(); i++) {
//				Log.i("所有组", grouplist.get(i).getName());
//			}
//			for (int i = 0; i < rosterEntryList1.size(); i++) {
//				Log.i("大学部所有好友", rosterEntryList1.get(i).getName());
//			}
		}
	}
	private void saveFriendInfo(final List<FriendInfo> list){
		if (list==null)return;
		new Thread(){
			@Override
		public void run()
		{
			super.run();
			
			try
			{
				appDataBaseHelper.deleteFriendInfoTabdle(db);
				String uid = mApplication.getOpenFireUid();
				for (int i = 0; i < list.size(); i++)
				{
					appDataBaseHelper.saveFriendInfo(db, list.get(i),uid);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}}.start();
	}
}
