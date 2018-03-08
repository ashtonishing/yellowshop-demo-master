package com.buyopicadmin.admin.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.models.Zone;

public class CustomZoneAdapter extends ArrayAdapter<Zone> {
	private Context mContext;
	private List<Zone> mAlerts;
	ViewHolder holder = null;

	public CustomZoneAdapter(Context context, List<Zone> objects) {
		super(context, -1, objects);
		this.mContext = context;
		this.mAlerts = objects;
	}

	@Override
	public int getCount() {
		return mAlerts.size();
	}

	@Override
	public Zone getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_custom_zone_checkedtextview, null);
			Utils.overrideFonts(mContext, convertView);
			holder = new ViewHolder();
			holder.mZoneNameView = (CheckedTextView) convertView
					.findViewById(R.id.checkedtextview);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Zone trafficZone = getItem(position);
		if (trafficZone.isSelected()) {
			holder.mZoneNameView.setChecked(true);
		} else {
			holder.mZoneNameView.setChecked(false);
		}
		holder.mZoneNameView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CheckedTextView checkedTextView = (CheckedTextView) v;
				trafficZone.setSelected(!checkedTextView.isChecked());
				checkedTextView.setChecked(!checkedTextView.isChecked());
			}
		});
		holder.mZoneNameView.setText(trafficZone.getmZoneName());
		return convertView;
	}

	static class ViewHolder {
		private CheckedTextView mZoneNameView;
	}

}
