package com.buyopicadmin.admin;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.fragments.SlideMenuOptionsFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivityNew;

public class BaseActivity extends SlidingFragmentActivityNew implements
		 OnTouchListener {

	protected ListFragment mFrag;
	private ActionBar actionBar;
	private int mTitleRes;
	private SlidingMenu sm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prepareActionBar();
		setTitle(mTitleRes);
		setBehindContentView(R.layout.menu_frame);
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		sm.setMode(SlidingMenu.LEFT);
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new SlideMenuOptionsFragment())
				.commit();
	}

	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	public BaseActivity() {
	}

	private void prepareActionBar() {
		actionBar = getActionBar();
		View v = LayoutInflater.from(this).inflate(
				R.layout.layout_custom_actionbar, null);
		int gravity = Gravity.CENTER | Gravity.LEFT;
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT, gravity);
		actionBar.setCustomView(v, params);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		actionBar.show();
		View homeIcon = findViewById(android.R.id.home);
		((View) homeIcon.getParent()).setVisibility(View.GONE);
		((View) homeIcon).setVisibility(View.GONE);
	}

	public void setMapActionBar(String title,int screen){

			if (screen == 0 || screen == 1) {
				restrictSlideMovement();
				View v = LayoutInflater.from(this).inflate(
						R.layout.layout_custom_actionbar_map, null);
				Utils.overrideFonts(this, v);
				TextView textView = (TextView) v
						.findViewById(R.id.actionbar_title);
				ImageButton closeButton = (ImageButton) v
						.findViewById(R.id.dialog_cancel);
				closeButton.setOnTouchListener(this);
				textView.setShadowLayer(1, 1, 1, Color.BLACK);
				textView.setText(title);
				ImageView homeButton = (ImageView) v
						.findViewById(R.id.ic_home);
				if(screen!=0)
				{
				homeButton.setOnTouchListener(this);
				}
				ActionBar.LayoutParams params = new ActionBar.LayoutParams(
						ActionBar.LayoutParams.MATCH_PARENT,
						ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);
				actionBar.setCustomView(v, params);
		}

	
	}
	
	public void setBeaconActionBar(String title, int screen) {
		if (actionBar != null) {
			if (screen == 0 || screen == 1) {
				restrictSlideMovement();
				View v = LayoutInflater.from(this).inflate(
						R.layout.layout_custom_actionbar_login, null);
				Utils.overrideFonts(this, v);
				TextView textView = (TextView) v
						.findViewById(R.id.actionbar_title);
				textView.setShadowLayer(1, 1, 1, Color.BLACK);
				textView.setText(title);
				ImageView homeButton = (ImageView) v
						.findViewById(R.id.ic_home);
				if(screen!=0)
				{
				homeButton.setOnTouchListener(this);
				}
				ActionBar.LayoutParams params = new ActionBar.LayoutParams(
						ActionBar.LayoutParams.MATCH_PARENT,
						ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);
				actionBar.setCustomView(v, params);
			} else {
				View v = LayoutInflater.from(this).inflate(
						R.layout.layout_custom_actionbar, null);
				ImageButton imageButton = (ImageButton) v
						.findViewById(R.id.menu_slideButtonbutton);
				imageButton.setOnTouchListener(this);
				ImageButton homeButton = (ImageButton) v
						.findViewById(R.id.ic_home);
				homeButton.setOnTouchListener(this);
				ActionBar.LayoutParams params = new ActionBar.LayoutParams(
						ActionBar.LayoutParams.MATCH_PARENT,
						ActionBar.LayoutParams.MATCH_PARENT);
				actionBar.setCustomView(v, params);
				TextView textView = (TextView) v
						.findViewById(R.id.actionbar_title);
				textView.setText(title);
				textView.setShadowLayer(1, 1, 1, Color.BLACK);
				Utils.overrideFonts(this, v);
				actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
						| ActionBar.DISPLAY_SHOW_HOME);
				actionBar.show();
			}
		}

	}

	public void closeMenu() {
		if (sm != null) {
			sm.showContent(true);
		}
	}
	

	public void restrictSlideMovement() {
		if (sm != null) {
			sm.showContent(false);
		}
	}

	public void showMenu() {
		if (sm != null) {
			sm.showMenu();
		}
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.menu_slideButtonbutton:
				showMenu();
				break;
			case R.id.ic_home:
				startActivity(new Intent(this, HomePageTabActivity.class));
				break;
			case R.id.dialog_cancel:
				onBackPressed();
				break;
			default:
				break;
			}
		}
		return true;
	}

}
