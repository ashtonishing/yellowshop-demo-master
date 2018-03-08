package com.buyopicadmin.admin.fragments;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomFootfallZoneByCustomerAdapter;
import com.buyopicadmin.admin.models.FootfallZoneByCustomer;

public class FootFallZonesByCustomerFragment extends Fragment implements
		BuyopicNetworkCallBack {

	private ListView listView;
	private Activity mContext;
	private ProgressDialog mProgressDialog;
	private TextView trafficTotalTimeView;
	private List<FootfallZoneByCustomer> footfallZoneCustomers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BuyOpic buyOpic = (BuyOpic) mContext.getApplication();
		BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
				.getInstance(mContext);
		
		 showProgressDialog();
		  buyopicNetworkServiceManager.sendCustomerAverageTrafficRequest(
		  Constants.REQUEST_FOOT_FALL_ZONE_BY_CUSTOMER, buyOpic.getmMerchantId(),
		  buyOpic.getmRetailerId(), buyOpic.getmStoreId(), this);
		 
	}

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
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.layout_foot_fall_zones_by_customer, null);
		trafficTotalTimeView = (TextView) view
				.findViewById(R.id.foot_fall_zones_total_time);
		listView = (ListView) view
				.findViewById(R.id.foot_fall_by_zone_listview);
		return view;
	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_FOOT_FALL_ZONE_BY_CUSTOMER:
			footfallZoneCustomers=JsonResponseParser.parseFootFallZoneByCustomerResponse((String)object);

			if (footfallZoneCustomers != null
					&& !footfallZoneCustomers.isEmpty()) {
				bindFootFallsZoneByCustomersViews(footfallZoneCustomers);
				trafficTotalTimeView.setText(String.valueOf(getTotalTime(footfallZoneCustomers)));
				
			} else {
				Utils.showToast(mContext, "No Results Found");
			}

			break;

		default:
			break;
		}
	}

	private int getTotalTime(
			List<FootfallZoneByCustomer> footfallZoneCustomers2) {
		int totalTime=0;
		for(FootfallZoneByCustomer byCustomer:footfallZoneCustomers2)
		{
			if(!TextUtils.isEmpty(byCustomer.getmTotalTime()) && TextUtils.isDigitsOnly(byCustomer.getmTotalTime().toString()))
			{
				totalTime+=Integer.parseInt(byCustomer.getmTotalTime());
			}
		}
		return totalTime;
	}

	private void bindFootFallsZoneByCustomersViews(
			List<FootfallZoneByCustomer> footFallZonesByCustomers) {
		if (footFallZonesByCustomers != null
				&& !footFallZonesByCustomers.isEmpty()) {
			listView.setAdapter(new CustomFootfallZoneByCustomerAdapter(
					mContext, footFallZonesByCustomers));
		}
	}

	public static String getMinutesSeconds(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm");
		try {
			Date d = dateFormat.parse(time);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("mm:ss");
			return dateFormat2.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "N/A";
	}

	public static String getAverageTime(String avgTime) {
		try {
			double d = Double.parseDouble(avgTime);
			DecimalFormat decimalFormat = new DecimalFormat("#0.00");
			return decimalFormat.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "N/A";
	}

	/*
	 * private HashMap<String, Object> parseTotal() { HashMap<String, Object>
	 * hashMap = new HashMap<String, Object>();
	 * 
	 * int totalCustomers = 0; int totalTime = 0; double avgTime = 0.0; try {
	 * for (TrafficZone trafficZone : footfallZoneCustomers) { totalCustomers +=
	 * Integer.parseInt(trafficZone .getmTotalCustomers()); totalTime += Integer
	 * .parseInt(trafficZone.getmTotalTimeInZone()); avgTime +=
	 * Double.parseDouble(trafficZone.getmAvgTimeinZone()); }
	 * hashMap.put("totalCust", totalCustomers); hashMap.put("totalTime",
	 * totalTime); hashMap.put("avgTime", avgTime); } catch (Exception e) {
	 * e.printStackTrace(); } return hashMap; }
	 */
	@Override
	public void onFailure(int requestCode, String message) {

	}

}
