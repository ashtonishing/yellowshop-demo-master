package com.buyopicadmin.admin.fragments;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.buyopicadmin.admin.adapters.CustomFootfallZoneByCustomerAdapter;
import com.buyopicadmin.admin.models.FootfallZoneByCustomer;

public class DwellTimeByConsumerFragment extends Fragment implements
		BuyopicNetworkCallBack ,OnClickListener,OnItemSelectedListener{

	private ListView listView;
	private Activity mContext;
	private ProgressDialog mProgressDialog;
	private TextView trafficTotalTimeView;
	private List<FootfallZoneByCustomer> footfallZoneCustomers;
	private Button mEmailReport;
	private Spinner mAllReports;
	private String mReportType;
	private BuyOpic buyOpic;
	private BuyopicNetworkServiceManager buyopicNetworkServiceManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buyOpic = (BuyOpic) mContext.getApplication();
		buyopicNetworkServiceManager = BuyopicNetworkServiceManager
				.getInstance(mContext);
		mReportType="day";
		showProgressDialog();
		 buyopicNetworkServiceManager.sendDwellTimeByConsumerRequest(
		 Constants.REQUEST_DWELL_TIME_BY_CONSUMER,buyOpic.getmStoreId(),
		 buyOpic.getmRetailerId(),buyOpic.getmMerchantId(), mReportType,this);
		 
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
		Utils.overrideFonts(mContext, view);
		trafficTotalTimeView = (TextView) view
				.findViewById(R.id.foot_fall_zones_total_time);
		listView = (ListView) view
				.findViewById(R.id.foot_fall_by_zone_listview);
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
		case Constants.REQUEST_DWELL_TIME_BY_CONSUMER:
		case Constants.REQUEST_GET_DWELL_TIME_BY_CONSUMER_EMAIL:
			footfallZoneCustomers=JsonResponseParser.parseFootFallZoneByCustomerResponse((String)object);

			if (footfallZoneCustomers != null
					&& !footfallZoneCustomers.isEmpty()) {
				bindFootFallsZoneByCustomersViews(footfallZoneCustomers);
				String totalSpentTime=(String.valueOf(getTotalTime(footfallZoneCustomers)));
				
				trafficTotalTimeView.setText(Utils.getHourMinutesSeconds(totalSpentTime));
				
			} else {
				Utils.showToast(mContext, "No Results Found");
				listView.setAdapter(null);
			}
			if(requestCode==Constants.REQUEST_GET_DWELL_TIME_BY_CONSUMER_EMAIL){
				Utils.showToast(mContext, "Email sent successfully");
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
			if(!TextUtils.isEmpty(byCustomer.getmTotalTime()))
			{
				try
				{
				totalTime+=Integer.parseInt(byCustomer.getmTotalTime());
				}
				catch(Exception e)
				{
					e.printStackTrace();
					totalTime=0;
				}
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm",Locale.US);
		try {
			Date d = dateFormat.parse(time);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("mm:ss",Locale.US);
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
		// TODO Auto-generated method stub
		int id=v.getId();
		if(id==R.id.emailreport){
			showProgressDialog();
			buyopicNetworkServiceManager.sendEmailForReportsRequest(com.buyopic.android.network.Constants.REQUEST_GET_DWELL_TIME_BY_CONSUMER_EMAIL,
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
		buyopicNetworkServiceManager.sendDwellTimeByConsumerRequest(
				 Constants.REQUEST_DWELL_TIME_BY_CONSUMER,buyOpic.getmStoreId(),
				 buyOpic.getmRetailerId(),buyOpic.getmMerchantId(), mReportType,this);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
