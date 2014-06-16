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
import com.deppon.app.addressbook.util.Constant;
import com.deppon.app.addressbook.util.HttpRequire;
import com.deppon.app.addressbook.util.IntentUtil;
import com.deppon.app.addressbook.util.MyGestureDetector;

/**
 * 首页.
 * 
 * @author 130126
 * 
 */
public class NewHomeActivity extends FragmentActivity implements
		HomeGridviewFragement.OnHomeGridViewSelectedListener,
		AddressListFragment.OnAddressListRefreshListener,
		EmpDetailFragment.EmpDetailListRefreshListener,
		WebviewFragment.OnWebViewListener {
	/**
	 * 新闻动态的url
	 */
	private static String NEWS_URL = Constant.MAPP_HOST
			+ "/jsp/rollnews/rollnews_list.jsp";
	/**
	 * 待办事项的url
	 */
	private static String WORKFLOW_URL = Constant.MAPP_HOST
			+ "/toWorkItemsList";
	/**
	 * 公告的url
	 */
	private static String NOTICE_URL = Constant.MAPP_HOST
			+ "/jsp/ios/notice/appoint_rmoval_announcement.jsp";
	/**
	 * 手势对象
	 */
	private MyGestureDetector detector;
	// 变量：登陆用户，token
	private String loginUser, token;
	private static final int DIALOG_KEY = 0;
	/**
	 * fragment的主体布局.
	 */
	private LinearLayout all;
	/**
	 * 弹出框
	 */
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
		// 加载布局
		setContentView(R.layout.new_home);
		all = (LinearLayout) findViewById(R.id.all);
		// 设置手势动作.
		detector = new MyGestureDetector(NewHomeActivity.this, all);
		detector.setRightFling();
		token = getIntent().getStringExtra("token");
		loginUser = getIntent().getStringExtra("loginUser");
		// 初始化页面的时候，加载Grid布局的碎片.
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
			onShowUrl(NEWS_URL, "新闻动态");
			break;
		// 待办事项
		case 1:
			onShowUrl(WORKFLOW_URL, "待办事项");
			break;
		// 公告
		case 2:
			onShowUrl(NOTICE_URL, "公告");
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
		// 传递的变量
		Bundle args = new Bundle();
		args.putInt(AddressListFragment.ROOT_ID, root);
		args.putString(AddressListFragment.USERID, loginUser);
		args.putString(AddressListFragment.TOKEN, token);
		// 创建通讯录碎片
		AddressListFragment newFragment = new AddressListFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		// 打开碎片的动画
		transaction.setCustomAnimations(R.anim.slide_left_in,
				R.anim.slide_left_out);
		// 设置传递参数
		newFragment.setArguments(args);
		transaction.replace(R.id.tab_content, newFragment);
		// 添加碎片到堆栈中，回退按钮可以操作.
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * 打开web连接.
	 * 
	 * @param url
	 */
	public void onShowUrl(String url, String title) {
		Bundle args = new Bundle();
		args.putString(WebviewFragment.URL, url);
		args.putString(WebviewFragment.TITLE, title);
		// web页面的碎片
		WebviewFragment newFragment = new WebviewFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		// 动画
		transaction.setCustomAnimations(R.anim.slide_left_in,
				R.anim.slide_left_out);
		// 设置参数
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
			// 请求服务端得到人员详情.
			result = HttpRequire.getEmpDetail(empId + "", loginUser, token);
			// 如果返回数据不是1，就说明出现异常.
			if (result.getErrorCode() < 0) {
				Toast.makeText(getApplicationContext(), "对不起查询人员出现异常....",
						Toast.LENGTH_SHORT).show();
			}
			// 否则就进行文件解析处理.
			else {
				// 进行json解析，得到人员的信息数据传递到页面中去.
				JSONObject json3 = result.getData();
				args.putString(EmpDetailFragment.EMPINFO, json3.toJSONString());
				EmpDetailFragment newFragment = new EmpDetailFragment();
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				// 打开页面设置动画.
				transaction.setCustomAnimations(R.anim.slide_left_in,
						R.anim.slide_left_out);
				newFragment.setArguments(args);
				// 进行碎片替换
				transaction.replace(R.id.tab_content, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		} catch (Exception e) {
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
	}

	@Override
	public void leftBack(MotionEvent event) {
		// 顶部菜单栏的回退操作，进行碎片堆栈的回退.
		detector.onTouchEvent(event);
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
}
