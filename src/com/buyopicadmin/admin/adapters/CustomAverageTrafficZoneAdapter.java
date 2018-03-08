package com.buyopicadmin.admin.adapters;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.models.TrafficZone;

public class CustomAverageTrafficZoneAdapter extends ArrayAdapter<TrafficZone> {
	private Context mContext;
	private List<TrafficZone> mAlerts;
	ViewHolder holder = null;

	public CustomAverageTrafficZoneAdapter(Context context, List<TrafficZone> objects) {
		super(context, -1, objects);
		this.mContext = context;
		this.mAlerts = objects;
	}

	@Override
	public int getCount() {
		return mAlerts.size();
	}

	@Override
	public TrafficZone getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_average_customer_traffic_zone_table_row,
					null);
			Utils.overrideFonts(mContext, convertView);
			holder = new ViewHolder();
			holder.mZoneNameView = (TextView) convertView
					.findViewById(R.id.traffic_zone_name);
			holder.mTotalCustomersView = (TextView) convertView
					.findViewById(R.id.traffic_total_no_of_customers);
			holder.mTotalTimeView = (TextView) convertView
					.findViewById(R.id.traffic_total_time);
			holder.mAvgTimeView = (TextView) convertView
					.findViewById(R.id.traffic_avg_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final View  view=LayoutInflater.from(mContext).inflate(
				R.layout.layout_average_customer_traffic_zone_table_row,
				null);

		TrafficZone trafficZone = getItem(position);

		holder.mZoneNameView.setText(trafficZone.getmZoneName());
		holder.mTotalCustomersView.setText(trafficZone.getmTotalCustomers());
		holder.mTotalTimeView.setText(Utils.getMinutesSeconds(trafficZone
				.getmTotalTimeInZone()));
		holder.mAvgTimeView.setText(Utils.getMinutesSeconds(trafficZone
				.getmAvgTimeinZone()));

		if (position % 2 == 0) {
			holder.mZoneNameView
					.setBackgroundResource(R.drawable.right_solid_grey_black_border_view);
			holder.mTotalCustomersView
					.setBackgroundResource(R.drawable.right_solid_grey_grey_border_view);
			holder.mTotalTimeView
					.setBackgroundResource(R.drawable.right_solid_grey_grey_border_view);
			convertView.setBackgroundColor(Color.parseColor("#e7e7e7"));
		} else {
			holder.mZoneNameView
					.setBackgroundResource(R.drawable.right_border_view);
			holder.mTotalCustomersView
					.setBackgroundResource(R.drawable.right_solid_white_grey_border_view);
			holder.mTotalTimeView
					.setBackgroundResource(R.drawable.right_solid_white_grey_border_view);
			convertView.setBackgroundColor(Color.WHITE);
		}
		return convertView;
	}

	public static String getMinutesSeconds(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm",Locale.US);
		try {
			Date d = dateFormat.parse(time);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("mm:ss",Locale.US);
			return dateFormat2.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "N/A";
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
		private TextView mZoneNameView;
		private TextView mTotalCustomersView;
		private TextView mTotalTimeView;
		private TextView mAvgTimeView;
	}


}
