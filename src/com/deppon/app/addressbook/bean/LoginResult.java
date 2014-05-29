package com.deppon.app.addressbook.bean;

import java.util.Map;

public class LoginResult {
	private String sessionId;
	private String casCookie;
	private String sid;
	private String androidVersion;
	private String iphoneVersion;
	private String iphoneForce;
	private String androidForce;
	private String iphoneUrl;
	private String androidUrl;
	public String getIphoneUrl() {
		return iphoneUrl;
	}
	public void setIphoneUrl(String iphoneUrl) {
		this.iphoneUrl = iphoneUrl;
	}
	public String getAndroidUrl() {
		return androidUrl;
	}
	public void setAndroidUrl(String androidUrl) {
		this.androidUrl = androidUrl;
	}
	public String getIphoneForce() {
		return iphoneForce;
	}
	public void setIphoneForce(String iphoneForce) {
		this.iphoneForce = iphoneForce;
	}
	public String getAndroidForce() {
		return androidForce;
	}
	public void setAndroidForce(String androidForce) {
		this.androidForce = androidForce;
	}
	private Map roleStr;
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getCasCookie() {
		return casCookie;
	}
	public String getAndroidVersion() {
		return androidVersion;
	}
	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}
	public String getIphoneVersion() {
		return iphoneVersion;
	}
	public void setIphoneVersion(String iphoneVersion) {
		this.iphoneVersion = iphoneVersion;
	}
	public void setCasCookie(String casCookie) {
		this.casCookie = casCookie;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public Map getRoleStr() {
		return roleStr;
	}
	public void setRoleStr(Map roleStr) {
		this.roleStr = roleStr;
	} 
}
