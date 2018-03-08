package com.buyopicadmin.admin.adapters;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.SpinnerAdapter;

import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.models.ReportData;

public class CustomReportSpinnerAdapter implements SpinnerAdapter{
	
	private Context mContext;
	private List<ReportData> mReports;

	public CustomReportSpinnerAdapter(Context mContext,List<ReportData> mReports)
	{
		this.mContext=mContext;
		this.mReports=mReports;
	}
	@Override
	public int getCount() {
		return mReports.size();
	}

	@Override
	public ReportData getItem(int arg0) {
		return mReports.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView=LayoutInflater.from(mContext).inflate(R.layout.layout_report_fragment_child_view, null);
		Utils.overrideFonts(mContext, convertView);
		CheckedTextView checkedTextView=(CheckedTextView) convertView.findViewById(android.R.id.text1);
		ReportData reportData=getItem(position);
		checkedTextView.setText(reportData.getmReportData());
		checkedTextView.setChecked(reportData.isSelected());
		if(position==0)
		{
			checkedTextView.setCheckMarkDrawable(null);
		}
		checkedTextView.setCompoundDrawables(null, null, null, null);
		ImageButton imageButton=(ImageButton) convertView.findViewById(R.id.down_arrow_indicator);
//		checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse_indicator, 0);
		imageButton.setVisibility(View.GONE);
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		convertView=LayoutInflater.from(mContext).inflate(R.layout.layout_report_fragment_child_view, null);
		Utils.overrideFonts(mContext, convertView);
		CheckedTextView checkedTextView=(CheckedTextView) convertView.findViewById(android.R.id.text1);
		ReportData reportData=getItem(position);
		checkedTextView.setText(reportData.getmReportData());
		checkedTextView.setChecked(reportData.isSelected());
		
		if(position==0)
		{
			checkedTextView.setCheckMarkDrawable(null);
		}
		checkedTextView.setCompoundDrawables(null, null, null, null);
		ImageButton imageButton=(ImageButton) convertView.findViewById(R.id.down_arrow_indicator);
//		checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse_indicator, 0);
		imageButton.setVisibility(View.GONE);
		return convertView;
	}

}
