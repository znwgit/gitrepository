package com.znw.mydemo.third.umeng;

import android.content.Context;
import android.graphics.Bitmap;

import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.znw.mydemo.utils.string.StringUtils;

public class UmengShareUtil {
	/**
	 * 
	 * @param mController
	 * @param context
	 * @param title  分享标题
	 * @param content 分享内容
	 * @param imageUrl 分享图片
	 * @param targetUrl 分享链接
	 */
	public static void share(UMSocialService mController, Context context,
			String title, String content, String imageUrl, String targetUrl) {

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		// 设置qq空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		// 设置qq好友分享内容
		QQShareContent qqShareContent = new QQShareContent();
		// 设置新浪微博分享内容
		SinaShareContent sinaShareContent = new SinaShareContent();

		if (!StringUtils.isEmpty(content)) {
			// 设置分享文字
			weixinContent.setShareContent(content);
			qzone.setShareContent(content);
			qqShareContent.setShareContent(content);
			circleMedia.setShareContent(content);
			sinaShareContent.setShareContent(content);
		}
		if (!StringUtils.isEmpty(title)) {
			// 设置title
			weixinContent.setTitle(title);
			qzone.setTitle(title);
			qqShareContent.setTitle(title);
			circleMedia.setTitle(title);
			sinaShareContent.setTitle(title);
		}
		if (!StringUtils.isEmpty(targetUrl)) {
			// 设置分享内容跳转URL
			weixinContent.setTargetUrl(targetUrl);
			qzone.setTargetUrl(targetUrl);
			qqShareContent.setTargetUrl(targetUrl);
			circleMedia.setTargetUrl(targetUrl);
			sinaShareContent.setTargetUrl(targetUrl);
		}
		if (imageUrl != null) {
			// 设置分享图片
			UMImage image = new UMImage(context, imageUrl);
			weixinContent.setShareImage(image);
			qzone.setShareImage(image);
			qqShareContent.setShareImage(image);
			circleMedia.setShareImage(image);
			sinaShareContent.setShareImage(image);
		}

		mController.setShareMedia(weixinContent);
		mController.setShareMedia(qzone);
		mController.setShareMedia(qqShareContent);
		mController.setShareMedia(circleMedia);
		mController.setShareMedia(sinaShareContent);
	}
}
