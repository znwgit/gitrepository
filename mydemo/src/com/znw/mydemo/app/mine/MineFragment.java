package com.znw.mydemo.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.znw.mydemo.MainActivity;
import com.znw.mydemo.R;
import com.znw.mydemo.app.basic.BasicFragment;

public class MineFragment extends BasicFragment implements OnClickListener {
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my, container, false);
	}

	@Override
	protected void initVariable() {
		initTopBarForOnlyTitle("我");
		findViewById(R.id.tv_exit).setOnClickListener(this);
	}

	@Override
	protected void initContent() {

	}

	@Override
	protected void initEvent() {

	}

	@Override
	protected Context getContext() {
		return getActivity();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_exit:
			mApplication.quit();
			mApplication.logout();
			/*
			 * JPushInterface.setAlias(getContext(),"",new TagAliasCallback() {
			 * 
			 * @Override public void gotResult(int responseCode, String alias,
			 * Set<String> tags) { if (responseCode == 0) {
			 * LogUtils.info("[JPush]", "别名清空成功=" + alias); } else {
			 * LogUtils.info("[JPush]", "清空responseCode=" + responseCode); } }
			 * });
			 */
			in = new Intent(getContext(), MainActivity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(in);

			break;
		}

	}

}
