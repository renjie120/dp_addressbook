package com.deppon.app.addressbook.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * 包装服务器端请求结果的类.
 */
public class ServerResult {
	private JSONObject data;
	private int errorCode;
	private int count;
	private String errorMessage;

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
