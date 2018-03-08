package com.buyopicadmin.admin.models;

import java.util.List;

public class StoreOffersList {

	private String mStoreLogo;
	private String mStoreName;
	private String mStoreAddress;
	private List<Alert> alerts;
	
	public String getmStoreLogo() {
		return mStoreLogo;
	}

	public void setmStoreLogo(String mStoreLogo) {
		this.mStoreLogo = mStoreLogo;
	}

	public String getmStoreName() {
		return mStoreName;
	}

	public void setmStoreName(String mStoreName) {
		this.mStoreName = mStoreName;
	}

	public String getmStoreAddress() {
		return mStoreAddress;
	}

	public void setmStoreAddress(String mStoreAddress) {
		this.mStoreAddress = mStoreAddress;
	}

	public List<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}

}
