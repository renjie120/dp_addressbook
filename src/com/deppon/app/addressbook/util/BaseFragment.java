package com.deppon.app.addressbook.util;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {
	public void alert(String mess) {
		new AlertDialog.Builder(this.getActivity()).setTitle("提示")
				.setMessage(mess).setPositiveButton("确定", null).show();
	}
	 
	public View findViewById(int id){
		return getView().findViewById(id);
	}
	
	public Context getApplicationContext(){
		return this.getActivity().getApplicationContext();
	}
}
