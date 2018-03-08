package com.buyopicadmin.admin.adapters;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.models.Zone;

public class CustomEditZoneAdapter extends ArrayAdapter<Zone> {

	private Context mContext;
	private List<Zone> mZones;

	public CustomEditZoneAdapter(Context context, int resource,
			List<Zone> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mZones = objects;
	}

	@Override
	public int getCount() {
		return mZones.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_custom_edit_zone_adapter_view, null);
			viewHolder = new ViewHolder();
			viewHolder.mZoneNumberView = (TextView) convertView
					.findViewById(R.id.layout_custom_edit_zone_number);
			viewHolder.mZoneNameView = (EditText) convertView
					.findViewById(R.id.layout_custom_edit_zone_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Zone zone = getItem(position);
		if (zone != null)
			Utils.showLog("Zone Name" + zone.getmZoneName());
		viewHolder.mZoneNumberView.setText("" + position);
		viewHolder.mZoneNameView.setText(zone.getmZoneName());
		viewHolder.mZoneNameView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				zone.setmZoneName(s.toString());

			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView mZoneNumberView;
		EditText mZoneNameView;
	}

}
