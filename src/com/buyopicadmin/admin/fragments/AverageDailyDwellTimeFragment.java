package com.buyopicadmin.admin.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomAverageTrafficZoneAdapter;
import com.buyopicadmin.admin.models.Chart;
import com.buyopicadmin.admin.models.TrafficZone;

public class AverageDailyDwellTimeFragment extends Fragment implements BuyopicNetworkCallBack, OnClickListener,OnItemSelectedListener {

	private ListView listView;
	private Activity mContext;
	private TextView totalCustomersView;
	private TextView trafficTotalTimeView;
	private TextView traffic_avg_time;
	private List<TrafficZone> trafficZones;
	private ProgressDialog mProgressDialog;
	private onBuyOpicItemClickListener buyOpicItemClickListener;
	private String response=null;
	private Button mEmailReport;
	private Spinner mAllReports;
	private BuyopicNetworkServiceManager buyopicNetworkServiceManager;
	private BuyOpic buyOpic;
	private String mReportType;
	CustomAverageTrafficZoneAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
		buyOpicItemClickListener=(onBuyOpicItemClickListener) activity;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		buyOpic=(BuyOpic) mContext.getApplication();
		buyopicNetworkServiceManager=BuyopicNetworkServiceManager.getInstance(mContext);
		showProgressDialog();
		mReportType="day";
		buyopicNetworkServiceManager.sendAverageDailyDwellTimeRequest(
				Constants.REQUEST_AVERAGE_DAILY_DWELL_TIME, buyOpic.getmStoreId(),
				buyOpic.getmRetailerId(),buyOpic.getmMerchantId(),mReportType, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_average_customer_traffic,
				null);
		Utils.overrideFonts(mContext, view);
		totalCustomersView=(TextView)view.findViewById(R.id.traffic_total_no_of_customers);
		trafficTotalTimeView=(TextView)view.findViewById(R.id.traffic_total_time);
		traffic_avg_time=(TextView)view.findViewById(R.id.traffic_avg_time);
		listView = (ListView) view.findViewById(R.id.traffic_listview);
		listView.setDrawingCacheEnabled(true);
		Button showchartbuttonView=(Button) view.findViewById(R.id.showchartbutton);
		showchartbuttonView.setOnClickListener(this);
		mEmailReport=(Button)view.findViewById(R.id.emailreport);
		mEmailReport.setOnClickListener(this);
		mAllReports=(Spinner)view.findViewById(R.id.timewisereportspinneer);
		List<String> list = new ArrayList<String>();
        list.add("Day");
        list.add("Week");
        list.add("Month");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAllReports.setAdapter(dataAdapter);	
        mAllReports.setSelected(true);
        mAllReports.setOnItemSelectedListener(this);
		
		return view;
	}


	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_AVERAGE_DAILY_DWELL_TIME:
		case Constants.REQUEST_GET_AVERAGE_DAILY_DWELL_TIME_EMAIL:	
				response=(String)object;
				trafficZones=JsonResponseParser.parseCustomerAverageTrafficZoneResponse((String)object);
				if(trafficZones!=null && !trafficZones.isEmpty())
				{
					adapter=new CustomAverageTrafficZoneAdapter(mContext, trafficZones);
					adapter.notifyDataSetChanged();
					listView.setAdapter(adapter);
					HashMap<String, Object> hashMap=parseTotal();

					if(hashMap!=null && !hashMap.isEmpty())
					{
					totalCustomersView.setText(""+(hashMap.containsKey("totalCust")?hashMap.get("totalCust"):"N/A"));
					trafficTotalTimeView.setText(""+(hashMap.containsKey("totalTime")?Utils.getHourMinutesSeconds(hashMap.get("totalTime").toString()):"N/A"));
					traffic_avg_time.setText((hashMap.containsKey("avgTime")?Utils.getHourMinutesSeconds(hashMap.get("avgTime").toString()):""));
					}
				}
				else
				{
					listView.setAdapter(null);
					Utils.showToast(mContext, "No Results Found");
				}
				if(requestCode==Constants.REQUEST_GET_AVERAGE_DAILY_DWELL_TIME_EMAIL){
					Utils.showToast(mContext, "Email sent successfully");
				}
			
			break;

		default:
			break;
		}
	}
	
	

	
	private HashMap<String, Object> parseTotal()
	{
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		
		int totalCustomers=0;
		int totalTime=0;
		int avgTime=0;
		try
		{
		for(TrafficZone trafficZone:trafficZones)
		{
			totalCustomers+=Integer.parseInt(trafficZone.getmTotalCustomers());
			totalTime+=Integer.parseInt(trafficZone.getmTotalTimeInZone());
			avgTime+=Integer.parseInt(trafficZone.getmAvgTimeinZone());
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

		dismissProgressDialog();
		
		if(requestCode==Constants.REQUEST_FOOT_TRAFFIC_BY_TIME_OF_DAY_EMAIL){
			Utils.showToast(mContext, "Failed to sent Email");
		}
		else
		{
			Utils.showToast(mContext, "Failed to Load");
		}
	
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.showchartbutton)
		{
			if(response!=null)
			{
				Chart chart=new Chart();
				chart.setResponse(response);
				chart.setRequestCode(Constants.REQUEST_AVERAGE_DAILY_DWELL_TIME);
				buyOpicItemClickListener.onItemClicked(chart);
			}
		}

		// TODO Auto-generated method stub
	
		else if(id==R.id.emailreport){
			showProgressDialog();
			buyopicNetworkServiceManager.sendEmailForReportsRequest(com.buyopic.android.network.Constants.REQUEST_GET_AVERAGE_DAILY_DWELL_TIME_EMAIL,
					buyOpic.getmStoreId(), buyOpic.getmRetailerId(), buyOpic.getmMerchantId(), mReportType,"yes",this);

		}
	
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		showProgressDialog();
		if(position==0)
			mReportType="day";
		else if(position==1)
			mReportType="week";
		else if(position==2)
			mReportType="month";
		buyopicNetworkServiceManager.sendAverageDailyDwellTimeRequest(
				Constants.REQUEST_AVERAGE_DAILY_DWELL_TIME, buyOpic.getmStoreId(),
				buyOpic.getmRetailerId(),buyOpic.getmMerchantId(),mReportType, this);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
