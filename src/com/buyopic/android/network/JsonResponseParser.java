package com.buyopic.android.network;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.buyopicadmin.admin.LoginActivity;
import com.buyopicadmin.admin.models.Alert;
import com.buyopicadmin.admin.models.AlertsResponse;
import com.buyopicadmin.admin.models.ClickThrough;
import com.buyopicadmin.admin.models.Consumer;
import com.buyopicadmin.admin.models.ConsumerInformation;
import com.buyopicadmin.admin.models.FootFallByTimeOfDay;
import com.buyopicadmin.admin.models.FootfallZoneByCustomer;
import com.buyopicadmin.admin.models.FootfallZoneByTime;
import com.buyopicadmin.admin.models.Merchant;
import com.buyopicadmin.admin.models.OrderList;
import com.buyopicadmin.admin.models.StatusList;
import com.buyopicadmin.admin.models.StoreMap;
import com.buyopicadmin.admin.models.TrafficZone;
import com.buyopicadmin.admin.models.Zone;

/*
 * This Class Used to Parse the Json Response
 */

public class JsonResponseParser {

	public static final String TAG_PROCESS_URL = "process_url";
	public static final String TAG_PROCESS_ID = "process_id";
	public static final String TAG_PROCESS_TYPE = "process_type";

	private static final String TAG_CONSUMER_PHONE_NO = "consumer_phone_no";
	private static final String TAG_ZONES_TRAFFIC_BY_TIME = "zones_traffic_by_time";
	private static final String TAG_CONSUMER_FIRST_NAME = "consumer_first_name";
	private static final String STATUS_OK = "ok";
	private static final String TAG_ALERT_THUMBNAIL_URL = "alert_thumbnail_url";
	private static final String TAG_STATUS = "status";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_ASSOCIATE = "associate";
	private static final String TAG_USER = "user";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_MERCHANT_ID = "buyopic_store_associate_id";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_PHONE_NO = "phone_no";
	private static final String TAG_STORE_ID = "store_id";
	private static final String TAG_RETAILER_ID = "retailer_id";

	private static final String TAG_ALERT_ID = "alert_id";
	private static final String TAG_ALERT_TITLE = "title";
	private static final String TAG_ALERT_MESSAGE = "desctiption";
	private static final String TAG_ALERT_DESCRIPTION = "description";
	private static final String TAG_ALERT_PRICE = "price";
	private static final String TAG_ALERT_THUMBNAIL = "image_url";
	private static final String TAG_START_DATE = "start_date";
	private static final String TAG_END_DATE = "end_date";
	private static final String TAG_IS_SPECIAL = "isSpecial";
	private static final String TAG_ALERTS = "alerts";
	private static final String TAG_ALERT = "alert";
	private static final String TAG_STATUS_SUCCESS = STATUS_OK;
	private static final String TAG_STORE_ADDRESS = "store_address";
	private static final String TAG_STORE_IMAGE_URL = "store_image_url";
	private static final String TAG_STORE_NAME = "store_name";
	private static final String TAG_STORE_ADDRESS_IMAGE = "store_address_image";
	private static final String TAG_GOOGLE_ICON_IMAGE = "google_icon_image";
	private static final String TAG_GOOGLE_PLACE_ID = "google_place_id";
	private static final String TAG_STORE_ADDRESS1 = "store_address1";
	private static final String TAG_STORE_ADDRESS2 = "store_address2";
	private static final String TAG_STORE_CITY = "store_city";
	private static final String TAG_STORE_STATE = "store_state";
	private static final String TAG_STORE_POSTAL_CODE = "store_postal_code";

	private static final String TAG_ZONES = "zones";
	private static final String TAG_ZONE_NO = "zone_no";
	private static final String TAG_ZONE_ID = "zone_id";
	private static final String TAG_ZONE_NAME = "zone_name";

	public static final String TAG_TOTAL_TIME = "total_time";
	public static final String TAG_AVERAGE_TIME = "average_time";
	public static final String TAG_CUSTOMERS = "customers";

	// orderslist

	private static final String TAG_ORDERS_LIST = "orders_list";
	private static final String TAG_REQUESTEDDELIVERY = "requestedDelivery";
	private static final String TAG_STATE = "state";
	private static final String TAG_EXPECTEDDELIVERY = "expectedDelivery";
	private static final String TAG_ITEMPRICE = "itemPrice";

	public static final String TAG_ADDRESSLINE3 = "addressLine3";
	public static final String TAG_ADDRESSLINE2 = "addressLine2";
	public static final String TAG_ADDRESSLINE1 = "addressLine1";

	private static final String TAG_TOTALORDERPRICE = "totalOrderPrice";
	private static final String TAG_CONSUMERID = "consumerId";
	private static final String TAG_CITY = "city";
	private static final String TAG_OFFERITEMNAME = "offerItemName";

	public static final String TAG_CONSUMERNAME = "consumerName";
	public static final String TAG_PINCODE = "pincode";
	public static final String TAG_STATUSID = "statusId";
	public static final String TAG_CONSUMERPHONENO="consumerPhoneNo";
	public static final String TAG_CONSUMERMAILID="consumerMailId";
	private static final String TAG_STATUSDESC = "statusDesc";

	public static final String TAG_OFFERITEMID = "offerItemId";
	public static final String TAG_ORDERQUANTITY = "orderQuantity";
	public static final String TAG_ORDERID = "orderId";
	public static final String TAG_STATUS_DESC = "status_desc";
	public static final String TAG_STATUS_ID = "status_id";
	public static final String TAG_STATUS_LIST = "status_list";
	static StatusList statusListObj;
	
