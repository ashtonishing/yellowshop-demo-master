package com.buyopicadmin.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.models.Merchant;

@SuppressWarnings("unused")
public class RegisterActivity extends BaseActivity implements OnClickListener,
		BuyopicNetworkCallBack {

	private EditText mEmailView;
	private EditText mUserName;
	private EditText mPassword;
	private ProgressDialog mProgressDialog;
	private EditText mMerchantId;
	private EditText mStoreId;
	private EditText mPhoneNumber;

	public RegisterActivity()
	{
		super(R.string.action_settings);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=LayoutInflater.from(this).inflate(R.layout.layout_register, null);
		setContentView(view);
		Utils.overrideFonts(this, view);
		prepareViews();
	}

	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this, "", "Please wait",
					false, false);
		}
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	private void prepareViews() {
		mMerchantId=(EditText)findViewById(R.id.layout_register_merchant_id);
		mStoreId=(EditText)findViewById(R.id.layout_register_merchant_id_store_id);
		mEmailView = (EditText) findViewById(R.id.layout_register_email);
		mPassword = (EditText) findViewById(R.id.layout_register_password);
		mUserName = (EditText) findViewById(R.id.layout_register_username_view);
		mPhoneNumber= (EditText) findViewById(R.id.layout_register_phone_no);
		findViewById(R.id.layout_register_button).setOnClickListener(this);
		
		findViewById(R.id.layout_register_login_button)
				.setOnClickListener(this);
		BaseActivity baseActivity=this;
		if(baseActivity!=null){
			baseActivity.setBeaconActionBar("Yellow Shop: Registration", 1);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.layout_register_button:
			submitDataToServer();
			break;
		case R.id.layout_register_login_button:
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);

		default:
			break;
		}

	}

	private void submitDataToServer() {
		if (!isEmpty(mEmailView) && !isEmpty(mPassword)&&!isEmpty(mMerchantId)&& !isEmpty(mStoreId)) {
			showProgressDialog();
			BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
					.getInstance(this);
			buyopicNetworkServiceManager.sendMerchantRegistrationRequest(
					Constants.REQUEST_REGISTER, mEmailView
							.getText().toString(), mPassword.getText()
							.toString(),mMerchantId.getText().toString(),mStoreId.getText().toString(), this);
		} else {
			Utils.showToast(this, "Some Fields are missing");
		}
	}

	private boolean isEmpty(EditText editText) {
		return TextUtils.isEmpty(editText.getText().toString());
	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		switch (requestCode) {

		case Constants.REQUEST_REGISTER:
//			String message = JsonResponseParser
//					.parseCreateAlertResponse((String) object);
			
			Merchant consumer=JsonResponseParser.parseMerchantLoginResponse((String)object);
			if(consumer!=null)
			{
				Utils.showToast(this, "User Registered Successfully");
				BuyOpic buyOpic=(BuyOpic) getApplication();
				if(consumer.getmZones()!=null && !consumer.getmZones().isEmpty())
				{
					buyOpic.setmZones(consumer.getmZones());
				}
				Intent intent=new Intent(this,MerchantOfferListActivity.class);
				;
				startActivity(intent);
				finish();
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

}
