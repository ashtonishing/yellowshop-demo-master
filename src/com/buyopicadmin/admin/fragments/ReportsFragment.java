package com.buyopicadmin.admin.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.adapters.CustomReportSpinnerAdapter;
import com.buyopicadmin.admin.models.ReportData;

public class ReportsFragment extends Fragment implements
		OnItemSelectedListener, OnClickListener, OnTouchListener {

	private Spinner spinner;
	private onBuyOpicItemClickListener buyOpicItemClickListener;
	private Context mContext;
	private ImageButton dropDownArrowButton;
	private ArrayList<ReportData> mReports;
	private boolean shouldRedirect = false;
	private BuyOpic buyOpic;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		buyOpic = (BuyOpic) mContext.getApplicationContext();
		String merchantRole = buyOpic.getHashMap().get("store_tier");

		List<String> stringList = new ArrayList<String>();
		stringList.add("Select Report");
		mReports = new ArrayList<ReportData>();
		mReports.add(new ReportData("Select Report", false));
		if (merchantRole != null && merchantRole.equalsIgnoreCase("standard")) {
			mReports.add(new ReportData("Foot Traffic By Consumer", false));
			mReports.add(new ReportData("Foot Traffic By Time-of-day", false));
			mReports.add(new ReportData("Click-Through Details", false));
		} else {

			mReports.add(new ReportData("Foot Traffic By Consumer", false));
			mReports.add(new ReportData("Foot Traffic By Time-of-day", false));
			mReports.add(new ReportData("Click-Through Details", false));
			mReports.add(new ReportData("Dwell Time By Consumer", false));
			mReports.add(new ReportData("Dwell Time By Time-of-day", false));
			mReports.add(new ReportData("Average Daily Dwell Time", false));
		}
		/*
		 * spinner.setAdapter(new ArrayAdapter<String>(getActivity(),
		 * R.layout.layout_report_fragment_child_view, android.R.id.text1,
		 * stringList));
		 */
		spinner.setOnItemSelectedListener(this);
		if(mReports!=null && !mReports.isEmpty())
		{
		spinner.setAdapter(new CustomReportSpinnerAdapter(mContext, mReports));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
		buyOpicItemClickListener = (onBuyOpicItemClickListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_reports_fragment, null);
		Utils.overrideFonts(mContext, view);
		spinner = (Spinner) view.findViewById(R.id.layout_reports_spinner);

		spinner.setOnTouchListener(this);
		dropDownArrowButton = (ImageButton) view
				.findViewById(R.id.layout_reports_spinner_down_btn);
		dropDownArrowButton.setOnClickListener(this);

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		buyOpic.setmSelectedReportPos(spinner.getSelectedItemPosition());
	}

	@Override
	public void onResume() {
		super.onResume();
		shouldRedirect = false;
	//	spinner.setSelection(buyOpic.getmSelectedReportPos());
	  spinner.setSelection(0);
	}

	private void disableSelection() {
		if (mReports != null && !mReports.isEmpty()) {
			for (ReportData reportData : mReports) {
				reportData.setSelected(false);
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		disableSelection();
		ReportData reportData = (ReportData) spinner.getAdapter().getItem(arg2);
		reportData.setSelected(true);

		LinearLayout layout = (LinearLayout) arg0.getChildAt(0);
		if (layout != null) {
			layout.setBackgroundColor(Color.TRANSPARENT);
			CheckedTextView checkedTextView = (CheckedTextView) layout
					.findViewById(android.R.id.text1);
			checkedTextView.setText("Report:\t" + reportData.getmReportData());
			checkedTextView.setCheckMarkDrawable(null);
			checkedTextView.setShadowLayer(1, 1, 1, Color.BLACK);
			ImageButton imageButton = (ImageButton) layout
					.findViewById(R.id.down_arrow_indicator);
			imageButton.setVisibility(View.VISIBLE);
		}

		if (shouldRedirect) {
			switch (arg2) {
			case 1:
				buyOpicItemClickListener.onItemClicked(100);
				break;
			case 2:
				buyOpicItemClickListener.onItemClicked(101);
				break;
			case 3:
				buyOpicItemClickListener.onItemClicked(102);
				break;
			case 4:
				buyOpicItemClickListener.onItemClicked(103);
				break;
			case 5:
				buyOpicItemClickListener.onItemClicked(104);
				break;
			case 6:
				buyOpicItemClickListener.onItemClicked(105);
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_reports_spinner_down_btn:
			spinner.performClick();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		shouldRedirect = true;
		spinner.performClick();
		return true;
	}

}
