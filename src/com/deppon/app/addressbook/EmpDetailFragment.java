package com.deppon.app.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.deppon.app.addressbook.bean.EmployeeVO;
import com.deppon.app.addressbook.util.ActionBar;
import com.deppon.app.addressbook.util.ActionBar.AbstractAction;
import com.deppon.app.addressbook.util.BaseFragment;

/**
 * 人员详情.
 * 
 * @author 130126
 * 
 */
public class EmpDetailFragment extends BaseFragment {
	private EmpDetailListRefreshListener listener;
	public static final String EMPINFO = "empInfo";
	private TextView jobName, empName, empEmail, empPhone, orgName, empPhone2;
	private EmployeeVO empVo;
	private ImageView callImg, shortmessageImg, callHome2, sendEmail;

	/**
	 * 操作页面详情碎片的一些接口.
	 * 
	 * @author 130126
	 * 
	 */
	public interface EmpDetailListRefreshListener {
		/**
		 * 页面人员的详情
		 * 
		 * @param empId
		 */
		public void onShowEmpdetail(int empId);

		/**
		 * 回退到上一个界面.
		 */
		public void back();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			// 进行父亲activity的监听绑定.
			listener = (EmpDetailListRefreshListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement EmpDetailListRefreshListener");
		}
	}

	private ActionBar head;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		head = (ActionBar) findViewById(R.id.empdetail_head);
		head.init(R.string.emp_detail, true, false, 50);
		head.setLeftAction(new AbstractAction(R.drawable.logo) {
			@Override
			public void performAction(View view) {
				listener.back();
			}
		});
		String empInfo = getArguments().getString(EMPINFO);
		empVo = (EmployeeVO) JSON.parseObject(empInfo, EmployeeVO.class);
		// 用户密码
		jobName = (TextView) findViewById(R.id.jobName);
		empName = (TextView) findViewById(R.id.empName);
		empEmail = (TextView) findViewById(R.id.empEmail);
		empPhone = (TextView) findViewById(R.id.empPhone);
		orgName = (TextView) findViewById(R.id.orgName);
		empPhone2 = (TextView) findViewById(R.id.empPhone2);
		callImg = (ImageView) findViewById(R.id.callHome);
		shortmessageImg = (ImageView) findViewById(R.id.messageHome);
		callHome2 = (ImageView) findViewById(R.id.callHome2);
		sendEmail = (ImageView) findViewById(R.id.sendEmail);
		jobName.setText(empVo.getJobName());
		empName.setText(empVo.getEmpName());
		empEmail.setText(empVo.getEmail());
		empPhone.setText(empVo.getMobileNo());
		orgName.setText(empVo.getOrgName());
		empPhone2.setText(empVo.getTel());
		callImg.setTag(empVo.getMobileNo());
		sendEmail.setTag(empVo.getEmail());
		callHome2.setTag(empVo.getTel());
		if (empVo.getTel() == null || "".equals(empVo.getTel().trim())) {
			callHome2.setVisibility(View.GONE);
		}

		if (empVo.getMobileNo() == null
				|| "".equals(empVo.getMobileNo().trim())) {
			callImg.setVisibility(View.GONE);
			shortmessageImg.setVisibility(View.GONE);
		}
		shortmessageImg.setTag(empVo.getMobileNo());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("EmpDetailFragment____onCreateView");
		return inflater.inflate(R.layout.emp_detail, container, false);
	}
}
