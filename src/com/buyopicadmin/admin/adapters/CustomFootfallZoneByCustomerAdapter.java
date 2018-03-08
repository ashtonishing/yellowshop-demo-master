package com.buyopicadmin.admin.adapters;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.models.FootfallZoneByCustomer;

public class CustomFootfallZoneByCustomerAdapter extends
		ArrayAdapter<FootfallZoneByCustomer> {
	private Context mContext;
	private List<FootfallZoneByCustomer> mAlerts;
	PopupWindow popupWindow;

	public CustomFootfallZoneByCustomerAdapter(Context context,
			List<FootfallZoneByCustomer> objects) {
		super(context, -1, objects);
		this.mContext = context;
		this.mAlerts = objects;
	}

	@Override
	public int getCount() {
		return mAlerts.size();
	}

	@Override
	public FootfallZoneByCustomer getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_custom_foot_fall_zone_by_customer, null);
			Utils.overrideFonts(mContext, convertView);
			holder = new ViewHolder();
			holder.mTimeOfTheDayView = (TextView) convertView
					.findViewById(R.id.custom_foot_fall_customer_id);
			holder.mZoneNameView = (TextView) convertView
					.findViewById(R.id.custom_foot_fall_zone_name);
			holder.mTotalTimeView = (TextView) convertView
					.findViewById(R.id.custom_foot_fall_total_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final FootfallZoneByCustomer trafficZone = getItem(position);

		holder.mTimeOfTheDayView.setText(trafficZone.getmCustomerId());
		holder.mZoneNameView.setText(trafficZone.getmZone());
		
		holder.mTotalTimeView.setText(Utils.getMinutesSeconds(trafficZone.getmTotalTime()));
		if (position % 2 == 0) {
			holder.mTimeOfTheDayView
					.setBackgroundResource(R.drawable.right_solid_grey_black_border_view);
			holder.mZoneNameView
					.setBackgroundResource(R.drawable.right_solid_grey_grey_border_view);
			// holder.mTotalTimeView
			// .setBackgroundResource(R.drawable.right_solid_grey_grey_border_view);
			convertView.setBackgroundColor(Color.parseColor("#e7e7e7"));
		} else {
			holder.mTimeOfTheDayView
					.setBackgroundResource(R.drawable.right_border_view);
			holder.mZoneNameView
					.setBackgroundResource(R.drawable.right_solid_white_grey_border_view);
			// holder.mTotalTimeView
			// .setBackgroundResource(R.drawable.right_solid_white_grey_border_view);
			convertView.setBackgroundColor(Color.WHITE);
		}
		final View  view=LayoutInflater.from(mContext).inflate(
				R.layout.layout_custom_foot_fall_zone_by_customer,
				null);
	
		holder.mTimeOfTheDayView.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					popupWindow=new PopupWindow(mContext);
				popupWindow.setOutsideTouchable(true);
				popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
				popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
				View view1=LayoutInflater.from(mContext).inflate(R.layout.layout_customer_details_dialog_view_of_customer, null);
				view1.findViewById(R.id.dialog_cancel).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(popupWindow!=null && popupWindow.isShowing())
						{
							popupWindow.dismiss();
						}
						
						
					}
				});
				TextView nameView=(TextView) view1.findViewById(R.id.customer_name);
				TextView emailView=(TextView) view1.findViewById(R.id.customer_email);
				TextView phoneView=(TextView) view1.findViewById(R.id.customer_phone);
				emailView.setText(trafficZone.getmCustomerEmailId());
				nameView.setText(trafficZone.getmCustomerId());
				phoneView.setText(trafficZone.getmCustomerPhoneNumber());
				popupWindow.setContentView(view1);
				popupWindow.showAsDropDown(view);
				
			}
		});

		return convertView;
	}


	public static String getAverageTime(String avgTime) {
		try {
			double d = Double.parseDouble(avgTime);
			DecimalFormat decimalFormat = new DecimalFormat("#0.00");
			return decimalFormat.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "N/A";
	}

	static class ViewHolder {
		private TextView mTimeOfTheDayView;
		private TextView mZoneNameView;
		private TextView mTotalTimeView;
	}

}
