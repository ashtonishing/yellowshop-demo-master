package com.buyopicadmin.admin.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.buyopic.android.network.Constants;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BaseActivity;
import com.buyopicadmin.admin.LoginActivity;
import com.buyopicadmin.admin.MerchantCreateActivity;
import com.buyopicadmin.admin.MerchantOfferListActivity;

public class SlideMenuOptionsListFragment extends Fragment implements
		OnItemClickListener {

	private List<String> list;
	private ListView listView;
	private Context mContext;

	@Override
	public void onResume() {
		super.onResume();
		list = Utils.getStrings();
		listView.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		listView = new ListView(getActivity());
		listView.setOnItemClickListener(this);
		return listView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(getActivity() instanceof BaseActivity)
		{
			BaseActivity baseActivity=(BaseActivity) getActivity();
			baseActivity.closeMenu();
		}
		Intent intent=null;
		
		switch (arg2) {
		case 0:
			intent=new Intent(mContext,MerchantOfferListActivity.class);
			break;
		case 1:
			intent=new Intent(mContext,MerchantCreateActivity.class);
			intent.putExtra(MerchantCreateActivity.KEY_IS_FROM_UPDATE, true);
			break;
		case 2:
			intent=new Intent(mContext,LoginActivity.class);
			mContext.sendBroadcast(new Intent(Constants.CUSTOM_ACTION_INTENT));
			break;

		default:
			break;
		}
		if(intent!=null)
		{
		startActivity(intent);
		}

	}
}
