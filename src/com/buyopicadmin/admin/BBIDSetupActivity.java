package com.buyopicadmin.admin;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.fragments.BeaconSetupFragment;
import com.buyopicadmin.admin.fragments.PreprovisioingFragment;
import com.buyopicadmin.admin.fragments.ProvisioingFragment;

public class BBIDSetupActivity extends BaseActivity 
		 {

	private FragmentTabHost mFragmentTabHost;
	private ProgressDialog progressDialog;
	private ActionBar actionBar;

	public BBIDSetupActivity() {
		super(R.string.action_settings);
	}

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.layout_dining_tabs);
		BaseActivity baseActivity = this;
		if (baseActivity != null) {
			baseActivity.setBeaconActionBar("Provisioning", 3);
		}
		
		actionBar = getActionBar();
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(android.R.color.transparent)));
		
		mFragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mFragmentTabHost.setup(this, getSupportFragmentManager(),
				R.id.realtabcontent);
		mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec("Pre-Provisioning")
				.setIndicator(makeTab("Beacon Pre-Provisioning")),
				PreprovisioingFragment.class, null);

		mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec("Provisioning")
				.setIndicator(makeTab("Merchant Provisioning")),
				ProvisioingFragment.class, null);
		mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec("Beacon setup")
				.setIndicator(makeTab("Merchant Beacon setup")),
				BeaconSetupFragment.class, null);
		mFragmentTabHost.setCurrentTab(0);
		
	}

	private View makeTab(String tag) {
		View tabView = LayoutInflater.from(this).inflate(
				R.layout.custom_list_tab_textview, null);
		Utils.overrideFonts(this, tabView);
		TextView tabText = (TextView) tabView.findViewById(R.id.tab_text);
		tabText.setText(tag);
		return tabView;
	}

	public void showProgressDialog() {
		try {
			if (progressDialog == null) {
				progressDialog = ProgressDialog.show(this, "", "Please wait..",
						false, true);
			}
			progressDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public boolean isEmpty(EditText editText) {
		return TextUtils.isEmpty(editText.getText().toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Add").setIcon(R.drawable.ic_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 2, 0, "Add").setIcon(R.drawable.ic_add)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	if(item.getItemId()==2)
	{
		Intent intent=new Intent(this,MerchantStoreSetupActivity.class);
		startActivity(intent);
		
	}
	return super.onOptionsItemSelected(item);
}

}
