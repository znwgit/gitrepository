package com.znw.mydemo.third.jpush;

import com.znw.mydemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jpush_test);
		TextView result=(TextView) findViewById(R.id.tv_result);
		String s=getIntent().getStringExtra("jpushresult");
		result.setText("推送内容"+s);
		
	}
}
