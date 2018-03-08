package com.buyopic.android.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.Uri;

public class BuyopicNetworkServiceManager {

	private static final String REQUEST_PARAM_REQUESTFROMBUYOPIC = "requestfrombuyopic";

	private static final String REQUEST_PARAM_PROCESS_ID = "process_id";
	private static final String REQUEST_URL_PROCESSURL = "processurl/yellowshop";
	private static final String REQUEST_PARAM_PROCESS_URL = "process_url";
	private static final String REQUEST_PARAM_PROCESS_TYPE = "process_type";
	private static final String REQUEST_URL_SENDMAIL = "sendMailService";
	

//	public final String HTTPS_BASE_URL = "https://cloud-ml.herokuapp.com/";
//	public final static String BASE_URL = "http://cloud-ml.herokuapp.com/";
	
	
	private static final String REQUEST_URL_GETSTOREORDERS = "getStoreOrders";
	private static final String REQUEST_URL_UPDATESTOREORDERSTATUS = "updateStoreOrderStatus";

	
	public final String HTTPS_BASE_URL = "https://cloud-ml-client-demo-2.herokuapp.com/";
	public static final String BASE_URL = "http://cloud-ml-client-demo-2.herokuapp.com/";
	

 //public final String HTTPS_BASE_URL = "https://prod-yellow.herokuapp.com/";
 //public final static String BASE_URL = "http://prod-yellow.herokuapp.com/";
	
	
	
	
	public static final String TAG = BuyopicNetworkServiceManager.class
			.getSimpleName();
	private static final String MSG_YES = "Yes";
	private ExecutorService executorService;
	static BuyopicNetworkServiceManager buyopicNetworkServiceManager = null;

	public static BuyopicNetworkServiceManager getInstance(Context context) {

		return buyopicNetworkServiceManager == null ? new BuyopicNetworkServiceManager(
				context) : buyopicNetworkServiceManager;
	}
	public BuyopicNetworkServiceManager(Context context) {
		executorService = Executors.newFixedThreadPool(2);
	}

