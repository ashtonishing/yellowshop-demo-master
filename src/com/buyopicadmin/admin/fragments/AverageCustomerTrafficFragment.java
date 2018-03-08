package com.buyopicadmin.admin.fragments;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.buyopicadmin.admin.adapters.CustomAverageTrafficZoneAdapter;
import com.buyopicadmin.admin.models.TrafficZone;

public class AverageCustomerTrafficFragment extends Fragment implements BuyopicNetworkCallBack {

	private ListView listView;
	private Activity mContext;
	private TextView totalCustomersView;
	private TextView trafficTotalTimeView;
	private TextView traffic_avg_time;
	private List<TrafficZone> trafficZones;
	private ProgressDialog mProgressDialog;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BuyOpic buyOpic=(BuyOpic) mContext.getApplication();
		BuyopicNetworkServiceManager buyopicNetworkServiceManager=BuyopicNetworkServiceManager.getInstance(mContext);
		showProgressDialog();
		buyopicNetworkServiceManager.sendCustomerAverageTrafficRequest(
				Constants.REQUEST_CUSTOMER_AVERAGE_TRAFFIC, buyOpic.getmMerchantId(),
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
		this.mContext=activity;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_average_customer_traffic,
				null);
		totalCustomersView=(TextView)view.findViewById(R.id.traffic_total_no_of_customers);
		trafficTotalTimeView=(TextView)view.findViewById(R.id.traffic_total_time);
		traffic_avg_time=(TextView)view.findViewById(R.id.traffic_avg_time);
		listView = (ListView) view.findViewById(R.id.traffic_listview);
		return view;
	}


	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_CUSTOMER_AVERAGE_TRAFFIC:
			
				trafficZones=JsonResponseParser.parseCustomerAverageTrafficZoneResponse((String)object);
				if(trafficZones!=null && !trafficZones.isEmpty())
				{
					listView.setAdapter(new CustomAverageTrafficZoneAdapter(mContext, trafficZones));
					HashMap<String, Object> hashMap=parseTotal();
					if(hashMap!=null && !hashMap.isEmpty())
					{
					totalCustomersView.setText(""+(hashMap.containsKey("totalCust")?hashMap.get("totalCust"):"N/A"));
					trafficTotalTimeView.setText(""+(hashMap.containsKey("totalTime")?getMinutesSeconds(""+hashMap.get("totalTime")):"N/A"));
					traffic_avg_time.setText(""+getAverageTime(""+(hashMap.containsKey("avgTime")?hashMap.get("avgTime"):"")));
					}
				}
				else
				{
					Utils.showToast(mContext, "No Results Found");
				}
			
			break;

		default:
			break;
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
	
	private HashMap<String, Object> parseTotal()
	{
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		
		int totalCustomers=0;
		int totalTime=0;
		double avgTime=0.0;
		try
		{
		for(TrafficZone trafficZone:trafficZones)
		{
			totalCustomers+=Integer.parseInt(trafficZone.getmTotalCustomers());
			totalTime+=Integer.parseInt(trafficZone.getmTotalTimeInZone());
			avgTime+=Double.parseDouble(trafficZone.getmAvgTimeinZone());
		}
		hashMap.put("totalCust", totalCustomers);
		hashMap.put("totalTime", totalTime);
		hashMap.put("avgTime", avgTime);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return hashMap;
	}


	@Override
	public void onFailure(int requestCode, String message) {
		
	}
	
	

}
