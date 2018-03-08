package com.buyopicadmin.admin.fragments;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomClickThroughFragmentAdapter;
import com.buyopicadmin.admin.models.ClickThrough;

public class ClickThroughDetailsFragment extends Fragment implements BuyopicNetworkCallBack,OnClickListener,OnItemSelectedListener {

	private ListView mListView;
	private ProgressDialog mProgressDialog;
	private Context mContext;
	private BuyopicNetworkServiceManager buyopicNetworkServiceManager;
	private TextView mTotalTopLevelListingsView;
	private TextView mMerchantPageClickView;
	private TextView mDetailsPageClickView;
	private Button mEmailReport;
	private Spinner mAllReports;
	private String mReportType;
	private BuyOpic buyOpic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buyOpic=(BuyOpic) mContext.getApplicationContext();
		buyopicNetworkServiceManager=BuyopicNetworkServiceManager.getInstance(mContext);
		showProgressDialog();
		mReportType ="day";
		buyopicNetworkServiceManager.sendClickThroughDetailsTimeRequest(Constants.REQUEST_CLICK_THROUGH_DETAILS, buyOpic.getmStoreId(), buyOpic.getmRetailerId(), buyOpic.getmMerchantId(),mReportType, this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.layout_click_through_details_fragment, null);
		Utils.overrideFonts(mContext, view);
		mListView=(ListView)view.findViewById(R.id.click_through_listview);
		mTotalTopLevelListingsView=(TextView)view.findViewById(R.id.click_through_total_top_level_listings);
		mMerchantPageClickView=(TextView)view.findViewById(R.id.click_through_total_merchant_clicks);
		mDetailsPageClickView=(TextView)view.findViewById(R.id.click_through_total_details_clicks);
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
	public void onSuccess(int requestCode, Object object) {
			dismissProgressDialog();
			switch (requestCode) {
			case Constants.REQUEST_CLICK_THROUGH_DETAILS:
			case Constants.REQUEST_GET_CLICK_THROUGH_DETAIL_EMAIL:
				
				List<ClickThrough> clickThroughDetails=JsonResponseParser.parseClickThroughResponse((String)object);
				bindClickThroughDetailsToViews(clickThroughDetails);
				if(requestCode==Constants.REQUEST_GET_CLICK_THROUGH_DETAIL_EMAIL){
					Utils.showToast(mContext, "Email sent successfully");
				}
				break;

			default:
				break;
			}
	}

	private void bindClickThroughDetailsToViews(
			List<ClickThrough> clickThroughDetails) {
		if(clickThroughDetails!=null && !clickThroughDetails.isEmpty())
		{
			mListView.setAdapter(new CustomClickThroughFragmentAdapter(mContext, clickThroughDetails));
			HashMap<Integer, Integer> totals=calculateTotals(clickThroughDetails);
			mTotalTopLevelListingsView.setText(String.valueOf(totals.get(0).intValue()));
			mMerchantPageClickView.setText(String.valueOf(totals.get(1).intValue()));
			mDetailsPageClickView.setText(String.valueOf(totals.get(2).intValue()));
		}
		else
		{
			mListView.setAdapter(null);
			Utils.showToast(mContext, "No Results Found");
		}
		
		
	}
	
	private HashMap<Integer, Integer> calculateTotals(List<ClickThrough> clickThroughDetails)
	{
		HashMap<Integer, Integer> hashMap=new HashMap<Integer, Integer>();
		hashMap.put(0, 0);
		hashMap.put(1, 0);
		hashMap.put(2, 0);
		if(clickThroughDetails!=null && !clickThroughDetails.isEmpty())
		{
			for(ClickThrough clickThrough:clickThroughDetails)
			{
				hashMap.put(0, (hashMap.get(0).intValue())+Integer.parseInt(clickThrough.getmTopLevelListing()));
				hashMap.put(1, (hashMap.get(1).intValue())+Integer.parseInt(clickThrough.getmMerchantPage()));
				hashMap.put(2, (hashMap.get(2).intValue())+Integer.parseInt(clickThrough.getmDetailPage()));
			}
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
		// TODO Auto-generated method stub
		int id=v.getId();
		if(id==R.id.emailreport){
			showProgressDialog();
			buyopicNetworkServiceManager.sendEmailForReportsRequest(com.buyopic.android.network.Constants.REQUEST_GET_CLICK_THROUGH_DETAIL_EMAIL,
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
		buyopicNetworkServiceManager.sendClickThroughDetailsTimeRequest(Constants.REQUEST_CLICK_THROUGH_DETAILS, buyOpic.getmStoreId(), buyOpic.getmRetailerId(), buyOpic.getmMerchantId(),mReportType, this);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
