package com.deppon.app.addressbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deppon.app.adapter.EmpListAdapter;
import com.deppon.app.adapter.EmpListAdapter2;
import com.deppon.app.adapter.OrgListAdapter;
import com.deppon.app.addressbook.bean.EmployeeVO;
import com.deppon.app.addressbook.bean.OrganizationVO;
import com.deppon.app.addressbook.bean.SearchResult;
import com.deppon.app.addressbook.bean.ServerResult;
import com.deppon.app.addressbook.util.ActionBar;
import com.deppon.app.addressbook.util.ActionBar.AbstractAction;
import com.deppon.app.addressbook.util.ActionBar.OnRefreshClickListener;
import com.deppon.app.addressbook.util.Constant;
import com.deppon.app.addressbook.util.HttpRequire;

/**
 * 通讯录首页.
 * @author 130126
 *
 */
public class AddressListActivity extends BaseActivity {
	private ListView list;
	private String token, auth;
	private HorizontalListView list2;
	private static final int DIALOG_KEY = 0;
	private ProgressDialog dialog;
	private ServerResult result;
	// ListView底部View
	private View moreView;
	// 设置一个最大的数据条数，超过即不再加载
	private int MaxDateNum;
	// 每页显示的条数
	private static int pageSize = 5;
	// 默认开始显示的页码
	private int currentPage = 1;
	private static int rootId = 103;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	private ProgressBar pg;
	private ArrayList<HashMap<String, Object>> listItem;
	private OrgListAdapter adapter;
	private EmpListAdapter2 adapter3;
	private EmpListAdapter adapter2;
	private ActionBar head;
	private float screenHeight = 0;
	private float screenWidth = 0;
	// 查看详情按钮的高度比例
	private final static float statusBtnMT = 65 / 470f;
	private final static float statusBtnML = 92 / 266f;
	private final static float textW = 100 / 170f;
	private final static float statusBtnH = 24 / 321f;
	private final static float statusBtnW = 160 / 266f;
	private final static float contentH = 107 / 470f;
	private final static float contentW = 254 / 266f;
	private LinearLayout.LayoutParams p;
	private boolean showMorePage = true;
	private final static float contentLM = 4 / 266f;
	private LinkedList<String> ids = new LinkedList<String>();
	private String parentOrgs = "";

	/**
	 * 查看活动详情.
	 * 
	 * @param arg0
	 */
	public void seeDetail(View arg0) {
		// LinearLayout layout = (LinearLayout) arg0;
		// Intent intent = new Intent(AddressListActivity.this,
		// ActivitesInfo.class);
		// intent.putExtra("eventid", layout.getTag().toString());
		// intent.putExtra("token", token);
		// intent.putExtra("auth", auth);
		// this.startActivity(intent);
	}

	/**
	 * 取消搜索框里面的文字信息.
	 * 
	 * @param arg0
	 */
	public void cancel(View arg0) {
		// LinearLayout layout = (LinearLayout) arg0;
		// Intent intent = new Intent(ActivitesList.this, ActivitesInfo.class);
		// intent.putExtra("eventid", layout.getTag().toString());
		// intent.putExtra("token", token);
		// intent.putExtra("auth", auth);
		// this.startActivity(intent);
	}

