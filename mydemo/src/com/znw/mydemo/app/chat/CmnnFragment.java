package com.znw.mydemo.app.chat;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.znw.mydemo.R;
import com.znw.mydemo.app.basic.BasicFragment;

public class CmnnFragment extends BasicFragment implements OnClickListener {
	private TextView chatTV, contTV;
	private List<Fragment> fragments;
	private int currentTab = 0; // 当前Tab页面索引

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_cmnn, container, false);
	}

	@Override
	protected void initVariable() {
		fragments = new ArrayList<Fragment>();
		chatTV = (TextView) findViewById(R.id.tv_cmnn_chat);
		contTV = (TextView) findViewById(R.id.tv_cmnn_contacts);
	}

	@Override
	protected void initContent() {
		// fragments.add(new MessageListFragment());
		fragments.add(new ContactListFragment());
		showFragmet(currentTab);
	}

	@Override
	protected void initEvent() {
		chatTV.setOnClickListener(this);
		contTV.setOnClickListener(this);
	}

	@Override
	protected Context getContext() {
		return getActivity();
	}

	private void showFragmet(int idx) {
		Fragment fragment = fragments.get(idx);
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();

		getCurrentFragment().onPause(); // 暂停当前tab
		// getCurrentFragment().onStop(); // 暂停当前tab

		if (fragment.isAdded()) {
			// fragment.onStart(); // 启动目标tab的onStart()
			fragment.onResume(); // 启动目标tab的onResume()
		} else {
			ft.add(R.id.ll_content_cmnn, fragment);
		}
		showTab(idx); // 显示目标tab
		ft.commit();

	}

	/**
	 * 切换tab
	 * 
	 * @param idx
	 */
	private void showTab(int idx) {
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			FragmentTransaction ft = getActivity().getSupportFragmentManager()
					.beginTransaction();

			if (idx == i) {
				ft.show(fragment);
			} else {
				ft.hide(fragment);
			}
			ft.commit();
		}
		currentTab = idx; // 更新目标tab为当前tab
	}

	private Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cmnn_chat:
			chatTV.setTextColor(getResources().getColor(
					R.color.chat_top_text_color));
			chatTV.setBackgroundResource(R.drawable.border_radius_left_p);
			contTV.setTextColor(getResources().getColor(R.color.white));
			contTV.setBackgroundResource(R.drawable.border_radius_right_n);
			currentTab = 0;
			break;
		case R.id.tv_cmnn_contacts:
			contTV.setTextColor(getResources().getColor(
					R.color.chat_top_text_color));
			contTV.setBackgroundResource(R.drawable.border_radius_right_p);
			chatTV.setTextColor(getResources().getColor(R.color.white));
			chatTV.setBackgroundResource(R.drawable.border_radius_left_n);
			currentTab = 1;
			break;

		default:
			break;
		}
	showFragmet(currentTab);
	}

}
