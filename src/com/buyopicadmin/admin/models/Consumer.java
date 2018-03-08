package com.buyopicadmin.admin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Consumer implements Parcelable {

	private String mConsumerId;
	private String mConsumerEmail;
	private String mConsumerName;
	private String mPhoneNumber;
	

	public String getmPhoneNumber() {
		return mPhoneNumber;
	}

	public void setmPhoneNumber(String mPhoneNumber) {
		this.mPhoneNumber = mPhoneNumber;
	}

	public String getmConsumerId() {
		return mConsumerId;
	}

	public Consumer() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getmConsumerEmail() {
		return mConsumerEmail;
	}

	public void setmConsumerEmail(String mConsumerEmail) {
		this.mConsumerEmail = mConsumerEmail;
	}

	public void setmConsumerId(String mConsumerId) {
		this.mConsumerId = mConsumerId;
	}

	public String getmConsumerName() {
		return mConsumerName;
	}

	public void setmConsumerName(String mConsumerName) {
		this.mConsumerName = mConsumerName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.mConsumerId);
		dest.writeString(this.mConsumerName);
		dest.writeString(this.mConsumerEmail);
	}
	
	public Consumer(Parcel parcel)
	{
		this.mConsumerId=parcel.readString();
		this.mConsumerName=parcel.readString();
		this.mConsumerEmail=parcel.readString();
	}
	
	public static final Creator<Consumer> CREATOR=new Creator<Consumer>() {
		
		@Override
		public Consumer[] newArray(int size) {
			return new Consumer[size];
		}
		
		@Override
		public Consumer createFromParcel(Parcel source) {
			return new Consumer(source);
		}
	};

}
