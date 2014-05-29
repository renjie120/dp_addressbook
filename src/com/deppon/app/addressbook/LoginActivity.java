package com.deppon.app.addressbook;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deppon.app.addressbook.bean.Result;
import com.deppon.app.addressbook.util.Constant;
import com.deppon.app.addressbook.util.HttpRequire;
import com.example.jpushdemo.ExampleUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.litesuits.http.request.param.HttpParam;

public class LoginActivity extends Activity implements OnClickListener {
	private static final String url = Constant.DPM_HOST
			+ "/dpm/login_login.action?password=qqqqqq&userId=130126&token=2c249ddb88288c5cbb5ba7729a249ad5";
	// 反馈邮件地址
	public static String EMALADDRESS = "dpmobile@deppon.com";
	public static String EXIT_ACTION = "exitAction";
	// 登陆请求地址
	public static String LOGINURL = "http://app.deppon.com/center/checkLogin";
	// 文件解密地址
	public static String FILEURL = "http://app.deppon.com/center/decryptFile";
	// 登陆超时时间30秒
	public static final int TIMEOUT = 30;

	// false：正式环境.
	public static final boolean ISDEBUG = true;
	static {
		// 如果是调试
		if (ISDEBUG) {
			// 反馈地址
			EMALADDRESS = "lishuiqing110@163.com";
			// LOGINURL = "http://10.224.70.10:8081/center/checkLogin";
			// FILEURL = "http://10.224.70.10:8081/center/decryptFile";
			// 登陆地址
			LOGINURL = "http://10.224.70.132:8081/dpm/dpm/";
			// 文件解密地址
			FILEURL = "http://192.168.67.47/center/decryptFile";
		}
	}

	// 密码
	private EditText inputPass;
	// 用户名
	private EditText inputUser;
	// 登陆地址
	private Button buttonLogin;
	String deviceId = null;
	// 记住密码
	private CheckBox remeberPassword;
	// 用户名
	private String name;
	// 密码
	private String pass;
	// 弹出框
	private static final int DIALOG_KEY = 0;
	// 精度条
	private ProgressDialog dialog;

	/**
	 * 判断网络是否好用.
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		try {
			// 判断网络情况
			if (context != null) {
				// 链接管理器
				ConnectivityManager mConnectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mNetworkInfo = mConnectivityManager
						.getActiveNetworkInfo();
				// 返回网络状态
				if (mNetworkInfo != null) {
					return mNetworkInfo.isAvailable();
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private SharedPreferences mSharedPreferences;

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

	Intent in = null;
	// 打开文件
	Uri openFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		JPushInterface.init(getApplicationContext());
		// 用户密码
		inputPass = (EditText) findViewById(R.id.inputPass);
		// 用户名
		inputUser = (EditText) findViewById(R.id.inputName);
		// 手机临时存储变量
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		in = getIntent();
		// 是否从详情页面返回来的首页.
		String isBack = in.getStringExtra("back");
		// 打开intent
		if (in != null && in.getData() != null) {
			openFile = in.getData();
		}
		// 是否记住密码
		remeberPassword = (CheckBox) findViewById(R.id.remember_password);
		// 得到本地存储的变量
		String remeber = mSharedPreferences.getString("remeber", "false");
		String pass = mSharedPreferences.getString("pass", "");
		String user = mSharedPreferences.getString("userId", "");
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		// zhuceLogin = (Button) findViewById(R.id.registLogin);
		// 选择了记住密码
		if ("true".equals(remeber)) {
			remeberPassword.setChecked(true);
		}
		if (ISDEBUG) {
			// alert("使用的是测试版本！！");
		}

		// 注册事件
		// zhuceLogin.setOnClickListener(this);
		// 登陆事件
		buttonLogin.setOnClickListener(this);
		// 点击记住密码的按钮操作
		remeberPassword
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// 如果是记住密码
						if (arg1) {
							SharedPreferences.Editor mEditor = mSharedPreferences
									.edit();
							// 保存用户名密码到本地存储
							mEditor.putString("remeber", "true");
							mEditor.putString("pass", inputPass.getText()
									.toString());
							mEditor.putString("userId", inputUser.getText()
									.toString());
							mEditor.commit();
						} else {
							// 没有记住密码就清空本地存储里面的变量
							SharedPreferences.Editor mEditor = mSharedPreferences
									.edit();
							mEditor.putString("remeber", "false");
							mEditor.putString("pass", "");
							mEditor.putString("userId", "");
							mEditor.commit();
						}
					}

				});

		// 如果设置了记住密码，就自动进行登录验证.
		if ("true".equals(remeber)) {
			inputPass.setText(pass);
			remeberPassword.setChecked(true);
			inputUser.setText(user);
			// 如果不是点击的返回按钮回退的，并且有文件，就直接登录.
			if (!"true".equals(isBack)) {
				if (in != null && in.getData() != null) {
					go(openFile);
				} else {
					go(null);
				}
			}
		}
	}

	/**
	 * 记住密码
	 */
	private void savePass() {
		boolean arg1 = remeberPassword.isChecked();
		// 记住密码
		if (arg1) {
			SharedPreferences.Editor mEditor = mSharedPreferences.edit();
			mEditor.putString("remeber", "true");
			mEditor.putString("pass", inputPass.getText().toString());
			mEditor.putString("userId", inputUser.getText().toString());
			mEditor.commit();
		}
		// 不记住密码
		else {
			SharedPreferences.Editor mEditor = mSharedPreferences.edit();
			mEditor.putString("remeber", "false");
			mEditor.putString("pass", "");
			mEditor.putString("userId", "");
			mEditor.commit();
		}
	}

