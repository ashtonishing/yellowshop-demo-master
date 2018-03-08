package com.buyopicadmin.admin.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.models.Zone;

public class CustomTextViewAdapter extends ArrayAdapter<Zone> {
	private Context mContext;
	private List<String> mEntries;
	ViewHolder holder = null;

	public CustomTextViewAdapter(Context context, List<String> objects) {
		super(context, -1);
		this.mContext = context;
		this.mEntries = objects;
	}

	@Override
	public int getCount() {
		return mEntries.size();
	}

	@Override
	public Zone getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_custom_zone_textview, null);
			//Utils.overrideFonts(mContext, convertView);
			holder = new ViewHolder();
			holder.mZoneNameView = (TextView) convertView
					.findViewById(R.id.entriestextview);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mZoneNameView.setText(mEntries.get(position));
		return convertView;
	}

	static class ViewHolder {
		private TextView mZoneNameView;
	}

}
