package com.buyopicadmin.admin.fragments;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomStoreMapZoneAdapter;
import com.buyopicadmin.admin.models.StoreMap;

public class MallTierStoreMapFragment extends Fragment implements
		BuyopicNetworkCallBack, OnItemClickListener {

	private Context mContext;
	private onBuyOpicItemClickListener buyOpicItemClickListener;
	private List<StoreMap> storeMaps;
	private ListView listView;
	private TextView mTotalNoOfCustomerView;
	private ProgressDialog mProgressDialog;
	private Timer timer;
	private BuyopicNetworkServiceManager buyopicNetworkServiceManager;
	private BuyOpic buyOpic;

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Bundle bundle=getArguments();
		if(bundle!=null && bundle.containsKey("a"))
		{
		if (storeMaps != null && !storeMaps.isEmpty()) {
			bindStoreMapsInfo(storeMaps);
		} else {
			 buyOpic = (BuyOpic) mContext.getApplicationContext();
			showProgressDialog();
			buyopicNetworkServiceManager = BuyopicNetworkServiceManager
					.getInstance(mContext);
			buyopicNetworkServiceManager.sendStoreActiveConsumersRequest(
					Constants.REQUEST_STORE_ACTIVE_CONSUMERS,
					buyOpic.getmStoreId(), buyOpic.getmRetailerId(),
					buyOpic.getmMerchantId(), this);
		}
		}

	}
	
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(timer!=null)
		{
			timer.cancel();
		}
	}



	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			Message.obtain(handler).sendToTarget();
		}
	};

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			buyopicNetworkServiceManager.sendStoreActiveConsumersRequest(
					Constants.REQUEST_STORE_ACTIVE_CONSUMERS,
					buyOpic.getmStoreId(), buyOpic.getmRetailerId(),
					buyOpic.getmMerchantId(), MallTierStoreMapFragment.this);
		}

	};

	private void showProgressDialog() {

		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(mContext, "", "Please wait",
					false, false);
		}
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
		buyOpicItemClickListener = (onBuyOpicItemClickListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (timer == null) {
			timer = new Timer();
		}

		timer.scheduleAtFixedRate(task, 10000, 10000);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_mall_tier_store_map, null);
		Utils.overrideFonts(mContext, view);
		listView = (ListView) view
				.findViewById(R.id.mall_tier_store_map_list_view);
		listView.setOnItemClickListener(this);
		mTotalNoOfCustomerView = (TextView) view
				.findViewById(R.id.mall_tier_total_customers);
		return view;
	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_STORE_ACTIVE_CONSUMERS:
			storeMaps = JsonResponseParser
					.parseMallStoreActiveUserResponse((String) object);
			bindStoreMapsInfo(storeMaps);

			break;

		default:
			break;
		}

	}

	private void bindStoreMapsInfo(List<StoreMap> mStoreMaps) {
		if (mStoreMaps != null && !mStoreMaps.isEmpty()) {
			listView.setAdapter(new CustomStoreMapZoneAdapter(mContext,
					mStoreMaps));
		}
		mTotalNoOfCustomerView.setText(String
				.valueOf(getTotalNoOfCustomerCount(mStoreMaps)));
	}

	private int getTotalNoOfCustomerCount(List<StoreMap> mStoreMaps) {
		int totalCustomers = 0;
		for (StoreMap storeMap : mStoreMaps) {
			if (storeMap.getmConsumers() != null
					&& !storeMap.getmConsumers().isEmpty()) {
				totalCustomers += storeMap.getmConsumers().size();
			}
		}
		return totalCustomers;
	}

	@Override
	public void onFailure(int requestCode, String message) {
		dismissProgressDialog();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		StoreMap storeMap = (StoreMap) listView.getAdapter().getItem(arg2);
		if (storeMap != null) {
			buyOpicItemClickListener.onItemClicked(storeMap);
		}
	}

}