	private void resetPage() {
		buttonLogin.setEnabled(true);
		removeDialog(DIALOG_KEY);
	}

	/**
	 * 进行登陆操作
	 */
	private void go(Uri file) {
		if (!isNetworkConnected(this)) {
			alert("网络异常，请确认联网后重试");
		} else {
			TelephonyManager tm = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);
			savePass();
			name = inputUser.getText().toString();
			// 密码
			pass = inputPass.getText().toString();
			login(name,pass);
		}
	}

	private void login(String uid, String pass) {
		try {
			HttpUtils http = new HttpUtils();
			RequestParams p = new RequestParams();
			p.addBodyParameter("userId", uid);
			p.addBodyParameter("password", pass);
			String tk = HttpRequire.getMD5(HttpRequire.getBase64(uid));
			p.addBodyParameter("token", tk); 
			http.configResponseTextCharset("GBK");
			http.send(HttpRequest.HttpMethod.POST, url, p,
					new RequestCallBack<String>() {
						@Override
						public void onStart() {
							showDialog(DIALOG_KEY);
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							// resultText.setText(current + "/" + total);
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							removeDialog(DIALOG_KEY);
							System.out.println(responseInfo.result);
							Result r = (Result) JSON.parseObject(
									responseInfo.result, Result.class);
							if (r.getErrorCode() == 0) {
								String _res = r.getData().toString();
								JSONObject obj = JSON.parseObject(_res);
								Intent intent2 = new Intent(LoginActivity.this,
										NewHomeActivity.class);
								 
								startActivity(intent2);
							} else {
								alert(r.getErrorMessage());
							}
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							removeDialog(DIALOG_KEY);
						}
					});
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * 登陆请求服务器数据
//	 * 
//	 * @param userName
//	 * @param password
//	 */
//	public void login(final String userName, final String password) {
//		// 得到url请求.
//		DefaultHttpClient httpclient = new DefaultHttpClient();
//		try {
//			ServerResult result = HttpRequire.login(userName, password);
//			if (result == null || 1 != result.getErrorCode()) {
//				myHandler.sendEmptyMessage(1);
//			}
//			// 成功了就跳转到活动列表页面.
//			else {
//				Message mes = new Message();
//				mes.obj = result.getData();
//				mes.what = 7;
//				myHandler.sendMessage(mes);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			myHandler.sendEmptyMessage(6);
//		}
//	}
 
	class LoginArg implements HttpParam {
		private String userId;
		private String password;

		public LoginArg(String userId, String pass) {
			this.userId = userId;
			this.password = pass;
		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.buttonLogin) {
			if ("".equals(inputUser.getText().toString().trim())) {
				alert("请输入账号");
				resetPage();
			} else if ("".equals(inputPass.getText().toString().trim())) {
				alert("请输入密码");
				resetPage();
			} else {
				go(null);
			}
		} /*
		 * else if (v.getId() == R.id.registLogin) { regist(); }
		 */
	}

	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				alert("对不起，该用户没有权限");
				removeDialog(DIALOG_KEY);
				buttonLogin.setEnabled(true);
				break;
			case 2:
				alert("对不起，手机序列号不匹配");
				removeDialog(DIALOG_KEY);
				buttonLogin.setEnabled(true);
				break;
			case 3:
				alert("对不起，密码错误");
				removeDialog(DIALOG_KEY);
				buttonLogin.setEnabled(true);
				break;
			case 4:
				alert("对不起，参数错误");
				removeDialog(DIALOG_KEY);
				buttonLogin.setEnabled(true);
				break;
			case 5:
				alert("没有安装相关软件，请安装软件后重试");
				removeDialog(DIALOG_KEY);
				buttonLogin.setEnabled(true);
				break;
			case 6:
				alert("对不起，服务端异常或者网络异常，请稍候重试");
				removeDialog(DIALOG_KEY);
				buttonLogin.setEnabled(true);
				break;
			case 7:
				// JSONObject m = (JSONObject) msg.obj;
				Map m = (HashMap) msg.obj;
				Intent intent = new Intent(LoginActivity.this,
						HomePageActivity.class);
				String userName = m.get("name") + "";
				intent.putExtra("name", userName);
				intent.putExtra("pass", m.get("pass") + "");
				removeDialog(DIALOG_KEY);
				buttonLogin.setEnabled(true);
				saveJpush(userName, "dpm");
				startActivity(intent);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	private void saveJpush(String userName, String sysCode) {
		String jpushId = mSharedPreferences.getString("jpushId", "-1");
		String jpushName = mSharedPreferences.getString("jpushuser", "-1");
		// 如果当前用户没有在本地保存过token，就重新注册一下
		if (!userName.equals(jpushName) || "-1".equals(jpushId))
			try {
				System.out.println("新注册用户：" + userName);
				jpushId = HttpRequire.regiestJpush(userName);
				mSharedPreferences.edit().putString("jpushuser", userName)
						.commit();
				mSharedPreferences.edit().putString("jpushId", jpushId)
						.commit();
				Set<String> s = new HashSet<String>();
				s.add(sysCode);
				// 设置系统名称为别名.
				// JPushInterface.setTags(getApplicationContext(), s, null);
				// 设置别名
				JPushInterface.setAlias(getApplicationContext(), jpushId,
						mAliasCallback);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("保存token出现问题");
			}
		System.out.println("注册登录用户：" + userName + ",,token=" + jpushId);
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				System.out.println("设置别名成功.");
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";

				if (ExampleUtil.isConnected(getApplicationContext())) {
					System.out.println("设置别名失败.");
				} else {
					System.out.println("网络不通畅导致别名设置失败");

				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				System.out.println("网络不通畅导致别名设置失败code===" + code);
			}

			ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

	/**
	 * 退出程序的时候，删除临时文件夹里面的解密文件.
	 */
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("正在登录,请稍候");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	/**
	 * 弹出提示信息.
	 * 
	 * @param mess
	 */
	public void alert(String mess) {
		new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
				.setMessage(mess).setPositiveButton("确定", null).show();
	}

	/**
	 * 处理结果.
	 * 
	 * @param result
	 */
	public void console(String result) {
		if ("40002".equals(result)) {
			myHandler.sendEmptyMessage(1);
		} else if ("40003".equals(result)) {
			myHandler.sendEmptyMessage(2);
		} else if ("40004".equals(result)) {
			myHandler.sendEmptyMessage(3);
		} else if ("40005".equals(result)) {
			myHandler.sendEmptyMessage(4);
		}
	}

}