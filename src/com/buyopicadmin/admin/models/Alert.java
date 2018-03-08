package com.buyopicadmin.admin.models;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Alert implements Parcelable{

	private String mAlertId;
	private String mAlertDescription;
	private String mPrice;
	private String mThumbnailUrl;
	private String mImageThumbUrl;
	private String mAlertTitle;
	private String mStartDate;
	private String mEndDate;
	private String mOfferId;
	private boolean mIsSpecialOffer;
	private boolean mIsActivated;
	private String mZonesId;
	private List<Zone>mZonesList;
	private boolean isAlertActivated;
	private String mAlertStoreAddressImage;
	private String mAlertGoogleIconImage;
	private String mAlertGooglePlaceId;
	private String mAlertStoreAddress1;
	private String mAlertStoreAddress2;
	private String mAlertStoreCity;
	private String mAlertStoreState;
	private String mAlertStorePostalCode;

	
	public String getmAlertStoreAddressImage() {
		return mAlertStoreAddressImage;
	}

	public void setmAlertStoreAddressImage(String mAlertStoreAddressImage) {
		this.mAlertStoreAddressImage = mAlertStoreAddressImage;
	}

	public String getmAlertGoogleIconImage() {
		return mAlertGoogleIconImage;
	}

	public void setmAlertGoogleIconImage(String mAlertGoogleIconImage) {
		this.mAlertGoogleIconImage = mAlertGoogleIconImage;
	}

	public String getmAlertGooglePlaceId() {
		return mAlertGooglePlaceId;
	}

	public void setmAlertGooglePlaceId(String mAlertGooglePlaceId) {
		this.mAlertGooglePlaceId = mAlertGooglePlaceId;
	}

	public String getmAlertStoreAddress1() {
		return mAlertStoreAddress1;
	}

	public void setmAlertStoreAddress1(String mAlertStoreAddress1) {
		this.mAlertStoreAddress1 = mAlertStoreAddress1;
	}

	public String getmAlertStoreAddress2() {
		return mAlertStoreAddress2;
	}

	public void setmAlertStoreAddress2(String mAlertStoreAddress2) {
		this.mAlertStoreAddress2 = mAlertStoreAddress2;
	}

	public String getmAlertStoreCity() {
		return mAlertStoreCity;
	}

	public void setmAlertStoreCity(String mAlertStoreCity) {
		this.mAlertStoreCity = mAlertStoreCity;
	}

	public String getmAlertStoreState() {
		return mAlertStoreState;
	}

	public void setmAlertStoreState(String mAlertStoreState) {
		this.mAlertStoreState = mAlertStoreState;
	}

	public String getmAlertStorePostalCode() {
		return mAlertStorePostalCode;
	}

	public void setmAlertStorePostalCode(String mAlertStorePostalCode) {
		this.mAlertStorePostalCode = mAlertStorePostalCode;
	}

	public String getmImageThumbUrl() {
		return mImageThumbUrl;
	}

	public void setmImageThumbUrl(String mImageThumbUrl) {
		this.mImageThumbUrl = mImageThumbUrl;
	}

	public String getmAlertDescription() {
		return mAlertDescription;
	}

	public void setmAlertDescription(String mAlertDescription) {
		this.mAlertDescription = mAlertDescription;
	}

	public String getmAlertTitle() {
		return mAlertTitle;
	}

	public void setmAlertTitle(String mAlertTitle) {
		this.mAlertTitle = mAlertTitle;
	}

	public String getmZonesId() {
		return mZonesId;
	}

	public void setmZonesId(String mZonesId) {
		this.mZonesId = mZonesId;
	}

	public boolean isAlertActivated() {
		return isAlertActivated;
	}

	public void setAlertActivated(boolean isAlertActivated) {
		this.isAlertActivated = isAlertActivated;
	}

	public List<Zone> getmZonesList() {
		return mZonesList;
	}

	public void setmZonesList(List<Zone> mZonesList) {
		this.mZonesList = mZonesList;
	}

	public String getmZones() {
		return mZonesId;
	}

	public void setmZones(String mZones) {
		this.mZonesId = mZones;
	}

	public String getmAlertId() {
		return mAlertId;
	}

	public void setmAlertId(String mAlertId) {
		this.mAlertId = mAlertId;
	}

	public boolean ismIsActivated() {
		return mIsActivated;
	}

	public void setmIsActivated(boolean mIsActivated) {
		this.mIsActivated = mIsActivated;
	}

	public boolean ismIsSpecialOffer() {
		return mIsSpecialOffer;
	}

	public void setmIsSpecialOffer(boolean mIsSpecialOffer) {
		this.mIsSpecialOffer = mIsSpecialOffer;
	}

	public String getmOfferTitle() {
		return mAlertTitle;
	}

	public void setmOfferTitle(String mOfferTitle) {
		this.mAlertTitle = mOfferTitle;
	}

	public String getmStartDate() {
		return mStartDate;
	}

	public void setmStartDate(String mStartDate) {
		this.mStartDate = mStartDate;
	}

	public String getmEndDate() {
		return mEndDate;
	}

	public void setmEndDate(String mEndDate) {
		this.mEndDate = mEndDate;
	}

	public String getmOfferId() {
		return mOfferId;
	}

	public void setmOfferId(String mOfferId) {
		this.mOfferId = mOfferId;
	}

	public String getmThumbnailUrl() {
		return mThumbnailUrl;
	}

	public void setmThumbnailUrl(String mThumbnailUrl) {
		this.mThumbnailUrl = mThumbnailUrl;
	}

	public String getmOfferMessage() {
		return mAlertDescription;
	}

	public void setmOfferMessage(String mOfferMessage) {
		this.mAlertDescription = mOfferMessage;
	}

	public String getmPrice() {
		return mPrice;
	}

	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.mAlertId);
		dest.writeString(this.mEndDate);
		dest.writeString(this.mOfferId);
		dest.writeString(this.mAlertDescription);
		dest.writeString(this.mAlertTitle);
		dest.writeString(this.mPrice);
		dest.writeString(this.mStartDate);
		dest.writeString(this.mThumbnailUrl);
		dest.writeString(String.valueOf(this.mIsActivated));
		dest.writeString(String.valueOf(this.mIsSpecialOffer));
		
	}
	
	public Alert(Parcel parcel)
	{
		this.mAlertId=parcel.readString();
		this.mEndDate=parcel.readString();
		this.mOfferId=parcel.readString();
		this.mAlertDescription=parcel.readString();
		this.mAlertTitle=parcel.readString();
		this.mPrice=parcel.readString();
		this.mStartDate=parcel.readString();
		this.mThumbnailUrl=parcel.readString();
		this.mIsSpecialOffer=parcel.readString().equalsIgnoreCase("true")?true:false;
		this.mIsActivated=parcel.readString().equalsIgnoreCase("true")?true:false;
	}
	
	public Alert() {
	}

	public static final Creator<Alert> CREATOR=new Creator<Alert>() {

		@Override
		public Alert[] newArray(int size) {
			return new Alert[size];
		}
		
		@Override
		public Alert createFromParcel(Parcel source) {
			return new Alert(source);
		}
	};

}
