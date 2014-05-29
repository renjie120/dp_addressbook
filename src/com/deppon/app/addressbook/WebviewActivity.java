package com.deppon.app.addressbook;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends BaseActivity {
	private WebView mWebView;
	private Handler mHandler = new Handler();
	private String url;
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.webviewdemo);
		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		url = getIntent().getStringExtra("url");
		System.out.println("url=========="+url);
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
		mWebView.setWebViewClient(new WebViewClient());
		if(url==null||"null".equals(url))
			mWebView.loadUrl("http://10.224.70.67:8089/DPMontal");
		else
			mWebView.loadUrl(url);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			// 返回键退回
			mWebView.goBack();
			return true;
		} 
		return super.onKeyDown(keyCode, event);
	}
}