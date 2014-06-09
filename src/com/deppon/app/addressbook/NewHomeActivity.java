package com.deppon.app.addressbook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.deppon.app.addressbook.bean.ServerResult;
import com.deppon.app.addressbook.util.ActionBar;
import com.deppon.app.addressbook.util.HttpRequire;

/**
 * 首页.
 * 
 * @author 130126
 * 
 */
public class NewHomeActivity extends FragmentActivity implements
		HomeGridviewFragement.OnHomeGridViewSelectedListener,
		AddressListFragment.OnAddressListRefreshListener,
		EmpDetailFragment.EmpDetailListRefreshListener, OnTouchListener,
		OnGestureListener {
	private GestureDetector detector;
	private ActionBar head;
	private String loginUser, token;
	private static final int DIALOG_KEY = 0;
	private ProgressDialog dialog;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("正在查询...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	private LinearLayout all;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.new_home);
		all = (LinearLayout) findViewById(R.id.all);
		detector = new GestureDetector((OnGestureListener) this);

		all.setLongClickable(true);

		all.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return detector.onTouchEvent(event);
			}

		});
		token = getIntent().getStringExtra("token");
		loginUser = getIntent().getStringExtra("loginUser");
		// 初始化页面的时候，加载Grid布局的片段.
		if (findViewById(R.id.tab_content) != null) {
			if (savedInstanceState != null) {
				return;
			}
			HomeGridviewFragement firstFragment = new HomeGridviewFragement();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.tab_content, firstFragment).commit();
		}
	}

	@Override
	public void onTongxunluSelected(int index) {
		switch (index) {
		// 新闻动态
		case 0:
			break;
		// 待办事项
		case 1:

			break;
		// 公告
		case 2:

			break;
		// 通讯录
		case 3:
			// 根目录是总裁部门.
			onShowAddressList(104);
			break;
		// BI
		case 4:
			Toast.makeText(getApplicationContext(), "开发中，敬请期待...",
					Toast.LENGTH_SHORT).show();
			break;
		// 邮件
		case 5:
			Toast.makeText(getApplicationContext(), "开发中，敬请期待...",
					Toast.LENGTH_SHORT).show();
			break;
		// 更多
		case 6:
			Toast.makeText(getApplicationContext(), "开发中，敬请期待...",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}

	}

	/**
	 * 显示通讯录列表.
	 */
	@Override
	public void onShowAddressList(int root) {
		Bundle args = new Bundle();
		args.putInt(AddressListFragment.ROOT_ID, root);
		args.putString(AddressListFragment.USERID, loginUser);
		args.putString(AddressListFragment.TOKEN, token);
		AddressListFragment newFragment = new AddressListFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_left_in,
				R.anim.slide_left_out);

		newFragment.setArguments(args);
		transaction.replace(R.id.tab_content, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * 显示人员详情.
	 */
	@Override
	public void onShowEmpdetail(int empId) {
		Bundle args = new Bundle();
		ServerResult result;
		try {
			result = HttpRequire.getEmpDetail(empId + "", loginUser, token);
			// 如果返回数据不是1，就说明出现异常.
			if (result.getErrorCode() < 0) {
				Toast.makeText(getApplicationContext(), "对不起查询人员出现异常....",
						Toast.LENGTH_SHORT).show();
			}
			// 否则就进行文件解析处理.
			else {
				JSONObject json3 = result.getData();
				args.putString(EmpDetailFragment.EMPINFO, json3.toJSONString());
				EmpDetailFragment newFragment = new EmpDetailFragment();
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.setCustomAnimations(R.anim.slide_left_in,
						R.anim.slide_left_out);
				newFragment.setArguments(args);
				transaction.replace(R.id.tab_content, newFragment);
				transaction.addToBackStack(null);

				transaction.commit();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 回退片段到前一个页面.
	 */
	@Override
	public void back() {
		FragmentManager fm = getSupportFragmentManager();
		fm.popBackStack();
		System.out.println("getBackStackEntryCount:::"
				+ fm.getBackStackEntryCount());
	}

	@Override
	public boolean onDown(MotionEvent e) {
		System.out.println("onDown");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// e1 触摸的起始位置，e2 触摸的结束位置，velocityX X轴每一秒移动的像素速度（大概这个意思） velocityY
		if (e2.getX() - e1.getX() > 50 ) {
			FragmentManager fm = getSupportFragmentManager();
			fm.popBackStack();
		}
		// if (Math.abs(e2.getX() - e1.getX()) > 50) {
		// Toast.makeText(getApplicationContext(), "向左滑动", Toast.LENGTH_SHORT)
		// .show();
		// }
		// if (Math.abs(e2.getY() - e1.getY()) > 50) {
		// Toast.makeText(getApplicationContext(), "向上滑动", Toast.LENGTH_SHORT)
		// .show();
		// }
		// if (e2.getY() - e1.getY() > 50) {
		// Toast.makeText(getApplicationContext(), "向下滑动", Toast.LENGTH_SHORT)
		// .show();
		// }
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		System.out.println("onLongPress");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		System.out.println("onScroll");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		System.out.println("onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		System.out.println("onSingleTapUp");
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return onTouchEvent(arg1);
	}

	@Override
	public void leftBack(MotionEvent event) {
		detector.onTouchEvent(event);
	}
}
