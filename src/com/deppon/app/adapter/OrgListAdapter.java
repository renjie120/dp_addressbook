package com.deppon.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deppon.app.addressbook.R;
import com.deppon.app.addressbook.AddressListActivity.ViewHolder;
import com.deppon.app.addressbook.bean.OrganizationVO;

/**
 * 组织机构列表适配器 .
 * @author 130126
 *
 */
public class OrgListAdapter extends BaseAdapter {
	private List<OrganizationVO> data;// 用于接收传递过来的Context对象
	private Context context;
	private float width;
	private float height;

	public OrgListAdapter(List<OrganizationVO> data, Context contex,
			float width, float height) {
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
	public OrganizationVO getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.org_item, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.orgname);
			viewHolder.parent = (TextView) convertView
					.findViewById(R.id.orgparent);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		OrganizationVO markerItem = getItem(position);
		if (null != markerItem) {
			viewHolder.name.setText(markerItem.getOrgName());
			viewHolder.name.setTag(markerItem.getOrgId() + "");
			viewHolder.parent.setTag(markerItem.getParentId());
		}
		return convertView;
	}
}
