package com.buyopicadmin.admin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.buyopicadmin.admin.models.Zone;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BuyOpic extends Application {

	private SharedPreferences preferences;
	private List<Zone> mZones;
	private int mSelectedReportPos=0;
	private String mEmailId;
	private String mPassword;
	private HashSet mHashSet;

	public String getmEmailId() {
		return preferences.getString("emailid", null);
	}

	public void setmEmailId(String mEmailId) {
		preferences.edit().putString("emailid", mEmailId).commit();
	}
	
	
	public HashSet getmStringSet() {
		return (HashSet) preferences.getStringSet("statusset",null);
	}

	public void setmStringSet(HashSet mEmailId) {
		preferences.edit().putStringSet("statusset",mHashSet).commit();
	}

	public String getmPassword() {
		return preferences.getString("password", null);
	}

	public void setmPassword(String mPassword) {
		preferences.edit().putString("password", mPassword).commit();
	}

	public int getmSelectedReportPos() {
		return mSelectedReportPos;
	}

	public void setmSelectedReportPos(int mSelectedReportPos) {
		this.mSelectedReportPos = mSelectedReportPos;
	}

	private HashMap<String, String> hashMap;
	private LinkedHashMap<String, String> mStatusHashMap;

	public HashMap<String, String> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, String> hashMap) {
		this.hashMap = hashMap;
	}

	public boolean isRegistered() {
		return preferences.getBoolean("is_registered", false);
	}

	public void setRegistered(boolean isRegistered) {
		preferences.edit().putBoolean("is_registered", isRegistered).commit();
	}

	public List<Zone> getmZones() {
		return mZones;
	}

	public void setmZones(List<Zone> mZones) {
		this.mZones = mZones;
	}

	public String getmMerchantId() {
		return hashMap!=null?hashMap.get("buyopic_store_associate_id"):null;
	}

	public String getmStoreId() {
		return hashMap!=null?hashMap.get("store_id"):null;
	}

	public String getmRetailerId() {
		return hashMap!=null?hashMap.get("retailer_id"):null;
	}

	public String getmEmail() {
		return hashMap!=null?hashMap.get("email"):null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(this);
		Crashlytics.start(this);
		preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
	}

	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.memoryCache(new LruMemoryCache(5 * 1024 * 1024))
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}

	public void setStatusHashMap(LinkedHashMap<String, String> hashMap) {
		
		// TODO Auto-generated method stub
		this.mStatusHashMap=hashMap;
	}
	public LinkedHashMap<String, String> getStatusHashMap() {
		return mStatusHashMap;
	}

}
