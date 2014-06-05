package com.deppon.app.addressbook;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.deppon.app.addressbook.util.ActionBar;
import com.deppon.app.addressbook.util.ActionBar.AbstractAction;
import com.deppon.app.addressbook.util.BaseFragment;

/**
 * 首页.
 * 
 * @author 130126
 * 
 */
public class HomeGridviewFragement extends BaseFragment {
	private GridView gridview;
	int[] allMages = { R.drawable.img_1, R.drawable.img_2, R.drawable.img_3,
			R.drawable.img_4, R.drawable.img_5, R.drawable.img_6 };
	int[] allitem = { R.string.img1_title, R.string.img2_title,
			R.string.img3_title, R.string.img4_title, R.string.img5_title,
			R.string.img6_title };
	private String loginUser, token;
	private OnHomeGridViewSelectedListener listener;

	public interface OnHomeGridViewSelectedListener {
		public void onTongxunluSelected(int index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("HomeGridviewFragement____onCreateView");
		return inflater.inflate(R.layout.home_gridview, container, false);
	}

	private ActionBar head;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (OnHomeGridViewSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHomeGridViewSelectedListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("HomeGridviewFragement____onActivityCreated");
		head = (ActionBar) findViewById(R.id.homepage_head);
		head.init(R.string.app_name, true, false, 50);
		head.setLeftAction(new AbstractAction(R.drawable.logo) {
			@Override
			public void performAction(View view) {

			}
		});

		gridview = (GridView) this.getView().findViewById(R.id.GridView);

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

		SimpleAdapter saItem = new SimpleAdapter(this.getActivity(), meumList, // 数据源
				R.layout.more_item, // xml实现
				new String[] { "ItemImage", "ItemText" }, // 对应map的Key
				new int[] { R.id.ItemImage, R.id.ItemText }); // 对应R的Id
		// 添加Item到网格中
		gridview.setAdapter(saItem);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 添加点击事件
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listener.onTongxunluSelected(arg2);
			}
		});
	}

}
