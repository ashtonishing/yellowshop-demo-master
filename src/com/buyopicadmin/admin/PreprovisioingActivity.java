package com.buyopicadmin.admin;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;

public class PreprovisioingActivity extends BaseActivity implements
		OnClickListener, BuyopicNetworkCallBack {

	private EditText mUUIDView;
	private EditText mMajorView;
	private EditText mMinorView;
	private ProgressDialog mProgressDialog;
	private String userId=null;
	private LinearLayout mLinearLayout;
	private EditText mBBIDView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=LayoutInflater.from(this).inflate(R.layout.layout_preprovisioning, null);
		setContentView(view);
		Utils.overrideFonts(this, view);
		BuyOpic buyOpic=(BuyOpic) getApplication();
		userId=buyOpic.getmMerchantId();
		prepareViews();
		
	}

	private void prepareViews() {
		mLinearLayout=(LinearLayout)findViewById(R.id.layout_preprovisioning_bbid_layout);
		mBBIDView=(EditText)findViewById(R.id.layout_preprovisioning_bbid);
		mUUIDView = (EditText) findViewById(R.id.layout_preprovisioing_uuid);
		mMajorView = (EditText) findViewById(R.id.layout_preprovisioing_major);
		mMinorView = (EditText) findViewById(R.id.layout_preprovisioning_minor);
		findViewById(R.id.layout_preprovisioing_button_create_button)
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.layout_preprovisioing_button_create_button:
			submitDataToServer();

			break;

		default:
			break;
		}

	}
private void showProgressDialog() {
		
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this, "", "Please wait",
					false, false);
		}
		mProgressDialog.show();
	}
	
	private void dismissProgressDialog()
	{
		if(mProgressDialog!=null && mProgressDialog.isShowing())
		{
			mProgressDialog.dismiss();
		}
	}


	private void submitDataToServer() {
		// TODO Auto-generated method stub
		if (!isEmpty(mMajorView) 
				&& !isEmpty(mMinorView) 
				&& !isEmpty(mUUIDView)) {
			showProgressDialog();
			BuyopicNetworkServiceManager buyopicNetworkServiceManager=BuyopicNetworkServiceManager.getInstance(this);
			buyopicNetworkServiceManager.sendPreProvisioningRequest(Constants.REQUEST_PRE_PROVISION_ING, mUUIDView.getText().toString(),
					mMinorView.getText().toString(), mMajorView.getText().toString(), this);

		}
		else
		{
			Utils.showToast(this, "All fields are mandatory");
		}
	}

	private boolean isEmpty(EditText editText) {
		return TextUtils.isEmpty(editText.getText().toString());
	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_PRE_PROVISION_ING:
			HashMap<String, String> hashMap=JsonResponseParser.parsePreprovisioingResponse((String)object);
			if(!hashMap.isEmpty())
			{
				if(hashMap.containsKey("message"))
				{
					Utils.showToast(this, hashMap.get("message"));
				}
				if(hashMap.containsKey("buyopic_bbid"))
				{
					String bbid=hashMap.get("buyopic_bbid");
					mLinearLayout.setVisibility(View.VISIBLE);
					mBBIDView.setText(bbid);
				}
//				Intent intent=new Intent()
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestCode, String message) {
		dismissProgressDialog();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Provisioing").setIcon(R.drawable.ic_settings).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==1)
		{
			Intent intent=new Intent(this,ProvisioingActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
