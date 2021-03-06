package com.deppon.app.addressbook;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deppon.app.addressbook.bean.LoginResult;
import com.deppon.app.addressbook.bean.Result;
import com.deppon.app.addressbook.util.Constant;
import com.deppon.app.addressbook.util.HttpRequire;
import com.example.jpushdemo.ExampleUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class LoginActivity extends BaseActivity {
	private static final String url = Constant.DPM_HOST
			+ "/dpm/login_login.action";
	private static final String loginUrl = Constant.MAPP_HOST + "/logincheck";
	// 登陆超时时间30秒
	public static final int TIMEOUT = 30;

	// 密码
	@ViewInject(R.id.inputPass)
	private EditText inputPass;

	// 用户名
	@ViewInject(R.id.inputName)
	private EditText inputUser;

	// 登陆地址
	@ViewInject(R.id.buttonLogin)
	private Button buttonLogin;
	@ViewInject(R.id.all)
	private LinearLayout all;
	private SharedPreferences mSharedPreferences;
	String deviceId = null;
	// 记住密码
	@ViewInject(R.id.remember_password)
	private CheckBox remeberPassword;

	// 用户名
	private String name;
	// 密码
	private String pass;
	// 弹出框
	private static final int DIALOG_KEY = 0;
	// 精度条
	private ProgressDialog dialog;

	@OnClick({ R.id.buttonLogin })
	public void loginButton(View v) {
		if ("".equals(inputUser.getText().toString().trim())) {
			alert("请输入账号");
			resetPage();
		} else if ("".equals(inputPass.getText().toString().trim())) {
			alert("请输入密码");
			resetPage();
		} else {
			go(null);
		}
	}

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
		ViewUtils.inject(this);

		JPushInterface.init(getApplicationContext());
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
		// 得到本地存储的变量
		String remeber = mSharedPreferences.getString("remeber", "false");
		String pass = mSharedPreferences.getString("pass", "");
		String user = mSharedPreferences.getString("userId", "");
		// zhuceLogin = (Button) findViewById(R.id.registLogin);
		// 选择了记住密码
		if ("true".equals(remeber)) {
			remeberPassword.setChecked(true);
		}
		// 注册事件
		// zhuceLogin.setOnClickListener(this);
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

			Drawable mIconSearchClear = getResources().getDrawable(
					R.drawable.txt_search_clear);
			// 如果有用户名，就显示出来清除按钮.
			if (!"".equals(inputPass.getText().toString())) {
				inputPass.setCompoundDrawablesWithIntrinsicBounds(null, null,
						mIconSearchClear, null);
			}
			if (!"".equals(inputUser.getText().toString())) {
				inputUser.setCompoundDrawablesWithIntrinsicBounds(null, null,
						mIconSearchClear, null);
			}

			// 如果不是点击的返回按钮回退的，并且有文件，就直接登录.
			if (!"true".equals(isBack)) {
				if (in != null && in.getData() != null) {
					go(openFile);
				} else {
					go(null);
				}
			}
		}

		addCleanBtn(inputPass);
		addCleanBtn(inputUser);

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
			savePass();
			name = inputUser.getText().toString();
			// 密码
			pass = inputPass.getText().toString();
			login(name, pass);
		}
	}

	private void login(final String uid, String pass) {
		try {
			HttpUtils http = new HttpUtils();
			RequestParams p = new RequestParams();
			p.addBodyParameter("userId", uid);
			p.addBodyParameter("password", pass);
			final String tk = HttpRequire.getMD5(HttpRequire.getBase64(uid));
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
							System.out.println("第一次返回结果是："
									+ responseInfo.result);
							Result r = (Result) JSON.parseObject(
									responseInfo.result, Result.class);
							if (r.getErrorCode() == 0) {
								String _res = r.getData().toString();
								LoginResult result = (LoginResult) JSON
										.parseObject(_res, LoginResult.class);
								System.out.println("result.getSessionId()=="
										+ result.getSessionId());
								System.out.println("result.getSid()=="
										+ result.getSid());
								loginCheck(uid, result.getCasCookie(),
										result.getSessionId(), tk);

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

	private void loginCheck(final String userId, String cassSession,
			String sessionId, final String tk) {
		final HttpUtils http = new HttpUtils();
		RequestParams p = new RequestParams();
		p.addBodyParameter("userid", userId);
		p.addBodyParameter("sessionId", sessionId);
		if (cassSession != null) {
			cassSession = cassSession.split("=")[1];
		}
		p.addBodyParameter("CASTGC", cassSession);
		System.out.println("请求参数：" + userId);
		System.out.println("请求参数：sessionId---" + sessionId);
		System.out.println("请求参数：CASTGC---" + cassSession);
		http.configResponseTextCharset("GBK");
		http.configUserAgent("APP-Android");
		System.out.println("第二次返回结果是：" + sessionId);
		http.send(HttpRequest.HttpMethod.POST, loginUrl, p,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						arg0.printStackTrace();
						System.out.println("第二次请求出现错误：" + arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						JSONObject m = JSON.parseObject(JSON.toJSONString(http
								.getHttpClient()));
						JSONObject cookies = JSON.parseObject(m
								.get("cookieStore") + "");
						System.out.println(cookies.get("cookies"));
						JSONArray cks = JSON.parseArray(cookies.get("cookies")
								+ "");
						String sessionid = null;
						if (cks.size() > 0) {
							for (Object obj : cks) {
								String str = obj.toString();
								JSONObject o = JSON.parseObject(str);
								if ("JSESSIONID".equals(o.get("name"))) {
									sessionid = o.getString("value") + "";
									break;
								}
							}
						}
						System.out.println(",,计算得到的session===" + sessionid);
						Intent intent2 = new Intent(LoginActivity.this,
								HomePageActivity.class);
						intent2.putExtra("loginUser", userId);
						intent2.putExtra("sessionId", sessionid);
						intent2.putExtra("token", tk);
						saveJpush(userId, "dpm");
						startActivity(intent2);
					}
				});
	}

	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
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
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	/**
	 * 保持推送信息.
	 * 
	 * @param userName
	 * @param sysCode
	 */
	private void saveJpush(String userName, String sysCode) {
		String jpushId = mSharedPreferences.getString("jpushId", "-1");
		String jpushName = mSharedPreferences.getString("jpushuser", "-1");
		// 如果当前用户没有在本地保存过token，就重新注册一下
		if (!userName.equals(jpushName) || "-1".equals(jpushId))
			try {
				String tk = HttpRequire.getMD5(HttpRequire.getBase64(userName));
				System.out.println("新注册用户：" + userName);
				jpushId = HttpRequire.regiestJpush(userName, userName, tk);
				mSharedPreferences.edit().putString("jpushuser", userName)
						.commit();
				mSharedPreferences.edit().putString("jpushId", jpushId)
						.commit();
				Set<String> s = new HashSet<String>();
				s.add(sysCode);
				// 设置别名
				JPushInterface.setAlias(getApplicationContext(), jpushId,
						mAliasCallback);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("保存token出现问题");
			}
		System.out.println("注册登录用户：" + userName + ",,token=" + jpushId);
	}

	/**
	 * 以下代码来自推送jpush自带的示例类.
	 */
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

}