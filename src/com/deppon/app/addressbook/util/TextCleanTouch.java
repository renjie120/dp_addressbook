package com.deppon.app.addressbook.util;

import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

public class TextCleanTouch implements OnTouchListener {
	private EditText et;

	public TextCleanTouch(EditText et) {
		this.et = et;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			int curX = (int) event.getX();
			if (curX > v.getWidth() - 38 && !TextUtils.isEmpty(et.getText())) {
				et.setText("");
				int cacheInputType = et.getInputType();
				et.setInputType(InputType.TYPE_NULL);
				et.onTouchEvent(event);
				et.setInputType(cacheInputType);
				return true;
			}
			break;
		}
		return false;
	}
};