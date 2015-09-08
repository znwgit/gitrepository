package com.znw.mydemo.app.chat.activity;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.RosterGroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.znw.mydemo.R;
import com.znw.mydemo.app.basic.BasicActivity;
import com.znw.mydemo.third.openfile.XmppConnection;
import com.znw.mydemo.third.openfile.XmppService;

public class groupActivity extends BasicActivity implements OnClickListener {

	private ListView lvChatGroup;
	private List<RosterGroup> rosterGroupList;
	private List<String> groupList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		init();
	}

	@Override
	protected void initVariable() {
		lvChatGroup = (ListView) findViewById(R.id.lv_chat_group);
	}

	@Override
	protected void initContent() {
		initTopBarForOnlyTitle("群组");
		rosterGroupList = XmppService.getGroups(XmppConnection.getConnection(
				false).getRoster());
		for (int i = 0; i < rosterGroupList.size(); i++) {
			Log.i("所有组", rosterGroupList.get(i).getName());
			groupList.add(rosterGroupList.get(i).getName());
		}
		GroupAdapter groupAdapter = new GroupAdapter(getContext());
		lvChatGroup.setAdapter(groupAdapter);
		lvChatGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(getContext(), GroupChatActivity.class);
				intent.putExtra("groupName",groupList.get(position) );
				startActivity(intent);
			}
		});
	}

	@Override
	protected void initEvent() {
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:

		}
	}

	class GroupAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public GroupAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return groupList.size();
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
			convertView = mInflater.inflate(R.layout.item_group, null);
			TextView groupName = (TextView) convertView
					.findViewById(R.id.tv_group_name);
			groupName.setText(groupList.get(position));
			return convertView;
		}

	}
}
