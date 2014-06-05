package com.deppon.app.addressbook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.deppon.app.addressbook.bean.EmployeeVO;
import com.deppon.app.addressbook.util.ActionBar;
import com.deppon.app.addressbook.util.ActionBar.AbstractAction;
import com.deppon.app.addressbook.util.BaseFragment;

public class EmpDetailFragment extends BaseFragment {
	private EmpDetailListRefreshListener listener;
	public static final String EMPINFO = "empInfo";

	public interface EmpDetailListRefreshListener {
		public void onShowEmpdetail(int empId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
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

			}
		});
		String empInfo = getArguments().getString(EMPINFO);
		empVo = (EmployeeVO) JSON.parseObject(empInfo, EmployeeVO.class);
		// 用户密码
		jobName = (TextView) findViewById(R.id.jobName);
		empName = (TextView) findViewById(R.id.empName);
		empEmail = (TextView) findViewById(R.id.empEmail);
		empPhone = (TextView) findViewById(R.id.empPhone);
		empAddress = (TextView) findViewById(R.id.empAddress);
		orgName = (TextView) findViewById(R.id.orgName);
		call = (Button) findViewById(R.id.call);
		message = (Button) findViewById(R.id.message);

		jobName.setText(empVo.getJobName());
		empName.setText(empVo.getEmpName());
		empEmail.setText(empVo.getEmail());
		empPhone.setText(empVo.getMobileNo());
		empAddress.setText(empVo.getAddress());
		orgName.setText(empVo.getOrgName());

		call.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Uri telUri = Uri.parse("tel:" + empPhone.getText().toString());
				startActivity(new Intent(Intent.ACTION_DIAL, telUri));
			} 
		});

		message.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse("smsto:" + empPhone.getText().toString());
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);
				it.putExtra("sms_body", "");
				startActivity(new Intent(Intent.ACTION_DIAL, uri));
			}

		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("EmpDetailFragment____onCreateView");
		return inflater.inflate(R.layout.emp_detail, container, false);
	}

	private TextView jobName, empName, empEmail, empPhone, empAddress, orgName;
	private Button call, message;
	private EmployeeVO empVo;

}
