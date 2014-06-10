package com.deppon.app.addressbook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deppon.app.adapter.EmpListAdapter2;
import com.deppon.app.adapter.OrgListAdapter;
import com.deppon.app.addressbook.bean.EmployeeVO;
import com.deppon.app.addressbook.bean.OrganizationVO;
import com.deppon.app.addressbook.bean.ServerResult;
import com.deppon.app.addressbook.bean.ServerResults;
import com.deppon.app.addressbook.util.ActionBar;
import com.deppon.app.addressbook.util.ActionBar.AbstractAction;
import com.deppon.app.addressbook.util.BaseFragment;
import com.deppon.app.addressbook.util.HttpRequire;

/**
 * 人员搜索界面的碎片.
 * @author 130126
 *
 */
public class EmpQueryFragment extends BaseFragment {
	private OnEmpQueryListener listener;

	public interface OnEmpQueryListener {
		/**
		 * 显示人员详情界面.
		 * 
		 * @param empId
		 */
		public void onShowEmpdetail(int empId);

		/**
		 * 显示根节点下面的孩子节点信息.
		 * 
		 * @param root
		 */
		public void onShowAddressList(int root);

		/**
		 * 点击左上角按钮进行回退.
		 */
		public void back();

		/**
		 * 页面的向右滑动进行页面的退回.
		 * 
		 * @param event
		 */
		public void leftBack(MotionEvent event);
	}

	private EditText searchText; 
	private ImageView searchEmp, searchOrg;
	private ImageView searchImage;
	private ListView list;
	private boolean isSearchEmp = true;
	private ServerResults results;
	private int startIndex = 0;
	private EmpListAdapter2 empAdapter;
	private ServerResult result;
	private OrgListAdapter orgAdapter;
	private ActionBar head;
	private String loginUser, token;

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
				list.setDivider(null);
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
							getApplicationContext(), 0, 0);
					list.setAdapter(empAdapter);
				} else {
					List<EmployeeVO> emps = new ArrayList<EmployeeVO>();
					empAdapter = new EmpListAdapter2(emps,
							getApplicationContext(), 0, 0);
					list.setAdapter(empAdapter);
				}

				break;
			// 显示组织机构
			case 2:
				List json2 = results.getData();
				list.setDivider(null);
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
							getApplicationContext(), 0, 0);
					list.setAdapter(orgAdapter);
				} else {
					List<OrganizationVO> orgs = new ArrayList<OrganizationVO>();
					orgAdapter = new OrgListAdapter(orgs,
							getApplicationContext(), 0, 0);
					list.setAdapter(orgAdapter);
				}
				break;
			// 人员详情
			case 4:
				// 从url返回的数据进行解析，然后加载到列表中.
				// JSONObject json3 = result.getData();
				// EmployeeVO t2 = (EmployeeVO) JSON.parseObject(
				// json3.toJSONString(), EmployeeVO.class);
				// Intent intent = new Intent(EmpQueryActivity.this,
				// EmpDetailActivity.class);
				// intent.putExtra("empDetail", t2);
				// startActivity(intent);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	private void search(String content) {
		try {
			results = HttpRequire.search(isSearchEmp, content, startIndex,
					loginUser, token);
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (OnEmpQueryListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnEmpQueryListener");
		}
	}

	private void setImage(boolean b) {
		if (b) {
			searchOrg.setBackgroundResource(R.drawable.zuzhi);
			searchEmp.setBackgroundResource(R.drawable.renyuan);
			searchText.setHint(R.string.search_emp_hint);
		} else {
			searchOrg.setBackgroundResource(R.drawable.zuzhi2);
			searchEmp.setBackgroundResource(R.drawable.renyuan2);
			searchText.setHint(R.string.search_org_hint);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEmpQuery();
	}
	
	public void onResume(){
		super.onResume();
		setImage(true);
		isSearchEmp = true;
		searchText.setText("");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.emp_query, container, false);
	}

	private void initEmpQuery() {
		// 用户密码
		searchText = (EditText) findViewById(R.id.searchText);
		searchImage = (ImageView) findViewById(R.id.searchImage);
		searchEmp = (ImageView) findViewById(R.id.searchEmp);
		searchOrg = (ImageView) findViewById(R.id.searchOrg);
		// searchBtn = (TextView) findViewById(R.id.searchBtn);
		// cancelBtn = (Button) findViewById(R.id.cancelBtn);
		list = (ListView) findViewById(R.id.ListView);

		list.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				listener.leftBack(event);
				return false;
			}
		});
		// 去掉分割线。。
		Bundle intent = this.getArguments();
		list.setDivider(null);
		loginUser = intent.getString("loginUser");
		token = intent.getString("token");
		head = (ActionBar) findViewById(R.id.query_head);
		head.init(R.string.search, true, false, 50);
		head.setLeftAction(new AbstractAction(R.drawable.logo) {
			@Override
			public void performAction(View view) {
				listener.back();
			}
		});

		searchOrg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				isSearchEmp = false;
				setImage(isSearchEmp);
			}
		});

		searchEmp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				isSearchEmp = true;
				setImage(isSearchEmp);
			}

		});

		searchImage.setOnClickListener(new OnClickListener() {

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
					String id = "" + arg1.findViewById(R.id.orgname).getTag();
					goChild(id);
				} else {
					String id = "" + arg1.findViewById(R.id.empName).getTag();
					goEmpDetail(id);
				}
			}

		});
	}

	public void goEmpDetail(String r) {
		listener.onShowEmpdetail(Integer.parseInt(r));
	}

	public void goChild(String r) {
		listener.onShowAddressList(Integer.parseInt(r));
	}

	private static final int DIALOG_KEY = 0;
 
	/**
	 * 加载查询列表页面
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
				getActivity().showDialog(DIALOG_KEY);
			}
		}

		public String doInBackground(String... p) {
			search(content);
			return "";
		}

		@Override
		public void onPostExecute(String Re) {
			if (showDialog) {
				EmpQueryFragment.this.getActivity().removeDialog(DIALOG_KEY);
			}
		}

		@Override
		protected void onCancelled() {
			// 取消进度栏.
			if (showDialog) {
				EmpQueryFragment.this.getActivity().removeDialog(DIALOG_KEY);
			}
		}
	}
}
