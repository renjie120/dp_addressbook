package com.deppon.app.addressbook.bean;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 
 * <pre>
 * 组织机构实体类.
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
public class OrganizationVO implements Serializable {
	private static final long serialVersionUID = 4409637740522012381L;
	private int start;
	private int limit;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}

	private int orgId;
	private String orgName;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	private String orgCode;
	private int orgLevel;
	private String orgAddr;
	private String zipCode;
	private String managerId;
	private String linkTel;
	private String email;
	private String area;
	private String remark;

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public int getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(int orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgAddr() {
		return orgAddr;
	}

	public void setOrgAddr(String orgAddr) {
		this.orgAddr = orgAddr;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAppSysCode() {
		return appSysCode;
	}

	public void setAppSysCode(String appSysCode) {
		this.appSysCode = appSysCode;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getFinaSysCode() {
		return finaSysCode;
	}

	public void setFinaSysCode(String finaSysCode) {
		this.finaSysCode = finaSysCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	private String appSysCode;
	private int parentId;
	private String finaSysCode;
	private Date createTime;
	private Date lastUpdateTime;

}
