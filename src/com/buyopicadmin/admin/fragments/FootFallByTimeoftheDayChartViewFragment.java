package com.buyopicadmin.admin.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.R;

public class FootFallByTimeoftheDayChartViewFragment extends Fragment implements
		BuyopicNetworkCallBack, OnClickListener,OnCheckedChangeListener {

	private WebView webView;
	private BuyopicNetworkServiceManager buyopicNetworkServiceManager;
	private Button showTableView;
	private TextView foot_traffic_time_of_day_labelView;
	private LinearLayout headerLayout;
	private CheckBox mToggleMapView;
	Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buyopicNetworkServiceManager = BuyopicNetworkServiceManager
				.getInstance(getActivity());

	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_chart_view, null);
		Utils.overrideFonts(getActivity(), view);
		headerLayout=(LinearLayout) view.findViewById(R.id.header_chart_layout);
		mToggleMapView=(CheckBox)view.findViewById(R.id.layout_mapswitch_activate);
		webView = (WebView) view.findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		showTableView = (Button) view.findViewById(R.id.showTableView);
		showTableView.setOnClickListener(this);
		webView.setWebChromeClient(new WebChromeClient());
		foot_traffic_time_of_day_labelView=(TextView)view.findViewById(R.id.foot_traffic_time_of_day_label);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	       bundle = getArguments();
		if (bundle != null && bundle.containsKey("requstcode")) {
			int code = bundle.getInt("requstcode");
			switch (code) {
			case Constants.REQUEST_FOOT_TRAFFIC_TIME_OF_DAY:
				headerLayout.setVisibility(View.GONE);
				String response = bundle.getString("jsonObject");
				foot_traffic_time_of_day_labelView.setText("Foot Traffic By Time-of-day:");
				buyopicNetworkServiceManager
						.sendGetFootTrafficByTimeofDayChartRequest(
								Constants.REQUEST_FOOT_TRAFFIC_TIME_OF_DAY,
								response, this);
				break;
			case Constants.REQUEST_AVERAGE_DAILY_DWELL_TIME:
				headerLayout.setVisibility(View.GONE);
				mToggleMapView.setVisibility(View.VISIBLE);
				mToggleMapView.setOnCheckedChangeListener(this);
				foot_traffic_time_of_day_labelView.setText("Average Daily Dwell Time:");
				response = bundle.getString("jsonObject");
				buyopicNetworkServiceManager
						.sendAverageDailyDwellTimeChartRequest(
								Constants.REQUEST_AVERAGE_DAILY_DWELL_TIME_CONSUMERS,
								response, this);
				break;
			case Constants.REQUEST_DWELL_TIME_BY_TIME_OF_DAY:
				headerLayout.setVisibility(View.GONE);
				foot_traffic_time_of_day_labelView.setText("Dwell Time By Time Of Day:");
				response = bundle.getString("jsonObject");
				buyopicNetworkServiceManager
						.sendDwellTimeByTimeOfDayChartRequest(
								Constants.REQUEST_DWELL_TIME_BY_TIME_OF_DAY,
								response, this);
				break;
			default:
				break;
			}

		}
	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		switch (requestCode) {
		case Constants.REQUEST_FOOT_TRAFFIC_TIME_OF_DAY:
			webView.loadData((String) object, "text/html", "UTF-8");
			break;
		case Constants.REQUEST_AVERAGE_DAILY_DWELL_TIME_CONSUMERS:
			webView.loadData((String) object, "text/html", "UTF-8");
			break;
		case Constants.REQUEST_DWELL_TIME_BY_TIME_OF_DAY:
			webView.loadData((String) object, "text/html", "UTF-8");
			break;
		case Constants.REQUEST_AVERAGE_DAILY_DWELL_AVG_TIME:
			webView.loadData((String) object, "text/html", "UTF-8");
			break;

			
		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestCode, String message) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.showTableView:
			getActivity().getSupportFragmentManager().popBackStack();
			break;

		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		String response = bundle.getString("jsonObject");
		if(!isChecked){
			
			response = bundle.getString("jsonObject");
			buyopicNetworkServiceManager
					.sendAverageDailyDwellTimeChartRequest(
							Constants.REQUEST_AVERAGE_DAILY_DWELL_TIME_CONSUMERS,
							response, this);
		}
		else{
			response = bundle.getString("jsonObject");
			buyopicNetworkServiceManager
					.sendAverageDailyDwellTimeChartRequestDwellTime(
							Constants.REQUEST_AVERAGE_DAILY_DWELL_AVG_TIME,
							response, this);
		}
	}

}
