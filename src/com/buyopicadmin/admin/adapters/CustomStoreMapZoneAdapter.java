package com.buyopicadmin.admin.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.models.StoreMap;

public class CustomStoreMapZoneAdapter extends ArrayAdapter<StoreMap> {
	private Context mContext;
	private List<StoreMap> mAlerts;
	ViewHolder holder = null;

	public CustomStoreMapZoneAdapter(Context context, List<StoreMap> objects) {
		super(context, -1, objects);
		this.mContext = context;
		this.mAlerts = objects;
	}

	@Override
	public int getCount() {
		return mAlerts.size();
	}

	@Override
	public StoreMap getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_custom_mall_tier_storemap_adapter_view,
					null);
			Utils.overrideFonts(mContext, convertView);
			holder = new ViewHolder();
			holder.mZoneNameView = (TextView) convertView
					.findViewById(R.id.mall_tier_store_map_zone_name);
			holder.mTotalCustomers = (TextView) convertView
					.findViewById(R.id.mall_tier_store_map_customers);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		StoreMap alert = getItem(position);
		holder.mZoneNameView.setText(alert.getmZoneName());

		if (alert.getmConsumers() != null && !alert.getmConsumers().isEmpty()) {
			holder.mTotalCustomers.setText("" + alert.getmConsumers().size());
		}
		else
		{
			holder.mTotalCustomers.setText("0");
		}
		if(position%2==0)
		{
			holder.mZoneNameView.setBackgroundResource(R.drawable.custom_listview_mall_first_row_bg);
			holder.mTotalCustomers.setBackgroundResource(R.drawable.custom_listview_mall_first_row_bg);
		}
		else
		{
			holder.mZoneNameView.setBackgroundResource(R.drawable.custom_listview_mall_second_row_bg);
			holder.mTotalCustomers.setBackgroundResource(R.drawable.custom_listview_mall_second_row_bg);
		}
		return convertView;
	}

	static class ViewHolder {
		private TextView mZoneNameView;
		private TextView mTotalCustomers;
	}

}
