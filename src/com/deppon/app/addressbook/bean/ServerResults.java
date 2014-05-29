package com.deppon.app.addressbook.bean;

import java.util.List;

/**
 * 包装服务端请求返回数组的类.
 * 
 * @param <T>
 */
public class ServerResults<T> {
	public int errorCode; // 错误码，参见下面错误码说明
	public int count;

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

	public String errormsg; // 错误说明，调用正确时为空
	public List<T> data;// 具体响应数据

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

}
