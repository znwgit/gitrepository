package com.znw.mydemo.app.home;

import java.util.ArrayList;
import java.util.List;



import com.znw.mydemo.R;
import com.znw.mydemo.app.basic.BasicActivity;
import com.znw.mydemo.app.chat.CmnnFragment;
import com.znw.mydemo.app.mine.MineFragment;
import com.znw.mydemo.app.task.WorkFragment;
import com.znw.mydemo.application.SoftApplication;
import com.znw.mydemo.third.openfile.ChatService;
import com.znw.mydemo.third.openfile.ChatService.ChatBinder;
import com.znw.mydemo.third.openfile.XmppConnection;
import com.znw.mydemo.utils.sp.SharedPrefHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends BasicActivity implements OnClickListener {
	private List<Fragment> fragments;
	private List<ImageView> imageViews;
	private List<TextView> textViews;
	private List<Drawable> drab_n;
	private List<Drawable> drab_p;

	private int currentTab = 0; // 当前Tab页面索引
	private SharedPrefHelper sp;
	boolean isXmppLogin = false;
	private boolean isChatServiceBind;
	private ServiceConnection chatConnection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = SharedPrefHelper.getInstance();
		xmppLogin(sp.getUserName(), sp.getUserPassword());
		init();

		// JPushInterface.setAlias(HomeActivity.this, sp.getUserName(),
		// new TagAliasCallback() {
		// @Override
		// public void gotResult(int responseCode, String alias,
		// Set<String> tags) {
		// if (responseCode == 0) {
		// LogUtils.info(HomeActivity.this.getClass(),
		// "别名设置成功=" + alias);
		// } else {
		// LogUtils.info(HomeActivity.this.getClass(),
		// "responseCode=" + responseCode);
		// }
		// }
		// });
	}

	/**
	 * 登录xmpp服务器
	 */
	private void xmppLogin(final String account, final String pwd) {
		new Thread() {

			@Override
			public void run() {
				super.run();
				try {
					XmppConnection.closeConnection();
					// 登录
					isXmppLogin = XmppConnection.login(account, pwd);
					SharedPrefHelper.getInstance().setIsXmppLogin(isXmppLogin);
					mApplication.setOpenFireUid(account);//登录openFire服务器的ID
					bindChatService();
				} catch (Exception e) {
					System.out.println(e);
					XmppConnection.closeConnection();
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	protected void initVariable() {
		fragments = new ArrayList<Fragment>();
		imageViews = new ArrayList<ImageView>();
		textViews = new ArrayList<TextView>();
		drab_n = new ArrayList<Drawable>();
		drab_p = new ArrayList<Drawable>();
	}

	@Override
	protected void initContent() {
		fragments.add(new WorkFragment());
		fragments.add(new CmnnFragment());
		fragments.add(new WorkFragment());
		fragments.add(new WorkFragment());
		fragments.add(new MineFragment());
		// fragments.add(new CmnnFragment());
		// fragments.add(new SubjeckFragment());
		// fragments.add(new ToolFragment());
		// fragments.add(new MyFragment());
		imageViews.add((ImageView) findViewById(R.id.iv_tab_a));
		imageViews.add((ImageView) findViewById(R.id.iv_tab_b));
		imageViews.add((ImageView) findViewById(R.id.iv_tab_c));
		imageViews.add((ImageView) findViewById(R.id.iv_tab_d));
		imageViews.add((ImageView) findViewById(R.id.iv_tab_e));

		drab_n.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn01_n));
		drab_n.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn02_n));
		drab_n.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn03_n));
		drab_n.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn04_n));
		drab_n.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn05_n));

		drab_p.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn01_p));
		drab_p.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn02_p));
		drab_p.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn03_p));
		drab_p.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn04_p));
		drab_p.add(getContext().getResources().getDrawable(
				R.drawable.home_bottom_btn05_p));

		textViews.add((TextView) findViewById(R.id.tv_tab_a));
		textViews.add((TextView) findViewById(R.id.tv_tab_b));
		textViews.add((TextView) findViewById(R.id.tv_tab_c));
		textViews.add((TextView) findViewById(R.id.tv_tab_d));
		textViews.add((TextView) findViewById(R.id.tv_tab_e));

		showFragmet(currentTab);
	}

	@Override
	protected void initEvent() {
		super.initEvent();
		findViewById(R.id.fl_home_tab1).setOnClickListener(this);
		findViewById(R.id.fl_home_tab2).setOnClickListener(this);
		findViewById(R.id.fl_home_tab3).setOnClickListener(this);
		findViewById(R.id.fl_home_tab4).setOnClickListener(this);
		findViewById(R.id.fl_home_tab5).setOnClickListener(this);
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
		case R.id.fl_home_tab1:
			currentTab = 0;
			break;
		case R.id.fl_home_tab2:
			currentTab = 1;
			break;
		case R.id.fl_home_tab3:
			currentTab = 2;
			break;
		case R.id.fl_home_tab4:
			currentTab = 3;
			break;
		case R.id.fl_home_tab5:
			currentTab = 4;
			break;
		}
		showFragmet(currentTab);
	}

	private void showFragmet(int idx) {
		Fragment fragment = fragments.get(idx);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		getCurrentFragment().onPause(); // 暂停当前tab
		// getCurrentFragment().onStop(); // 暂停当前tab

		if (fragment.isAdded()) {
			// fragment.onStart(); // 启动目标tab的onStart()
			fragment.onResume(); // 启动目标tab的onResume()
		} else {
			ft.add(R.id.ll_content, fragment);
		}
		showTab(idx); // 显示目标tab
		ft.commit();
	}

	private Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	/**
	 * 切换tab
	 * 
	 * @param idx
	 */
	private void showTab(int idx) {
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();

			if (idx == i) {
				ft.show(fragment);
				imageViews.get(i).setImageDrawable(drab_p.get(i));
				textViews.get(i).setTextColor(
						getContext().getResources().getColor(R.color.white));
			} else {
				ft.hide(fragment);
				imageViews.get(i).setImageDrawable(drab_n.get(i));
				textViews.get(i)
						.setTextColor(
								getContext().getResources().getColor(
										R.color.white_999));
			}
			ft.commit();
		}
		currentTab = idx; // 更新目标tab为当前tab
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			showSystemExitDialog();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	private void showSystemExitDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this)
				.setMessage("您确定退出吗？").setTitle("退出应用")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
						mApplication.quit();
						dialog.dismiss();
					}
				}).create();

		alertDialog.show();
	}
	@Override
	protected void onPause() {
		super.onPause();
		// JPushInterface.onPause(getContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		// JPushInterface.onResume(getContext());
		if (SharedPrefHelper.getInstance().getIsXmppLogin()) {
			bindChatService();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindChatService();
	}
	/**
	 * 绑定聊天的服务
	 */
	private void bindChatService() {
		Intent intent = new Intent(this, ChatService.class);

		if (!isChatServiceBind) {

			chatConnection = new ServiceConnection() {
				@Override
				public void onServiceDisconnected(ComponentName name) {

				}

				@Override
				public void onServiceConnected(ComponentName name,
						IBinder service) {
					// 服务连接成功之后可以通过IBinder调用服务
					ChatBinder chatBinder = (ChatBinder) service;
					SoftApplication.softApplication.setChatBinder(chatBinder);
				}
			};
			isChatServiceBind = bindService(intent, chatConnection,
					BIND_AUTO_CREATE);
		}
	}

	/**
	 * 解绑聊天的服务
	 */
	private void unbindChatService() {
		// 如果服务存在且需要关闭的话就需要再此进行关闭
		if (isChatServiceBind && null != chatConnection) {
			unbindService(chatConnection);
			isChatServiceBind = false;
		}
	}
}
