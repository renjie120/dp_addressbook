package com.deppon.app.addressbook;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.deppon.app.addressbook.util.ActionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AboutActivity extends BaseActivity {
	@ViewInject(R.id.about_head)
	private ActionBar head;
	@ViewInject(R.id.version_line)
	private LinearLayout version_line;
	@ViewInject(R.id.shoushi_line)
	private LinearLayout shoushi_line;
	@ViewInject(R.id.quit)
	private ImageView quit;
	private float screenHeight = 0;
	private float screenWidth = 0;

	@OnClick({ R.id.version_line })
	public void seeVersion(View v) { // 注入添加单击按钮
		Toast.makeText(AboutActivity.this, "you clicked button!", // 进行短暂的提示！
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		ViewUtils.inject(this); // 进行注入
		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];
		head.init(R.string.about_title, false, false, 50);
	}
}
