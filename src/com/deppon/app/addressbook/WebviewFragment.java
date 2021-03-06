package com.deppon.app.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.deppon.app.addressbook.util.ActionBar;
import com.deppon.app.addressbook.util.ActionBar.AbstractAction;
import com.deppon.app.addressbook.util.BaseFragment;

/**
 * webView碎片显示网页.
 * 
 * @author 130126
 * 
 */
public class WebviewFragment extends BaseFragment {
	public static final String URL = "url";
	public static final String TITLE = "title";
	public static final String SESSION_ID = "sessionId";
	private WebView mWebView;
	private Handler mHandler = new Handler();
	private String url, title, sessionId;
	private ActionBar head;
	private OnWebViewListener listener;

	public interface OnWebViewListener {
		/**
		 * 点击左上角按钮进行回退.
		 */
		public void back();

		/**
		 * 页面的向右滑动进行页面的退回.
		 * 
		 * @param event
		 */
		public void leftBack(MotionEvent event);

		public void onShowUrl(String url, String title);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnWebViewListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnWebViewListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.webviewdemo, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mWebView = (WebView) findViewById(R.id.webview);
		url = getArguments().getString(URL);
		title = getArguments().getString(TITLE);
		sessionId = getArguments().getString(SESSION_ID);
		head = (ActionBar) findViewById(R.id.webview_head);
		head.init(title, true, false, 50);
		head.setLeftAction(new AbstractAction(R.drawable.logo) {
			@Override
			public void performAction(View view) {
				listener.back();
			}
		});
		mWebView.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				listener.leftBack(event);
				// getActivity().getGestureDetector().onTouchEvent(event);//
				// 需要这样写！
				return false;
			}
		});
		mWebView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& mWebView.canGoBack()) { // 表示按返回键时的操作
						mWebView.goBack(); // 后退

						return true; // 已处理
					}
				}
				return false;
			}
		});
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUserAgentString("APP-Android");
		// mWebView.addJavascriptInterface(new Object() {
		// public void clickOnAndroid() {
		// mHandler.post(new Runnable() {
		// public void run() {
		// // mWebView.loadUrl("javascript:wave()");
		// }
		// });
		// }
		// }, "demo");
		mWebView.setWebViewClient(new MyWebViewClient());
		CookieSyncManager cookieSyncManager = CookieSyncManager
				.createInstance(getActivity());
		cookieSyncManager.sync();
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setCookie(url, "JSESSIONID=" + sessionId);
		CookieSyncManager.getInstance().sync();
		mWebView.loadUrl(url);
	}

	private final class MyWebViewClient extends WebViewClient {

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			System.out.println("shouldOverrideUrlLoading----" + url);
			listener.onShowUrl(url, title);
			// view.loadUrl(url);
			return true;
		}

		public void onPageFinished(WebView view, String url) {
			CookieManager cookieManager = CookieManager.getInstance();
			String CookieStr = cookieManager.getCookie(url);
			System.out.println("CookieStr===" + CookieStr);
			// Log.e("sunzn", "Cookies = " + CookieStr);
			super.onPageFinished(view, url);
		}

	}

}