	public void sendCreateOffersDataRequest(final int requestCode,
			final String mAlertId, boolean isInEditMode, final String mStoreId,
			final String mRetailerId, final String mOfferTitle,
			final String mOfferDescription, final String isSpecialOffer,
			final String mStartDate, final String mEndDate, String mZones,
			String merchantId, final String mPrice, final String status,
			final String imageName, final String encodedImageString,String mThumbnailPath,
			BuyopicNetworkCallBack callBack) {
		final String url;
		if (!isInEditMode) {
			url = BASE_URL + "createStoreAlert";
		} else {
			url = BASE_URL + "updatestorealert";
		}
		Utils.showLog("Request Url-->" + url);

		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (isInEditMode) {
			nameValuePairs.add(new BasicNameValuePair("alert_id", mAlertId));
			nameValuePairs.add(new BasicNameValuePair("alert_thumbnail_url", mThumbnailPath));
		}
		nameValuePairs.add(new BasicNameValuePair("store_id", mStoreId));
		nameValuePairs.add(new BasicNameValuePair("retailer_id", mRetailerId));
		nameValuePairs.add(new BasicNameValuePair("alert_title", mOfferTitle));
		nameValuePairs.add(new BasicNameValuePair("alert_message",
				mOfferDescription));
		nameValuePairs.add(new BasicNameValuePair("offer_of_theday", String
				.valueOf(isSpecialOffer)));
		nameValuePairs.add(new BasicNameValuePair("start_date", mStartDate));
		nameValuePairs.add(new BasicNameValuePair("end_date", mEndDate));
		nameValuePairs.add(new BasicNameValuePair("zone_id", mZones));
		nameValuePairs.add(new BasicNameValuePair("alert_created_user_id",
				merchantId));
		nameValuePairs.add(new BasicNameValuePair("status", String
				.valueOf(status)));
		nameValuePairs.add(new BasicNameValuePair("price", mPrice));
		nameValuePairs.add(new BasicNameValuePair("image_name", imageName));
		nameValuePairs.add(new BasicNameValuePair("image_file",
				encodedImageString));

		for (NameValuePair nameValuePair : nameValuePairs) {
			Utils.showLog("Key:" + nameValuePair.getName() + "   ,Value:"
					+ nameValuePair.getValue());
		}

		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);
			}
		});
	}

	public void sendMerchantLoginRequest(final int requestCode,
			final String mConsumerEmail, String mConsumerPassword,
			BuyopicNetworkCallBack callBack) {
		final String url = HTTPS_BASE_URL+ "associatelogin";
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email_id", mConsumerEmail));
		nameValuePairs
				.add(new BasicNameValuePair("password", mConsumerPassword));
		final String totalurl = String.format(url, mConsumerEmail,
				mConsumerPassword);
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});
	}

	public void sendMerchantRegistrationRequest(final int requestCode,
			final String mMerchantEmail, String mMerchantPassword,
			String mRetailerId, String mStoreId, BuyopicNetworkCallBack callBack) {
		final String url = HTTPS_BASE_URL + "associateregistration";

		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("retailer_id", mRetailerId));
		nameValuePairs.add(new BasicNameValuePair("store_id", mStoreId));
		nameValuePairs.add(new BasicNameValuePair("email_id", mMerchantEmail));
		nameValuePairs
				.add(new BasicNameValuePair("password", mMerchantPassword));
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});
	}

	public void sendMerchantStoreRegistrationRequest(final int requestCode,
			String merchantId, boolean isFromUpdate, String mStoreId,
			String mRetailerId, final String mMerchantEmail,
			String mMerchantPassword, final String mStoreName,
			final String mStoreAddress, final String mCity,
			final String mState, final String mLat, final String mLong,
			final String imageName, final String imageFile,final String mRole,String mPhoneNumber,
			final String addressImageName,
			final String addressImageFile,
			final String googlePlaceId,
			final String googleIconImage,
			final String storeAddress2,
			final String storePostalCode,
			
			BuyopicNetworkCallBack callBack) {

		final String url;
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (!isFromUpdate) {
			url = HTTPS_BASE_URL + "associateregistration";
		} else {
			url = HTTPS_BASE_URL+ "updateassociateaccount";
			nameValuePairs.add(new BasicNameValuePair("store_id", mStoreId));
			nameValuePairs.add(new BasicNameValuePair("retailer_id",
					mRetailerId));
			nameValuePairs.add(new BasicNameValuePair(
					"buyopic_store_associate_id", merchantId));
			nameValuePairs.add(new BasicNameValuePair("store_tier", mRole));
		}

		nameValuePairs.add(new BasicNameValuePair("email_id", mMerchantEmail));
		nameValuePairs
				.add(new BasicNameValuePair("password", mMerchantPassword));
		nameValuePairs.add(new BasicNameValuePair("store_name", mStoreName));
		nameValuePairs.add(new BasicNameValuePair("store_address",
				mStoreAddress));
		nameValuePairs.add(new BasicNameValuePair("store_city", mCity));
		nameValuePairs.add(new BasicNameValuePair("store_state", mState));
		nameValuePairs.add(new BasicNameValuePair("latitude", mLat));
		nameValuePairs.add(new BasicNameValuePair("longitude", mLong));
		nameValuePairs.add(new BasicNameValuePair("image_name", imageName));
		nameValuePairs.add(new BasicNameValuePair("image_file", imageFile));
		nameValuePairs.add(new BasicNameValuePair("phone_no", mPhoneNumber));
		nameValuePairs.add(new BasicNameValuePair("address_image_name", addressImageName));
		nameValuePairs.add(new BasicNameValuePair("address_image_file", addressImageFile));
		nameValuePairs.add(new BasicNameValuePair("google_place_id", googlePlaceId));
		nameValuePairs.add(new BasicNameValuePair("google_icon_image", googleIconImage));
		nameValuePairs.add(new BasicNameValuePair("store_address2", storeAddress2));
		nameValuePairs.add(new BasicNameValuePair("store_postal_code", storePostalCode));
		

		for (NameValuePair nameValuePair : nameValuePairs) {
			Utils.showLog("Key:" + nameValuePair.getName() + "   ,Value:"
					+ nameValuePair.getValue());
		}

		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});
	}

	public void sendMerchantOfferListRequest(int requestCode, String storeId,
			String retailerId, String merchantId,
			BuyopicNetworkCallBack callback) {
		final String url = Uri.parse(BASE_URL + "storealertslist").buildUpon()
				.appendQueryParameter("store_id", storeId)
				.appendQueryParameter("retailer_id", retailerId)
				.appendQueryParameter("associate_id", merchantId).build()
				.toString();
		Utils.showLog("Request-->" + url);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callback);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.getRequestData(url, buyopicHandler);
			}
		});
	}

	public void sendPreProvisioningRequest(final int requestCode,
			final String uuid, String minor, String major,
			BuyopicNetworkCallBack callBack) {
		final String url = BASE_URL + "beaconpreprovision";

		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("beacon_id", uuid));
		nameValuePairs.add(new BasicNameValuePair("minor", minor));
		nameValuePairs.add(new BasicNameValuePair("major", major));
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});
	}

	public void sendProvisioningRequest(final int requestCode,
			final String bbid, String storeId, String retailerId,
			String userId, String yelpId, String imageName, String imageData,
			BuyopicNetworkCallBack callBack) {
		final String url = BASE_URL + "beaconprovision";
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("buyopic_bbid", bbid));
		nameValuePairs.add(new BasicNameValuePair("store_id", storeId));
		nameValuePairs.add(new BasicNameValuePair("retailer_id", retailerId));
		nameValuePairs.add(new BasicNameValuePair("user_id", userId));
		nameValuePairs.add(new BasicNameValuePair("yelp_id", yelpId));
		if (imageName != null) {
			nameValuePairs.add(new BasicNameValuePair("image_name", imageName));
			nameValuePairs.add(new BasicNameValuePair("image_file", imageData));
		}

		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});
	}

	public void sendAlertDetailsRequest(final int requestCode,
			final String mConsumerId, String mAlertId,
			BuyopicNetworkCallBack callBack) {
		final String totalurl = Uri.parse(BASE_URL + "storealertinfo")
				.buildUpon()
				.appendQueryParameter("buyopic_store_alert_id", mAlertId)
				.appendQueryParameter("consumer_id", mConsumerId).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);

			}
		});
	}

	public void sendBeaconSetupRequest(final int requestCode,
			final String bbid, String zoneId, String zoneName, String latitude,
			String longitude, BuyopicNetworkCallBack callBack) {
		final String url = BASE_URL + "beaconsetup";
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("buyopic_bbid", bbid));
		nameValuePairs.add(new BasicNameValuePair("zone_no", zoneId));
		nameValuePairs.add(new BasicNameValuePair("zone_name", zoneName));
		nameValuePairs.add(new BasicNameValuePair("lattitude", latitude));
		nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
		nameValuePairs.add(new BasicNameValuePair("relative_position", ""));

		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});
	}

	public void sendsetUpStoreRequest(final int requestCode,
			final String mStoreId, final String mMerchantId,
			final String mStoreName, final String mAddress, final String mCity,
			final String mState, final String mCountry, final String mPinCode,
			final String mYelpId, final String latitude,
			final String longitude, final String imageName,
			final String imageString, BuyopicNetworkCallBack callBack) {

		final String url = BASE_URL + "merchantstoresetup";
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs
				.add(new BasicNameValuePair("retailer_store_id", mStoreId));
		nameValuePairs.add(new BasicNameValuePair("retailer_retailer_id",
				mMerchantId));
		nameValuePairs.add(new BasicNameValuePair("store_address_line1",
				mAddress));
		nameValuePairs.add(new BasicNameValuePair("store_city", mCity));
		nameValuePairs.add(new BasicNameValuePair("store_state", mState));
		nameValuePairs.add(new BasicNameValuePair("store_country", mCountry));
		nameValuePairs.add(new BasicNameValuePair("store_lattitude", latitude));
		nameValuePairs
				.add(new BasicNameValuePair("store_longitude", longitude));
		nameValuePairs
				.add(new BasicNameValuePair("store_postal_code", mPinCode));
		nameValuePairs.add(new BasicNameValuePair("store_yelp_business_id",
				mYelpId));
		nameValuePairs.add(new BasicNameValuePair("store_name", mStoreName));
		nameValuePairs.add(new BasicNameValuePair("image_name", imageName));
		nameValuePairs.add(new BasicNameValuePair("image_file", imageString));

		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});

	}

	public void sendZonesOfStoreRequest(final int requestCode,
			final String mStoreAssociateId, String mMerchantId,
			String mStoreId, BuyopicNetworkCallBack callBack) {
		final String totalurl = Uri.parse(BASE_URL + "getzonesofstore")
				.buildUpon().appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", mStoreAssociateId)
				.appendQueryParameter("retailer_id", mMerchantId).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);

			}
		});
	}

	public void sendCustomerAverageTrafficRequest(final int requestCode,
			final String mStoreAssociateId, String mMerchantId,
			String mStoreId, BuyopicNetworkCallBack callBack) {
		final String totalurl = Uri
				.parse(BASE_URL + "getcustomeraveragetraffic").buildUpon()
				.appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", mStoreAssociateId)
				.appendQueryParameter("retailer_id", mMerchantId).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);

			}
		});
	}

	public void sendFootfallZoneByCustomersRequest(final int requestCode,
			final String mStoreAssociateId, String mMerchantId,
			String mStoreId, BuyopicNetworkCallBack callBack) {
		final String totalurl = Uri
				.parse(BASE_URL + "getfootfallofzonesbyconsumer").buildUpon()
				.appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", mStoreAssociateId)
				.appendQueryParameter("retailer_id", mMerchantId).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);

			}
		});
	}

	public void sendFootfallZoneByTimeRequest(final int requestCode,
			final String mStoreAssociateId, String mMerchantId,
			String mStoreId, BuyopicNetworkCallBack callBack) {

		final String totalurl = Uri
				.parse(BASE_URL + "getfootfallofzonesbytime").buildUpon()
				.appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", mStoreAssociateId)
				.appendQueryParameter("retailer_id", mMerchantId).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);
				
			}
		});
	}

	public void sendStoreActiveConsumersRequest(final int requestCode,
			String storeId, String retailerId, String associateId,
			BuyopicNetworkCallBack networkCallBack) {
		final String totalurl = Uri.parse(BASE_URL + "getstoreactiveconsumers")
				.buildUpon().appendQueryParameter("store_id", storeId)
				.appendQueryParameter("associate_id", associateId)
				.appendQueryParameter("timezone", Utils.getTimeZone())
				.appendQueryParameter("retailer_id", retailerId).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, networkCallBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);

			}
		});
	}

	public void sendUpdateZonesInformationRequest(final int requestCode,
			String storeId, String retailerId, String merchantAssociateId,
			String zonesInfoJsonObject, BuyopicNetworkCallBack callBack) {

		final String url = BASE_URL + "updatezonesdetails";
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("associate_id", merchantAssociateId));
		nameValuePairs.add(new BasicNameValuePair("store_id", storeId));
		nameValuePairs.add(new BasicNameValuePair("retailer_id", retailerId));
		nameValuePairs.add(new BasicNameValuePair("zones_data", zonesInfoJsonObject));
		
		printNameValuePairs(nameValuePairs);
		
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});

		
	}
	
	private void printNameValuePairs(List<NameValuePair> nameValuePairs) {
		for (NameValuePair nameValuePair : nameValuePairs) {
			Utils.showLog(nameValuePair.getName() + ","
					+ nameValuePair.getValue());
		}
	}
	
	public void sendFootTrafficByConsumerRequest(int requestCode,String mStoreId,String mRetailerId,String associateId,String mReportType,BuyopicNetworkCallBack callBack)
	{
		final String totalurl = Uri.parse(BASE_URL + "getfoottrafficbyconsumer")
				.buildUpon().appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", associateId)
				.appendQueryParameter("timezone", Utils.getTimeZone())
				.appendQueryParameter("retailer_id", mRetailerId)
				.appendQueryParameter("report_type", mReportType).build()
				
				.toString();
		
//		final String totalurl="http://cloud-ml.herokuapp.com/getfoottrafficbyconsumer?store_id=S67787518487286&associate_id=67787521722279&timezone=EDT&retailer_id=R67787518495169";
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);

			}
		});
	}
	
	
	
	public void sendFootTrafficByTimeOfDayRequest(int requestCode,String mStoreId,String mRetailerId,String associateId,String reportType,BuyopicNetworkCallBack callBack)
	{
		final String totalurl = Uri.parse(BASE_URL + "getfoottrafficbytimeofday")
				.buildUpon().appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", associateId)
				.appendQueryParameter("timezone", Utils.getTimeZone())
				.appendQueryParameter("retailer_id", mRetailerId)
				.appendQueryParameter("report_type",reportType).build()
				.toString();
//		final String totalurl="http://cloud-ml.herokuapp.com/getfoottrafficbytimeofday?store_id=S67787518487286&associate_id=67787521722279&timezone=EDT&retailer_id=R67787518495169";
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);

			}
		});
	}

	public void sendDwellTimeByConsumerRequest(int requestCode,String mStoreId,String mRetailerId,String associateId,String mReportType,BuyopicNetworkCallBack callBack)
	{
		final String totalurl = Uri.parse(BASE_URL + "getdwelltimebyconsumer")
				.buildUpon().appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", associateId)
				.appendQueryParameter("timezone", Utils.getTimeZone())
				.appendQueryParameter("report_type", mReportType)
				.appendQueryParameter("retailer_id", mRetailerId).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);
				
			}
		});
	}

	public void sendDwellTimeByTimeOfDayRequest(int requestCode,String mStoreId,String mRetailerId,String associateId,String mReportType,BuyopicNetworkCallBack callBack)
	{
		final String totalurl = Uri.parse(BASE_URL + "getdwelltimebytimeofday")
				.buildUpon().appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", associateId)
				.appendQueryParameter("timezone", Utils.getTimeZone())
				.appendQueryParameter("report_type", mReportType)
				.appendQueryParameter("retailer_id", mRetailerId).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);
				
			}
		});
	}
	
	public void sendAverageDailyDwellTimeRequest(int requestCode,String mStoreId,String mRetailerId,String associateId,String reportType,BuyopicNetworkCallBack callBack)
	{
		final String totalurl = Uri.parse(BASE_URL + "getaveragedailydwelltime")
				.buildUpon().appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", associateId)
				.appendQueryParameter("timezone", Utils.getTimeZone())
				.appendQueryParameter("retailer_id", mRetailerId)
				.appendQueryParameter("report_type",reportType ).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);
				
			}
		});
	}
	
	public void sendClickThroughDetailsTimeRequest(int requestCode,String mStoreId,String mRetailerId,String associateId,String mReportType,BuyopicNetworkCallBack callBack)
	{
		final String totalurl = Uri.parse(BASE_URL + "getclickthroughdetail")
				.buildUpon().appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("associate_id", associateId)
				.appendQueryParameter("timezone", Utils.getTimeZone())
				.appendQueryParameter("retailer_id", mRetailerId)
				.appendQueryParameter("report_type", mReportType).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);
				
			}
		});
	}
	
	public void sendChartsRequest(int requestCode,String mStoreId,String mRetailerId,String associateId,BuyopicNetworkCallBack callBack)
	{
		final String totalurl = Uri.parse(BASE_URL + "charts")
				.buildUpon().build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);
				
			}
		});
	}
	

	public void sendGetFootTrafficByTimeofDayChartRequest(int requestCode,String jsonObject,BuyopicNetworkCallBack callBack)
	{

		final String url = BASE_URL + "getfoottrafficbytimeofdaychatview";
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("chart_data", jsonObject));
		
		printNameValuePairs(nameValuePairs);
		
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});

	}
	
	public void sendAverageDailyDwellTimeChartRequest(int requestCode,String jsonObject,BuyopicNetworkCallBack callBack)
	{
		
	//	final String url = BASE_URL + "getaveragedailydwelltimechartview";
		final String url = BASE_URL + "averageDailyDwellTimeChartViewConsumerCount";
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("chart_data",jsonObject));
		
		printNameValuePairs(nameValuePairs);
		
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);
				
			}
		});
		
	}
	
	public void sendAverageDailyDwellTimeChartRequestDwellTime(int requestCode,String jsonObject,BuyopicNetworkCallBack callBack)
	{
		
		final String url = BASE_URL + "averageDailyDwellTimeChartViewAvgTime";
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("chart_data",jsonObject));
		
		printNameValuePairs(nameValuePairs);
		
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);
				
			}
		});
		
	}
	public void sendDwellTimeByTimeOfDayChartRequest(int requestCode,String jsonObject,BuyopicNetworkCallBack callBack)
	{
		
		final String url = BASE_URL + "dwellTimeByTimeOfDayChartView";
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("chart_data",jsonObject));
		
		printNameValuePairs(nameValuePairs);
		
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);
				
			}
		});
		
	}
	
	
	public void sendMerchantConfirmAssociateAccount(int requestCode,String emailId,String associateId,BuyopicNetworkCallBack back)
	{
		final String url = Uri.parse(BASE_URL + "confirmassociateaccount").buildUpon()
				.build().toString();

		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email_id", emailId));
		nameValuePairs.add(new BasicNameValuePair("associate_id",
				associateId));
		Utils.showLog(url);

		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, back);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);
			}
		});
	}
	
	public void sendResetPasswordRequest(int requestCode,String emailId,BuyopicNetworkCallBack back)
	{
		final String url = Uri.parse(BASE_URL + "resetstoreassociatepassword").buildUpon()
				.build().toString();

		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email_id", emailId));
		Utils.showLog(url);

		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, back);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs,
						buyopicHandler);

			}
		});
	}
	
	public void sendUrlProcessDetailsRequest(int requestCode, String processId,
			BuyopicNetworkCallBack buyopicNetworkCallBack) {

		final String totalurl = Uri
					.parse(BASE_URL + REQUEST_URL_PROCESSURL)
					.buildUpon()
					.appendQueryParameter(REQUEST_PARAM_PROCESS_TYPE,
							Constants.PROCESS_TYPE_REGISTRATION)
					.appendQueryParameter(REQUEST_PARAM_PROCESS_ID, processId)
					.appendQueryParameter(REQUEST_PARAM_REQUESTFROMBUYOPIC,
							MSG_YES).build().toString();

		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, buyopicNetworkCallBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);

			}
		});

	}
	
	public void sendEmailOfAddress(final int requestCode,StringBuffer mailBody,BuyopicNetworkCallBack callBack){
		
		
		
		final String url = HTTPS_BASE_URL + REQUEST_URL_SENDMAIL;
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("mail_subject","Invalid Address issue raised from Yellow Shop"));
		nameValuePairs.add(new BasicNameValuePair("mail_body",mailBody+""));
		nameValuePairs.add(new BasicNameValuePair("alert_invalid_address_issue","yes"));
		
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs, buyopicHandler);
			}
		});
	}
	
	
	public void sendGetOrderStausRequest(int requestCode,String mStoreId,String mRetailerId,BuyopicNetworkCallBack callBack)
	{
		final String totalurl = Uri.parse(BASE_URL + REQUEST_URL_GETSTOREORDERS)
				.buildUpon().appendQueryParameter("store_id", mStoreId)
				.appendQueryParameter("retailer_id", mRetailerId).build()
				.toString();
		Utils.showLog(totalurl);
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpRestConn.getRequestData(totalurl, buyopicHandler);
				
			}
		});
	}
	
