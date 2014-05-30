package com.deppon.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deppon.app.addressbook.AddressListActivity.EmpViewHolder;
import com.deppon.app.addressbook.R;
import com.deppon.app.addressbook.bean.EmployeeVO;

/**
 * 横向的人员列表适配器.
 * 
 * @author 130126
 * 
 */
public class EmpListAdapter extends BaseAdapter {
	private List<EmployeeVO> data;// 用于接收传递过来的Context对象
	private Context context;
	private float width;
	private float height;

	public EmpListAdapter(List<EmployeeVO> data, Context contex, float width,
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
		EmpViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new EmpViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.emp_item, null);
			viewHolder.empName = (TextView) convertView
					.findViewById(R.id.empName);
			viewHolder.people = (ImageView) convertView
					.findViewById(R.id.people);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (EmpViewHolder) convertView.getTag();
		}

		EmployeeVO markerItem = getItem(position);
		if (null != markerItem) {
			viewHolder.empName.setText(markerItem.getEmpName());
			viewHolder.empName.setTag(markerItem.getEmpId() + "");
			if ("m".equals(markerItem.getGender()))
				viewHolder.people.setBackgroundResource(R.drawable.man);
			else
				viewHolder.people.setBackgroundResource(R.drawable.women);

		}
		return convertView;
	}
}
