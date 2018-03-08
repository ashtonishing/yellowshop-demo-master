package com.buyopicadmin.admin.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.DateTimePicker;
import com.buyopicadmin.admin.LoginActivity;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomSpinnerAdapter;
import com.buyopicadmin.admin.adapters.OrdersListCustomAdapter;
import com.buyopicadmin.admin.models.OrderList;

public class OrdersFragment extends Fragment implements OnItemClickListener,
		OnClickListener, BuyopicNetworkCallBack {

	private onBuyOpicItemClickListener buyOpicItemClickListener;
	private Context mContext;
	private BuyOpic buyOpic;
	private ListView mListView;
	private ProgressDialog mProgressDialog;
	private List<OrderList> orderStatusList;

	Dialog dialog;
	TextView itemNameText;
	TextView consumerMailId;
	TextView consumerPhoneNum;
	TextView priceValueText;
	TextView addressText;
	TextView quantityNumber;
	EditText orderDeliveryTime;
	Button cancelBtn;
	Button updateButton;
	ImageButton cancelDialog;
	TextView timeOfDelivery;
	Spinner orderStatusSpinner;
	OrderList orderitem;
	BuyopicNetworkServiceManager buyopicNetworkServiceManager;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// buyOpic = (BuyOpic) mContext.getApplicationContext();

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
		dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_orders_fragment, null);
		Utils.overrideFonts(mContext, view);
		mListView = (ListView) view.findViewById(R.id.listviewinorders);
		buyOpic = (BuyOpic) mContext.getApplicationContext();
		buyopicNetworkServiceManager = BuyopicNetworkServiceManager
				.getInstance(mContext);
		showProgressDialog();
		buyopicNetworkServiceManager.sendGetOrderStausRequest(
				Constants.REQUEST_GET_ORDERSTATUS, buyOpic.getmStoreId(),
				buyOpic.getmRetailerId(), this);

		return view;
	}

	private void bindOrderListViews(List<OrderList> ordersListObject) {

		if (ordersListObject != null && !ordersListObject.isEmpty()) {
			mListView.setAdapter(new OrdersListCustomAdapter(mContext,
					ordersListObject));
		}
		mListView.setOnItemClickListener(this);
		mListView.setSelection(0);
	}

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
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	/*
	 * @Override public void onItemSelected(AdapterView<?> arg0, View arg1, int
	 * arg2, long arg3) {
	 * 
	 * //String offerId=orderStatusList.get( (Integer)
	 * mListView.getAdapter().getItem(arg2)).getOfferItemId();
	 * 
	 * }
	 * 
	 * @Override public void onNothingSelected(AdapterView<?> arg0) {
	 * 
	 * }
	 */

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.dialog_cancel) {
			dialog.dismiss();
		} else if (v.getId() == R.id.cancelbuttoninorderdialog) {
			dialog.dismiss();
		} else if (v.getId() == R.id.exporderdeliverytime) {
			showDateTimeDialog((EditText) v);
		}

		else if (v.getId() == R.id.updatebuttoninorderdialog) {
			// submit request to server
			showProgressDialog();
			String selecteditem = orderStatusSpinner.getSelectedItemId() + 1
					+ "";
			Log.i("LIST", "selecteditem is" + selecteditem);
			;
			buyopicNetworkServiceManager.sendUpdateStoreOrderStatus(
					Constants.REQUEST_UPDATE_ORDERSTATUS,
					buyOpic.getmStoreId(), buyOpic.getmRetailerId(), orderitem
							.getOrderId(), selecteditem, orderDeliveryTime
							.getText().toString(), this);
		} else if (v.getId() == R.id.orderstatusspinner) {
		}

	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_GET_ORDERSTATUS:
			orderStatusList = JsonResponseParser
					.parseOrderListStatus((String) object);
			buyOpic.setStatusHashMap(LoginActivity.mStatusHashMap);

			if (orderStatusList != null && !orderStatusList.isEmpty()) {
				bindOrderListViews(orderStatusList);
			} else {
				Utils.showToast(mContext, "No Results Found");
			}

			break;
		case Constants.REQUEST_UPDATE_ORDERSTATUS:
			dismissProgressDialog();
			dialog.dismiss();
			Log.i("LIST", "msg obj is" + object);
			HashMap<String, String> mHashMap = JsonResponseParser
					.parseUpdateOrderStatus((String) object);
			String status = mHashMap.get("status");
			if (status != null && status.equalsIgnoreCase("ok")) {
				Utils.showToast(mContext, mHashMap.get("message"));
				showProgressDialog();
				buyopicNetworkServiceManager.sendGetOrderStausRequest(
						Constants.REQUEST_GET_ORDERSTATUS,
						buyOpic.getmStoreId(), buyOpic.getmRetailerId(), this);
			} else if (status != null && status.equalsIgnoreCase("error")) {
				Utils.showToast(mContext, mHashMap.get("message"));
			} else {
				Utils.showToast(mContext, "Order not updated ");
			}
		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestCode, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		orderitem = (OrderList) mListView.getAdapter().getItem(arg2);
		showUpdateOrderPopUp(orderitem);
	}

	private void showUpdateOrderPopUp(OrderList orderitem) {

		// TODO Auto-generated method stub

		View view = null;
		try {

			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.updatenowdialog, null);

			dialog.setContentView(view);
			cancelDialog = (ImageButton) dialog
					.findViewById(R.id.dialog_cancel);
			orderDeliveryTime = (EditText) dialog
					.findViewById(R.id.exporderdeliverytime);
			itemNameText = (TextView) dialog.findViewById(R.id.itemnametext);
			consumerMailId = (TextView) dialog
					.findViewById(R.id.consumermailidtext);
			consumerPhoneNum = (TextView) dialog
					.findViewById(R.id.consumerphonenumtext);
			timeOfDelivery = (TextView) dialog
					.findViewById(R.id.timeofdelivery);
			orderStatusSpinner = (Spinner) dialog
					.findViewById(R.id.orderstatusspinner);
			priceValueText = (TextView) dialog
					.findViewById(R.id.pricevaluetext);
			addressText = (TextView) dialog.findViewById(R.id.addresstext);
			quantityNumber = (TextView) dialog
					.findViewById(R.id.quantitynumber);
			cancelBtn = (Button) dialog
					.findViewById(R.id.cancelbuttoninorderdialog);
			updateButton = (Button) dialog
					.findViewById(R.id.updatebuttoninorderdialog);

			itemNameText.setText(orderitem.getOfferItemName());
			consumerMailId.setText(orderitem.getmConsumerMailId());
			consumerPhoneNum.setText(orderitem.getmConsumerPhoneNo());
			timeOfDelivery.setText(orderitem.getRequestedDelivery());

			priceValueText.setText(orderitem.getTotalOrderPrice());
			quantityNumber.setText(orderitem.getOrderQuantity());
			addressText.setText(orderitem.getAddressLine1() + " "
					+ orderitem.getAddressLine2());

			orderDeliveryTime.setOnClickListener(this);
			itemNameText.setOnClickListener(this);
			priceValueText.setOnClickListener(this);
			quantityNumber.setOnClickListener(this);
			updateButton.setOnClickListener(this);
			cancelBtn.setOnClickListener(this);
			cancelDialog.setOnClickListener(this);

			Calendar calendar = Calendar.getInstance();
			String currentDateTime = (calendar.get(Calendar.MONTH) + 1) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "/"
					+ calendar.get(Calendar.YEAR) + " "
			/*
			 * + calendar.get(Calendar.HOUR) + ":" +
			 * calendar.get(Calendar.MINUTE) + " " +
			 * (calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM")
			 */;

			int hourTime = calendar.get(Calendar.HOUR);
			String hourValue = String.format("%02d", hourTime);
			int minuteTime = calendar.get(Calendar.MINUTE);
			String minuteValue = String.format("%02d", minuteTime);
			currentDateTime = currentDateTime
					+ hourValue
					+ ":"
					+ minuteValue
					+ ""
					+ " "
					+ (calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM"
							: "PM");
			orderDeliveryTime.setText(currentDateTime);

			Resources res = getResources();
			LinkedHashMap<String, String> statusHashMap;
			statusHashMap = new LinkedHashMap<String, String>();
			statusHashMap = buyOpic.getStatusHashMap();
			ArrayList<String> list = Collections.list(Collections
					.enumeration(statusHashMap.values()));

			orderStatusSpinner.setAdapter(new CustomSpinnerAdapter(mContext,
					R.layout.spinner_rows, list, res, orderitem));
			int statusID = Integer.parseInt(orderitem.getStatusId());
			switch (statusID) {
			case 1:
				orderStatusSpinner.setSelection(0);
				break;
			case 2:
				orderStatusSpinner.setSelection(1);

				break;
			case 3:
				orderStatusSpinner.setSelection(2);
				break;
			case 4:
				orderStatusSpinner.setSelection(3);
				break;
			case 5:
				orderStatusSpinner.setSelection(4);
				break;

			default:
				break;
			}
			orderStatusSpinner.setOnClickListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}

		dialog.show();
	}

	private void showDateTimeDialog(final EditText edittext) {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(getActivity());
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getActivity()
				.getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
				.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as
		// expected though)
		final String timeS = android.provider.Settings.System.getString(
				getActivity().getContentResolver(),
				android.provider.Settings.System.TIME_12_24);
		final boolean is24h = !(timeS == null || timeS.equals("12"));

		// Update demo TextViews when the "OK" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mDateTimePicker.clearFocus();

						String dateTime = ((mDateTimePicker.get(Calendar.MONTH)) + 1)
								+ "/"
								+ mDateTimePicker.get(Calendar.DAY_OF_MONTH)
								+ "/"
								+ mDateTimePicker.get(Calendar.YEAR)
								+ " "
						/*
						 * + mDateTimePicker.get(Calendar.HOUR) + ":" +
						 * mDateTimePicker.get(Calendar.MINUTE) + " " +
						 * (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ?
						 * "AM" : "PM")
						 */;

						int hourTime = mDateTimePicker.get(Calendar.HOUR);
						String hourValue = String.format("%02d", hourTime);
						int minuteTime = mDateTimePicker.get(Calendar.MINUTE);
						String minuteValue = String.format("%02d", minuteTime);
						dateTime = dateTime
								+ hourValue
								+ ":"
								+ minuteValue
								+ ""
								+ " "
								+ (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ? "AM"
										: "PM");
						Calendar calendar = Calendar.getInstance();
						String currentDateTime = (calendar.get(Calendar.MONTH) + 1)
								+ "/"
								+ calendar.get(Calendar.DAY_OF_MONTH)
								+ "/"
								+ calendar.get(Calendar.YEAR)
								+ " "
								+ calendar.get(Calendar.HOUR)
								+ ":"
								+ calendar.get(Calendar.MINUTE)
								+ " "
								+ (calendar.get(Calendar.AM_PM) == 0 ? "AM"
										: "PM");

						// showing time

						edittext.setText(dateTime);
						mDateTimeDialog.dismiss();
					}
				});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimeDialog.cancel();
					}
				});

		// Reset Date and Time pickers when the "Reset" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimePicker.reset();
					}
				});

		// Setup TimePicker
		mDateTimePicker.setIs24HourView(is24h);
		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();
	}

}
