package com.deppon.app.addressbook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.deppon.app.addressbook.bean.ServerResult;
import com.deppon.app.addressbook.util.HttpRequire;
import com.deppon.app.addressbook.util.IntentUtil;
import com.deppon.app.addressbook.util.MyGestureDetector;

/**
 * 人员搜索界面.
 * 
 * @author 130126
 * 
 */
public class EmpQueryActivity extends FragmentActivity implements
		EmpQueryFragment.OnEmpQueryListener,
		EmpDetailFragment.EmpDetailListRefreshListener,
		AddressListFragment.OnAddressListRefreshListener {
	// 登陆人员，token
	private String loginUser, token;
	// 碎片主体所在的布局
	private LinearLayout all;
	// 手势对象.
	// private GestureDetector detector;
	private static final int DIALOG_KEY = 0;
	// 弹出框.
	private ProgressDialog dialog;
	private MyGestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置布局.
		setContentView(R.layout.emp_query_act);

		token = getIntent().getStringExtra("token");
		loginUser = getIntent().getStringExtra("loginUser");
		all = (LinearLayout) findViewById(R.id.all);
		// 设置手势.
		detector = new MyGestureDetector(EmpQueryActivity.this, all);
		detector.setRightFling();

		// 初始化页面的时候，加载Grid布局的片段.
		if (findViewById(R.id.tab_content) != null) {
			if (savedInstanceState != null) {
				return;
			}
			Bundle args = new Bundle();
			args.putString("loginUser", loginUser);
			args.putString("token", token);
			EmpQueryFragment firstFragment = new EmpQueryFragment();
			firstFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.tab_content, firstFragment).commit();
		}
	}

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
			e.printStackTrace();
		}
	}

	/**
	 * 打电话.
	 * 
	 * @param v
	 */
	public void call(View v) {
		String p = (String) v.getTag();
		IntentUtil.call(this, p); 
	}

	/**
	 * 发送短信.
	 * 
	 * @param v
	 */
	public void sendMessage(View v) {
		String p = (String) v.getTag();
		IntentUtil.sendMessage(this, p);  
	}

	/**
	 * 发送邮件.
	 * 
	 * @param v
	 */
	public void sendEmail(View v) {
		String p = (String) v.getTag();
		IntentUtil.sendEmail(this, p);   
	}

	@Override
	public void back() {
		FragmentManager fm = getSupportFragmentManager();
		fm.popBackStack();
	}

	@Override
	public void leftBack(MotionEvent event) {
		detector.onTouchEvent(event);
	}

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

}
