package com.buyopicadmin.admin.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomStoreMapZoneAdapter;
import com.buyopicadmin.admin.models.StoreMap;

public class CopyOfStoreMapFragment extends Fragment implements OnClickListener {

	private GridLayout zone0GridLayout;
	private GridLayout zone1GridLayout;
	private GridLayout zone2GridLayout;
	private GridLayout zone3GridLayout;
	private TextView zone0NameView;
	private TextView zone1NameView;
	private TextView zone2NameView;
	private TextView zone3NameView;
	private Context mContext;
	private GridView gridView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		List<StoreMap> mStoreMaps = new ArrayList<StoreMap>();
		StoreMap storeMap=new StoreMap();
		storeMap.setmZoneName("Zone0");
		mStoreMaps.add(storeMap);
		
		storeMap=new StoreMap();
		storeMap.setmZoneName("Zone1");
		mStoreMaps.add(storeMap);
		
		storeMap=new StoreMap();
		storeMap.setmZoneName("Zone2");
		mStoreMaps.add(storeMap);
		
		storeMap=new StoreMap();
		storeMap.setmZoneName("Zone3");
		mStoreMaps.add(storeMap);
		gridView.setAdapter(new CustomStoreMapZoneAdapter(mContext, mStoreMaps));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext=activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.temp_layout_store_map, null);
		gridView=(GridView) view.findViewById(R.id.gridview);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zone0_grid_layout:
			
			break;

		default:
			break;
		}
	}

}
