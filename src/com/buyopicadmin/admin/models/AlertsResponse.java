package com.buyopicadmin.admin.models;

import java.util.List;

public class AlertsResponse {

	private String mYelpId;
	private String mStoreId;
	private String mStoreLogo;
	private String mStoreAddress;
	private List<Alert> mAlerts;
	private List<Zone> mZones;
	
	
	public String getmStoreId() {
		return mStoreId;
	}
	public void setmStoreId(String mStoreId) {
		this.mStoreId = mStoreId;
	}
	public String getmStoreLogo() {
		return mStoreLogo;
	}
	public void setmStoreLogo(String mStoreLogo) {
		this.mStoreLogo = mStoreLogo;
	}
	public String getmStoreAddress() {
		return mStoreAddress;
	}
	public void setmStoreAddress(String mStoreAddress) {
		this.mStoreAddress = mStoreAddress;
	}
	public String getmYelpId() {
		return mYelpId;
	}
	public void setmYelpId(String mYelpId) {
		this.mYelpId = mYelpId;
	}
	public List<Alert> getmAlerts() {
		return mAlerts;
	}
	public void setmAlerts(List<Alert> mAlerts) {
		this.mAlerts = mAlerts;
	}
	public List<Zone> getmZones() {
		return mZones;
	}
	public void setmZones(List<Zone> mZones) {
		this.mZones = mZones;
	}
	
	
}
