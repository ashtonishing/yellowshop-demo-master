package com.buyopicadmin.admin.models;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class StoreMap implements Parcelable {

	private String mZoneId;
	private String mZoneName;
	private List<Consumer> mConsumers;

	
	public List<Consumer> getmConsumers() {
		return mConsumers;
	}

	public void setmConsumers(List<Consumer> mConsumers) {
		this.mConsumers = mConsumers;
	}

	public String getmZoneId() {
		return mZoneId;
	}

	public void setmZoneId(String mZoneId) {
		this.mZoneId = mZoneId;
	}

	public String getmZoneName() {
		return mZoneName;
	}

	public void setmZoneName(String mZoneName) {
		this.mZoneName = mZoneName;
	}

	public StoreMap(Parcel parcel) {
		this.mZoneId = parcel.readString();
		this.mZoneName = parcel.readString();
		mConsumers = new ArrayList<Consumer>();
		parcel.readList(mConsumers, this.getClass().getClassLoader());
	}

	public StoreMap() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.mZoneId);
		dest.writeString(this.mZoneName);
		dest.writeList(mConsumers);
	}

	public static final Creator<StoreMap> CREATOR = new Creator<StoreMap>() {

		@Override
		public StoreMap[] newArray(int size) {
			return new StoreMap[size];
		}

		@Override
		public StoreMap createFromParcel(Parcel source) {
			return new StoreMap(source);
		}
	};

}
