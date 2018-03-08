package com.buyopic.android.network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Typeface;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {

	private static final String TAG_NAME = "BuyOpic";

	public static void showLog(String message) {
		Log.v(TAG_NAME, message);
	}

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	}

	public static void overrideFonts(final Context context, final View v) {
		try {

			if (v instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0; i < vg.getChildCount(); i++) {
					View child = vg.getChildAt(i);
					overrideFonts(context, child);
				}
			} else if (v instanceof Button) {
				Typeface typeFace = Typeface.createFromAsset(
						context.getAssets(), "Tahoma.ttf");
				((Button) v).setTypeface(typeFace);
			} else if (v instanceof TextView) {
				TextView textView = (TextView) v;
				if (textView.getTypeface() != null
						&& textView.getTypeface().getStyle() == Typeface.BOLD) {
					Typeface typeFace = Typeface.createFromAsset(
							context.getAssets(), "tahomabd.ttf");
					textView.setTypeface(typeFace);
				} else {
					Typeface typeFace = Typeface.createFromAsset(
							context.getAssets(), "Tahoma.ttf");
					textView.setTypeface(typeFace);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> getStrings() {
		List<String> stringList = new ArrayList<String>();
		stringList.add("Home");
		stringList.add("Edit Profile");
		stringList.add("Logout");
		return stringList;
	}

	public final static Pattern emailPattern = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
	public static final int RESULT_MAP = 1000;

	public static boolean isValidEmail(String email) {
		return emailPattern.matcher(email.trim()).matches();
	}

	public static String getMinutesSeconds(String time) {/*
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("ss", Locale.US);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss",Locale.US);
		try {
			Date d = dateFormat2.parse(time);
		return simpleDateFormat.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	*/
		long secondsInLong = Long.parseLong(time);

	    long days = (int)TimeUnit.SECONDS.toDays(secondsInLong);        
	    long hours = TimeUnit.SECONDS.toHours(secondsInLong) - (days *24);
	    long minutes = TimeUnit.SECONDS.toMinutes(secondsInLong) - (TimeUnit.SECONDS.toHours(secondsInLong)* 60);
	    long seconds= TimeUnit.SECONDS.toSeconds(secondsInLong) - (TimeUnit.SECONDS.toMinutes(secondsInLong) *60);

//	    System.out.println(days + " Day(s) " + hours + " Hour(s) " + minutes + " Minute(s) " + seconds + " Seconds");
	   return (days > 0)? (days + ":" + hours + ":" + minutes + ":" + seconds) : (hours + ":" + minutes + ":" + seconds);
	
	}

	public static String getHourMinutesSeconds(String time) {/*
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("ss", Locale.US);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss",Locale.US);
		try {
			Date d = dateFormat2.parse(time);
			return simpleDateFormat.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	*/
		
		 
		long secondsInLong = Long.parseLong(time);

    long days = TimeUnit.SECONDS.toDays(secondsInLong);        
    long hours = TimeUnit.SECONDS.toHours(secondsInLong) - (days *24);
    long minutes = TimeUnit.SECONDS.toMinutes(secondsInLong) - (TimeUnit.SECONDS.toHours(secondsInLong)* 60);
    long seconds= TimeUnit.SECONDS.toSeconds(secondsInLong) - (TimeUnit.SECONDS.toMinutes(secondsInLong) *60);

//    System.out.println(days + " Day(s) " + hours + " Hour(s) " + minutes + " Minute(s) " + seconds + " Seconds");
   return (days > 0)? (days + ":" + hours + ":" + minutes + ":" + seconds) : (hours + ":" + minutes + ":" + seconds);

	}

	public static String getTimeZone() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"z",Locale.US);
		return dateFormat.format(new Date());
	}
	public static String  getCountryName(Context context){

	    String CountryID="";

	   TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	          //getSimCountryIso
	    CountryID= manager.getSimCountryIso().toUpperCase();
	    
		return CountryID;
	}
	


		public static  String calculateTime(String timeInSeconds) { 
			long secondsInLong = Long.parseLong(timeInSeconds);
  
        long days = (int)TimeUnit.SECONDS.toDays(secondsInLong);        
        long hours = TimeUnit.SECONDS.toHours(secondsInLong) - (days *24);
        long minutes = TimeUnit.SECONDS.toMinutes(secondsInLong) - (TimeUnit.SECONDS.toHours(secondsInLong)* 60);
        long seconds= TimeUnit.SECONDS.toSeconds(secondsInLong) - (TimeUnit.SECONDS.toMinutes(secondsInLong) *60);

//        System.out.println(days + " Day(s) " + hours + " Hour(s) " + minutes + " Minute(s) " + seconds + " Seconds");
       return (days > 0)? (days + " " + hours + ":" + minutes + ":" + seconds) : (hours + ":" + minutes + ":" + seconds);
  }


}
