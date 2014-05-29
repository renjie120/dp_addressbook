package com.deppon.app.addressbook.bean;


/**
 * 根据父亲组织节点查询得到的全部的孩子节点list.
 * 
 * @author 130126
 * 
 */
public class JpushTokenResult {
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
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

	private int errorCode;
	private int count;
	private String errorMessage;
}
