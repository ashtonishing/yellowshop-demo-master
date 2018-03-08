package com.buyopicadmin.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.models.StatusList;

public class LoginActivity extends BaseActivity implements OnClickListener,
		BuyopicNetworkCallBack {
	
	private String mEmaiIld;
	private String mPassword;
	private EditText mEmailView;
	private EditText mPasswordView;
	private ProgressDialog mProgressDialog;
	private BuyOpic buyOpic;
	private TextView emailTextView;
	private TextView passwordTextView;
	private TextView forgotPasswordView;
	private boolean isLogin=false;
	StatusList statusListObj;
	public static List<StatusList> statusOrderList=new ArrayList<StatusList>();
	public static LinkedHashMap<String, String> mStatusHashMap;
	public LoginActivity() {
		super(R.string.action_settings);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = LayoutInflater.from(this).inflate(
				R.layout.layout_login_view, null);
		Utils.overrideFonts(this, view);
		setContentView(view);
		mStatusHashMap=new LinkedHashMap<String, String>();
		mStatusHashMap.put("1","Inbox");
		mStatusHashMap.put("2","Confirmed");
		mStatusHashMap.put("3","Dispatched");
		mStatusHashMap.put("4","Delivered");
		mStatusHashMap.put("5","Canceled");
		
		String countryname=Utils.getCountryName(getApplicationContext());
		if(countryname!=null&&countryname.equalsIgnoreCase("IN")){
			Constants.CURRENCYSYMBOL=this.getResources().getString(R.string.rs);
		}
		else if(countryname!=null&&countryname.equalsIgnoreCase("US")){
			Constants.CURRENCYSYMBOL=this.getResources().getString(R.string.dollar);
		}
		buyOpic = (BuyOpic) getApplication();
	
		
		
		 

		final Intent intent = getIntent();
		if (intent != null) {
			String url = intent.getDataString();
			if (url != null) {
				Uri uri = Uri.parse(url);
				String processId = uri.getQueryParameter(JsonResponseParser.TAG_PROCESS_ID);
				showProgressDialog();
				BuyopicNetworkServiceManager buyopicNetworkServiceManager=BuyopicNetworkServiceManager.getInstance(this);
				buyopicNetworkServiceManager.sendUrlProcessDetailsRequest(Constants.REQUEST_MERCHANT_CONFIRMATION, processId, this);
			
			}
	}
		  if (buyOpic.isRegistered()) {
			  if(buyOpic.getmEmailId()!=null&&buyOpic.getmPassword()!=null)
			  {
				  showProgressDialog();
					View view1 = LayoutInflater.from(this).inflate(
							R.layout.layout_home, null);
					setContentView(view1);
			  BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
						.getInstance(this);
			  mEmaiIld=buyOpic.getmEmailId();
			  mPassword=buyOpic.getmPassword();
			  isLogin=true;
				buyopicNetworkServiceManager.sendMerchantLoginRequest(
						Constants.REQUEST_LOGIN, buyOpic.getmEmailId(),
						buyOpic.getmPassword(), this);
			  }
			  }	
		  else{
				prepareViews();
		  }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(MerchantCreateActivity.IS_LOGGED_IN)
		{
			MerchantCreateActivity.IS_LOGGED_IN=false;
			finish();
		}
	}

	private void prepareViews() {
		mEmailView = (EditText) findViewById(R.id.layout_login_email);
		mPasswordView = (EditText) findViewById(R.id.layout_login_password);
		findViewById(R.id.layout_login_button).setOnClickListener(this);
		TextView textView = (TextView) findViewById(R.id.layout_login_register_button);
		String str="<html><body><u>Register Here</u></body></html>";
		textView.setText(Html.fromHtml(str));
		textView.setOnClickListener(this);

		String postText = "<font color='#EE0000'>*</font>";
		emailTextView=(TextView)findViewById(R.id.layout_login_view_email_textview);
		emailTextView.setText(Html.fromHtml("Email" + postText+":"));
		
		passwordTextView=(TextView)findViewById(R.id.layout_login_view_password_textview);
		passwordTextView.setText(Html.fromHtml("Password" + postText+":"));
		
		forgotPasswordView=(TextView)findViewById(R.id.layout_login_forgot_password_text);
		forgotPasswordView.setOnClickListener(this);
		str="<html><body><u>Forgot Password</u></body></html>";
		forgotPasswordView.setText(Html.fromHtml(str));
		BaseActivity baseActivity = this;
		if (baseActivity != null) {
			baseActivity.setBeaconActionBar("Yellow Shop Login", 0);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.layout_login_button:
			if (!isEmpty(mEmailView) && !isEmpty(mPasswordView)) {
				if(Utils.isValidEmail(mEmailView.getText().toString()))
				{
				showProgressDialog();
					BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
						.getInstance(this);
					  mEmaiIld= mEmailView.getText()
								.toString();
					  mPassword=mPasswordView.getText().toString();
				buyopicNetworkServiceManager.sendMerchantLoginRequest(
						Constants.REQUEST_LOGIN, mEmailView.getText()
								.toString(),
						mPasswordView.getText().toString(), this);
				}
				else
				{
					Utils.showToast(this, "Please give a valid email address");
				}

			} else {
				Utils.showToast(this, "Please fill all fields");
			}

			break;
		case R.id.layout_login_register_button:
			Intent intent = new Intent(this, MerchantCreateActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_login_forgot_password_text:
			BuyopicNetworkServiceManager buyopicNetworkServiceManager=BuyopicNetworkServiceManager.getInstance(this);
			if(!isEmpty(mEmailView))
			{
				if(Utils.isValidEmail(mEmailView.getText().toString()))
				{
				showProgressDialog();
				buyopicNetworkServiceManager.sendResetPasswordRequest(Constants.REQUEST_RESET_PASSWORD, mEmailView.getText().toString(), this);
			}
				else
				{
					Utils.showToast(this, "Please give valid email id");
				}
			}
			else
			{
				Utils.showToast(this, "Please fill the email Id");
			}
			
			break;
		default:
			break;
		}

	}

	private void forgotPassword() {
		final Dialog dialog=new Dialog(this, R.style.Theme_Dialog);
		View view=LayoutInflater.from(this).inflate(R.layout.layout_forgotpassword_dialog_view, null);
		Utils.overrideFonts(this, view);
		dialog.setContentView(view);
		TextView forgotPasswordText=(TextView)view.findViewById(R.id.forgot_password_text);
		String forgotPasswordResponseText="We have just sent you an email with a new temporary password (to your email address above). Please open that email and enter that password into the Password field above.\n\n(Once you are logged in to Yellow, for your own security, go to the Edit Profile item on the main menu and change your password immediately.)";
		forgotPasswordText.setText(forgotPasswordResponseText);
		ImageButton cancelDialog=(ImageButton)view.findViewById(R.id.dialog_cancel);
		cancelDialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
		
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

	private boolean isEmpty(EditText editText) {
		return TextUtils.isEmpty(editText.getText().toString());
	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_LOGIN:
			HashMap<String, String> hashMap = JsonResponseParser
					.parseMerchantStoreRegisterResponse((String) object);
			if (hashMap != null && hashMap.get("status").equalsIgnoreCase("ok")) {
				buyOpic.setHashMap(hashMap);
				buyOpic.setRegistered(true);
				buyOpic.setmEmailId(mEmaiIld);
				buyOpic.setmPassword(mPassword);
				Intent intent = new Intent(this,
						HomePageTabActivity.class);
				startActivity(intent);
				finish();
			} else if(hashMap.get("status").equalsIgnoreCase("notconfirmed"))
			{
				Utils.showToast(this, "Complete the one-time registration");
			}
			else {
				Utils.showToast(this, hashMap.get("message"));
				if(isLogin)
				{
					buyOpic.setRegistered(false);
					buyOpic.setmEmailId(null);
					buyOpic.setmPassword(null);
					Intent intent = new Intent(this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				}
				Utils.showToast(this, hashMap.get("message"));
			}

			break;
		case Constants.REQUEST_MERCHANT_CONFIRMATION:
//			String message=JsonResponseParser.parseCreateAlertResponse((String)object);
//			Utils.showToast(this, message);
			
			HashMap<String, String> hashMap1 = JsonResponseParser
					.parseMerchantStoreRegisterResponse((String) object);
			if (hashMap1 != null && hashMap1.get("status").equalsIgnoreCase("ok")&& hashMap1.get("store_tier")!=null) {
				buyOpic.setHashMap(hashMap1);
				Intent intent = new Intent(this,
						HomePageTabActivity.class);
				startActivity(intent);
				finish();
			}
			else
			{
				Utils.showToast(this, "Unable to Confirmation your registration");
			}
			break;
		case Constants.REQUEST_RESET_PASSWORD:
			String response=JsonResponseParser.parseCreateAlertResponse((String)object);
			if(response.equalsIgnoreCase("Password updated successfully, please check your mail."))
			{
			forgotPassword();
			}
			else
			{
				Utils.showToast(this, response);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestCode, String message) {
		dismissProgressDialog();
		Utils.showToast(this, message);
	}

}
