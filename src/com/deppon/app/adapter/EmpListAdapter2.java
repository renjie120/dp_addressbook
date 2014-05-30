package com.deppon.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deppon.app.addressbook.AddressListActivity.EmpViewHolder2;
import com.deppon.app.addressbook.R;
import com.deppon.app.addressbook.bean.EmployeeVO;

/**
 * 最后一个组织机构级别下面的人员列表竖向adapter.
 * 
 * @author 130126
 * 
 */
public class EmpListAdapter2 extends BaseAdapter {
	private List<EmployeeVO> data;// 用于接收传递过来的Context对象
	private Context context;
	private float width;
	private float height;

	public EmpListAdapter2(List<EmployeeVO> data, Context contex, float width,
			float height) {
		super();
		this.data = data;
		this.context = contex;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != data) {
			count = data.size();
		}
		return count;
	}

	@Override
	public EmployeeVO getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		EmpViewHolder2 viewHolder = null;
		if (null == convertView) {
			viewHolder = new EmpViewHolder2();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.emp_item2, null);
			viewHolder.empName = (TextView) convertView
					.findViewById(R.id.empName);
			viewHolder.jobname = (TextView) convertView
					.findViewById(R.id.jobName);
			viewHolder.empPhone = (TextView) convertView
					.findViewById(R.id.empPhone);
			viewHolder.call = (ImageView) convertView
					.findViewById(R.id.call);
			viewHolder.shortmessage = (ImageView) convertView
					.findViewById(R.id.shortmessage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (EmpViewHolder2) convertView.getTag();
		}

		EmployeeVO markerItem = getItem(position);
		if (null != markerItem) {
			viewHolder.empName.setText(markerItem.getEmpName());
			viewHolder.empPhone.setText(markerItem.getMobileNo());
			viewHolder.jobname.setText(markerItem.getJobName());
			viewHolder.empName.setTag(markerItem.getEmpId() + "");
			viewHolder.call.setTag(markerItem.getMobileNo() + "");
			viewHolder.shortmessage.setTag(markerItem.getMobileNo() + "");
		}
		return convertView;
	}
}