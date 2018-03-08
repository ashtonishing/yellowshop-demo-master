package com.buyopicadmin.admin.fragments;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BBIDSetupActivity;
import com.buyopicadmin.admin.R;

public class BeaconSetupFragment extends Fragment implements OnClickListener, BuyopicNetworkCallBack {

	private Activity context;
	private BBIDSetupActivity bbidSetupActivity;
	private EditText zoneNameView;
	private EditText latitudeView;
	private EditText longitudeView;
	private EditText bbidSpinnerView;
	private Spinner zoneSpinnerView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_beaconsetup, null);
		Utils.overrideFonts(context, view);
		view.findViewById(R.id.layout_create_offer_down_arrow_zone).setOnClickListener(this);
		zoneNameView = (EditText) view
				.findViewById(R.id.layout_beacon_setup_zonename);
		latitudeView = (EditText) view
				.findViewById(R.id.layout_beacon_setup_lat);
		longitudeView = (EditText) view
				.findViewById(R.id.layout_beacon_setup_long);
		 bbidSpinnerView = (EditText) view
				.findViewById(R.id.layout_beacon_setup__spinner_bbid);
		 zoneSpinnerView = (Spinner) view
				.findViewById(R.id.layout_beacon_setup_zoneid);
		view.findViewById(R.id.layout_beacon_setup_button_save)
				.setOnClickListener(this);
		view.findViewById(R.id.layout_beacon_setup_button_clear)
				.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_beacon_setup_button_save:
			submitDataToServer();
			break;
		case R.id.layout_create_offer_down_arrow_zone:
			zoneSpinnerView.performClick();
			break;
		case R.id.layout_beacon_setup_button_clear:
			Activity activity=context;
			activity.finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
		bbidSetupActivity = (BBIDSetupActivity) activity;
	}

	private void submitDataToServer() {
		bbidSetupActivity.showProgressDialog();
		BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
				.getInstance(context);
		buyopicNetworkServiceManager.sendBeaconSetupRequest(Constants.REQUEST_BEACON_SETUP, bbidSpinnerView.getText().toString(),
				""+zoneSpinnerView.getSelectedItemPosition(), zoneNameView.getText().toString(), latitudeView.getText().toString(), longitudeView.getText().toString(), this);

	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		bbidSetupActivity.dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_BEACON_SETUP:
			HashMap<String,String> hashMap=JsonResponseParser.parsePreprovisioingResponse((String)object);
			if(hashMap!=null && hashMap.containsKey("message"))
			{
				Utils.showToast(context, hashMap.get("message"));
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestCode, String message) {
		bbidSetupActivity.dismissProgressDialog();
	}

}
