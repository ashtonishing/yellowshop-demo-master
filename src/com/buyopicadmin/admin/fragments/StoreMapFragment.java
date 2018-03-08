package com.buyopicadmin.admin.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.models.Consumer;
import com.buyopicadmin.admin.models.StoreMap;
import com.buyopicadmin.admin.models.Zone;

public class StoreMapFragment extends Fragment implements OnClickListener,
		BuyopicNetworkCallBack {

	private GridLayout zone0GridLayout;
	private GridLayout zone1GridLayout;
	private GridLayout zone2GridLayout;
	private GridLayout zone3GridLayout;
	private TextView zone0NameView;
	private TextView zone1NameView;
	private TextView zone2NameView;
	private TextView zone3NameView;
	private Context mContext;
	private onBuyOpicItemClickListener buyOpicItemClickListener;
	// private List<StoreMap> storeMaps;
	private HashMap<Integer, StoreMap> storeMaps;
	private ProgressDialog mProgressDialog;
	private Timer timer;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (storeMaps != null && !storeMaps.isEmpty()) {
			bindStoreMapsInfo(storeMaps);
		}
		bindZoneNamesToUI();

	}

	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			Message.obtain(handler).sendToTarget();
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			buyopicNetworkServiceManager.sendStoreActiveConsumersRequest(
					Constants.REQUEST_STORE_ACTIVE_CONSUMERS,
					buyOpic.getmStoreId(), buyOpic.getmRetailerId(),
					buyOpic.getmMerchantId(), StoreMapFragment.this);
		}

	};
	private BuyOpic buyOpic;
	private BuyopicNetworkServiceManager buyopicNetworkServiceManager;
	private List<Zone> mZones;

	private void showProgressDialog() {

		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(mContext, "", "Please wait",
					false, false);
		}
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
		buyOpicItemClickListener = (onBuyOpicItemClickListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buyOpic = (BuyOpic) mContext.getApplicationContext();
		buyopicNetworkServiceManager = BuyopicNetworkServiceManager
				.getInstance(mContext);
		Bundle bundle = getArguments();
		if (bundle != null && bundle.containsKey("a") && bundle.getBoolean("a")) {
			showProgressDialog();

			buyopicNetworkServiceManager.sendZonesOfStoreRequest(
					Constants.REQUEST_ZONES, buyOpic.getmMerchantId(),
					buyOpic.getmRetailerId(), buyOpic.getmStoreId(), this);

			buyopicNetworkServiceManager.sendStoreActiveConsumersRequest(
					Constants.REQUEST_STORE_ACTIVE_CONSUMERS,
					buyOpic.getmStoreId(), buyOpic.getmRetailerId(),
					buyOpic.getmMerchantId(), this);

			if (timer == null) {
				timer = new Timer();
			}
			timer.scheduleAtFixedRate(task, 10000, 10000);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_store_map, null);

		zone0NameView = (TextView) view.findViewById(R.id.zone0_grid_zone_name);
		zone1NameView = (TextView) view.findViewById(R.id.zone1_grid_zone_name);
		zone2NameView = (TextView) view.findViewById(R.id.zone2_grid_zone_name);
		zone3NameView = (TextView) view.findViewById(R.id.zone3_grid_zone_name);

		zone0GridLayout = (GridLayout) view
				.findViewById(R.id.zone0_grid_layout);
		zone1GridLayout = (GridLayout) view
				.findViewById(R.id.zone1_grid_layout);
		zone2GridLayout = (GridLayout) view
				.findViewById(R.id.zone2_grid_layout);
		zone3GridLayout = (GridLayout) view
				.findViewById(R.id.zone3_grid_layout);

		zone0GridLayout.setOnClickListener(this);
		zone1GridLayout.setOnClickListener(this);
		zone2GridLayout.setOnClickListener(this);
		zone3GridLayout.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		if (storeMaps != null && !storeMaps.isEmpty()) {
			int size = storeMaps.size();
			switch (v.getId()) {
			case R.id.zone0_grid_layout:
				if (storeMaps != null && storeMaps.containsKey(0)
						&& storeMaps.get(0) != null) {
					StoreMap storeMap = storeMaps.get(0);
					buyOpicItemClickListener.onItemClicked(storeMap);
				}
				break;
			case R.id.zone1_grid_layout:
				if (storeMaps != null && storeMaps.containsKey(1)
						&& storeMaps.get(1) != null) {
					StoreMap storeMap = storeMaps.get(1);
					buyOpicItemClickListener.onItemClicked(storeMap);
				}
				break;
			case R.id.zone2_grid_layout:
				if (storeMaps != null && storeMaps.containsKey(2)
						&& storeMaps.get(2) != null) {
					StoreMap storeMap = storeMaps.get(2);
					buyOpicItemClickListener.onItemClicked(storeMap);
				}
				break;
			case R.id.zone3_grid_layout:
				if (storeMaps != null && storeMaps.containsKey(3)
						&& storeMaps.get(3) != null) {
					StoreMap storeMap = storeMaps.get(3);
					buyOpicItemClickListener.onItemClicked(storeMap);
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_STORE_ACTIVE_CONSUMERS:
			storeMaps = JsonResponseParser
					.parseStoreActiveUserUpdateResponse((String) object);
			bindStoreMapsInfo(storeMaps);

			break;
		case Constants.REQUEST_ZONES:
			mZones = JsonResponseParser
			.parseZonesOfStoreInfoResponse((String) object);
			bindZoneNamesToUI();

		default:
			break;
		}

	}

	private void bindZoneNamesToUI() {
		if(mZones!=null && !mZones.isEmpty())
		{
			
			if(mZones.size()==1&&mZones.get(0).getmZoneNumber().equalsIgnoreCase("na")){
				
			}
			else{
			for(Zone zone:mZones)
			{
				
				if(zone.getmZoneNumber()!=null)
				{
				int zoneNumber=Integer.parseInt(zone.getmZoneNumber());
				switch (zoneNumber) {
				case 0:
					zone0NameView.setText(zone.getmZoneName());
					break;
				case 1:
					zone1NameView.setText(zone.getmZoneName());
					break;
				case 2:
					zone2NameView.setText(zone.getmZoneName());
					break;
				case 3:
					zone3NameView.setText(zone.getmZoneName());
					break;
				default:
					break;
				}
				}
			}
			}
		}
	}

	private void bindStoreMapsInfo(HashMap<Integer, StoreMap> mStoreMaps) {
		zone0GridLayout.removeAllViews();
		zone1GridLayout.removeAllViews();
		zone2GridLayout.removeAllViews();
		zone3GridLayout.removeAllViews();
		if (mStoreMaps != null && !mStoreMaps.isEmpty()) {
			List keys = new ArrayList(mStoreMaps.keySet());
			for (int i = 0; i < mStoreMaps.size(); i++) {
				int j = (Integer) keys.get(i);
				switch (j) {
				case 0:
					StoreMap storeMap = mStoreMaps.get(j);
					zone0NameView.setText(storeMap.getmZoneName());
					if (storeMap != null && storeMap.getmConsumers() != null
							&& !storeMap.getmConsumers().isEmpty()) {
						for (Consumer consumer : storeMap.getmConsumers()) {
							ImageButton view = (ImageButton) LayoutInflater
									.from(mContext)
									.inflate(
											R.layout.layout_zone_user_image_view,
											null);
							view.setTag(consumer.getmConsumerId());
							GridLayout.LayoutParams param = new GridLayout.LayoutParams();
							param.height = 100;
							param.width = 100;
							param.setGravity(Gravity.CENTER);

							zone0GridLayout.addView(view, param);
						}
					}

					break;
				case 1:
					storeMap = mStoreMaps.get(j);
					zone1NameView.setText(storeMap.getmZoneName());

					if (storeMap != null && storeMap.getmConsumers() != null
							&& !storeMap.getmConsumers().isEmpty()) {
						for (Consumer consumer : storeMap.getmConsumers()) {
							ImageButton view = (ImageButton) LayoutInflater
									.from(mContext)
									.inflate(
											R.layout.layout_zone_user_image_view,
											null);
							view.setTag(consumer.getmConsumerId());
							GridLayout.LayoutParams param = new GridLayout.LayoutParams();
							param.height = 100;
							param.width = 100;
							param.setGravity(Gravity.CENTER);
							zone1GridLayout.addView(view, param);
						}
					}
					break;
				case 2:

					storeMap = mStoreMaps.get(j);
					zone2NameView.setText(storeMap.getmZoneName());

					if (storeMap != null && storeMap.getmConsumers() != null
							&& !storeMap.getmConsumers().isEmpty()) {
						for (Consumer consumer : storeMap.getmConsumers()) {

							ImageButton view = (ImageButton) LayoutInflater
									.from(mContext)
									.inflate(
											R.layout.layout_zone_user_image_view,
											null);
							view.setTag(consumer.getmConsumerId());
							GridLayout.LayoutParams param = new GridLayout.LayoutParams();
							param.height = 100;
							param.width = 100;
							param.setGravity(Gravity.CENTER);
							zone2GridLayout.addView(view, param);
						}
					}
					break;
				case 3:
					storeMap = mStoreMaps.get(j);
					zone3NameView.setText(storeMap.getmZoneName());

					if (storeMap != null && storeMap.getmConsumers() != null
							&& !storeMap.getmConsumers().isEmpty()) {
						for (Consumer consumer : storeMap.getmConsumers()) {
							ImageButton view = (ImageButton) LayoutInflater
									.from(mContext)
									.inflate(
											R.layout.layout_zone_user_image_view,
											null);
							view.setTag(consumer.getmConsumerId());
							GridLayout.LayoutParams param = new GridLayout.LayoutParams();
							param.height = 100;
							param.width = 100;
							param.setGravity(Gravity.CENTER);
							zone3GridLayout.addView(view, param);
						}
					}

				default:
					break;
				}
			}

		}
	}

	private void bindStoreMapsInfo(List<StoreMap> mStoreMaps) {
		zone0GridLayout.removeAllViews();
		zone1GridLayout.removeAllViews();
		zone2GridLayout.removeAllViews();
		zone3GridLayout.removeAllViews();
		if (mStoreMaps != null && !mStoreMaps.isEmpty()) {

			for (int i = 0; i < mStoreMaps.size(); i++) {

				switch (i) {
				case 0:
					StoreMap storeMap = mStoreMaps.get(0);
					zone0NameView.setText(storeMap.getmZoneName());
					if (storeMap.getmConsumers() != null
							&& !storeMap.getmConsumers().isEmpty()) {
						for (Consumer consumer : storeMap.getmConsumers()) {
							ImageButton view = (ImageButton) LayoutInflater
									.from(mContext)
									.inflate(
											R.layout.layout_zone_user_image_view,
											null);
							view.setTag(consumer.getmConsumerId());
							GridLayout.LayoutParams param = new GridLayout.LayoutParams();
							param.height = 100;
							param.width = 100;
							param.setGravity(Gravity.CENTER);

							zone0GridLayout.addView(view, param);
						}
					}

					break;
				case 1:
					storeMap = mStoreMaps.get(1);
					zone1NameView.setText(storeMap.getmZoneName());

					if (storeMap.getmConsumers() != null
							&& !storeMap.getmConsumers().isEmpty()) {
						for (Consumer consumer : storeMap.getmConsumers()) {
							ImageButton view = (ImageButton) LayoutInflater
									.from(mContext)
									.inflate(
											R.layout.layout_zone_user_image_view,
											null);
							view.setTag(consumer.getmConsumerId());
							GridLayout.LayoutParams param = new GridLayout.LayoutParams();
							param.height = 100;
							param.width = 100;
							param.setGravity(Gravity.CENTER);
							zone1GridLayout.addView(view, param);
						}
					}
					break;
				case 2:

					storeMap = mStoreMaps.get(2);
					zone2NameView.setText(storeMap.getmZoneName());

					if (storeMap.getmConsumers() != null
							&& !storeMap.getmConsumers().isEmpty()) {
						for (Consumer consumer : storeMap.getmConsumers()) {
							ImageButton view = (ImageButton) LayoutInflater
									.from(mContext)
									.inflate(
											R.layout.layout_zone_user_image_view,
											null);
							view.setTag(consumer.getmConsumerId());
							GridLayout.LayoutParams param = new GridLayout.LayoutParams();
							param.height = 100;
							param.width = 100;
							param.setGravity(Gravity.CENTER);
							zone2GridLayout.addView(view, param);
						}
					}
					break;
				case 3:
					storeMap = mStoreMaps.get(3);
					zone3NameView.setText(storeMap.getmZoneName());

					if (storeMap.getmConsumers() != null
							&& !storeMap.getmConsumers().isEmpty()) {
						for (Consumer consumer : storeMap.getmConsumers()) {
							ImageButton view = (ImageButton) LayoutInflater
									.from(mContext)
									.inflate(
											R.layout.layout_zone_user_image_view,
											null);
							view.setTag(consumer.getmConsumerId());
							GridLayout.LayoutParams param = new GridLayout.LayoutParams();
							param.height = 100;
							param.width = 100;
							param.setGravity(Gravity.CENTER);
							zone3GridLayout.addView(view, param);
						}
					}

				default:
					break;
				}
			}

		}
	}

	@Override
	public void onFailure(int requestCode, String message) {

	}

}
