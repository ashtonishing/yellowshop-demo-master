package com.buyopicadmin.admin;

import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.buyopic.android.network.Constants;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.fragments.AverageDailyDwellTimeFragment;
import com.buyopicadmin.admin.fragments.ClickThroughDetailsFragment;
import com.buyopicadmin.admin.fragments.DwellTimeByConsumerFragment;
import com.buyopicadmin.admin.fragments.DwellTimeByTimeofDayFragment;
import com.buyopicadmin.admin.fragments.FootFallByTimeoftheDayChartViewFragment;
import com.buyopicadmin.admin.fragments.FootTrafficByConsumer;
import com.buyopicadmin.admin.fragments.FootTrafficByTimeofDayFragment;
import com.buyopicadmin.admin.fragments.ListingsFragment;
import com.buyopicadmin.admin.fragments.MallTierStoreMapFragment;
import com.buyopicadmin.admin.fragments.OfferCreateFragment;
import com.buyopicadmin.admin.fragments.OrdersFragment;
import com.buyopicadmin.admin.fragments.ReportsFragment;
import com.buyopicadmin.admin.fragments.StoreMapFragment;
import com.buyopicadmin.admin.fragments.StoreMapZoneFragment;
import com.buyopicadmin.admin.fragments.onBuyOpicItemClickListener;
import com.buyopicadmin.admin.models.Alert;
import com.buyopicadmin.admin.models.Chart;
import com.buyopicadmin.admin.models.StoreMap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class HomePageTabActivity extends BaseActivity implements
		onBuyOpicItemClickListener, OnTabChangeListener {

	private FragmentTabHost mFragmentTabHost;
	private BuyOpic buyOpic;
	private ImageView mStoreLogoView;
	private TextView mStoreNameView;
	private TextView mStoreAddressView;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private FinishActivityReceiver activityReceiver;
	private String storeName;
	private String storeAddress;
	private String storeLogo;
	private String role;

	public HomePageTabActivity() {
		super(R.string.action_settings);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home_page_store);
		prepareViews();
		registerReceiver();
		buyOpic = (BuyOpic) getApplication();
		Bundle bundle1=getIntent().getExtras();
		
	
		if (buyOpic != null && buyOpic.getHashMap() != null) {
			role = buyOpic.getHashMap().containsKey("store_tier") ? buyOpic
					.getHashMap().get("store_tier") : null;
			mFragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
			mFragmentTabHost.setup(this, getSupportFragmentManager(),
					R.id.realtabcontent);
			Bundle bundle = new Bundle();
			bundle.putBoolean("a", true);

			mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec("My Listings")
					.setIndicator(makeTab("My Listings")),
					ListingsFragment.class, bundle);
			mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec("Reports")
					.setIndicator(makeTab("Reports")), ReportsFragment.class,
					null);
		
			if (role!=null && role.equalsIgnoreCase("mall")) {
				mFragmentTabHost.addTab(
						mFragmentTabHost.newTabSpec("Store Map").setIndicator(
								makeTab("Store Map")),
						MallTierStoreMapFragment.class, null);
			} else {
				mFragmentTabHost.addTab(
						mFragmentTabHost.newTabSpec("Store Map").setIndicator(
								makeTab("Store Map")), StoreMapFragment.class,
						null);
			}
			mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec("Orders")
					.setIndicator(makeTab("Orders")), OrdersFragment.class,
					null);

			
			mFragmentTabHost.getTabWidget().getChildAt(1)
					.setVisibility(View.INVISIBLE);
			mFragmentTabHost.getTabWidget().getChildAt(2)
					.setVisibility(View.INVISIBLE);
			mFragmentTabHost.getTabWidget().setDividerDrawable(
					new ColorDrawable(getResources().getColor(
							R.color.screen_bg_color)));

			Utils.showLog("Role:" + role);
			if (role != null && role.equalsIgnoreCase("standard")) {
				mFragmentTabHost.getTabWidget().getChildAt(1)
						.setVisibility(View.VISIBLE);
			}
			if (role != null && role.equalsIgnoreCase("Premium")) {
				mFragmentTabHost.getTabWidget().getChildAt(1)
						.setVisibility(View.VISIBLE);
				mFragmentTabHost.getTabWidget().getChildAt(2)
						.setVisibility(View.VISIBLE);
			}

			if (role != null && role.equalsIgnoreCase("Mall")) {
				mFragmentTabHost.getTabWidget().getChildAt(1)
						.setVisibility(View.VISIBLE);
				mFragmentTabHost.getTabWidget().getChildAt(2)
						.setVisibility(View.VISIBLE);
			}
			// mFragmentTabHost.setOnTabChangedListener(this);
			mFragmentTabHost.setCurrentTab(0);
			for (int i = 0; i < mFragmentTabHost.getTabWidget().getChildCount(); i++) {
				final int value = i;
				View view = mFragmentTabHost.getTabWidget().getChildAt(i);
				view.setTag(i);
				view.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						clearBackStack();
						if(event.getAction()==MotionEvent.ACTION_UP)
						{
						int tagValue = ((Integer) v.getTag()).intValue();
						mFragmentTabHost.getTabWidget().setCurrentTab(value);
						if (tagValue == 0) {
							setActionBarTitle("My Listings");
							ListingsFragment listingsFragment = new ListingsFragment();
							Bundle bundle = new Bundle();
							bundle.putBoolean("a", true);
							listingsFragment.setArguments(bundle);
							FragmentTransaction fragmentTransaction = getSupportFragmentManager()
									.beginTransaction();
							fragmentTransaction.replace(R.id.realtabcontent,
									listingsFragment).commit();
						} else if (tagValue == 1) {
							setActionBarTitle("Reports");
							ReportsFragment listingsFragment = new ReportsFragment();
							Bundle bundle = new Bundle();
							bundle.putBoolean("a", true);
							listingsFragment.setArguments(bundle);

							FragmentTransaction fragmentTransaction = getSupportFragmentManager()
									.beginTransaction();
							fragmentTransaction.replace(R.id.realtabcontent,
									listingsFragment).commit();
						} else if (tagValue == 2) {
							setActionBarTitle("Map");
							if (role.equalsIgnoreCase("mall")) {
								MallTierStoreMapFragment listingsFragment = new MallTierStoreMapFragment();
								Bundle bundle = new Bundle();
								bundle.putBoolean("a", true);
								listingsFragment.setArguments(bundle);

								FragmentTransaction fragmentTransaction = getSupportFragmentManager()
										.beginTransaction();
								fragmentTransaction.replace(
										R.id.realtabcontent, listingsFragment)
										.commit();
							}
							else {
								StoreMapFragment listingsFragment = new StoreMapFragment();

								Bundle bundle = new Bundle();
								bundle.putBoolean("a", true);
								listingsFragment.setArguments(bundle);

								FragmentTransaction fragmentTransaction = getSupportFragmentManager()
										.beginTransaction();
								fragmentTransaction.replace(
										R.id.realtabcontent, listingsFragment)
										.commit();
							}
						}
						else if (tagValue == 3) {
							setActionBarTitle("Orders");
							OrdersFragment ordersFragment = new OrdersFragment();
							Bundle bundle = new Bundle();
							bundle.putBoolean("a", true);
							ordersFragment.setArguments(bundle);

							FragmentTransaction fragmentTransaction = getSupportFragmentManager()
									.beginTransaction();
							fragmentTransaction.replace(R.id.realtabcontent,
									ordersFragment).commit();
						}
						}
						return true;
					}
				});
			}
		}
		
		if(bundle1!=null && bundle1.containsKey("is_from_edit_profile"))
		{
			boolean b=bundle1.getBoolean("is_from_edit_profile");
			if(b)
			{
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				OfferCreateFragment createFragment = new OfferCreateFragment();
				fragmentTransaction
						.replace(R.id.realtabcontent, createFragment)
						.addToBackStack("").commit();
			}
		}
		
	}
	
