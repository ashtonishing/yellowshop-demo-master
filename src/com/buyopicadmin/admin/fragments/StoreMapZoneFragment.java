package com.buyopicadmin.admin.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.buyopicadmin.admin.BaseActivity;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomTextViewAdapter;
import com.buyopicadmin.admin.models.Consumer;
import com.buyopicadmin.admin.models.StoreMap;

public class StoreMapZoneFragment extends Fragment implements OnClickListener, OnItemClickListener {
	private GridLayout gridLayout;
	private Activity mContext;
	private TextView zoneNameText;
	protected PopupWindow popupWindow;
	protected ListView listView;
	public static final String KEY_EXTRA_STORE_MAP = "store_map";
	public Consumer consumer;
	protected View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.store_map_zone_specific_layout,
				null);
		zoneNameText = (TextView) view
				.findViewById(R.id.store_map_zone_specific_text_view);
		gridLayout = (GridLayout) view
				.findViewById(R.id.store_map_zone_specific_grid_layout);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(getActivity() instanceof BaseActivity)
		{
			BaseActivity baseActivity=(BaseActivity) getActivity();
			baseActivity.setBeaconActionBar("Customer Info", 2);
		}
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(popupWindow!=null && popupWindow.isShowing())
		{
			popupWindow.dismiss();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();
		if (bundle != null && bundle.containsKey(KEY_EXTRA_STORE_MAP)) {
			StoreMap storeMap = (StoreMap) bundle.get(KEY_EXTRA_STORE_MAP);
			if (storeMap != null && storeMap.getmConsumers() != null
					&& !storeMap.getmConsumers().isEmpty()) {
				zoneNameText.setText(storeMap.getmZoneName());
				for (final Consumer consumer : storeMap.getmConsumers()) {

					final ImageButton view = (ImageButton) LayoutInflater.from(
							mContext).inflate(
							R.layout.layout_zone_user_image_view, null);
					view.setTag(consumer.getmConsumerId());
					GridLayout.LayoutParams param = new GridLayout.LayoutParams();
					param.height = 100;
					param.width = 100;
					param.setGravity(Gravity.CENTER);
					view.setSelected(false);
					view.setFocusable(false);
					view.setOnClickListener(new OnClickListener() {
						

						@Override
						public void onClick(View v) {
							disableUserIcons();
							view.setSelected(true);
							view.setFocusable(false);
							StoreMapZoneFragment.this.consumer=consumer;
							StoreMapZoneFragment.this.view=v;
							
							if(popupWindow==null)
							{
							popupWindow=new PopupWindow(mContext);
							}
							popupWindow.setOutsideTouchable(true);
							popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
							popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
							View view1=LayoutInflater.from(mContext).inflate(R.layout.layout_actions_dialog_view, null);
							listView=(ListView) view1.findViewById(R.id.listview);
							String[] object=getResources().getStringArray(R.array.zone_specific_actions);
							ArrayList<String> objects=new ArrayList<String>();
							for(int i=0;i<object.length;i++){
							objects.add(object[i]);
							}
							ListAdapter adapter=new CustomTextViewAdapter(mContext, objects);
							listView.setAdapter(adapter);
							listView.setOnItemClickListener(StoreMapZoneFragment.this);
					    	view1.findViewById(R.id.dialog_cancel).setOnClickListener(StoreMapZoneFragment.this);
					    	view1.setFocusable(false);
							popupWindow.setContentView(view1);
							popupWindow.showAsDropDown(view);
							popupWindow.setOnDismissListener(new OnDismissListener() {
								
								@Override
								public void onDismiss() {
																	
								}
							});
						}
					});

					gridLayout.addView(view, param);
				}
			}
		}
	}

	protected void disableUserIcons() {
		for(int i=0;i<gridLayout.getChildCount();i++)
		{
			View v=gridLayout.getChildAt(i);
			v.setSelected(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_cancel:
			if(popupWindow!=null && popupWindow.isShowing())
			{
				popupWindow.dismiss();
			}
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(consumer!=null && arg2==0)
		{
			if(popupWindow!=null && popupWindow.isShowing())
			{
				popupWindow.dismiss();
			}
			popupWindow=new PopupWindow(mContext);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
			popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
			View view1=LayoutInflater.from(mContext).inflate(R.layout.layout_customer_details_dialog_view, null);
			view1.findViewById(R.id.dialog_cancel).setOnClickListener(StoreMapZoneFragment.this);
			TextView nameView=(TextView) view1.findViewById(R.id.customer_name);
			TextView emailView=(TextView) view1.findViewById(R.id.customer_email);
			TextView phoneView=(TextView) view1.findViewById(R.id.customer_phone);
			emailView.setText(consumer.getmConsumerEmail());
			nameView.setText(consumer.getmConsumerName());
			phoneView.setText(consumer.getmPhoneNumber());
			popupWindow.setContentView(view1);
			popupWindow.showAsDropDown(view);
			popupWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					disableUserIcons();		
				}
			});
		}
	}

}
