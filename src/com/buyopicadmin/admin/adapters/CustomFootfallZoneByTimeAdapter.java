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
import com.buyopicadmin.admin.models.FootfallZoneByTime;

public class CustomFootfallZoneByTimeAdapter extends
		ArrayAdapter<FootfallZoneByTime> {
	private Context mContext;
	private List<FootfallZoneByTime> mAlerts;

	public CustomFootfallZoneByTimeAdapter(Context context,
			List<FootfallZoneByTime> objects) {
		super(context, -1, objects);
		this.mContext = context;
		this.mAlerts = objects;
	}

	@Override
	public int getCount() {
		return mAlerts.size();
	}

	@Override
	public FootfallZoneByTime getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_custom_foot_fall_zone_by_time, null);
			Utils.overrideFonts(mContext, convertView);
			holder = new ViewHolder();
			holder.mTimeOfTheDayView = (TextView) convertView
					.findViewById(R.id.custom_foot_fall_time_of_the_day);
			holder.mZoneNameView = (TextView) convertView
					.findViewById(R.id.custom_foot_fall_zone_name);
			holder.mTotalTimeView = (TextView) convertView
					.findViewById(R.id.custom_foot_fall_total_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FootfallZoneByTime trafficZone = getItem(position);

		holder.mTimeOfTheDayView.setText(trafficZone.getmTimeofDay());
		holder.mZoneNameView.setText(trafficZone.getmZoneName());
		
		holder.mTotalTimeView.setText(Utils.getMinutesSeconds(trafficZone
				.getmTotalTime()));
		if (position % 2 == 0) {
			holder.mTimeOfTheDayView
					.setBackgroundResource(R.drawable.right_solid_grey_black_border_view);
			holder.mZoneNameView
					.setBackgroundResource(R.drawable.right_solid_grey_grey_border_view);
			convertView.setBackgroundColor(Color.parseColor("#e7e7e7"));
		} else {
			holder.mTimeOfTheDayView
					.setBackgroundResource(R.drawable.right_border_view);
			holder.mZoneNameView
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
		private TextView mTimeOfTheDayView;
		private TextView mZoneNameView;
		private TextView mTotalTimeView;
	}

}
