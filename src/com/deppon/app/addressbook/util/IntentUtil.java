package com.deppon.app.addressbook.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentUtil {
	/**
	 * 打电话.
	 * 
	 * @param v
	 */
	public static void call(Context ct, String p) {
		if (p != null && !"".equals(p.trim())) {
			Intent intent = new Intent(Intent.ACTION_DIAL,
					Uri.parse("tel:" + p));
			ct.startActivity(intent);
		}
	}

	/**
	 * 发送短信.
	 * 
	 * @param v
	 */
	public static void sendMessage(Context ct, String p) {
		if (p != null && !"".equals(p.trim())) {
			Uri uri = Uri.parse("smsto:" + p);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			ct.startActivity(intent);
		}
	}

	/**
	 * 发送邮件.
	 * 
	 * @param v
	 */
	public static void sendEmail(Context ct, String p) {
		if (p != null && !"".equals(p.trim())) {
			Intent data = new Intent(Intent.ACTION_SENDTO);
			data.setData(Uri.parse("mailto:" + p));
			ct.startActivity(data);
		}
	}
}
