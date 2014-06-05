package com.deppon.app.addressbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.deppon.app.addressbook.util.BaseFragment;
import com.deppon.app.addressbook.util.Constant;
import com.deppon.app.addressbook.util.HttpRequire;

/**
 * 通讯录首页.
 * 
 * @author 130126
 * 
 */
public class AddressListFragment extends BaseFragment {
	private OnAddressListRefreshListener listener;
	public interface OnAddressListRefreshListener {
		public void onShowAddressList(int root);
		
		public void onShowEmpdetail(int empId);
	} 
	public static final String ROOT_ID = "rootId";
	public static final String USERID = "userId";
	public static final String TOKEN = "token";
	private ListView list;
	private int rootId;
	private String token, loginUser;
	private HorizontalListView list2;
	private static final int DIALOG_KEY = 0;
	private ServerResult result;
	// ListView底部View
	private View moreView;
	// 设置一个最大的数据条数，超过即不再加载
	private int MaxDateNum;
	// 每页显示的条数
	private static int pageSize = 5;
	// 默认开始显示的页码
	private int currentPage = 1;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	private ProgressBar pg;
	private ArrayList<HashMap<String, Object>> listItem;
	private OrgListAdapter adapter;
	private EmpListAdapter2 adapter3;
	private EmpListAdapter adapter2;
	private float screenHeight = 0;
	private float screenWidth = 0;
	private LinkedList<String> ids = new LinkedList<String>();
	private String parentOrgs = "";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (OnAddressListRefreshListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnAddressListRefreshListener");
		}
	}
	
	/**
	 * 查看活动详情.
	 * 
	 * @param arg0
	 */
	public void seeDetail(View arg0) {
		// LinearLayout layout = (LinearLayout) arg0;
		// Intent intent = new Intent(AddressListFragment.this.getActivity(),
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

	private ActionBar head;

	/**
	 * 设置初始化界面.
	 */
	private void initAddress() {
		head = (ActionBar) findViewById(R.id.list_head);
		head.init(R.string.app_name, true, false, 50);
		head.setLeftAction(new AbstractAction(R.drawable.logo) {
			@Override
			public void performAction(View view) {

			}
		});
		list = (ListView) findViewById(R.id.ListView);
		list2 = (HorizontalListView) findViewById(R.id.list2);
		Bundle intent = this.getArguments();
		token = intent.getString(TOKEN);
		loginUser = intent.getString(USERID);
		rootId = intent.getInt(ROOT_ID);
		// 查询全部的订到的票的信息.
		// 实例化底部布局
		moreView = this.getActivity().getLayoutInflater()
				.inflate(R.drawable.moredata, null);
		pg = (ProgressBar) moreView.findViewById(R.id.pg);

		// 加载listview
		new MyListLoader(true, rootId).execute("");

		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 如果不是最后一个级别的列表，就进行下一级的显示.
				if (!isLastLevel) {
					String id = "" + arg1.findViewById(R.id.orgname).getTag();
					String parentId = ""
							+ arg1.findViewById(R.id.orgparent).getTag();
					ids.addLast(id);
					System.out.println("当前查看的节点id是："+id);
					listener.onShowAddressList(Integer.parseInt(id));
					
//					if ("".equals(parentOrgs)) {
//						parentOrgs = parentId;
//					} else {
//						parentOrgs += "," + parentId;
//					}
//
//					new MyListLoader(true, Integer.parseInt(id)).execute("");
				} else {
					//得到empID
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
	}

	/**
	 * 人员详情.
	 */
	public void goEmpDetail(String r) {
		listener.onShowEmpdetail(Integer.parseInt(r));
//		if (Constant.debug) {
//			myHandler.sendEmptyMessage(9);
//		} else {
//			try {
//				System.out.println("查询人员详情："+r);
//				result = HttpRequire.getEmpDetail(r, loginUser, token);
//				// 如果返回数据不是1，就说明出现异常.
//				if (result.getErrorCode() < 0) {
//					myHandler.sendEmptyMessage(1);
//				}
//				// 否则就进行文件解析处理.
//				else {
//					myHandler.sendEmptyMessage(4);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ids.addFirst(rootId + "");
		initAddress();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("HomeGridviewFragement____onCreateView");
		return inflater.inflate(R.layout.addresslist, container, false);
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
							AddressListFragment.this.getActivity(),
							screenWidth, screenHeight);
					adapter2 = new EmpListAdapter(employees,
							AddressListFragment.this.getActivity(),
							screenWidth, screenHeight);
					list2.setAdapter(adapter2);
					list.setAdapter(adapter);
				} else {
					isLastLevel = true;
					list2.setVisibility(View.GONE);
					adapter3 = new EmpListAdapter2(employees,
							AddressListFragment.this.getActivity(),
							screenWidth, screenHeight);
					list.setAdapter(adapter3);
				}
				break;
			// 人员详情
			case 4:
				// 从url返回的数据进行解析，然后加载到列表中.
				JSONObject json2 = result.getData();
				EmployeeVO t2 = (EmployeeVO) JSON.parseObject(
						json2.toJSONString(), EmployeeVO.class);
				Intent intent = new Intent(
						AddressListFragment.this.getActivity(),
						EmpDetailActivity.class);
				intent.putExtra("empDetail", t2);
				startActivity(intent);
				break;
			// 人员查询界面
			case 5:
				Intent intent2 = new Intent(
						AddressListFragment.this.getActivity(),
						EmpQueryActivity.class);
				startActivity(intent2);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

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
				getActivity().showDialog(DIALOG_KEY);
			}
		}

		public String doInBackground(String... p) {
			getOrgByChildern(root);
			return "";
		}

		@Override
		public void onPostExecute(String Re) {
			/**
			 * 完成的时候就取消进度栏.
			 */
			if (showDialog) {
				AddressListFragment.this.getActivity().removeDialog(DIALOG_KEY);
			}
		}

		@Override
		protected void onCancelled() {
			// 取消进度栏.
			if (showDialog) {
				AddressListFragment.this.getActivity().removeDialog(DIALOG_KEY);
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
				result = HttpRequire.getOrgByChildern(r, loginUser, token);
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
		public ImageView people;
	}

	public final static class EmpViewHolder2 {
		public TextView jobname;
		public TextView empName;
		public TextView empPhone;
		public ImageView call;
		public ImageView shortmessage;
	}
}
