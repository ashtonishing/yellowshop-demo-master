package com.buyopicadmin.admin;

import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.adapters.CustomAlertMessageAdapter;
import com.buyopicadmin.admin.models.Alert;
import com.buyopicadmin.admin.models.AlertsResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MerchantOfferListActivity extends BaseActivity implements
		OnItemClickListener, BuyopicNetworkCallBack {

	private ListView listView;
	private TextView emptyView;
	private ProgressBar progressBar;
	private String storeId;
	private String retailerId;
	private String mAssociateId;
	private BuyOpic buyOpic;
	private ImageView mStoreLogoView;
	private TextView mStoreAddressView;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private TextView mStoreNameView;
	private String storeName;
	private String storeAddress;
	private String storeLogo;
	private FinishActivityReceiver activityReceiver;

	public MerchantOfferListActivity() {
		super(R.string.action_settings);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver();
		View view = LayoutInflater.from(this).inflate(R.layout.layout_home,
				null);
		setContentView(view);
		Utils.overrideFonts(this, view);
		prepareViews();
		buyOpic = (BuyOpic) getApplication();
		storeId = buyOpic.getmStoreId();
		retailerId = buyOpic.getmRetailerId();
		mAssociateId = buyOpic.getmMerchantId();
		sendRequestToServer();
	}

	private void sendRequestToServer() {
		TextView buyopicTextView = (TextView) findViewById(R.id.powered_by_buyopic_text);
		Typeface typeFace = Typeface
				.createFromAsset(getAssets(), "timesbi.ttf");
		buyopicTextView.setTypeface(typeFace);
		progressBar.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
				.getInstance(this);
		buyopicNetworkServiceManager.sendMerchantOfferListRequest(
				Constants.REQUEST_MERCHANT_ALERTS, storeId, retailerId,
				mAssociateId, this);
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

	private void prepareViews() {
		listView = (ListView) findViewById(R.id.alerts_list_view);
		emptyView = (TextView) findViewById(R.id.emptyview);
		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		listView.setOnItemClickListener(this);
		mStoreLogoView = (ImageView) findViewById(R.id.layout_offer_desc_store_logo);
		mStoreNameView = (TextView) findViewById(R.id.layout_offer_desc_store_name);
		mStoreAddressView = (TextView) findViewById(R.id.layout_offer_desc_store_address);
		BaseActivity baseActivity = this;
		if (baseActivity != null) {
			baseActivity.setBeaconActionBar("My Listings", 2);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Add").setIcon(R.drawable.ic_add)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		// menu.add(0, 2, 0, "Settings").setIcon(R.drawable.ic_settings)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 3, 0, "Search").setIcon(R.drawable.ic_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intent = new Intent(this, MerchantCreateOfferActivity.class);
			intent.putExtra("name", storeName);
			intent.putExtra("address", storeAddress);
			intent.putExtra("logo", storeLogo);
			startActivityForResult(intent, 100);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 100) {
				int value = data.getIntExtra("a", Constants.FAILURE);
				if (value == Constants.SUCCESS) {
					sendRequestToServer();
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Object object = listView.getAdapter().getItem(arg2);
		if (object instanceof Alert) {
			Alert alert = (Alert) object;
			Intent intent = new Intent(this, MerchantCreateOfferActivity.class);
			intent.putExtra(MerchantCreateOfferActivity.KEY_EXTRA_ALERT,
					alert.getmOfferId());
			intent.putExtra("name", storeName);
			intent.putExtra("address", storeAddress);
			intent.putExtra("logo", storeLogo);

			startActivityForResult(intent, 100);
		}
	}

	@Override
	public void onSuccess(int requestCode, Object object) {

		switch (requestCode) {
		case Constants.REQUEST_MERCHANT_ALERTS:
			progressBar.setVisibility(View.GONE);
			AlertsResponse alertsResponse = JsonResponseParser
					.parseAlertsResponse((String) object);
			if (alertsResponse.getmAlerts() != null) {
				bindDataToListView(alertsResponse.getmAlerts());
			}
			storeName = alertsResponse.getmStoreId();
			storeAddress = alertsResponse.getmStoreAddress();
			storeLogo = alertsResponse.getmStoreLogo();
			mStoreNameView.setText(alertsResponse.getmStoreId());
			mStoreAddressView.setText(alertsResponse.getmStoreAddress());
			if (!TextUtils.isEmpty(alertsResponse.getmStoreLogo())) {
				imageLoader.displayImage(alertsResponse.getmStoreLogo(),
						mStoreLogoView, configureOptions());
			} else {
				imageLoader.displayImage("drawable://"
						+ R.drawable.ic_placeholder_image, mStoreLogoView,
						configureOptions());
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestCode, String message) {
		switch (requestCode) {
		case Constants.REQUEST_MERCHANT_ALERTS:
			Utils.showToast(this, message);
			break;
		}
	}

	private void bindDataToListView(List<Alert> alerts) {
		if (alerts != null && !alerts.isEmpty()) {
			listView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			listView.setAdapter(new CustomAlertMessageAdapter(this, alerts));
		} else {
			listView.setVisibility(View.GONE);
			emptyView.setText("No Alerts Found");
			emptyView.setVisibility(View.VISIBLE);
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

}
