package com.deppon.app.addressbook.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 
 * <pre>
 * 人员实体类.
 * </pre>
 * 
 * @since
 * 
 *        <pre>
 *   modify by 130126 on 2014-4-1
 *    fix->1.
 *         2.
 * </pre>
 */
public class JpushVO implements Serializable {
	private static final long serialVersionUID = 4409637740522012381L;

	public String toString() {
		return JSON.toJSONString(this);
	}

	private int pushId;
	private String userId;
	private String token;
	private String deviceType;
	private String sysCode;

	public int getPushId() {
		return pushId;
	}

	public void setPushId(int pushId) {
		this.pushId = pushId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
}
