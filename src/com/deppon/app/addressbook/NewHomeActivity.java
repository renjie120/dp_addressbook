package com.deppon.app.addressbook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deppon.app.addressbook.bean.EmployeeVO;
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
		EmpDetailFragment.EmpDetailListRefreshListener {
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.new_home);

		token = getIntent().getStringExtra("token");
		loginUser = getIntent().getStringExtra("loginUser");
		if (findViewById(R.id.tab_content) != null) {

			if (savedInstanceState != null) {
				return;
			}
			HomeGridviewFragement firstFragment = new HomeGridviewFragement();
			// firstFragment.setArguments(args);
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
			onShowAddressList(103);
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

	@Override
	public void onShowAddressList(int root) {
		Bundle args = new Bundle();
		args.putInt(AddressListFragment.ROOT_ID, root);
		args.putString(AddressListFragment.USERID, loginUser);
		args.putString(AddressListFragment.TOKEN, token);
		AddressListFragment newFragment = new AddressListFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		newFragment.setArguments(args);
		transaction.replace(R.id.tab_content, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
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
}
