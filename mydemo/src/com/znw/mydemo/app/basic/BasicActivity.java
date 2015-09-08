package com.znw.mydemo.app.basic;

import java.util.regex.Pattern;


import com.znw.mydemo.R;
import com.znw.mydemo.application.SoftApplication;
import com.znw.mydemo.third.myself.CustomDialog;
import com.znw.mydemo.third.myself.HeaderLayout;
import com.znw.mydemo.third.myself.HeaderLayout.HeaderStyle;
import com.znw.mydemo.third.myself.HeaderLayout.onLeftImageButtonClickListener;
import com.znw.mydemo.third.myself.HeaderLayout.onRightImageButtonClickListener;
import com.znw.mydemo.utils.db.AppDataBaseHelper;
import com.znw.mydemo.utils.sp.SharedPrefHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public abstract class BasicActivity extends FragmentActivity {
	// 用于退出
	public static boolean isQuit = false;

	public SoftApplication mApplication;
	private ProgressDialog progressDialog;
	public SharedPrefHelper spHelper;

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

	protected HeaderLayout mHeaderLayout;
	protected String title;
	protected int totalPage;
	protected ImageButton back;

	protected NotificationManager mNotificationManager;
	protected AppDataBaseHelper appDataBaseHelper;
	protected SQLiteDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UIStyle.setFullWindowStyle(this);
		mApplication = SoftApplication.getInstance();
		mApplication.addActivity(this);
		spHelper=SharedPrefHelper.getInstance();
	}

	protected void init() {
		initPost();
		initVariable();
		initContent();
		initEvent();
	}

	protected void initPost() {
		bundle = getIntent().getExtras();
		if (bundle == null) {
			isTransBundle = false;
			bundle = new Bundle();
		}
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		appDataBaseHelper = AppDataBaseHelper.getInstance(this);
		db = appDataBaseHelper.getWritableDatabase();

	}

	protected abstract void initVariable();

	protected abstract void initContent();

	protected void initEvent() {
		if (back != null) {
			back.setVisibility(View.VISIBLE);
			back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().finish();
				}
			});
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mApplication.finishActivity(this);
	}

	protected abstract Context getContext();

	protected abstract Activity getActivity();

	protected void showToast(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void showToast(int resId) {
		Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
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

	/**
	 * 初始化标题栏-带左右按钮-右边是文字
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBothRtv(String titleName, String text,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightButton(titleName, text, listener);
	}

	/**
	 * 初始化标题栏-带左右按钮-右边是文字 + 左边按钮点击事件
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBothRtv(String titleName, String text,
			onRightImageButtonClickListener listener,
			onLeftImageButtonClickListener leftListener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, leftListener);
		mHeaderLayout.setTitleAndRightButton(titleName, text, listener);
	}

	/**
	 * 初始化标题栏-带左右按钮-右边是文字-右边文字颜色
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBothRtv(String titleName, String text, int color,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightButton(titleName, text, listener);
		mHeaderLayout.setRightButtonColor(color);
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
	 * 初始化标题栏-带左右按钮-右边是图片+左边按钮点击事件
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBothRib(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener,
			onLeftImageButtonClickListener leftListener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, leftListener);
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
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
	 * 只有左边按钮和Title +HaderBackground
	 * 
	 * @throws
	 */
	public void initTopBarForLeft(String titleName, int headerColorId) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);

		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.selector_btn_back, new OnLeftButtonClickListener());
		mHeaderLayout.setHaderBackground(headerColorId);
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

	public void showProgressDialog(String msg) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(BasicActivity.this);
		progressDialog.setMessage(msg);
		try {
			progressDialog.show();
		} catch (BadTokenException exception) {
			exception.printStackTrace();
		}
	}

	public ProgressDialog createProgressDialog(String msg) {
		ProgressDialog progressDialog = new ProgressDialog(BasicActivity.this);
		progressDialog.setMessage(msg);
		return progressDialog;
	}

	/**
	 * 隐藏正在加载的进度条
	 * 
	 */
	public void dismissProgressDialog() {
		if (null != progressDialog && progressDialog.isShowing() == true) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public static boolean isNumeric(String str) {
		if (str == null || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 本段代码用来处理如果输入法还显示的话就消失掉输入法
	 */
	protected void dismissSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManage = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManage.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showYesNoDialog(String title, String message,
			final Intent yesin, final Intent noin) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						getContext().startActivity(yesin);
						getActivity().finish();
					}
				});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						if (noin != null)
							getContext().startActivity(noin);
						else
							getActivity().finish();
					}
				});
		builder.show();
	}

	protected void quit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("退出提示");
		builder.setMessage("确定退出客户端？");
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						try {
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("isExit", true);
							startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});
		builder.show();
	}

	protected void showCallPhoneDialog(final String tel) {
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.setMessage("是否要进行拨号？").setTitle("拨号")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						Intent intent = new Intent();

						// 系统默认的action，用来打开默认的电话界面
						intent.setAction(Intent.ACTION_DIAL);
						intent.setData(Uri.parse("tel:" + tel));
						startActivity(intent);
					}
				}).create();

		alertDialog.show();
	}

	/**
	 * 用户自定义dialog
	 * 
	 * @param view
	 */
	protected CustomDialog dialog;

	protected void customDialog(final String msgString,
			View.OnClickListener listener) {
		dialog = new CustomDialog(this, R.layout.layout_dialog,
				R.style.custom_dialog);
		if (!dialog.isShowing()) {
			dialog.show();
		}

	}

	protected void dismissCustomDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
