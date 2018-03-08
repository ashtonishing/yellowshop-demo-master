package com.buyopicadmin.admin.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomFootFallByConsumerAdapter;
import com.buyopicadmin.admin.models.ConsumerInformation;

public class FootTrafficByConsumer extends Fragment implements BuyopicNetworkCallBack,OnItemSelectedListener,OnClickListener
{

	private Context mContext;
	private ListView mListView;
	private ProgressDialog mProgressDialog;
	private Button mEmailReport;
	private Spinner mAllReports;
	private String mReportType;
	private BuyOpic buyOpic;
	private BuyopicNetworkServiceManager buyopicNetworkServiceManager;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext=activity;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buyOpic=(BuyOpic) mContext.getApplicationContext();
		showProgressDialog();
		mReportType="day";
		buyopicNetworkServiceManager=BuyopicNetworkServiceManager.getInstance(mContext);
		buyopicNetworkServiceManager.sendFootTrafficByConsumerRequest(com.buyopic.android.network.Constants.REQUEST_FOOT_TRAFFIC_BY_CONSUMER,
				buyOpic.getmStoreId(), buyOpic.getmRetailerId(), buyOpic.getmMerchantId(), mReportType,this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.layout_foot_traffic_by_consumer, null);
		Utils.overrideFonts(mContext, view);
		mListView=(ListView)view.findViewById(R.id.foot_traffic_by_consumer_listview);
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
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		if(requestCode==com.buyopic.android.network.Constants.REQUEST_FOOT_TRAFFIC_BY_CONSUMER||requestCode==Constants.REQUEST_FOOT_TRAFFIC_BY_CONSUMER_EMAIL)
		{
			List<ConsumerInformation> consumerList=JsonResponseParser.parseFootTrafficByConsumerResponse((String)object);
			bindDataToViews(consumerList);
			if(requestCode==Constants.REQUEST_FOOT_TRAFFIC_BY_CONSUMER_EMAIL){
				Utils.showToast(mContext, "Email sent successfully");
			}
		}
	
		
	}

	private void bindDataToViews(List<ConsumerInformation> consumerList) {
		if(consumerList!=null && !consumerList.isEmpty())
		{
			mListView.setAdapter(new CustomFootFallByConsumerAdapter(mContext, consumerList));
		}
		else
		{
			Utils.showToast(mContext, "No Results Found");
			mListView.setAdapter(null);
		
		}
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
		buyopicNetworkServiceManager.sendFootTrafficByConsumerRequest(com.buyopic.android.network.Constants.REQUEST_FOOT_TRAFFIC_BY_CONSUMER,
				buyOpic.getmStoreId(), buyOpic.getmRetailerId(), buyOpic.getmMerchantId(), mReportType,this);

		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		if(id==R.id.emailreport){
			showProgressDialog();
			buyopicNetworkServiceManager.sendEmailForReportsRequest(com.buyopic.android.network.Constants.REQUEST_FOOT_TRAFFIC_BY_CONSUMER_EMAIL,
					buyOpic.getmStoreId(), buyOpic.getmRetailerId(), buyOpic.getmMerchantId(), mReportType,"yes",this);

		}
	}
	
	

}
