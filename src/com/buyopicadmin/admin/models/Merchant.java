package com.buyopicadmin.admin.models;

import java.util.List;

public class Merchant {

private String mUserName;
private String mMerchantId;
private String email;
private String mPhone;
private String mStoreId;
private String mRetailerId;
private boolean status;
private String mMessage;



public String getmMessage() {
	return mMessage;
}
public void setmMessage(String mMessage) {
	this.mMessage = mMessage;
}
public boolean isStatus() {
	return status;
}
public void setStatus(boolean status) {
	this.status = status;
}
private List<Zone> mZones;

public List<Zone> getmZones() {
	return mZones;
}
public void setmZones(List<Zone> mZones) {
	this.mZones = mZones;
}
public String getmUserName() {
	return mUserName;
}
public void setmUserName(String mUserName) {
	this.mUserName = mUserName;
}
public String getmMerchantId() {
	return mMerchantId;
}
public void setmMerchantId(String mMerchantId) {
	this.mMerchantId = mMerchantId;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getmPhone() {
	return mPhone;
}
public void setmPhone(String mPhone) {
	this.mPhone = mPhone;
}
public String getmStoreId() {
	return mStoreId;
}
public void setmStoreId(String mStoreId) {
	this.mStoreId = mStoreId;
}
public String getmRetailerId() {
	return mRetailerId;
}
public void setmRetailerId(String mRetailerId) {
	this.mRetailerId = mRetailerId;
}


}
