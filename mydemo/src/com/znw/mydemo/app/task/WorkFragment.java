package com.znw.mydemo.app.task;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.znw.mydemo.R;
import com.znw.mydemo.app.basic.BaseListAdapter;
import com.znw.mydemo.app.basic.BasicAsyncTask;
import com.znw.mydemo.app.basic.BasicFragment;
import com.znw.mydemo.app.basic.OnTaskCompleteListener;
import com.znw.mydemo.app.basic.ViewHolder;
import com.znw.mydemo.app.task.UserJio.UserEntity;
import com.znw.mydemo.net.HttpRequestManager;
import com.znw.mydemo.parse.ParseUtil;
import com.znw.mydemo.parse.jio.BasicJio;
import com.znw.mydemo.third.myself.CircularImageView;
import com.znw.mydemo.third.xlistview.XListView;
import com.znw.mydemo.third.xlistview.XListView.IXListViewListener;
import com.znw.mydemo.utils.debug.DebugUtils;
import com.znw.mydemo.utils.string.StringUtils;

public class WorkFragment extends BasicFragment implements IXListViewListener{
	protected String response = "";
	private XListView xlvUser;
	private int currentPageIndex = 1;
	UserAdapter userAdapter;
	List<UserEntity> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_main, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	@Override
	protected void initVariable() {
		initTopBarForOnlyTitle("沟通");
		xlvUser = (XListView) findViewById(R.id.xlv_user);
		list = new ArrayList<UserEntity>();
	}

	@Override
	protected void initContent() {
		
		xlvUser.setPullRefreshEnable(true);
		xlvUser.setPullLoadEnable(true);
		xlvUser.setXListViewListener(this);
		getUserList();
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Context getContext() {
		return getActivity();
	}

	private void getUserList() {
		final UserTask task = new UserTask("", getContext());
		task.execute("15712924882");
		task.setOnTaskCompListener(new OnTaskCompleteListener() {

			@Override
			public void taskComplete(BasicJio basicJio) {
				UserJio jio = (UserJio) basicJio;
				if (jio.getUserEntity() != null) {
					UserEntity user1 = new UserEntity();
					user1.cname = "znw";
					user1.avatar = "http://image.photophoto.cn/nm-6/018/030/0180300244.jpg";
					list.add(user1);
					UserEntity user2 = new UserEntity();
					user2.cname = "天天高星";
					user2.avatar = null;
					list.add(user2);
					UserEntity user3 = new UserEntity();
					user3.cname = "啥快递发多少克";
					user3.avatar = "http://img0.bdstatic.com/img/image/shouye/bizhi0525.jpg";
					list.add(user3);
					UserEntity user4 = new UserEntity();
					user4.cname = "啥快递发多少克";
					user4.avatar = "http://img0.bdstatic.com/img/image/shouye/bizhi0525.jpg";
					list.add(user4);
					UserEntity user5 = new UserEntity();
					user5.cname = "啥快递发多少克";
					user5.avatar = "http://img0.bdstatic.com/img/image/shouye/bizhi0525.jpg";
					list.add(user5);
					UserEntity user6 = new UserEntity();
					user6.cname = "啥快递发多少克";
					user6.avatar = "http://img0.bdstatic.com/img/image/shouye/bizhi0525.jpg";
					list.add(user6);
					UserEntity user7 = new UserEntity();
					user7.cname = "啥快递发多少克";
					user7.avatar = "http://img0.bdstatic.com/img/image/shouye/bizhi0525.jpg";
					list.add(user7);
					UserEntity user8 = new UserEntity();
					user8.cname = "啥快递发多少克";
					user8.avatar = "http://img0.bdstatic.com/img/image/shouye/bizhi0525.jpg";
					list.add(user8);
					list.add(jio.getUserEntity());
					userAdapter = new UserAdapter(getContext(), list);
					xlvUser.setPullLoadEnable(true);
					xlvUser.setAdapter(userAdapter);
				}
			}
		});
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
			CircularImageView userImage = ViewHolder.get(convertView,
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

	class User implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String username;
		private String userImage;
	}

	private void onLoad() {
		xlvUser.stopRefresh();
		xlvUser.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		xlvUser.setPullLoadEnable(false);
		currentPageIndex = 1;
		onLoad();
		list.clear();
		getUserList();
	}

	@Override
	public void onLoadMore() {
		onLoad();
		if (currentPageIndex<3)
		{
			currentPageIndex++;
			int loadMorePage=currentPageIndex;
			UserEntity user = new UserEntity();
			user.avatar = "http://img0.bdstatic.com/img/image/shouye/mingxing0720.jpg";
			user.cname = "周杰伦";
			list.add(user);
			userAdapter.setList(list);
		}else{
			showToast("没有更多数据了");
			xlvUser.setPullLoadEnable(false);
		}
	}

	class UserTask extends BasicAsyncTask<String, String, UserJio> {
		public UserTask(String msg, Context context) {
			super(msg, context);
		}

		@Override
		protected UserJio doInBackground(String... params) {
			response = HttpRequestManager.create().getUserInfo(params[0]);
			DebugUtils.print("response=" + response);
			if (StringUtils.isEmpty(response)) {
				return null;
			}
			UserJio userJio = ParseUtil.parseObject(response, UserJio.class);
			UserEntity userEntity = ParseUtil.parseObjectSelf(response, "data",
					UserEntity.class);
			userJio.setUserEntity(userEntity);
			return userJio;
		}

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
