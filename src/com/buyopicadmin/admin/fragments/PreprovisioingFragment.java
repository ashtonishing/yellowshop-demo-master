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
import android.widget.LinearLayout;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BBIDSetupActivity;
import com.buyopicadmin.admin.R;

public class PreprovisioingFragment extends Fragment implements OnClickListener, BuyopicNetworkCallBack {

	private EditText uuidView;
	private EditText major;
	private EditText minor;
	private Activity context;
	private LinearLayout layout;
	private EditText bbidView;
	private BBIDSetupActivity bbidSetupActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_preprovisioning, null);
		Utils.overrideFonts(context, view);
		uuidView = (EditText) view
				.findViewById(R.id.layout_preprovisioing_uuid);
		major = (EditText) view
				.findViewById(R.id.layout_preprovisioing_major);
		minor = (EditText) view
				.findViewById(R.id.layout_preprovisioning_minor);
		view.findViewById(R.id.layout_preprovisioing_button_create_button)
				.setOnClickListener(this);
		view.findViewById(R.id.layout_preprovisioing_button_cancel)
				.setOnClickListener(this);
		layout=(LinearLayout) view.findViewById(R.id.layout_preprovisioning_bbid_layout);
		bbidView=(EditText)view.findViewById(R.id.layout_preprovisioning_bbid);
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
		bbidSetupActivity=(BBIDSetupActivity) activity;
	}
	
	

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.layout_preprovisioing_button_create_button:
			if(!bbidSetupActivity.isEmpty(uuidView) && !bbidSetupActivity.isEmpty(major) && !bbidSetupActivity.isEmpty(minor))
			{
				bbidSetupActivity.showProgressDialog();
				BuyopicNetworkServiceManager buyopicNetworkServiceManager=BuyopicNetworkServiceManager.getInstance(context);
				buyopicNetworkServiceManager.sendPreProvisioningRequest(Constants.REQUEST_PRE_PROVISION_ING, uuidView.getText().toString(), minor.getText().toString(), major.getText().toString(),this);
			}
			else
			{
				Utils.showToast(getActivity(), "All Fields are mandatory");
			}

			break;
		case R.id.layout_preprovisioing_button_cancel:
			Activity activity=context;
			activity.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		bbidSetupActivity.dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_PRE_PROVISION_ING:
			HashMap<String, String> hashMap=JsonResponseParser.parsePreprovisioingResponse((String)object);
			if(!hashMap.isEmpty())
			{
				if(hashMap.containsKey("message"))
				{
					Utils.showToast(context, hashMap.get("message"));
				}
				if(hashMap.containsKey("buyopic_bbid"))
				{
					String bbid=hashMap.get("buyopic_bbid");
					layout.setVisibility(View.VISIBLE);
					bbidView.setText(bbid);
				}
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
