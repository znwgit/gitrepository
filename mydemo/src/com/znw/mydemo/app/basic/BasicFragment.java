package com.znw.mydemo.app.basic;

import com.znw.mydemo.R;
import com.znw.mydemo.application.SoftApplication;
import com.znw.mydemo.third.myself.HeaderLayout;
import com.znw.mydemo.third.myself.HeaderLayout.HeaderStyle;
import com.znw.mydemo.third.myself.HeaderLayout.onLeftImageButtonClickListener;
import com.znw.mydemo.third.myself.HeaderLayout.onRightImageButtonClickListener;
import com.znw.mydemo.utils.db.AppDataBaseHelper;
import com.znw.mydemo.utils.sp.SharedPrefHelper;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public abstract class BasicFragment extends Fragment {
	// 用于退出
	public static boolean isQuit = false;

	public SoftApplication mApplication;
	protected AppDataBaseHelper appDataBaseHelper;
	protected SQLiteDatabase db;

	/* 以下三个变量用于接口调用 */
	public Handler mHandler;
	protected Runnable runnable;
	protected ProgressBar progressbar;

	/* 页面中经常会用到的变量 */
	protected Bundle bundle;
	protected String response = "";
	protected StringBuffer sb;
	protected boolean isTransBundle = true;
	protected Intent in = null;
	protected View contentView;
	/**
	 * 公用的Header布局
	 */
	public HeaderLayout mHeaderLayout;
	public LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mInflater = LayoutInflater.from(getActivity());
		mApplication = SoftApplication.getInstance();
		appDataBaseHelper=AppDataBaseHelper.getInstance(getActivity());
		db=appDataBaseHelper.getWritableDatabase();
	}

	protected void init() {

		initVariable();
		initContent();
		initEvent();
	}

	protected abstract void initVariable();

	protected abstract void initContent();

	protected abstract void initEvent();

	protected abstract Context getContext();

	protected void showToast(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void showToast(int resId) {
		Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
	}

	public View findViewById(int paramInt) {
		return getView().findViewById(paramInt);
	}

	/**
	 * 只有title initTopBarLayoutByTitle
	 * 
	 * @Title: initTopBarLayoutByTitle
	 * @throws
	 */
	public void initTopBarForOnlyTitle(String titleName) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName);
	}

	public void initTopBarForOnlyTitle(String titleName, Integer color) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName);
		mHeaderLayout.setHaderBackground(R.drawable.home_top_title_bg);
		mHeaderLayout.setTitleColor(color);

	}

	/**
	 * 初始化标题栏-带左右按钮-右边是文字
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBothRtv(String titleName, String rightText,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightButton(titleName, rightText, listener);
	}

	/**
	 * 初始化标题栏-带左右按钮-右边是文字 + 左边按钮点击事件
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBothRtv(String titleName, String rightText,
			onRightImageButtonClickListener listener,
			onLeftImageButtonClickListener leftListener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, leftListener);
		mHeaderLayout.setTitleAndRightButton(titleName, rightText, listener);
	}

	/**
	 * 初始化标题栏-带左右按钮-右边是图片
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBothRib(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}

	/**
	 * 初始化标题栏-带左右按钮-右边是图片 + 左边按钮点击事件
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBoth(String titleName, int rightDrawableId,
			onRightImageButtonClickListener rightListener,
			onLeftImageButtonClickListener leftListener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, leftListener);
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				rightListener);
	}

	/**
	 * 初始化标题栏-带左右按钮-右边是图片+HaderBackground
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBothRibBg(String titleName, int rightDrawableId,
			int leftDrawableId, int headerColorId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.NO_TITLE_BG);
		mHeaderLayout.setTitleAndLeftImageButton(titleName, leftDrawableId,
				new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
		mHeaderLayout.setHaderBackground(headerColorId);
	}

	/**
	 * 只有左边按钮和Title initTopBarLayout
	 * 
	 * @throws
	 */
	public void initTopBarForLeft(String titleName) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, new OnLeftButtonClickListener());
	}

	/**
	 * 只有左边按钮和Title +左边按钮点击事件
	 * 
	 * @throws
	 */
	public void initTopBarForLeft(String titleName,
			onLeftImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, listener);
	}

	/**
	 * 右边+title initTopBarForRight
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForRight(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}

	// 左边按钮的点击事件
	public class OnLeftButtonClickListener implements
			onLeftImageButtonClickListener {

		@Override
		public void onClick() {
			getActivity().finish();
		}
	}

}
