package com.deppon.app.addressbook;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.deppon.app.addressbook.util.ActionBar;
import com.deppon.app.addressbook.util.ActionBar.AbstractAction;

/**
 * 首页.
 * 
 * @author 130126
 * 
 */
public class NewHomeActivity extends BaseActivity {
	private ActionBar head;
	private GridView gridview;
	int[] allMages = { R.drawable.img_1, R.drawable.img_2, R.drawable.img_3,
			R.drawable.img_4, R.drawable.img_5, R.drawable.img_6 };
	int[] allitem = { R.string.img1_title, R.string.img2_title,
			R.string.img3_title, R.string.img4_title, R.string.img5_title,
			R.string.img6_title };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.new_home);
		gridview = (GridView) findViewById(R.id.GridView);
		head = (ActionBar) findViewById(R.id.homepage_head);
		head.init(R.string.app_name, true, false, 50);
		head.setLeftAction(new AbstractAction(R.drawable.logo) {
			@Override
			public void performAction(View view) {

			}
		});
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < allitem.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", allMages[i]);
			map.put("ItemText", getText(allitem[i]).toString());
			meumList.add(map);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.img_7);
		map.put("ItemText", "");
		meumList.add(map);

		SimpleAdapter saItem = new SimpleAdapter(this, meumList, // 数据源
				R.layout.more_item, // xml实现
				new String[] { "ItemImage", "ItemText" }, // 对应map的Key
				new int[] { R.id.ItemImage, R.id.ItemText }); // 对应R的Id
		// 添加Item到网格中
		gridview.setAdapter(saItem);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}
}
