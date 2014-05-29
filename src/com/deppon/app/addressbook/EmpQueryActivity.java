package com.deppon.app.addressbook;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deppon.app.adapter.EmpListAdapter2;
import com.deppon.app.adapter.OrgListAdapter;
import com.deppon.app.addressbook.bean.EmployeeVO;
import com.deppon.app.addressbook.bean.OrganizationVO;
import com.deppon.app.addressbook.bean.ServerResult;
import com.deppon.app.addressbook.bean.ServerResults;
import com.deppon.app.addressbook.util.ActionBar;
import com.deppon.app.addressbook.util.Constant;
import com.deppon.app.addressbook.util.HttpRequire;
import com.deppon.app.addressbook.util.ActionBar.AbstractAction;

public class EmpQueryActivity extends BaseActivity {
	private EditText searchText;
	private TextView searchBtn;
	private Button searchEmp, searchOrg,cancelBtn;
	private ListView list;
	private boolean isSearchEmp = true;
	private ServerResults results;
	private int startIndex = 0;
	private EmpListAdapter2 empAdapter;
	private ServerResult result;
	private OrgListAdapter orgAdapter;
	private ActionBar head;
	
	/**
	 * 对页面的元素进行处理的回调类.
	 */
	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				alert("对不起，出现异常");
				break;
			// 显示人员
			case 3:
				// 从url返回的数据进行解析，然后加载到列表中.
				List json = results.getData();
				if (json != null && json.size() > 0) {
					List<EmployeeVO> emps = new ArrayList<EmployeeVO>(
							json.size());
					for (Object obj : json) {
						JSONObject _json = (JSONObject) obj;
						EmployeeVO user = JSON.parseObject(
								_json.toJSONString(), EmployeeVO.class);
						emps.add(user);
					}
					empAdapter = new EmpListAdapter2(emps,
							EmpQueryActivity.this, 0, 0);
					list.setAdapter(empAdapter);
				} else {
					List<EmployeeVO> emps = new ArrayList<EmployeeVO>();
					empAdapter = new EmpListAdapter2(emps,
							EmpQueryActivity.this, 0, 0);
					list.setAdapter(empAdapter);
				}
				break;
			// 显示组织机构
			case 2:
				List json2 = results.getData();
				if (json2 != null && json2.size() > 0) {
					List<OrganizationVO> orgs = new ArrayList<OrganizationVO>(
							json2.size());
					for (Object obj : json2) {
						JSONObject _json = (JSONObject) obj;
						OrganizationVO org = JSON.parseObject(
								_json.toJSONString(), OrganizationVO.class);
						orgs.add(org);
					}
					orgAdapter = new OrgListAdapter(orgs,
							EmpQueryActivity.this, 0, 0);
					list.setAdapter(orgAdapter);
				} else {
					List<OrganizationVO> orgs = new ArrayList<OrganizationVO>();
					orgAdapter = new OrgListAdapter(orgs,
							EmpQueryActivity.this, 0, 0);
					list.setAdapter(orgAdapter);
				}
				break;
			// 人员详情
			case 4:
				// 从url返回的数据进行解析，然后加载到列表中.
				JSONObject json3 = result.getData();
				EmployeeVO t2 = (EmployeeVO) JSON.parseObject(
						json3.toJSONString(), EmployeeVO.class);
				Intent intent = new Intent(EmpQueryActivity.this,
						EmpDetailActivity.class);
				intent.putExtra("empDetail", t2);
				startActivity(intent);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	private void search(String content) {
		if (Constant.debug) {
			myHandler.sendEmptyMessage(9);
		} else {
			try {
				results = HttpRequire.search(isSearchEmp, content, startIndex);
				// 如果返回数据不是1，就说明出现异常.
				if (results.getErrorCode() < 0) {
					myHandler.sendEmptyMessage(1);
				}
				// 否则就进行文件解析处理.
				else {
					if (isSearchEmp)
						// 显示人员
						myHandler.sendEmptyMessage(3);
					else
						// 显示组织
						myHandler.sendEmptyMessage(2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.emp_query);
		// 用户密码
		searchText = (EditText) findViewById(R.id.searchText);
		searchEmp = (Button) findViewById(R.id.searchEmp);
		searchOrg = (Button) findViewById(R.id.searchOrg);
		searchBtn = (TextView) findViewById(R.id.searchBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		list = (ListView) findViewById(R.id.ListView);
		
		head = (ActionBar) findViewById(R.id.query_head);
		head.init(R.string.search, true, false, 50);
		head.setLeftAction(new AbstractAction(R.drawable.logo) {
			@Override
			public void performAction(View view) {

			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				searchText.setText("");
			}
			
		});
		searchOrg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				isSearchEmp = false;
			}
		});

		searchEmp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				isSearchEmp = true;
			}

		});

		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = searchText.getText().toString();
				if ("".equals(text)) {
					alert("必须输入查询条件");
				} else {
					new MyListLoader(true, text).execute("");
				}
			}

		});

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 如果不是最后一个级别的列表，就进行下一级的显示.
				if (!isSearchEmp) {
					 alert("没有开发.");
				} else {
					String id = "" + arg1.findViewById(R.id.empName).getTag();
					goEmpDetail(id);
				}
			}

		});
	}

	/**
	 * 人员详情.
	 */
	private void goEmpDetail(String r) {
		if (Constant.debug) {
			myHandler.sendEmptyMessage(9);
		} else {
			try {
				result = HttpRequire.getEmpDetail(r);
				// 如果返回数据不是1，就说明出现异常.
				if (result.getErrorCode() < 0) {
					myHandler.sendEmptyMessage(1);
				}
				// 否则就进行文件解析处理.
				else {
					myHandler.sendEmptyMessage(4);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final int DIALOG_KEY = 0;
	private ProgressDialog dialog;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("正在查询...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	/**
	 * 加载订票的列表.
	 */
	private class MyListLoader extends AsyncTask<String, String, String> {

		private boolean showDialog;
		private String content;

		public MyListLoader(boolean showDialog, String content) {
			this.showDialog = showDialog;
			this.content = content;
		}

		@Override
		protected void onPreExecute() {
			// 执行过程中显示进度栏.
			if (showDialog) {
				showDialog(DIALOG_KEY);
			}
		}

		public String doInBackground(String... p) {
			search(content);
			return "";
		}

		@Override
		public void onPostExecute(String Re) {
			if (showDialog) {
				removeDialog(DIALOG_KEY);
			}
		}

		@Override
		protected void onCancelled() {
			// 取消进度栏.
			if (showDialog) {
				removeDialog(DIALOG_KEY);
			}
		}
	}
}
