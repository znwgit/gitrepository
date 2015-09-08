package com.znw.mydemo.third.myself;

import com.znw.mydemo.R;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class CustomDialog extends Dialog {
	// 设置默认高度为160，宽度120，并且可根据屏幕像素密度自动进行大小调整
	private static int default_width = 120; // 默认宽度
	private static int default_height = 90;// 默认高度

	public CustomDialog(Context context, int layout, int style) {
		this(context, default_width, default_height, layout, style);
	}

	public CustomDialog(Context context, int width, int height, int layout,
			int style) {
		super(context, style);

		// set content
		setContentView(layout);

		// set window params
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();

		// set width,height by density and gravity
		float density = getDensity(context);
		params.width = (int) (width * density);
		params.height = (int) (height * density);
		params.gravity = Gravity.CENTER;

		window.setAttributes(params);
	}

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}

	/*
	 * 自定义弹窗使用 
	 * CustomDialog dialog1 = new CustomDialog(this,
	 * R.layout.layout_dialog, R.style.custom_dialog); 
	 * dialog1.show();
	 * CustomDialog dialog2 = new CustomDialog(this, 180, 180,
	 * R.layout.layout_dialog, R.style.custom_dialog);
	 *  dialog2.show();
	 *  //显示Dialog TextView mMessage = (TextView)
	 * dialog2.findViewById(R.id.message); mMessage.setText("加载中...");
	 */

}
