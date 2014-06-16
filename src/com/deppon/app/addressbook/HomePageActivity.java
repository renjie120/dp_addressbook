package com.deppon.app.addressbook;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

/**
 * 首页.
 * 
 * @author 130126
 * 
 */
public class HomePageActivity extends TabActivity {
	private static final String Tab1 = "Tab1";
	private static final String Tab2 = "Tab2";
	private static final String Tab3 = "Tab3";
	private static final String Tab4 = "Tab4";
	private String token, loginUser, sessionId;
	private float screenHeight = 0;
	private float screenWidth = 0;
	public static float barH = 0.1f;

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	/**
	 * 得到屏幕的高宽.
	 * 
	 * @return
	 */
	public float[] getScreen2() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		return new float[] { dm.widthPixels, dm.heightPixels };
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		JPushInterface.init(getApplicationContext());
		Intent intent = getIntent();
		token = intent.getStringExtra("token");
		sessionId = intent.getStringExtra("sessionId");
		loginUser = intent.getStringExtra("loginUser");

		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];

		final TabHost tabHost = this.getTabHost();
		final TabWidget tabWidget = tabHost.getTabWidget();

		// 设置第一个tab页的对应的intent布局
		TabHost.TabSpec tabSpec = tabHost.newTabSpec(Tab1);
		tabSpec.setIndicator(composeLayout("首页", R.drawable.icon1));
		tabSpec.setContent(new Intent(HomePageActivity.this,
				NewHomeActivity.class).putExtra("token", token)
				.putExtra("sessionId", sessionId)
				.putExtra("loginUser", loginUser));
		tabHost.addTab(tabSpec);

		// 设置第二个tab页的对应的intent布局
		TabHost.TabSpec tabSpec2 = tabHost.newTabSpec(Tab2);
		tabSpec2.setIndicator(composeLayout("搜索", R.drawable.icon2));
		tabSpec2.setContent(new Intent(HomePageActivity.this,
				EmpQueryActivity.class).putExtra("token", token).putExtra(
				"loginUser", loginUser));
		tabHost.addTab(tabSpec2);

		// 设置第三个tab页的对应的intent布局
		TabHost.TabSpec tabSpec3 = tabHost.newTabSpec(Tab3);
		tabSpec3.setIndicator(composeLayout("工具", R.drawable.icon3));
		tabSpec3.setContent(new Intent(HomePageActivity.this,
				AboutActivity.class).putExtra("token", token).putExtra(
				"loginUser", loginUser));
		tabHost.addTab(tabSpec3);

		// 设置第四个tab页的对应的intent布局
		TabHost.TabSpec tabSpec4 = tabHost.newTabSpec(Tab3);
		tabSpec4.setIndicator(composeLayout("关于", R.drawable.icon4));
		tabSpec4.setContent(new Intent(HomePageActivity.this,
				AboutActivity.class).putExtra("token", token));
		tabHost.addTab(tabSpec4);

		// 这是对Tab标签本身的设置
		int height = (int) (screenHeight * barH);
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			// 设置高度、宽度，不过宽度由于设置为fill_parent，在此对它没效果
			tabWidget.getChildAt(i).getLayoutParams().height = height;
			View v = tabWidget.getChildAt(i);
			v.setBackgroundColor(getResources().getColor(
					R.color.homepage_bottom));
		}

		// 设置Tab变换时的监听事件
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				for (int i = 0; i < tabWidget.getChildCount(); i++) {
					View v = tabWidget.getChildAt(i);
					LinearLayout layout = (LinearLayout) v;
					ImageView iv = (ImageView) layout.getChildAt(0);
					if (tabHost.getCurrentTab() == i) {
						if (i == 0)
							iv.setImageDrawable(getResources().getDrawable(
									R.drawable.icon1_selected));
						else if (i == 1)
							iv.setImageDrawable(getResources().getDrawable(
									R.drawable.icon2_selected));
						else if (i == 2)
							iv.setImageDrawable(getResources().getDrawable(
									R.drawable.icon3_selected));
						else if (i == 3)
							iv.setImageDrawable(getResources().getDrawable(
									R.drawable.icon4_selected));
					} else {
						if (i == 0)
							iv.setImageDrawable(getResources().getDrawable(
									R.drawable.icon1));
						else if (i == 1)
							iv.setImageDrawable(getResources().getDrawable(
									R.drawable.icon2));
						else if (i == 2)
							iv.setImageDrawable(getResources().getDrawable(
									R.drawable.icon3));
						else if (i == 3)
							iv.setImageDrawable(getResources().getDrawable(
									R.drawable.icon4));
					}
				}
			}
		});

	}

	/**
	 * 动态生成下面的tab底部图标的布局.
	 * 
	 * @param s
	 * @param i
	 * @return
	 */
	public View composeLayout(String s, int i) {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		ImageView iv = new ImageView(this);
		iv.setImageResource(i);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 5, 0, 0);
		iv.setLayoutParams(lp);
		layout.addView(iv);

		TextView tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setSingleLine(true);
		tv.setText(s);
		tv.setTextSize(12);
		tv.setTextColor(Color.WHITE);
		layout.addView(tv);
		return layout;
	}
}
