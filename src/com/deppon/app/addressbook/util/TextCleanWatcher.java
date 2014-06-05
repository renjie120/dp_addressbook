package com.deppon.app.addressbook.util;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextCleanWatcher implements TextWatcher {

	// 缓存上一次文本框内是否为空
	private boolean isnull = true;
	private EditText et;
	private Drawable mIconSearchClear;
	private Drawable mIconSearchDefault;

	/**
	 * 进行编辑文本框的监听
	 * 
	 * @param et
	 *            编辑文本
	 * @param mIconSearchClear
	 *            叉叉的图片
	 * @param mIconSearchDefault
	 *            默认的图片，例如搜索，如果没有就是null
	 */
	public TextCleanWatcher(EditText et, Drawable mIconSearchClear,
			Drawable mIconSearchDefault) {
		this.et = et;
		this.mIconSearchClear = mIconSearchClear;
		this.mIconSearchDefault = mIconSearchDefault;
	}

	public void afterTextChanged(Editable s) {
		if (TextUtils.isEmpty(s)) {
			if (!isnull) {
				et.setCompoundDrawablesWithIntrinsicBounds(null, null,
						mIconSearchDefault, null);
				isnull = true;
			}
		} else {
			if (isnull) {
				et.setCompoundDrawablesWithIntrinsicBounds(null, null,
						mIconSearchClear, null);
				isnull = false;
			}
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	/**
	 * 随着文本框内容改变动态改变列表内容
	 */
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}
};
