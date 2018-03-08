package com.buyopicadmin.admin.adapters;

import java.util.List;

import android.content.Context;
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
import com.buyopicadmin.admin.models.ClickThrough;

public class CustomClickThroughFragmentAdapter extends ArrayAdapter<ClickThrough>{
	
	private Context mContext;
	private List<ClickThrough> mDetails;

	public CustomClickThroughFragmentAdapter(Context context,
			List<ClickThrough> objects) {
		super(context, -1, objects);
		this.mContext=context;
		this.mDetails=objects;
	}
	
	@Override
	public int getCount() {
		return mDetails.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.layout_custom_click_through_details_view, null);
			holder=new ViewHolder();
			Utils.overrideFonts(mContext, convertView);
			holder.mCustomerIdView=(TextView) convertView.findViewById(R.id.custom_click_through_customer_id);
			holder.mTopLevelListingView=(TextView)convertView.findViewById(R.id.custom_click_through_top_level_listings);
			holder.mMerchantPageView=(TextView)convertView.findViewById(R.id.custom_click_through_merchant_page);
			holder.mDetailsPageView=(TextView)convertView.findViewById(R.id.custom_click_through_detail_page);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder) convertView.getTag();
		}
		
		final ClickThrough clickThrough=getItem(position);
		holder.mCustomerIdView.setText(clickThrough.getmCustomerIdView());
		holder.mDetailsPageView.setText(clickThrough.getmDetailPage());
		holder.mMerchantPageView.setText(clickThrough.getmMerchantPage());
		holder.mTopLevelListingView.setText(clickThrough.getmTopLevelListing());
		final View  view=LayoutInflater.from(mContext).inflate(
				R.layout.layout_custom_click_through_details_view,
				null);
		
		holder.mCustomerIdView.setOnClickListener(new OnClickListener() {
			
			private PopupWindow popupWindow;

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
				emailView.setText(clickThrough.getmCustomerEmailId());
				nameView.setText(clickThrough.getmCustomerIdView());
				phoneView.setText(clickThrough.getmCustomerPhoneNumber());
				popupWindow.setContentView(view1);
				popupWindow.showAsDropDown(view);
				
			}
		});


		return convertView;
	}
	
	static class ViewHolder
	{
		TextView mCustomerIdView;
		TextView mTopLevelListingView;
		TextView mMerchantPageView;
		TextView mDetailsPageView;
	}
	
	

}
