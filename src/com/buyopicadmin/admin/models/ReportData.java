package com.buyopicadmin.admin.models;

public class ReportData {

	private String mReportData;
	private boolean isSelected;

	public ReportData(String mReportData, boolean isSelected) {
		super();
		this.mReportData = mReportData;
		this.isSelected = isSelected;
	}
	public String getmReportData() {
		return mReportData;
	}
	public void setmReportData(String mReportData) {
		this.mReportData = mReportData;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
