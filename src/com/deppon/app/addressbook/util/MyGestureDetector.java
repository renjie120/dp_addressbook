package com.deppon.app.addressbook.util;

import com.deppon.app.addressbook.R;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyGestureDetector implements OnTouchListener, OnGestureListener {
	private FragmentActivity act;
	private GestureDetector detector;
	private View view;

	public MyGestureDetector(FragmentActivity act, View view) {
		this.act = act;
		this.view = view;
	}

	public void onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
	}

	public void setRightFling() {
		detector = new GestureDetector((OnGestureListener) this);
		view.setLongClickable(true);

		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return detector.onTouchEvent(event);
			}

		});
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// e1 触摸的起始位置，e2 触摸的结束位置，velocityX X轴每一秒移动的像素速度（大概这个意思） velocityY
		if (e2.getX() - e1.getX() > 50) {
			FragmentManager fm = act.getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			// 打开碎片的动画
			transaction.setCustomAnimations(R.anim.slide_right_out,
					R.anim.slide_right_in);
			fm.popBackStack();
			transaction.commit();
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return act.onTouchEvent(arg1);
	}
}
