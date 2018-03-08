package com.buyopicadmin.admin.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buyopic.android.network.Constants;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BaseActivity;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.HomePageTabActivity;
import com.buyopicadmin.admin.LoginActivity;
import com.buyopicadmin.admin.MerchantCreateActivity;
import com.buyopicadmin.admin.R;

public class SlideMenuOptionsFragment extends Fragment implements
		OnClickListener {

	private Context mContext;
	private TextView loginView;
	private onBuyOpicItemClickListener buyOpicItemClickListener;

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_slidemenu_options, null);
		Utils.overrideFonts(mContext, view);

		view.findViewById(R.id.slidemenu_home).setOnClickListener(this);
		view.findViewById(R.id.slidemenu_create_listing).setOnClickListener(
				this);
		view.findViewById(R.id.slidemenu_settings).setOnClickListener(this);
		view.findViewById(R.id.slidemenu_send_us_feedback).setOnClickListener(
				this);
		loginView = (TextView) view.findViewById(R.id.slidemenu_logout);
		loginView.setOnClickListener(this);
		loginView.setText("Logout");
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
		if(activity instanceof HomePageTabActivity)
		{
		buyOpicItemClickListener=(onBuyOpicItemClickListener) activity;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {

		if (getActivity() instanceof BaseActivity) {
			BaseActivity baseActivity = (BaseActivity) getActivity();
			baseActivity.closeMenu();
		}
		switch (v.getId()) {
		case R.id.slidemenu_home:
			startActivity(new Intent(mContext, HomePageTabActivity.class));
			break;
		case R.id.slidemenu_create_listing:
			if(buyOpicItemClickListener!=null)
			{
			buyOpicItemClickListener.onItemClicked(1);
			}
			else
			{
				Intent intent=new Intent(mContext, HomePageTabActivity.class);
				intent.putExtra("is_from_edit_profile", true);
				startActivity(intent);
			}
			break;
		case R.id.slidemenu_settings:
			Intent intent = new Intent(mContext, MerchantCreateActivity.class);
			intent.putExtra(MerchantCreateActivity.KEY_IS_FROM_UPDATE, true);
			startActivity(intent);
			break;
		case R.id.slidemenu_logout:
			mContext.sendBroadcast(new Intent(Constants.CUSTOM_ACTION_INTENT));
			BuyOpic buyOpic = (BuyOpic) mContext.getApplicationContext();
			buyOpic.setRegistered(false);
			startActivity(new Intent(mContext, LoginActivity.class));
			break;
		case R.id.slidemenu_send_us_feedback:
			displaydialog();
			break;
		default:
			break;
		}
	}

	private void displaydialog() {
		final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.layout_forgotpassword_dialog_view, null);
		Utils.overrideFonts(getActivity(), view);
		dialog.setContentView(view);
		TextView dialogHeader = (TextView) view
				.findViewById(R.id.dialog_header);
		dialogHeader.setText("Send us Feedback");
		TextView forgotPasswordText = (TextView) view
				.findViewById(R.id.forgot_password_text);
		String forgotPasswordResponseText = "We are obsessed with providing you with an absolutely great Yellow Shop experience.\n"
				+ "Therefore, we would love to hear your feedback regarding your Yellow Shop experience. Click on the link below to send us email:\n feedback@buyopic.com\n\n Thank you. The Buyopic Team";
		forgotPasswordText.setText(forgotPasswordResponseText);
		ImageButton cancelDialog = (ImageButton) view
				.findViewById(R.id.dialog_cancel);
		cancelDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();

	}

}
