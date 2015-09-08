package com.znw.mydemo.app.basic;

import com.znw.mydemo.R;
import com.znw.mydemo.parse.jio.BasicJio;
import com.znw.mydemo.third.myself.CustomDialog;
import com.znw.mydemo.utils.debug.LogUtils;
import com.znw.mydemo.utils.string.StringUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.widget.TextView;

public class BasicAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {
	public CustomDialog dialog;
	private boolean showDialog = true;
	private String mMessage;
	private Context context;
	private OnTaskCompleteListener listener;
	private OnTaskCodeErrorListener codeErrorListener;
	private OnTaskFailListener failListener;

	public BasicAsyncTask() {
		super();
	}

	public BasicAsyncTask(String msg) {
		this();
		mMessage = msg;
	}

	public BasicAsyncTask(String msg, Context context) {
		this(msg);
		mMessage = msg;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		// 准备执行
		super.onPreExecute();
		if (context != null) {
			getProgressDialog(context);
		}
	}

	@Override
	protected Result doInBackground(Params... params) {
		return null;
	}

	@Override
	protected void onPostExecute(Result result) {
		// 执行完成
		super.onPostExecute(result);
		dismissDialog();
		if (result != null) {
			BasicJio basicJio = (BasicJio) result;
			if (!StringUtils.isEmpty(basicJio.getCode())) {
				if ("0".equals(basicJio.getCode())) {
					if (listener != null)
						listener.taskComplete(basicJio);
				} else {
					LogUtils.error(
							"BasicAsyncTask",
							"code=" + basicJio.getCode() + " "
									+ basicJio.getMessage());
					if (codeErrorListener != null)
						codeErrorListener.taskCodeError(basicJio);
				}
			} else {
				LogUtils.error("BasicAsyncTask", "返回code为空");
			}
		} else {
			LogUtils.error("BasicAsyncTask", "获取网络数据失败");
			if (failListener != null)
				failListener.taskFail();
		}
	}

	@Override
	protected void onCancelled() {
		// 取消执行
		super.onCancelled();
		dismissDialog();
	}

	public void setOnTaskCompListener(OnTaskCompleteListener listener) {
		this.listener = listener;
	}

	public void setOnTaskFailListener(OnTaskFailListener failListener) {
		this.failListener = failListener;
	}

	public void setOnTaskCodeErrorListener(
			OnTaskCodeErrorListener codeErrorListener) {
		this.codeErrorListener = codeErrorListener;
	}

	private OnKeyListener onKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				dismissDialog();
			}
			return false;
		}
	};

	public void setDialogOpen(boolean open) {
		showDialog = open;
	}

	private void getProgressDialog(Context context2) {
		if (showDialog) {
			if (dialog == null) {
				dialog = new CustomDialog(context, R.layout.layout_dialog,
						R.style.custom_dialog);
				if (!StringUtils.isEmpty(mMessage)) {
					TextView tvMessage = (TextView) dialog
							.findViewById(R.id.message);
					tvMessage.setText(mMessage);
				}
				// dialog.setCancelable(false);
				// dialog.setCanceledOnTouchOutside(false);
				// dialog.setOnKeyListener(onKeyListener);
				dialog.show();
			} else {
				dialog.show();
			}
		}
	}

	public void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog = null;
	}

}
