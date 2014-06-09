package com.deppon.app.addressbook;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.deppon.app.addressbook.bean.EmployeeVO;

public class EmpDetailActivity extends BaseActivity {
	private TextView jobName, empName, empEmail, empPhone, orgName;
	private EmployeeVO empVo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.emp_detail);
		empVo = (EmployeeVO) getIntent().getExtras().get("empDetail");
		// 用户密码
		jobName = (TextView) findViewById(R.id.jobName);
		empName = (TextView) findViewById(R.id.empName);
		empEmail = (TextView) findViewById(R.id.empEmail);
		empPhone = (TextView) findViewById(R.id.empPhone);
		orgName = (TextView) findViewById(R.id.orgName);

		jobName.setText(empVo.getJobName());
		empName.setText(empVo.getEmpName());
		empEmail.setText(empVo.getEmail());
		empPhone.setText(empVo.getMobileNo());
		orgName.setText(empVo.getOrgName());

		// call.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Uri telUri = Uri.parse("tel:" + empPhone.getText().toString());
		// startActivity(new Intent(Intent.ACTION_DIAL, telUri));
		// }
		//
		// });

	}
}
