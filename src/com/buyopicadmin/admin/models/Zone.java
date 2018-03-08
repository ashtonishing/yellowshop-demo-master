package com.buyopicadmin.admin.models;

public class Zone {

	private String mZoneId;
	private String mZoneNumber;
	private String mZoneName;
	private boolean isSelected;
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getmZoneName() {
		return mZoneName;
	}
	public void setmZoneName(String mZoneName) {
		this.mZoneName = mZoneName;
	}
	public String getmZoneId() {
		return mZoneId;
	}
	public void setmZoneId(String mZoneId) {
		this.mZoneId = mZoneId;
	}
	public String getmZoneNumber() {
		return mZoneNumber;
	}
	public void setmZoneNumber(String mZoneNumber) {
		this.mZoneNumber = mZoneNumber;
	}
	
	@Override
	public String toString() {
		return this.mZoneId;
	}
	
	@Override
	public boolean equals(Object o) {
		return this.mZoneId.equalsIgnoreCase(((Zone)o).getmZoneId());
	}
}
