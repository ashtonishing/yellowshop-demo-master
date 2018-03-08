package com.buyopicadmin.admin.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.buyopicadmin.admin.BaseActivity;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomAlertMessageAdapter;
import com.buyopicadmin.admin.models.Alert;
import com.buyopicadmin.admin.models.AlertsResponse;

public class ListingsFragment extends Fragment implements OnItemClickListener,
		BuyopicNetworkCallBack {

	private ListView listView;
	private TextView emptyView;
	private ProgressBar progressBar;
	private String storeId;
	private String retailerId;
	private String mAssociateId;
	private BuyOpic buyOpic;
	private Context mContext;
	private onBuyOpicItemClickListener buyOpicItemClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		buyOpic = (BuyOpic) mContext.getApplicationContext();
		storeId = buyOpic.getmStoreId();
		retailerId = buyOpic.getmRetailerId();
		mAssociateId = buyOpic.getmMerchantId();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
		buyOpicItemClickListener = (com.buyopicadmin.admin.fragments.onBuyOpicItemClickListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_home, null);
		Utils.overrideFonts(mContext, view);
		prepareViews(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle=getArguments();
		if(bundle!=null && bundle.containsKey("a")&&bundle.getBoolean("a"))
		{
		sendRequestToServer();
		}
	}

	private void sendRequestToServer() {
		progressBar.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
				.getInstance(mContext);
		buyopicNetworkServiceManager.sendMerchantOfferListRequest(
				Constants.REQUEST_MERCHANT_ALERTS, storeId, retailerId,
				mAssociateId, this);
	}

	private void prepareViews(View v) {
		listView = (ListView) v.findViewById(R.id.alerts_list_view);
		emptyView = (TextView) v.findViewById(R.id.emptyview);
		progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
		listView.setOnItemClickListener(this);

		if (getActivity() instanceof BaseActivity) {
			BaseActivity baseActivity = (BaseActivity) getActivity();
			if (baseActivity != null) {
				baseActivity.setBeaconActionBar("My Listings", 2);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 100) {
				int value = data.getIntExtra("a", Constants.FAILURE);
				if (value == Constants.SUCCESS) {
					sendRequestToServer();
				}
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.add(0, 1, 0, "Add").setIcon(R.drawable.ic_add_new_listings)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		ImageView imageView=new ImageView(mContext);
		imageView.setImageResource(R.drawable.ic_search);
		imageView.setColorFilter(getResources().getColor(R.color.screen_bg_color));
		Drawable drawable=imageView.getDrawable();
		menu.add(0, 3, 0, "Search").setIcon(drawable)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			buyOpicItemClickListener.onItemClicked(1);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Object object = listView.getAdapter().getItem(arg2);
		
		
		if (object instanceof Alert) {
			Alert alert = (Alert) object;
			buyOpicItemClickListener.onItemClicked(alert);
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
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestCode, String message) {
		switch (requestCode) {
		case Constants.REQUEST_MERCHANT_ALERTS:
			Utils.showToast(mContext, message);
			break;
		}
	}

	private void bindDataToListView(List<Alert> alerts) {
		if (alerts != null && !alerts.isEmpty()) {
			listView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			listView.setAdapter(new CustomAlertMessageAdapter(mContext, alerts));
		} else {
			listView.setVisibility(View.GONE);
			emptyView.setText("No Alerts Found");
			emptyView.setVisibility(View.VISIBLE);
		}

	}

}
