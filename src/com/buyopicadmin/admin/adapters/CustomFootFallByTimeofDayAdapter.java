package com.buyopicadmin.admin.adapters;

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
import com.buyopicadmin.admin.models.FootFallByTimeOfDay;

public class CustomFootFallByTimeofDayAdapter extends ArrayAdapter<FootFallByTimeOfDay> {
	private Context mContext;
	private List<FootFallByTimeOfDay> mAlerts;
	ViewHolder holder = null;
	private String emailId;

	public CustomFootFallByTimeofDayAdapter(Context context, List<FootFallByTimeOfDay> objects) {
		super(context, -1, objects);
		this.mContext = context;
		this.mAlerts = objects;
	}

	@Override
	public int getCount() {
		return mAlerts.size();
	}

	@Override
	public FootFallByTimeOfDay getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_custom_footfall_consumer_adapter_view,
					null);
			Utils.overrideFonts(mContext, convertView);
			holder = new ViewHolder();
			holder.mZoneNameView = (TextView) convertView
					.findViewById(R.id.foot_fall_consumer_serialno);
			holder.mTotalCustomersView = (TextView) convertView
					.findViewById(R.id.foot_fall_consumer_id);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FootFallByTimeOfDay footFallByTimeOfDay=getItem(position);
		holder.mZoneNameView.setText(footFallByTimeOfDay.getmTimeOfDay());
		holder.mTotalCustomersView.setText(footFallByTimeOfDay.getmCustomerId());

		if (position % 2 == 0) {
			holder.mZoneNameView
					.setBackgroundResource(R.drawable.right_solid_grey_black_border_view);
//			holder.mTotalCustomersView
//					.setBackgroundResource(R.drawable.right_solid_grey_grey_border_view);
			convertView.setBackgroundColor(Color.parseColor("#e7e7e7"));
		} else {
			holder.mZoneNameView
					.setBackgroundResource(R.drawable.right_border_view);
//			holder.mTotalCustomersView
//					.setBackgroundResource(R.drawable.right_solid_white_grey_border_view);
			convertView.setBackgroundColor(Color.WHITE);
		}
		final View  view=LayoutInflater.from(mContext).inflate(
				R.layout.layout_custom_footfall_consumer_adapter_view,
				null);
	
	
	   
	    
		holder.mTotalCustomersView.setOnClickListener(new OnClickListener() {
			
			private PopupWindow popupWindow;

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					popupWindow=new PopupWindow(mContext);
					FootFallByTimeOfDay footFallByTimeOfDay=getItem(position);
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
				emailView.setText(footFallByTimeOfDay.getmCustomerEmailId());
				nameView.setText(footFallByTimeOfDay.getmCustomerId());
				phoneView.setText(footFallByTimeOfDay.getmCustomerPhoneNum());
				popupWindow.setContentView(view1);
				popupWindow.showAsDropDown(view);
				
			}
		});

		return convertView;
	}


	static class ViewHolder {
		private TextView mZoneNameView;
		private TextView mTotalCustomersView;
	}


}