/*	public void registerReceiver() {
		IntentFilter filter = new IntentFilter(Constants.CUSTOM_ACTION_INTENT);
		activityReceiver = new FinishActivityReceiver(this);
		registerReceiver(activityReceiver, filter);
	}

	public void unregisterReceiver() {
		if (activityReceiver != null) {
			unregisterReceiver(activityReceiver);
			activityReceiver = null;
		}
	}*/

	private void setActionBarTitle(String string) {
		if (this instanceof BaseActivity) {
			BaseActivity baseActivity = (BaseActivity) this;
			if (baseActivity != null) {
				baseActivity.setBeaconActionBar(string, 2);
			}
		}
	}

	private View makeTab(String tag) {
		View tabView = LayoutInflater.from(this).inflate(
				R.layout.custom_list_tab_textview, null);
		Utils.overrideFonts(this, tabView);
		TextView tabText = (TextView) tabView.findViewById(R.id.tab_text);
		if (tag.equalsIgnoreCase("Store Map")) {
			tabText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.ic_store_map, 0);
		}
		tabText.setText(tag);
		return tabView;
	}

	private void prepareViews() {
		mStoreLogoView = (ImageView) findViewById(R.id.layout_offer_desc_store_logo);
		mStoreNameView = (TextView) findViewById(R.id.layout_offer_desc_store_name);
		mStoreAddressView = (TextView) findViewById(R.id.layout_offer_desc_store_address);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (buyOpic != null && buyOpic.getHashMap() != null) {
			storeName = buyOpic.getHashMap().get("store_name");
			storeAddress = buyOpic.getHashMap().get("store_address1") + ", "
					+ buyOpic.getHashMap().get("store_city") + ", "
					+ buyOpic.getHashMap().get("store_state");
			storeLogo = buyOpic.getHashMap().get("store_image_url");
			mStoreNameView.setText(storeName);
			mStoreAddressView.setText(storeAddress);
			imageLoader.displayImage(storeLogo, mStoreLogoView,
					configureOptions());
		}
	}

	private DisplayImageOptions configureOptions() {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(android.R.color.transparent)
				.showImageForEmptyUri(R.drawable.ic_placeholder_image)
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
				.considerExifParams(true)
				.showImageOnFail(R.drawable.ic_placeholder_image)
				.cacheOnDisc(true).build();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
	}

	public void registerReceiver() {
		IntentFilter filter = new IntentFilter(Constants.CUSTOM_ACTION_INTENT);
		activityReceiver = new FinishActivityReceiver(this);
		registerReceiver(activityReceiver, filter);
	}

	public void unregisterReceiver() {
		if (activityReceiver != null) {
			unregisterReceiver(activityReceiver);
			activityReceiver = null;
		}
	}

	@Override
	public void onItemClicked(Object object) {
		if (object instanceof Integer) {
			int code = ((Integer) object).intValue();
			switch (code) {
			case 1:
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				OfferCreateFragment createFragment = new OfferCreateFragment();
				fragmentTransaction
						.replace(R.id.realtabcontent, createFragment)
						.addToBackStack("").commit();
				break;

			case 100:
				fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				FootTrafficByConsumer averageCustomerTraffic = new FootTrafficByConsumer();
				fragmentTransaction
						.replace(R.id.realtabcontent, averageCustomerTraffic)
						.addToBackStack("").commit();
				break;
			case 101:
				fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				FootTrafficByTimeofDayFragment footFallZoneByTimeFragment = new FootTrafficByTimeofDayFragment();
				fragmentTransaction
						.replace(R.id.realtabcontent,
								footFallZoneByTimeFragment).addToBackStack("")
						.commit();
				break;
			case 102:
				fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				ClickThroughDetailsFragment clickThroughDetailsFragment = new ClickThroughDetailsFragment();
				fragmentTransaction
						.replace(R.id.realtabcontent,
								clickThroughDetailsFragment).addToBackStack("")
						.commit();
				break;
			case 103:
				fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				DwellTimeByConsumerFragment dwellTimeByConsumerFragment = new DwellTimeByConsumerFragment();
				fragmentTransaction
						.replace(R.id.realtabcontent,
								dwellTimeByConsumerFragment).addToBackStack("")
						.commit();
				break;

			case 104:
				fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				DwellTimeByTimeofDayFragment dwellTimeByTimeofDayFragment = new DwellTimeByTimeofDayFragment();
				fragmentTransaction
						.replace(R.id.realtabcontent,
								dwellTimeByTimeofDayFragment)
						.addToBackStack("").commit();
				break;
			case 105:
				fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				AverageDailyDwellTimeFragment averageDailyDwellTimeFragment = new AverageDailyDwellTimeFragment();
				fragmentTransaction
						.replace(R.id.realtabcontent,
								averageDailyDwellTimeFragment)
						.addToBackStack("").commit();

			default:
				break;
			}

		} else if (object instanceof Alert) {
			String alertId = ((Alert) object).getmOfferId();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			OfferCreateFragment createFragment = new OfferCreateFragment();
			Bundle bundle = new Bundle();
			bundle.putString(OfferCreateFragment.KEY_EXTRA_ALERT_ID, alertId);
			createFragment.setArguments(bundle);
			fragmentTransaction.replace(R.id.realtabcontent, createFragment)
					.addToBackStack("").commit();
		} else if (object instanceof StoreMap) {
			StoreMap storeMap = (StoreMap) object;
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			StoreMapZoneFragment createFragment = new StoreMapZoneFragment();
			Bundle bundle = new Bundle();
			bundle.putParcelable(StoreMapZoneFragment.KEY_EXTRA_STORE_MAP,
					storeMap);
			createFragment.setArguments(bundle);
			fragmentTransaction.replace(R.id.realtabcontent, createFragment)
					.addToBackStack("").commit();
		} else if (object instanceof Chart) {
			Chart chart = (Chart) object;
			FootFallByTimeoftheDayChartViewFragment chartViewFragment = new FootFallByTimeoftheDayChartViewFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("requstcode", chart.getRequestCode());
			bundle.putString("jsonObject", chart.getResponse());
			chartViewFragment.setArguments(bundle);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.realtabcontent, chartViewFragment)
					.addToBackStack("").commit();
		}

	}

	@Override
	public void onTabChanged(String tabId) {
		boolean status = clearBackStack();
		if (status) {
			if (tabId.equalsIgnoreCase("My Listings")) {
				ListingsFragment listingsFragment = new ListingsFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.replace(R.id.realtabcontent,
						listingsFragment).commit();
			} else if (tabId.equalsIgnoreCase("Reports")) {
				ReportsFragment listingsFragment = new ReportsFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.replace(R.id.realtabcontent,
						listingsFragment).commit();
			} else if (tabId.equalsIgnoreCase("Store Map")) {
				if (role.equalsIgnoreCase("mall")) {
					MallTierStoreMapFragment listingsFragment = new MallTierStoreMapFragment();
					FragmentTransaction fragmentTransaction = getSupportFragmentManager()
							.beginTransaction();
					fragmentTransaction.replace(R.id.realtabcontent,
							listingsFragment).commit();
				} else {
					StoreMapFragment listingsFragment = new StoreMapFragment();
					FragmentTransaction fragmentTransaction = getSupportFragmentManager()
							.beginTransaction();
					fragmentTransaction.replace(R.id.realtabcontent,
							listingsFragment).commit();
				}
			}
		}

	}

	private boolean clearBackStack() {
		boolean status = false;
		FragmentManager fm = getSupportFragmentManager();
		int backstackCount = fm.getBackStackEntryCount();
		for (int i = 0; i < backstackCount; i++) {
			fm.popBackStack();
			status = true;
		}
		return status;
	}

}
