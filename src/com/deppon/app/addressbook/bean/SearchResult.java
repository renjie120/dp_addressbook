package com.deppon.app.addressbook.bean;

import java.util.List;

/**
 * 根据父亲组织节点查询得到的全部的孩子节点list.
 * @author 130126
 *
 */
public class SearchResult {
	private List<EmployeeVO> emps;
	private List<OrganizationVO> orgs;

	public List<EmployeeVO> getEmps() {
		return emps;
	}

	public void setEmps(List<EmployeeVO> emps) {
		this.emps = emps;
	}

	public List<OrganizationVO> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<OrganizationVO> orgs) {
		this.orgs = orgs;
	}
}