	public static final String TAG_CONSUMER_EMAIL_ID="consumer_email";
	public static final String TAG_CONSUMER_PHONE_NUMBER="consumer_phone_number";
	

	public static String parseCreateAlertResponse(String string) {
		try {
			JSONObject jsonObject = new JSONObject(string);
			return jsonObject.has(TAG_MESSAGE) ? jsonObject
					.getString(TAG_MESSAGE) : "Unknown Error";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "Unknown Error";
	}

	public static Merchant parseMerchantLoginResponse(String string) {
		Merchant merchant = null;
		merchant = new Merchant();
		try {
			JSONObject jsonObject2 = new JSONObject(string);

			if (jsonObject2.has(TAG_STATUS)
					&& jsonObject2.getString(TAG_STATUS).equalsIgnoreCase(
							TAG_STATUS_SUCCESS)) {
				if (jsonObject2.has(TAG_ASSOCIATE)) {
					JSONObject jsonObject = jsonObject2
							.getJSONObject(TAG_ASSOCIATE);
					merchant.setEmail(jsonObject.has(TAG_EMAIL) ? jsonObject
							.getString(TAG_EMAIL) : null);
					merchant.setmMerchantId(jsonObject.has(TAG_MERCHANT_ID) ? jsonObject
							.getString(TAG_MERCHANT_ID) : null);
					merchant.setmPhone(jsonObject.has(TAG_PHONE_NO) ? jsonObject
							.getString(TAG_PHONE_NO) : null);
					merchant.setmRetailerId(jsonObject.has(TAG_RETAILER_ID) ? jsonObject
							.getString(TAG_RETAILER_ID) : null);
					merchant.setmStoreId(jsonObject.has(TAG_STORE_ID) ? jsonObject
							.getString(TAG_STORE_ID) : null);
					merchant.setmUserName(jsonObject.has(TAG_USERNAME) ? jsonObject
							.getString(TAG_USERNAME) : null);
				} else if (jsonObject2.has(TAG_USER)) {
					JSONObject jsonObject = jsonObject2.getJSONObject(TAG_USER);
					merchant = new Merchant();
					merchant.setEmail(jsonObject.has(TAG_EMAIL) ? jsonObject
							.getString(TAG_EMAIL) : null);
					merchant.setmMerchantId(jsonObject.has(TAG_MERCHANT_ID) ? jsonObject
							.getString(TAG_MERCHANT_ID) : null);
					merchant.setmPhone(jsonObject.has(TAG_PHONE_NO) ? jsonObject
							.getString(TAG_PHONE_NO) : null);
					merchant.setmRetailerId(jsonObject.has(TAG_RETAILER_ID) ? jsonObject
							.getString(TAG_RETAILER_ID) : null);
					merchant.setmStoreId(jsonObject.has(TAG_STORE_ID) ? jsonObject
							.getString(TAG_STORE_ID) : null);
					merchant.setmUserName(jsonObject.has(TAG_USERNAME) ? jsonObject
							.getString(TAG_USERNAME) : null);
				}

			} else {
				merchant.setStatus(false);
				merchant.setmMessage(jsonObject2.has(TAG_MESSAGE) ? jsonObject2
						.getString(TAG_MESSAGE) : null);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return merchant;

	}

	public static AlertsResponse parseAlertsResponse(String response) {
		AlertsResponse alertsResponse = new AlertsResponse();
		List<Alert> alerts = new ArrayList<Alert>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject.has(TAG_ALERTS)) {
				JSONArray jAlertArrayObj = jsonObject.getJSONArray(TAG_ALERTS);
				if (jAlertArrayObj != null && jAlertArrayObj.length() > 0) {
					for (int i = 0; i < jAlertArrayObj.length(); i++) {
						Alert alert = null;
						JSONObject jAlertObj = jAlertArrayObj.getJSONObject(i);
						alert = parseAlertResponse(jAlertObj);
						alerts.add(alert);
					}
				}
				alertsResponse.setmAlerts(alerts);
			}

			alertsResponse
					.setmStoreAddress(jsonObject.has(TAG_STORE_ADDRESS) ? jsonObject
							.getString(TAG_STORE_ADDRESS) : null);
			alertsResponse
					.setmStoreLogo(jsonObject.has(TAG_STORE_IMAGE_URL) ? jsonObject
							.getString(TAG_STORE_IMAGE_URL) : null);
			alertsResponse
					.setmStoreId(jsonObject.has(TAG_STORE_NAME) ? jsonObject
							.getString(TAG_STORE_NAME) : null);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return alertsResponse;

	}

	public static Alert parseAlertDetailsResponse(String string) {
		Alert alert = null;
		try {
			JSONObject jsonObject = new JSONObject(string);
			if (jsonObject.has(TAG_STATUS)
					&& jsonObject.getString(TAG_STATUS).equalsIgnoreCase(
							TAG_STATUS_SUCCESS)) {
				if (jsonObject.has(TAG_ALERT)) {
					JSONArray jAlertsArrayObj = jsonObject
							.getJSONArray(TAG_ALERT);
					if (jAlertsArrayObj != null && jAlertsArrayObj.length() > 0) {
						alert = parseAlertResponse(jAlertsArrayObj
								.getJSONObject(0));
					}
				}
				if (jsonObject.has(TAG_ZONES)) {
					JSONArray jsonArray = jsonObject.getJSONArray(TAG_ZONES);
					if (jsonArray != null && jsonArray.length() > 0) {
						List<Zone> mZones = new ArrayList<Zone>();
						for (int i = 0; i < jsonArray.length(); i++) {
							Zone zone = new Zone();
							JSONObject zoneObject = jsonArray.getJSONObject(i);
							zone.setmZoneId(zoneObject.has(TAG_ZONE_ID) ? zoneObject
									.getString(TAG_ZONE_ID) : null);
							mZones.add(zone);
						}
						alert.setmZonesList(mZones);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return alert;
	}

	private static Alert parseAlertResponse(JSONObject jAlertObj) {
		Alert alert = new Alert();
		try {
			alert.setmOfferId(jAlertObj.has(TAG_ALERT_ID) ? jAlertObj
					.getString(TAG_ALERT_ID) : null);
			alert.setmOfferTitle(jAlertObj.has(TAG_ALERT_TITLE) ? jAlertObj
					.getString(TAG_ALERT_TITLE) : null);
			if (jAlertObj.has(TAG_ALERT_MESSAGE)) {
				alert.setmOfferMessage(jAlertObj.getString(TAG_ALERT_MESSAGE));
			} else if (jAlertObj.has(TAG_ALERT_DESCRIPTION)) {
				alert.setmOfferMessage(jAlertObj
						.getString(TAG_ALERT_DESCRIPTION));
			} else {
				alert.setmOfferMessage("");
			}
			// new response
			alert.setmAlertStoreAddressImage(jAlertObj
					.has(TAG_STORE_ADDRESS_IMAGE) ? jAlertObj
					.getString(TAG_STORE_ADDRESS_IMAGE) : null);

			alert.setmAlertGoogleIconImage(jAlertObj.has(TAG_GOOGLE_ICON_IMAGE) ? jAlertObj
					.getString(TAG_GOOGLE_ICON_IMAGE) : null);

			alert.setmAlertGooglePlaceId(jAlertObj.has(TAG_GOOGLE_PLACE_ID) ? jAlertObj
					.getString(TAG_GOOGLE_PLACE_ID) : null);

			alert.setmAlertStoreAddress1(jAlertObj.has(TAG_STORE_ADDRESS1) ? jAlertObj
					.getString(TAG_STORE_ADDRESS1) : null);

			alert.setmAlertStoreAddress2(jAlertObj.has(TAG_STORE_ADDRESS2) ? jAlertObj
					.getString(TAG_STORE_ADDRESS2) : null);

			alert.setmAlertStoreCity(jAlertObj.has(TAG_STORE_CITY) ? jAlertObj
					.getString(TAG_STORE_CITY) : null);
			alert.setmAlertStoreCity(jAlertObj.has(TAG_STORE_STATE) ? jAlertObj
					.getString(TAG_STORE_STATE) : null);

			alert.setmAlertStorePostalCode(jAlertObj.has(TAG_STORE_POSTAL_CODE) ? jAlertObj
					.getString(TAG_STORE_POSTAL_CODE) : null);

			DecimalFormat decimalFormat = new DecimalFormat("#0.00");
			String mPrice = decimalFormat.format(Double.valueOf(jAlertObj
					.getString(TAG_ALERT_PRICE)));

			alert.setmPrice(jAlertObj.has(TAG_ALERT_PRICE) ? mPrice : null);
			alert.setmThumbnailUrl(jAlertObj.has(TAG_ALERT_THUMBNAIL) ? jAlertObj
					.getString(TAG_ALERT_THUMBNAIL) : null);
			alert.setmStartDate(jAlertObj.has(TAG_START_DATE) ? jAlertObj
					.getString(TAG_START_DATE) : null);
			alert.setmEndDate(jAlertObj.has(TAG_END_DATE) ? jAlertObj
					.getString(TAG_END_DATE) : null);
			alert.setmIsSpecialOffer(jAlertObj.has(TAG_IS_SPECIAL) ? jAlertObj
					.getString(TAG_IS_SPECIAL).equalsIgnoreCase("true") ? true
					: false : false);
			alert.setmIsActivated(jAlertObj.has(TAG_STATUS) ? jAlertObj
					.getString(TAG_STATUS).equalsIgnoreCase("true") ? true
					: false : false);

			alert.setmImageThumbUrl(jAlertObj.has(TAG_ALERT_THUMBNAIL_URL) ? jAlertObj
					.getString(TAG_ALERT_THUMBNAIL_URL) : null);
			// .getString("zone_id") : null);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return alert;
	}

	public static HashMap<String, String> parsePreprovisioingResponse(
			String string) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(string);
			if (jsonObject.has(TAG_STATUS)) {
				Iterator<?> iterator = jsonObject.keys();
				if (iterator != null) {
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						String value = jsonObject.getString(key);
						hashMap.put(key, value);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return hashMap;
	}

	public static HashMap<String, String> parseProvisioingResponse(String string) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(string);
			if (jsonObject.has(TAG_STATUS)
					&& jsonObject.getString(TAG_STATUS).equalsIgnoreCase(
							TAG_STATUS_SUCCESS)) {
				Iterator<?> iterator = jsonObject.keys();
				if (iterator != null) {
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						String value = jsonObject.getString(key);
						hashMap.put(key, value);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return hashMap;
	}

	public static List<Zone> parseZonesOfStoreInfoResponse(String string) {
		List<Zone> zones = new ArrayList<Zone>();
		try {
			JSONObject jsonObjectZones = new JSONObject(string);
			if (jsonObjectZones.has(TAG_ZONES)) {
				JSONArray jsonArray = jsonObjectZones.getJSONArray(TAG_ZONES);

				if (jsonArray != null && jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						Zone zone = new Zone();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						zone.setmZoneId(jsonObject.has(TAG_ZONE_ID) ? jsonObject
								.getString(TAG_ZONE_ID) : null);
						zone.setmZoneNumber(jsonObject.has(TAG_ZONE_NO) ? jsonObject
								.getString(TAG_ZONE_NO) : null);
						zone.setmZoneName(jsonObject.has(TAG_ZONE_NAME) ? jsonObject
								.getString(TAG_ZONE_NAME) : null);
						zones.add(zone);
					}
				}
				if (zones.isEmpty()) {
					Zone zone = new Zone();
					zone.setmZoneId("NA");
					zone.setmZoneNumber("na");
					zone.setmZoneName("NA");
					zones.add(zone);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zones;
	}

	public static HashMap<String, String> parseMerchantStoreRegisterResponse(
			String response) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.has(TAG_STATUS)) {
				hashMap.put(TAG_STATUS, jsonObject.getString(TAG_STATUS));
			}
			if (jsonObject.has(TAG_MESSAGE)) {
				hashMap.put(TAG_MESSAGE, jsonObject.getString(TAG_MESSAGE));
			}

			if (jsonObject.has(TAG_ASSOCIATE)) {
				JSONObject userObject = jsonObject.getJSONObject(TAG_ASSOCIATE);
				Iterator<?> iterator = userObject.keys();
				if (iterator != null) {
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						String value = userObject.getString(key);
						hashMap.put(key, value);
					}
				}

			}
			if (jsonObject.has(TAG_USER)) {
				JSONObject userObject = jsonObject.getJSONObject(TAG_USER);
				Iterator<?> iterator = userObject.keys();
				if (iterator != null) {
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						String value = userObject.getString(key);
						hashMap.put(key, value);
					}

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashMap;

	}

	public static List<TrafficZone> parseCustomerAverageTrafficZoneResponse(
			String string) {
		List<TrafficZone> trafficZones = null;

		try {
			JSONObject jsonObject = new JSONObject(string);
			if (jsonObject.has(TAG_ZONES)) {
				JSONArray jsonArray = jsonObject.getJSONArray(TAG_ZONES);
				if (jsonArray != null && jsonArray.length() > 0) {
					trafficZones = new ArrayList<TrafficZone>();
					for (int i = 0; i < jsonArray.length(); i++) {
						TrafficZone trafficZone = new TrafficZone();
						JSONObject object = jsonArray.getJSONObject(i);
						trafficZone.setmAvgTimeinZone(object
								.has(TAG_AVERAGE_TIME) ? object
								.getString(TAG_AVERAGE_TIME) : null);
						trafficZone.setmTotalCustomers(object
								.has(TAG_CUSTOMERS) ? object
								.getString(TAG_CUSTOMERS) : null);
						trafficZone.setmTotalTimeInZone(object
								.has(TAG_TOTAL_TIME) ? object
								.getString(TAG_TOTAL_TIME) : null);
						trafficZone
								.setmZoneName(object.has(TAG_ZONE_NAME) ? object
										.getString(TAG_ZONE_NAME) : null);
						trafficZones.add(trafficZone);
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return trafficZones;
	}

	public static final String TAG_ZONE_CONSUMERS = "zone_consumers";
	public static final String TAG_CONSUMER_ID = "consumer_id";
	public static final String TAG_CONSUMER_EMAIL = "email";

	public static List<StoreMap> parseStoreActiveUserResponse(String string) {

		List<StoreMap> storeMaps = new ArrayList<StoreMap>();
		try {
			JSONObject jsonObject = new JSONObject(string);
			if (jsonObject.has(TAG_STATUS)
					&& jsonObject.getString(TAG_STATUS).equalsIgnoreCase(
							STATUS_OK)) {

				if (jsonObject.has(TAG_ZONES)) {
					JSONArray jsonArray = jsonObject.getJSONArray(TAG_ZONES);
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							StoreMap storeMap = new StoreMap();
							JSONObject storeMapObject = jsonArray
									.getJSONObject(i);
							storeMap.setmZoneName(storeMapObject
									.has(TAG_ZONE_NAME) ? storeMapObject
									.getString(TAG_ZONE_NAME) : null);

							if (storeMapObject.has(TAG_ZONE_CONSUMERS)) {
								JSONArray zoneConsumersArray = storeMapObject
										.getJSONArray(TAG_ZONE_CONSUMERS);
								if (zoneConsumersArray != null
										&& zoneConsumersArray.length() > 0) {
									List<Consumer> consumerList = new ArrayList<Consumer>();
									for (int j = 0; j < zoneConsumersArray
											.length(); j++) {
										JSONObject zoneConsumerObject = zoneConsumersArray
												.getJSONObject(j);
										Consumer consumer = new Consumer();
										consumer.setmConsumerId(zoneConsumerObject
												.has(TAG_CONSUMER_ID) ? zoneConsumerObject
												.getString(TAG_CONSUMER_ID)
												: null);
										consumer.setmConsumerEmail(zoneConsumerObject
												.has(TAG_CONSUMER_EMAIL) ? zoneConsumerObject
												.getString(TAG_CONSUMER_EMAIL)
												: null);
										consumer.setmPhoneNumber(zoneConsumerObject
												.has(TAG_PHONE_NO) ? zoneConsumerObject
												.getString(TAG_PHONE_NO) : null);
										consumerList.add(consumer);
									}
									storeMap.setmConsumers(consumerList);
								}
							}
							storeMaps.add(storeMap);
						}
					}

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return storeMaps;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<Integer, StoreMap> parseStoreActiveUserUpdateResponse(
			String string) {
		List<StoreMap> storeMaps = new ArrayList<StoreMap>();
		HashMap<Integer, StoreMap> hashMap = new HashMap<Integer, StoreMap>();
		try {
			JSONObject jsonObject = new JSONObject(string);
			if (jsonObject.has(TAG_STATUS)
					&& jsonObject.getString(TAG_STATUS).equalsIgnoreCase(
							STATUS_OK)) {

				if (jsonObject.has(TAG_CONSUMERS)) {
					JSONArray jsonArray = jsonObject
							.getJSONArray(TAG_CONSUMERS);
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							Consumer consumer = new Consumer();
							JSONObject storeMapObject = jsonArray
									.getJSONObject(i);
							int zoneNumber = storeMapObject.has(TAG_ZONE_NO) ? storeMapObject
									.getInt(TAG_ZONE_NO) : null;
							String zoneName = storeMapObject.has(TAG_ZONE_NAME) ? storeMapObject
									.getString(TAG_ZONE_NAME) : null;
							consumer.setmConsumerId(storeMapObject
									.has(TAG_CONSUMER_ID) ? storeMapObject
									.getString(TAG_CONSUMER_ID) : null);
							consumer.setmConsumerEmail(storeMapObject
									.has(TAG_CONSUMER_EMAIL) ? storeMapObject
									.getString(TAG_CONSUMER_EMAIL) : null);
							consumer.setmConsumerName(storeMapObject
									.has(TAG_CONSUMER_FIRST_NAME) ? storeMapObject
									.getString(TAG_CONSUMER_FIRST_NAME) : null);
							consumer.setmPhoneNumber(storeMapObject
									.has(TAG_CONSUMER_PHONE_NO) ? storeMapObject
									.getString(TAG_CONSUMER_PHONE_NO) : null);
							if (hashMap.get(zoneNumber) == null) {
								StoreMap storeMap = new StoreMap();
								storeMap.setmZoneName(zoneName);
								List<Consumer> consumerList = new ArrayList<Consumer>();
								consumerList.add(consumer);
								storeMap.setmConsumers(consumerList);
								hashMap.put(zoneNumber, storeMap);
							} else {
								StoreMap storeMap = hashMap.get(zoneNumber);
								List<Consumer> consumerList = storeMap
										.getmConsumers();
								consumerList.add(consumer);
								hashMap.put(zoneNumber, storeMap);
							}

						}

					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		/*
		 * if (hashMap != null && !hashMap.isEmpty()) { Iterator<?> iterator =
		 * hashMap.entrySet().iterator(); while (iterator.hasNext()) {
		 * 
		 * @SuppressWarnings("rawtypes") Map.Entry pairs = (Map.Entry)
		 * iterator.next(); StoreMap storeMap = new StoreMap();
		 * storeMap.setmZoneName((String) pairs.getKey());
		 * storeMap.setmConsumers((List<Consumer>) pairs.getValue());
		 * storeMaps.add(storeMap); } }
		 */

		return hashMap;
	}

	@SuppressWarnings("unchecked")
	public static List<StoreMap> parseMallStoreActiveUserResponse(String string) {
		List<StoreMap> storeMaps = new ArrayList<StoreMap>();
		HashMap<String, List<Consumer>> hashMap = new HashMap<String, List<Consumer>>();
		try {
			JSONObject jsonObject = new JSONObject(string);
			if (jsonObject.has(TAG_STATUS)
					&& jsonObject.getString(TAG_STATUS).equalsIgnoreCase(
							STATUS_OK)) {

				if (jsonObject.has(TAG_CONSUMERS)) {
					JSONArray jsonArray = jsonObject
							.getJSONArray(TAG_CONSUMERS);
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							Consumer consumer = new Consumer();
							JSONObject storeMapObject = jsonArray
									.getJSONObject(i);
							String zoneName = storeMapObject.has(TAG_ZONE_NAME) ? storeMapObject
									.getString(TAG_ZONE_NAME) : null;
							consumer.setmConsumerId(storeMapObject
									.has(TAG_CONSUMER_ID) ? storeMapObject
									.getString(TAG_CONSUMER_ID) : null);
							consumer.setmConsumerEmail(storeMapObject
									.has(TAG_CONSUMER_EMAIL) ? storeMapObject
									.getString(TAG_CONSUMER_EMAIL) : null);
							consumer.setmConsumerName(storeMapObject
									.has(TAG_CONSUMER_FIRST_NAME) ? storeMapObject
									.getString(TAG_CONSUMER_FIRST_NAME) : null);
							consumer.setmPhoneNumber(storeMapObject
									.has(TAG_CONSUMER_PHONE_NO) ? storeMapObject
									.getString(TAG_CONSUMER_PHONE_NO) : null);
							if (hashMap.get(zoneName) == null) {
								StoreMap storeMap = new StoreMap();
								storeMap.setmZoneName(zoneName);
								List<Consumer> consumerList = new ArrayList<Consumer>();
								consumerList.add(consumer);
								storeMap.setmConsumers(consumerList);
								hashMap.put(zoneName, consumerList);
							} else {
								List<Consumer> consumerList = hashMap
										.get(zoneName);
								consumerList.add(consumer);
								hashMap.put(zoneName, consumerList);
							}

						}

					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (hashMap != null && !hashMap.isEmpty()) {
			Iterator<?> iterator = hashMap.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) iterator.next();
				StoreMap storeMap = new StoreMap();
				storeMap.setmZoneName((String) pairs.getKey());
				storeMap.setmConsumers((List<Consumer>) pairs.getValue());
				storeMaps.add(storeMap);
			}
		}

		return storeMaps;
	}

	public static final String TAG_CONSUMERS = "consumers";

	public static List<FootfallZoneByCustomer> parseFootFallZoneByCustomerResponse(
			String response) {
		List<FootfallZoneByCustomer> footfallZoneByCustomers = new ArrayList<FootfallZoneByCustomer>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.has(TAG_CONSUMERS)) {
				JSONArray jsonArray = jsonObject.getJSONArray(TAG_CONSUMERS);
				if (jsonArray != null && jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						FootfallZoneByCustomer byCustomer = new FootfallZoneByCustomer();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						byCustomer.setmCustomerId(jsonObject2
								.has(TAG_CONSUMER_FIRST_NAME) ? jsonObject2
								.getString(TAG_CONSUMER_FIRST_NAME) : null);
						byCustomer.setmTotalTime(jsonObject2
								.has(TAG_TOTAL_TIME) ? jsonObject2
								.getString(TAG_TOTAL_TIME) : null);
						byCustomer
								.setmZone(jsonObject2.has(TAG_ZONE_NAME) ? jsonObject2
										.getString(TAG_ZONE_NAME) : null);
						byCustomer
						.setmCustomerEmailId(jsonObject2.has(TAG_CONSUMER_EMAIL_ID) ? jsonObject2
								.getString(TAG_CONSUMER_EMAIL_ID) : null);
						byCustomer
						.setmCustomerPhoneNumber(jsonObject2.has(TAG_CONSUMER_PHONE_NUMBER) ? jsonObject2
								.getString(TAG_CONSUMER_PHONE_NUMBER) : null);
						footfallZoneByCustomers.add(byCustomer);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return footfallZoneByCustomers;
	}

	public static List<OrderList> parseOrderListStatus(String response) {
		List<OrderList> orderStatusList = new ArrayList<OrderList>();

		try {
			JSONObject jsonObject = new JSONObject(response);

			if (jsonObject.has(TAG_ORDERS_LIST)) {
				JSONArray jsonArray = jsonObject.getJSONArray(TAG_ORDERS_LIST);
				if (jsonArray != null && jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						OrderList mOrderListObj = new OrderList();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						mOrderListObj.setRequestedDelivery(jsonObject2
								.has(TAG_REQUESTEDDELIVERY) ? jsonObject2
								.getString(TAG_REQUESTEDDELIVERY) : null);
						mOrderListObj
								.setState(jsonObject2.has(TAG_STATE) ? jsonObject2
										.getString(TAG_STATE) : null);
						mOrderListObj.setExpectedDelivery(jsonObject2
								.has(TAG_EXPECTEDDELIVERY) ? jsonObject2
								.getString(TAG_EXPECTEDDELIVERY) : null);
						mOrderListObj.setItemPrice(jsonObject
								.has(TAG_ITEMPRICE) ? jsonObject2
								.getString(TAG_ITEMPRICE) : null);
						mOrderListObj.setAddressLine3(jsonObject2
								.has(TAG_ADDRESSLINE3) ? jsonObject2
								.getString(TAG_ADDRESSLINE3) : null);
						mOrderListObj.setAddressLine2(jsonObject2
								.has(TAG_ADDRESSLINE2) ? jsonObject2
								.getString(TAG_ADDRESSLINE2) : null);
						mOrderListObj.setAddressLine1(jsonObject2
								.has(TAG_ADDRESSLINE1) ? jsonObject2
								.getString(TAG_ADDRESSLINE1) : null);
						mOrderListObj.setTotalOrderPrice(jsonObject2
								.has(TAG_TOTALORDERPRICE) ? jsonObject2
								.getString(TAG_TOTALORDERPRICE) : null);
						mOrderListObj.setConsumerId(jsonObject2
								.has(TAG_CONSUMER_ID) ? jsonObject2
								.getString(TAG_CONSUMER_ID) : null);
						mOrderListObj
								.setCity(jsonObject2.has(TAG_CITY) ? jsonObject2
										.getString(TAG_CITY) : null);
						mOrderListObj.setmConsumerMailId(jsonObject2
								.has(TAG_CONSUMERMAILID) ? jsonObject2
								.getString(TAG_CONSUMERMAILID) : null);
						mOrderListObj.setOfferItemName(jsonObject2
								.has(TAG_OFFERITEMNAME) ? jsonObject2
								.getString(TAG_OFFERITEMNAME) : null);
						mOrderListObj.setConsumerName(jsonObject2
								.has(TAG_CONSUMERNAME) ? jsonObject2
								.getString(TAG_CONSUMERNAME) : null);
						mOrderListObj
								.setCity(jsonObject2.has(TAG_CITY) ? jsonObject2
										.getString(TAG_CITY) : null);
						mOrderListObj
								.setPincode(jsonObject2.has(TAG_PINCODE) ? jsonObject2
										.getString(TAG_PINCODE) : null);
						mOrderListObj.setmConsumerPhoneNo(jsonObject2
								.has(TAG_CONSUMERPHONENO) ? jsonObject2
								.getString(TAG_CONSUMERPHONENO) : null);
						
						mOrderListObj
								.setStatusId(jsonObject2.has(TAG_STATUSID) ? jsonObject2
										.getString(TAG_STATUSID) : null);
						mOrderListObj.setStatusDesc(jsonObject2
								.has(TAG_STATUSDESC) ? jsonObject2
								.getString(TAG_STATUSDESC) : null);
						mOrderListObj.setOfferItemId(jsonObject2
								.has(TAG_OFFERITEMID) ? jsonObject2
								.getString(TAG_OFFERITEMID) : null);
						mOrderListObj.setOrderQuantity(jsonObject2
								.has(TAG_ORDERQUANTITY) ? jsonObject2
								.getString(TAG_ORDERQUANTITY) : null);
						mOrderListObj
								.setOrderId(jsonObject2.has(TAG_ORDERID) ? jsonObject2
										.getString(TAG_ORDERID) : null);
						orderStatusList.add(mOrderListObj);
					}
				}
			}

			statusListObj = new StatusList();
			HashMap<String, String> mHashMap = new LinkedHashMap<String, String>();
			if (jsonObject.has(TAG_STATUS_LIST)) {
				JSONArray jsonArray = jsonObject.getJSONArray(TAG_STATUS_LIST);
				if (jsonArray != null && jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						statusListObj.setStatusDesc(jsonObject2
								.has(TAG_STATUS_DESC) ? jsonObject2
								.getString(TAG_STATUS_DESC) : null);
						statusListObj.setStatusId(jsonObject2
								.has(TAG_STATUS_ID) ? jsonObject2
								.getString(TAG_STATUS_ID) : null);

						LoginActivity.mStatusHashMap.put(
								jsonObject2.has(TAG_STATUS_ID) ? jsonObject2
										.getString(TAG_STATUS_ID) : null,
								jsonObject2.has(TAG_STATUS_DESC) ? jsonObject2
										.getString(TAG_STATUS_DESC) : null);
						LoginActivity.statusOrderList.add(statusListObj);
					}
				}

			}
			/*
			 * BuyOpic buyOpic; buyOpic = new BuyOpic(); HashSet<StatusList>
			 * statusSet=new HashSet<StatusList>(statusOrderList); statusSet
			 * buyOpic.setmStringSet(statusSet);
			 */

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return orderStatusList;
	}

	public static final String TAG_TIME_INTERVAL = "time_interval";

	public static List<FootfallZoneByTime> parseFootFallZoneByTimeResponse(
			String response) {
		List<FootfallZoneByTime> footfallZoneByCustomers = new ArrayList<FootfallZoneByTime>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.has(TAG_ZONES_TRAFFIC_BY_TIME)) {
				JSONArray jsonArray = jsonObject
						.getJSONArray(TAG_ZONES_TRAFFIC_BY_TIME);
				if (jsonArray != null && jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						FootfallZoneByTime byZone = new FootfallZoneByTime();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						byZone.setmTimeofDay(jsonObject2.has(TAG_TIME_INTERVAL) ? jsonObject2
								.getString(TAG_TIME_INTERVAL) : null);
						byZone.setmTotalTime(jsonObject2.has(TAG_TOTAL_TIME) ? jsonObject2
								.getString(TAG_TOTAL_TIME) : null);
						byZone.setmZoneName(jsonObject2.has(TAG_ZONE_NAME) ? jsonObject2
								.getString(TAG_ZONE_NAME) : null);
						footfallZoneByCustomers.add(byZone);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return footfallZoneByCustomers;

	}

	public static List<ConsumerInformation> parseFootTrafficByConsumerResponse(
			String response) {
		List<ConsumerInformation> consumersList = new ArrayList<ConsumerInformation>();
		ConsumerInformation mConsumerInfoObj=new ConsumerInformation();
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.has(TAG_STATUS)
					&& jsonObject.getString(TAG_STATUS).equalsIgnoreCase(
							STATUS_OK)) {
				if (jsonObject.has(TAG_CONSUMERS)) {
					JSONArray jsonArray = jsonObject
							.getJSONArray(TAG_CONSUMERS);
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
							if (jsonObject2.has("consumer_email")) {
								String s = new String(
										jsonObject2
												.getString(TAG_CONSUMER_FIRST_NAME));
								mConsumerInfoObj.setmConsumerName(s);
								mConsumerInfoObj.setmConsumerEmailId(jsonObject2
												.getString("consumer_email"));
								mConsumerInfoObj.setmConsumerId(jsonObject2
										.getString("consumer_id"));
								mConsumerInfoObj.setmConsumerPhoneNum(jsonObject2
										.getString("consumer_phone_number"));
								consumersList.add(mConsumerInfoObj);
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return consumersList;

	}

	public static List<FootFallByTimeOfDay> parseFootTrafficByTimeResponse(
			String response) {
		List<FootFallByTimeOfDay> stringList = new ArrayList<FootFallByTimeOfDay>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.has(TAG_STATUS)
					&& jsonObject.getString(TAG_STATUS).equalsIgnoreCase(
							STATUS_OK)) {
				if (jsonObject.has("time_intervals")) {
					JSONArray jsonArray = jsonObject
							.getJSONArray("time_intervals");
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
							Iterator<?> iterator = jsonObject2.keys();
							while (iterator.hasNext()) {
								FootFallByTimeOfDay byTimeOfDay = new FootFallByTimeOfDay();
								String key = (String) iterator.next();
								byTimeOfDay.setmTimeOfDay(key);
								JSONArray jsonArray2 = jsonObject2
										.getJSONArray(key);
								byTimeOfDay.setmCustomerId("");
								if (jsonArray2 != null
										&& jsonArray2.length() > 0) {
									for (int j = 0; j < jsonArray2.length(); j++) {
										JSONObject jsonObject3 = jsonArray2
												.getJSONObject(j);
										FootFallByTimeOfDay byTimeOfDay2 = new FootFallByTimeOfDay();
										if (j == 0) {
											byTimeOfDay2
													.setmCustomerId(jsonObject3
															.has(TAG_CONSUMER_FIRST_NAME) ? jsonObject3
															.getString(TAG_CONSUMER_FIRST_NAME)
															: null);
											// byTimeOfDay2.setmCustomerId(jsonObject3.has("consumer_email")?jsonObject3.getString("consumer_email"):null);
											byTimeOfDay2
													.setmTimeOfDay(jsonObject3
															.has("time_interval") ? jsonObject3
															.getString("time_interval")
															: null);
											byTimeOfDay2.setmCustomerEmailId(jsonObject3
															.has(TAG_CONSUMER_EMAIL_ID) ? jsonObject3
															.getString(TAG_CONSUMER_EMAIL_ID)
															: null);
											byTimeOfDay2.setmCustomerPhoneNum(jsonObject3
													.has(TAG_CONSUMER_PHONE_NUMBER) ? jsonObject3
													.getString(TAG_CONSUMER_PHONE_NUMBER)
													: null);
											stringList.add(byTimeOfDay2);
										} else {
											byTimeOfDay2.setmTimeOfDay("");
											byTimeOfDay2
													.setmCustomerId(jsonObject3
															.has(TAG_CONSUMER_FIRST_NAME) ? jsonObject3
															.getString(TAG_CONSUMER_FIRST_NAME)
															: null);
											byTimeOfDay2.setmCustomerEmailId(jsonObject3
													.has(TAG_CONSUMER_EMAIL_ID) ? jsonObject3
													.getString(TAG_CONSUMER_EMAIL_ID)
													: null);
									byTimeOfDay2.setmCustomerPhoneNum(jsonObject3
											.has(TAG_CONSUMER_PHONE_NUMBER) ? jsonObject3
											.getString(TAG_CONSUMER_PHONE_NUMBER)
											: null);
											stringList.add(byTimeOfDay2);
										}
									}
								} else {
									stringList.add(byTimeOfDay);
								}
								// stringList.add(byTimeOfDay);
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stringList;

	}

	/*
	 * public static List<FootFallByTimeOfDay>
	 * parseFootTrafficByTimeResponse(String response) {
	 * List<FootFallByTimeOfDay> stringList=new
	 * ArrayList<FootFallByTimeOfDay>(); try { JSONObject jsonObject=new
	 * JSONObject(response); if(jsonObject.has(TAG_STATUS) &&
	 * jsonObject.getString(TAG_STATUS).equalsIgnoreCase("ok")) {
	 * if(jsonObject.has("time_intervals")) { JSONArray
	 * jsonArray=jsonObject.getJSONArray("time_intervals"); if(jsonArray!=null
	 * && jsonArray.length()>0) { for(int i=0;i<jsonArray.length();i++) {
	 * JSONObject jsonObject2=jsonArray.getJSONObject(i); FootFallByTimeOfDay
	 * byTimeOfDay=new FootFallByTimeOfDay();
	 * byTimeOfDay.setmCustomerId(jsonObject2
	 * .has("consumer_first_name")?jsonObject2
	 * .getString("consumer_first_name"):null);
	 * byTimeOfDay.setmTimeOfDay(jsonObject2
	 * .has("time_interval")?jsonObject2.getString("time_interval"):null);
	 * stringList.add(byTimeOfDay); } } } } } catch (JSONException e) {
	 * e.printStackTrace(); } return stringList;
	 * 
	 * }
	 */
	public static List<ClickThrough> parseClickThroughResponse(String response) {
		List<ClickThrough> clickThroughDetails = null;
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.has(TAG_STATUS)
					&& jsonObject.getString(TAG_STATUS).equalsIgnoreCase(
							STATUS_OK)) {
				if (jsonObject.has(TAG_CONSUMERS)) {
					clickThroughDetails = new ArrayList<ClickThrough>();
					JSONArray jsonArray = jsonObject
							.getJSONArray(TAG_CONSUMERS);
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							ClickThrough clickThrough = new ClickThrough();
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
							clickThrough.setmCustomerIdView(jsonObject2
									.has(TAG_CONSUMER_FIRST_NAME) ? jsonObject2
									.getString(TAG_CONSUMER_FIRST_NAME) : null);
							clickThrough.setmDetailPage(jsonObject2
									.has("details_page_clicks") ? jsonObject2
									.getString("details_page_clicks") : null);
							clickThrough.setmMerchantPage(jsonObject2
									.has("merchant_page_clicks") ? jsonObject2
									.getString("merchant_page_clicks") : null);
							clickThrough.setmTopLevelListing(jsonObject2
									.has("closeby_page_clicks") ? jsonObject2
									.getString("closeby_page_clicks") : null);
							clickThrough.setmCustomerEmailId(jsonObject2
									.has(TAG_CONSUMER_EMAIL_ID) ? jsonObject2
									.getString(TAG_CONSUMER_EMAIL_ID) : null);
							clickThrough.setmCustomerPhoneNumber(jsonObject2
									.has(TAG_CONSUMER_PHONE_NUMBER) ? jsonObject2
									.getString(TAG_CONSUMER_PHONE_NUMBER) : null);
							clickThroughDetails.add(clickThrough);
						}
					}
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clickThroughDetails;

	}

	public static HashMap<String, String> parseUpdateOrderStatus(String object) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(object);
			if (jsonObject.has(TAG_STATUS)) {
				hashMap.put(TAG_STATUS, jsonObject.getString(TAG_STATUS));
			}
			if (jsonObject.has(TAG_MESSAGE)) {
				hashMap.put(TAG_MESSAGE, jsonObject.getString(TAG_MESSAGE));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashMap;
	}
}