	/**
	 * 进行屏幕适配.
	 */
	private void adjustScreen() {
		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];
		head.init(R.string.app_name, false, true, (int) (screenHeight * barH));

	}

	/**
	 * 设置初始化界面.
	 */
	private void initAddress() {
		list = (ListView) findViewById(R.id.ListView);
		list2 = (HorizontalListView) findViewById(R.id.list2);
		Intent intent = getIntent();
		token = intent.getStringExtra("token");
		auth = intent.getStringExtra("auth");
		// 查询全部的订到的票的信息.
		// 实例化底部布局
		moreView = getLayoutInflater().inflate(R.drawable.moredata, null);
		pg = (ProgressBar) moreView.findViewById(R.id.pg);
		head = (ActionBar) findViewById(R.id.list_head);
		head.setLeftAction(new AbstractAction(R.drawable.back) {
			@Override
			public void performAction(View view) {
				String[] temps = parentOrgs.split(",");
				if (parentOrgs.lastIndexOf(",") > 0) {
					parentOrgs = parentOrgs.substring(0,
							parentOrgs.lastIndexOf(","));
				} else
					parentOrgs = "";
				new MyListLoader(true, Integer
						.parseInt(temps[temps.length - 1])).execute("");

			}
		});
		head.setRightAction(new ActionBar.RefreshAction(head));
		head.setRefreshEnabled(new OnRefreshClickListener() {
			public void onRefreshClick() {
				new MyListLoader(true, rootId).execute("");
			}
		});

		// 加载listview
		new MyListLoader(true, rootId).execute("");

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					//如果不是最后一个级别的列表，就进行下一级的显示.
				if (!isLastLevel){
					String id = "" + arg1.findViewById(R.id.orgname).getTag();
					String parentId = ""
							+ arg1.findViewById(R.id.orgparent).getTag();
					ids.addLast(id);
					if ("".equals(parentOrgs)) {
						parentOrgs = parentId;
					} else {
						parentOrgs += "," + parentId;
					}
				
					new MyListLoader(true, Integer.parseInt(id)).execute("");
				}
				else{
					String id = "" + arg1.findViewById(R.id.empName).getTag();
					goEmpDetail(id);
				}
			}

		});

		list2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String id = "" + arg1.findViewById(R.id.empName).getTag();
				goEmpDetail(id);
				// ids.addLast(id);
				// new MyListLoader(true, Integer.parseInt(id)).execute("");
			}

		});
		adjustScreen();
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

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.addresslist);
		JPushInterface.init(getApplicationContext());
		ids.addFirst(rootId+"");
		initAddress();
	}

	private boolean isLastLevel = false;
	/**
	 * 对页面的元素进行处理的回调类.
	 */
	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				alert("对不起，出现异常");
				break;
			case 3:
				// 从url返回的数据进行解析，然后加载到列表中.
				JSONObject json = result.getData();
				SearchResult t = (SearchResult) JSON.parseObject(
						json.toJSONString(), SearchResult.class);
				// 得到全部的组织列表
				List<OrganizationVO> orgs = t.getOrgs();
				List<EmployeeVO> employees = t.getEmps();
				// 如果有人员和组织机构，就显示两个列表
				if (orgs != null && orgs.size() > 0) {
					isLastLevel = false;
					list2.setVisibility(View.VISIBLE);
					adapter = new OrgListAdapter(orgs,
							AddressListActivity.this, screenWidth, screenHeight);
					adapter2 = new EmpListAdapter(employees,
							AddressListActivity.this, screenWidth, screenHeight);
					list2.setAdapter(adapter2);
					list.setAdapter(adapter);
				} else {
					isLastLevel = true;
					list2.setVisibility(View.GONE);
					adapter3 = new EmpListAdapter2(employees,
							AddressListActivity.this, screenWidth, screenHeight);
					list.setAdapter(adapter3);
				}
				break;
			// 人员详情
			case 4:
				// 从url返回的数据进行解析，然后加载到列表中.
				JSONObject json2 = result.getData();
				EmployeeVO t2 = (EmployeeVO) JSON.parseObject(
						json2.toJSONString(), EmployeeVO.class);
				Intent intent = new Intent(AddressListActivity.this,
						EmpDetailActivity.class);
				intent.putExtra("empDetail", t2);
				startActivity(intent);
				break;
			// 人员查询界面
			case 5:
				Intent intent2 = new Intent(AddressListActivity.this,
						EmpQueryActivity.class);
				startActivity(intent2);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

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
		private int root;

		public MyListLoader(boolean showDialog, int root) {
			this.showDialog = showDialog;
			this.root = root;
		}

		@Override
		protected void onPreExecute() {
			// 执行过程中显示进度栏.
			if (showDialog) {
				showDialog(DIALOG_KEY);
			}
		}

		public String doInBackground(String... p) {
			getOrgByChildern(root);
			return "";
		}

		@Override
		public void onPostExecute(String Re) {
			// 是顶级的时候，右边的按钮表示跳转到查询界面
			if (root == rootId) {
				head.setLeftVisible(false);
				head.setRefreshEnabled(new OnRefreshClickListener() {
					public void onRefreshClick() {
						myHandler.sendEmptyMessage(5);
					}
				});
			}
			// 不是顶级的时候，右边的菜单是返回顶级
			else {
				head.setLeftVisible(true);
				head.setRefreshEnabled(new OnRefreshClickListener() {
					public void onRefreshClick() {
						new MyListLoader(true, rootId).execute("");
					}
				});
			}
			/**
			 * 完成的时候就取消进度栏.
			 */
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

	/**
	 * 调用远程请求查询结果数据.
	 */
	private void getOrgByChildern(int r) {
		if (Constant.debug) {
			myHandler.sendEmptyMessage(9);
		} else {
			try {
				result = HttpRequire.getOrgByChildern(r);
				// 如果返回数据不是1，就说明出现异常.
				if (result.getErrorCode() < 0) {
					myHandler.sendEmptyMessage(1);
				}
				// 否则就进行文件解析处理.
				else {
					myHandler.sendEmptyMessage(3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
 
	

	public final static class ViewHolder {
		public TextView name;
		public TextView parent;
	}

	public final static class EmpViewHolder {
		public TextView empName;
	}

	public final static class EmpViewHolder2 {
		public TextView jobname;
		public TextView empName;
		public TextView empPhone;
	}
}
