package com.znw.mydemo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jivesoftware.smack.RosterEntry;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.znw.mydemo.app.basic.BasicActivity;
import com.znw.mydemo.app.home.HomeActivity;
import com.znw.mydemo.app.task.UserJio.UserEntity;
import com.znw.mydemo.third.openfile.XmppConnection;
import com.znw.mydemo.third.openfile.XmppService;
import com.znw.mydemo.third.umeng.UmengShareUtil;
import com.znw.mydemo.utils.sp.SharedPrefHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BasicActivity {
	private EditText loginName, loginPassword;
	private TextView login;
	private SharedPrefHelper sp;
	List<UserEntity> list;
	private UMSocialService mController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_index);
		init();
		initThirdLoginAndShare();//初始化友盟支持平台
	}

	private void initThirdLoginAndShare() {
		mController=mApplication.getInstance().getUMSocialService();
		// 首先在您的Activity中添加如下成员变量
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.TENCENT);
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.SINA);
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
				"1104846766", "15cqdiFpWhIUlCHv");
		qqSsoHandler.addToSocialSDK();
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				"1104846766", "15cqdiFpWhIUlCHv");
		qZoneSsoHandler.addToSocialSDK();

		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx6a2bfc5435cf325d";
		String appSecret = "0ef07f3f0b2c370661059a20fb228b55";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(getActivity(), appID, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appID,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	@Override
	protected void initVariable() {
		loginName = (EditText) findViewById(R.id.et_login_a);
		loginPassword = (EditText) findViewById(R.id.et_login_p);
		login = (TextView) findViewById(R.id.tv_login);
		sp = SharedPrefHelper.getInstance();
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sp.setUserName(loginName.getText().toString());
				sp.setUserPassword(loginPassword.getText().toString());
				mApplication.setOpenFireUid(loginName.getText().toString());// 登录openFire服务器的ID
				SharedPrefHelper.getInstance().setIsLogin(true);
				Intent intent = new Intent(getContext(), HomeActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void initContent() {
	}
	
	@Override
	protected void initEvent() {
		findViewById(R.id.tv_feixiang).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmengShareUtil.share(mController,getContext(),"郑州投资10亿建“金蛋” 被评中国十大最丑建筑","有媒体评出中国十大最丑建筑，其中投资近10亿元的河南艺术中心“有幸”上榜。该艺术中心是河南省建国以来投资规模最大、设计水平最高的重点文化设施建设项目，是河南省倾力打造的标志性文化工程，投资近10亿元，总建筑面积7.7万多平方米。因其圆形外观别致、且身披“土豪金”，当地人笑称之为“金蛋”。","http://img01.imgcdc.com/news/zh_cn/domestic/945/20150907/r_20343535_2015090713393913609200.jpg","http://news.china.com/domestic/945/20150907/20343535.html");
						// 是否只有已登录用户才能打开分享选择页
						mController.openShare(getActivity(), false);
					}
				});
		findViewById(R.id.tv_weibologin).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						weiboLogin();
					}
		});

		findViewById(R.id.tv_weixinlogin).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						weixinLogin();
					}
		});
		findViewById(R.id.tv_qqlogin).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				qqLogin();
			}
		});
	}

	@Override
	protected Context getContext() {
		return this;
	}

	@Override
	protected Activity getActivity() {
		return this;
	}

	// 获取所有用户
	public void getXmppUserList() {
		if (SharedPrefHelper.getInstance().getIsXmppLogin()) {
			List<RosterEntry> rosterEntryList = XmppService
					.getAllEntries(XmppConnection.getConnection(false)
							.getRoster());
			for (RosterEntry re : rosterEntryList) {
				UserEntity userEntity = new UserEntity();
				userEntity.cname = re.getName();
				userEntity.avatar = "http://image.photophoto.cn/nm-6/018/030/0180300244.jpg";
				userEntity.nameCode = re.getUser();
				list.add(userEntity);
				Log.i("好友信息：", re.getUser() + "-------------------------------"
						+ re.getName());
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	
	public void weiboLogin(){
		mController.doOauthVerify(getContext(),
				SHARE_MEDIA.SINA, new UMAuthListener() {
					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
					}

					@Override
					public void onComplete(Bundle value,
							SHARE_MEDIA platform) {
						if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
							// "授权成功.",
							mController.getPlatformInfo(getContext(),SHARE_MEDIA.SINA,
									new UMDataListener() {
										@Override
										public void onStart() {
											// "获取平台数据开始...",
										}

										@Override
										public void onComplete(int status,Map<String, Object> info) {
											if (status == 200&& info != null) {
												StringBuilder sb = new StringBuilder();
												Set<String> keys = info.keySet();
												for (String key : keys) {
													sb.append(key+ "="+ info.get(key).toString()+ "\r\n");
												}
												Log.d("TestData",sb.toString());
												Toast.makeText(getContext(),sb.toString(),Toast.LENGTH_LONG).show();
											} else {
												Log.d("TestData","发生错误："+ status);
											}
										}
									});
						} else {
							Toast.makeText(MainActivity.this,"授权失败", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
					}

					@Override
					public void onStart(SHARE_MEDIA platform) {
					}
				});
	}
	
	public void weixinLogin(){
		mController.doOauthVerify(getContext(),SHARE_MEDIA.WEIXIN, new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						//Toast.makeText(getContext(), "授权开始",Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,SHARE_MEDIA platform) {
						//Toast.makeText(getContext(), "授权错误",Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(Bundle value,SHARE_MEDIA platform) {
						// Toast.makeText(getContext(), "授权完成",Toast.LENGTH_SHORT).show();
						// 获取相关授权信息
						mController.getPlatformInfo(getContext(),SHARE_MEDIA.WEIXIN,
								new UMDataListener() {
									@Override
									public void onStart() {
										// Toast.makeText(MainActivity.this, "获取平台数据开始...",Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onComplete(int status,Map<String, Object> info) {
										if (status == 200&& info != null) {
											StringBuilder sb = new StringBuilder();
											Set<String> keys = info.keySet();
											for (String key : keys) {
												sb.append(key+ "="+ info.get(key).toString()+ "\r\n");
											}
											Log.d("TestData",sb.toString());
											Toast.makeText(getContext(),sb.toString(),Toast.LENGTH_LONG).show();
										} else {
											Log.d("TestData","发生错误："+ status);
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(getContext(), "授权取消",
								Toast.LENGTH_SHORT).show();
					}
			});
	}
	public void qqLogin(){
		mController.doOauthVerify(getContext(), SHARE_MEDIA.QQ,new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA platform) {
				// Toast.makeText(getContext(), "授权开始",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(SocializeException e,SHARE_MEDIA platform) {
				//Toast.makeText(getContext(), "授权错误",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(Bundle value,
					SHARE_MEDIA platform) {
				// Toast.makeText(getContext(), "授权完成", Toast.LENGTH_SHORT).show();
				// 获取相关授权信息
				mController.getPlatformInfo(MainActivity.this,
						SHARE_MEDIA.QQ, new UMDataListener() {
							@Override
							public void onStart() {
								// Toast.makeText(MainActivity.this,"获取平台数据开始...",Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onComplete(int status,Map<String, Object> info) {
								if (status == 200&& info != null) {
									StringBuilder sb = new StringBuilder();
									Set<String> keys = info.keySet();
									for (String key : keys) {sb.append(key+ "="+ info.get(key).toString()+ "\r\n");
									}
									Log.d("TestData",sb.toString());
									Toast.makeText(getContext(),sb.toString(),Toast.LENGTH_LONG).show();
								} else {
									Log.d("TestData", "发生错误："+ status);
								}
							}
						});
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				//Toast.makeText(getContext(), "授权取消",Toast.LENGTH_SHORT).show();
			}
		});
	}
}
