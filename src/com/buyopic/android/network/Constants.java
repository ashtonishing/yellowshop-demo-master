package com.buyopic.android.network;

public class Constants {

	/*
	 * Response Code Constants
	 */
	public static int SUCCESS = 100;
	public static int FAILURE = 101;
	public static String CURRENCYSYMBOL="";

	/*
	 * Network Call Request Constants
	 */

	public static final int REQUEST_CREATE_ALERT=1001;
	public static final int REQUEST_LOGIN=1002;
	public static final int REQUEST_REGISTER=1003;
	public static final int REQUEST_MERCHANT_ALERTS=1004;
	public static final int REQUEST_PRE_PROVISION_ING=1005;
	public static final int REQUEST_PROVISION_ING=1006;
	public static final int REQUEST_EDIT_ALERT=1007;
	public static final int REQUEST_BEACON_SETUP=1008;
	public static final int REQUEST_MERCHANT_SETUP=1009;
	public static final int REQUEST_MERCHANT_UPDATE=1011;
	public static final int REQUEST_ZONES=1010;
	public static final int REQUEST_CUSTOMER_AVERAGE_TRAFFIC=1011;
	public static final int REQUEST_STORE_ACTIVE_CONSUMERS=1012;
	public static final int REQUEST_FOOT_FALL_ZONE_BY_CUSTOMER=1013;
	public static final int REQUEST_UPDATE_ZONES_INFO=1014;
	public static final int REQUEST_FOOT_FALL_ZONE_BY_TIME=1015;
	public static final int REQUEST_FOOT_TRAFFIC_BY_CONSUMER=1016;
	public static final int REQUEST_FOOT_TRAFFIC_TIME_OF_DAY=1017;
	public static final int REQUEST_DWELL_TIME_BY_CONSUMER=1018;
	public static final int REQUEST_DWELL_TIME_BY_TIME_OF_DAY=1019;
	public static final int REQUEST_AVERAGE_DAILY_DWELL_TIME=1020;
	public static final int REQUEST_CLICK_THROUGH_DETAILS=1021;
	public static final int REQUEST_MERCHANT_CONFIRMATION=1022;
	public static final int REQUEST_RESET_PASSWORD=1023;
	public static final int REQUEST_URL_PROCESS_DETAILS= 1024;;
	
	public static final String CUSTOM_ACTION_INTENT = "com.buyopic.android";
	public static final String PROCESS_TYPE_REGISTRATION = "registration";
	public static final int REQUEST_SEND_MAIL = 1025;
	public static final int REQUEST_AVERAGE_DAILY_DWELL_TIME_CONSUMERS = 1025;
	public static final int REQUEST_AVERAGE_DAILY_DWELL_AVG_TIME = 1026;
	public static final int REQUEST_GET_ORDERSTATUS = 1027;
	public static final int REQUEST_UPDATE_ORDERSTATUS = 1028;
	public static final int REQUEST_FOOT_TRAFFIC_BY_CONSUMER_EMAIL = 1029;
	public static final int REQUEST_FOOT_TRAFFIC_BY_TIME_OF_DAY_EMAIL =1030;
	public static final int REQUEST_GET_CLICK_THROUGH_DETAIL_EMAIL = 1031;
	public static final int REQUEST_GET_DWELL_TIME_BY_CONSUMER_EMAIL = 1032;
	public static final int REQUEST_GET_DWELL_TIME_BY_TIMEOF_DAY_EMAIL = 1033;
	public static final int REQUEST_GET_AVERAGE_DAILY_DWELL_TIME_EMAIL = 1034;
	public static final int DELETE_OFFER = 1035;
	
}