public void sendUpdateStoreOrderStatus( int requestCode,String mStoreId,String mRetailerId,String mOrderId,String mOrderStatus,String mOrderDeliveryDate,BuyopicNetworkCallBack callBack){
		
		
		
		final String url = HTTPS_BASE_URL + REQUEST_URL_UPDATESTOREORDERSTATUS;
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("store_id",mStoreId));
		nameValuePairs.add(new BasicNameValuePair("retailer_id",mRetailerId));
		nameValuePairs.add(new BasicNameValuePair("order_id",mOrderId));
		nameValuePairs.add(new BasicNameValuePair("order_status",mOrderStatus));
		nameValuePairs.add(new BasicNameValuePair("delivery_date",mOrderDeliveryDate));
		
		final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
				requestCode, callBack);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				HttpRestConn.postRequestData(url, nameValuePairs, buyopicHandler);
			}
		});
	}

public void sendEmailForReportsRequest(int requestCode,String mStoreId,String mRetailerId,String associateId,String mReportType,String mYesrNo,BuyopicNetworkCallBack callBack)
{
	
	String REQUEST_URL = null;
	if(requestCode==Constants.REQUEST_FOOT_TRAFFIC_BY_CONSUMER_EMAIL){
		REQUEST_URL="getfoottrafficbyconsumer";
	}
	else if(requestCode==Constants.REQUEST_FOOT_TRAFFIC_BY_TIME_OF_DAY_EMAIL){
		REQUEST_URL="getfoottrafficbytimeofday";
	}
	else if(requestCode==Constants.REQUEST_GET_CLICK_THROUGH_DETAIL_EMAIL){
		REQUEST_URL="getclickthroughdetail";
	}
	else if(requestCode==Constants.REQUEST_GET_DWELL_TIME_BY_CONSUMER_EMAIL){
		REQUEST_URL="getdwelltimebyconsumer";
	}
	else if(requestCode==Constants.REQUEST_GET_DWELL_TIME_BY_TIMEOF_DAY_EMAIL){
		REQUEST_URL="getdwelltimebytimeofday";
	}
	else if(requestCode==Constants.REQUEST_GET_AVERAGE_DAILY_DWELL_TIME_EMAIL){
		REQUEST_URL="getaveragedailydwelltime";
	}

	final String totalurl = Uri.parse(BASE_URL +REQUEST_URL)
			.buildUpon().appendQueryParameter("store_id", mStoreId)
			.appendQueryParameter("associate_id", associateId)
			.appendQueryParameter("timezone", Utils.getTimeZone())
			.appendQueryParameter("retailer_id", mRetailerId)
			.appendQueryParameter("report_type", mReportType)
			.appendQueryParameter("send_email", mYesrNo)
			.build()
			
			.toString();
	
//	final String totalurl="http://cloud-ml.herokuapp.com/getfoottrafficbyconsumer?store_id=S115515219678826&retailer_id=R115515219693336&associate_id=115515225185725&timezone=IST&report_type=month&send_email=yes";
	Utils.showLog(totalurl);
	final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
			requestCode, callBack);
	executorService.execute(new Runnable() {

		@Override
		public void run() {
			HttpRestConn.getRequestData(totalurl, buyopicHandler);

		}
	});
}
public void deleteStoreAlertRequest(final int requestCode,
		 String mStoreId, String mRetailerId,String mAlertId,
		BuyopicNetworkCallBack callBack) {
	final String url = HTTPS_BASE_URL+ "deleteStoreAlert";
	final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("store_id",mStoreId));
	nameValuePairs.add(new BasicNameValuePair("retailer_id",mRetailerId));
	nameValuePairs.add(new BasicNameValuePair("alert_id",mAlertId));
	final String totalurl = String.format(url, mStoreId,mRetailerId,mAlertId);
	Utils.showLog(totalurl);
	final BuyopicRequestHandler buyopicHandler = new BuyopicRequestHandler(
			requestCode, callBack);
	executorService.execute(new Runnable() {

		@Override
		public void run() {
			HttpRestConn.postRequestData(url, nameValuePairs,
					buyopicHandler);

		}
	});
}
}
