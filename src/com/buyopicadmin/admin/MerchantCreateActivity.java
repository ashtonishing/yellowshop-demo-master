package com.buyopicadmin.admin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.adapters.CustomEditZoneAdapter;
import com.buyopicadmin.admin.models.MapData;
import com.buyopicadmin.admin.models.Zone;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MerchantCreateActivity extends BaseActivity implements
		OnClickListener, BuyopicNetworkCallBack {

	private EditText mPassword;
	private ProgressDialog mProgressDialog;
	private EditText mPhoneNumber;
	private EditText mMerchantName;
	private EditText mEmail;
	private Button mRegisterButton;
	private LinearLayout save_cancel_layout;
	private boolean isFromUpdate = false;
	protected String mCurrentFilePath;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView mProductImage;
	private String merchantId;
	private BuyOpic buyOpic;
	private LinearLayout linearLayout;
	public static boolean IS_LOGGED_IN = false;
	private TextView merchantRoleView;
	private TextView mMerchantNameText;
	private ListView listView;
	private BuyopicNetworkServiceManager buyopicNetworkServiceManager;
	private List<Zone> mZones;
	private String role;
	private TextView mAddressLabelView;
	private FinishActivityReceiver activityReceiver;
	private Button mSaveButton;
	private ImageView mShowMap;
	private String addressImagePath;
	private View view;
	private MapData mapData;
	public static final String KEY_IS_FROM_UPDATE = "is_from_update";

	protected static final int GALLERY_PICTURE = 1000;
	protected static final int CAMERA_PICTURE = 1001;
	public static final int SCALE_WIDTH = 500;
	public static final int SCALE_HEIGHT = 500;

	public MerchantCreateActivity() {
		super(R.string.action_settings);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		registerReceiver();
		view = LayoutInflater.from(this).inflate(
				R.layout.layout_register_merchant, null);
		setContentView(view);
		Utils.overrideFonts(this, view);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey(KEY_IS_FROM_UPDATE)) {
			isFromUpdate = bundle.getBoolean(KEY_IS_FROM_UPDATE);
		}
		buyOpic = (BuyOpic) getApplication();
		merchantId = buyOpic.getmMerchantId();
		prepareViews();
		if (isFromUpdate) {
			buyopicNetworkServiceManager = BuyopicNetworkServiceManager
					.getInstance(this);
			buyopicNetworkServiceManager.sendZonesOfStoreRequest(
					Constants.REQUEST_ZONES, buyOpic.getmMerchantId(),
					buyOpic.getmRetailerId(), buyOpic.getmStoreId(), this);
			bindDataToViews();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
	}

	public void registerReceiver() {
		IntentFilter filter = new IntentFilter(Constants.CUSTOM_ACTION_INTENT);
		activityReceiver = new FinishActivityReceiver(this);
		registerReceiver(activityReceiver, filter);
	}

	public void unregisterReceiver() {
		if (activityReceiver != null) {
			unregisterReceiver(activityReceiver);
			activityReceiver = null;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (addressImagePath != null) {
			File externalFile = new File(addressImagePath);
			Uri uri = Uri.fromFile(externalFile);
			imageLoader.displayImage(uri.toString(), mShowMap);
		}

	}

	private void bindDataToViews() {

		listView = (ListView) findViewById(R.id.layout_merchant_create_zone_list_view);
		listView.setVisibility(View.VISIBLE);
		HashMap<String, String> hashMap = buyOpic.getHashMap();
		if (hashMap != null) {
			findViewById(R.id.layout_register_merchant_role_layout)
					.setVisibility(View.VISIBLE);
			mEmail.setText(hashMap.get("email"));
			mMerchantNameText.setText("Your Name:");
			mPassword.setText(hashMap.get("password"));
			mMerchantName.setText(hashMap.get("store_name"));
			// mStreet.setText(hashMap.get("store_address1"));
			// mCity.setText(hashMap.get("store_city"));
			mPhoneNumber.setText(hashMap.get("phone_no"));
			// mStateZipCode.setText(hashMap.get("store_state"));
			mPhoneNumber.setText(hashMap.get("phone_no"));
			role = hashMap.get("store_tier");
			merchantRoleView.setText(role);
			mCurrentFilePath = hashMap.get("store_image_url");
			if (!TextUtils.isEmpty(hashMap.get("store_image_url").trim())) {
				imageLoader.displayImage(mCurrentFilePath, mProductImage,
						configureOptions());
			}
			// adressmap image
			if (hashMap.get("store_address_image_url") != null
					&& !TextUtils.isEmpty(hashMap
							.get("store_address_image_url"))) {
				Log.i("SRT",
						"addressimage url"
								+ hashMap.get("store_address_image_url"));
				imageLoader.displayImage(
						hashMap.get("store_address_image_url"), mShowMap,
						configureOptions());
				// addressImagePath=buyOpic.getmConsumerUserAddressImageUrl();
			}
		}
	}

	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this, "", "Please wait",
					false, false);
		}
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	private void prepareViews() {
		String postText = "<font color='#EE0000'>*</font>";

		mMerchantName = (EditText) findViewById(R.id.layout_register_merchant_name);
		// mStreet = (EditText) findViewById(R.id.layout_register_street);
		// mStreet.setHint(Html.fromHtml("Street" + postText));

		mAddressLabelView = (TextView) findViewById(R.id.address_label_view);
		mAddressLabelView.setText(Html.fromHtml("Address:" + postText));
		mMerchantNameText = (TextView) findViewById(R.id.layout_create_merchant_your_name_view);
		// mCity = (EditText) findViewById(R.id.layout_register_city);
		// mCity.setHint(Html.fromHtml("City" + postText));

		// mStateZipCode = (EditText)
		// findViewById(R.id.layout_register_city_zipcode);
		// mStateZipCode.setHint(Html.fromHtml("State, Zip" + postText));
		mEmail = (EditText) findViewById(R.id.layout_register_email);
		mPassword = (EditText) findViewById(R.id.layout_register_password);
		mPhoneNumber = (EditText) findViewById(R.id.layout_register_phone_number);
		mRegisterButton = (Button) findViewById(R.id.layout_register_button);
		mRegisterButton.setOnClickListener(this);
		save_cancel_layout = (LinearLayout) findViewById(R.id.layout_save_cancel);

		findViewById(R.id.layout_register_cancel_button).setOnClickListener(
				this);
		mSaveButton = (Button) findViewById(R.id.layout_register_save_button);
		mSaveButton.setOnClickListener(this);

		mProductImage = (ImageView) findViewById(R.id.layout_register_image);
		mProductImage.setOnClickListener(this);
		linearLayout = (LinearLayout) findViewById(R.id.layout_login_page);
		TextView loginButton = (TextView) findViewById(R.id.layout_login_button);
		loginButton.setOnClickListener(this);
		String str = "<html><body><u>Login Here</u></body></html>";
		loginButton.setText(Html.fromHtml(str));
		BaseActivity baseActivity = this;
		if (baseActivity != null) {
			if (isFromUpdate) {
				baseActivity.setBeaconActionBar("Edit Profile", 2);
				save_cancel_layout.setVisibility(View.VISIBLE);

				mRegisterButton.setVisibility(View.GONE);
				linearLayout.setVisibility(View.GONE);
			} else {
				baseActivity.setBeaconActionBar("Yellow Shop: Registration", 1);
				save_cancel_layout.setVisibility(View.GONE);
				mRegisterButton.setVisibility(View.VISIBLE);
				linearLayout.setVisibility(View.VISIBLE);
			}
		}
		merchantRoleView = (TextView) findViewById(R.id.layout_register_merchant_role);
		mShowMap = (ImageView) findViewById(R.id.layout_register_addressmap_image);
		mShowMap.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.layout_register_button:
			submitDataToServer();
			break;
		case R.id.layout_login_button:
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_register_image:
			startDialog();
			break;
		case R.id.layout_register_save_button:

			buyopicNetworkServiceManager.sendUpdateZonesInformationRequest(
					Constants.REQUEST_UPDATE_ZONES_INFO, buyOpic.getmStoreId(),
					buyOpic.getmRetailerId(), buyOpic.getmMerchantId(),
					prepareJsonObject(), this);
			submitDataToServer();
			break;
		case R.id.layout_register_cancel_button:
			finish();
			break;
		case R.id.layout_register_addressmap_image:

			Intent intent1 = new Intent(this, ShowMapActivity.class);
			HashMap<String, String> hashMap = buyOpic.getHashMap();
			if(!buyOpic.isRegistered()){
				hashMap=null;
			}
		

	//		intent1.putExtra("username",mMerchantName.getText().toString());
			intent1.putExtra("hashmap",hashMap);

			startActivityForResult(intent1, Utils.RESULT_MAP);
			break;

		default:
			break;
		}

	}

	private String prepareJsonObject() {
		JSONArray jsonArray = new JSONArray();
		if (mZones != null && !mZones.isEmpty()) {

			for (Zone zone : mZones) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("value", zone.getmZoneName());
					jsonObject.put("id", zone.getmZoneId());
					jsonArray.put(jsonObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return jsonArray.toString();
	}

	private void submitDataToServer() {
		if (!isEmpty(mEmail) && !isEmpty(mPassword) && !isEmpty(mMerchantName)

		&& !isEmpty(mPhoneNumber)) {
			HashMap<String, String> hashMap = buyOpic.getHashMap();

			String imageName = "";
			String imageFile = "";
			String addressImageName = "";
			String addressImageFile = "";

			String mGoogleIconImage = "";
			String mStoreAddress1 = "";
			String mStoreCity = "";
			String mStoreState = "";
			String mGooglePlaceID = "";
			String mStoreAddress2 = "";
			String mStorePostalCode = "";
			String longitude = "";
			String latitude = "";

			if (mCurrentFilePath != null
					&& mCurrentFilePath.startsWith("https://")) {
				imageName = new File(mCurrentFilePath).getName();
				imageFile = mCurrentFilePath;
			} else if (mCurrentFilePath != null) {
				imageName = new File(mCurrentFilePath).getName();
				imageFile = getEncodedImageString();
			}

			if (addressImagePath != null) {
				addressImageName = new File(addressImagePath).getName();
				addressImageFile = getEncodedImageString(addressImagePath);
			}

			if (addressImagePath != null
					&& addressImagePath.startsWith("https://")) {
				addressImageName = new File(addressImagePath).getName();
				addressImageFile = addressImagePath;
			}

			if (imageName == null || imageFile == null
					|| imageName.equalsIgnoreCase("")
					|| imageFile.equalsIgnoreCase("")) {
				if (hashMap != null) {

					imageName = new File(hashMap.get("store_image_url"))
							.getName();
					imageFile = hashMap.get("store_image_url");

				}

			}
			if (addressImageFile == null || addressImageName == null
					|| addressImageFile.equalsIgnoreCase("")
					|| addressImageName.equalsIgnoreCase("")) {
				if (hashMap != null) {

					if (hashMap.get("store_address_image_url") != null
							&& !TextUtils.isEmpty(hashMap
									.get("store_address_image_url"))) {
					addressImageName = new File(hashMap.get("store_address_image_url")).getName();
					addressImageFile = hashMap.get("store_address_image_url");
					}

				}
			}

			BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
					.getInstance(this);

			int requestCode = isFromUpdate ? Constants.REQUEST_MERCHANT_UPDATE
					: Constants.REQUEST_MERCHANT_SETUP;

			if (hashMap != null) {
				mGoogleIconImage = hashMap.get("google_icon_image");
				mStoreAddress1 = hashMap.get("store_address1");
				mStoreCity = hashMap.get("store_city");
				mStoreState = hashMap.get("store_state");
				mGooglePlaceID = hashMap.get("google_place_id");
				mStoreAddress2 = hashMap.get("store_address2");
				mStorePostalCode = hashMap.get("store_postal_code");
				latitude = hashMap.get("store_lattitude");
				longitude = hashMap.get("store_longitude");

			
			}


			if (mapData != null) {
				mGoogleIconImage = mapData.getIconurl();
				mStoreAddress1 = mapData.getmAddress1();
				mStoreCity = mapData.getmCity();
				mStoreState = mapData.getmState();
				mGooglePlaceID = mapData.getPlaceid();
				mStoreAddress2 = mapData.getmAddress2();
				mStorePostalCode = mapData.getmPostalCode();

				latitude = mapData.getLatitude();
				longitude = mapData.getLongitude();

			}
			

			if (Utils.isValidEmail(mEmail.getText().toString())) {
				showProgressDialog();
				mRegisterButton.setEnabled(false);
				mSaveButton.setEnabled(false);
				buyopicNetworkServiceManager
						.sendMerchantStoreRegistrationRequest(requestCode,
								merchantId, isFromUpdate,
								buyOpic.getmStoreId(),
								buyOpic.getmRetailerId(), mEmail.getText()
										.toString(), mPassword.getText()
										.toString(), mMerchantName.getText()
										.toString(), mStoreAddress1,
								mStoreCity, mStoreState, latitude, longitude,
								imageName, imageFile, role, mPhoneNumber
										.getText().toString(),

								addressImageName, addressImageFile,
								mGooglePlaceID, mGoogleIconImage,
								mStoreAddress2, mStorePostalCode, this);

			} else {
				Utils.showToast(this, "Please give a valid email address");
			}

		}

		else {
			Utils.showToast(this, "Please fill the mandatory fields");
		}
	}

	private boolean isEmpty(EditText editText) {

		return TextUtils.isEmpty(editText.getText().toString());
	}

	private void forgotPassword() {
		try {
			final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
			View view = LayoutInflater.from(this).inflate(
					R.layout.layout_forgotpassword_dialog_view, null);
			Utils.overrideFonts(this, view);
			dialog.setContentView(view);
			TextView forgotPasswordText = (TextView) view
					.findViewById(R.id.forgot_password_text);
			String forgotPasswordResponseText = "We have just sent you an email with a confirmation link (to your email address above). Please open that email and click on the confirmation link to complete this one-time registration. Welcome to Yellow!";
			forgotPasswordText.setText(forgotPasswordResponseText);
			ImageButton cancelDialog = (ImageButton) view
					.findViewById(R.id.dialog_cancel);
			cancelDialog.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					finish();
				}
			});
			dialog.setCancelable(false);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();

		switch (requestCode) {
		case Constants.REQUEST_UPDATE_ZONES_INFO:
			Utils.showLog("" + (String) object);
			break;

		case Constants.REQUEST_MERCHANT_SETUP:
			mRegisterButton.setEnabled(true);
			HashMap<String, String> hashMap = JsonResponseParser
					.parseMerchantStoreRegisterResponse((String) object);
			if (hashMap != null
					&& hashMap.containsKey("message")
					&& hashMap
							.get("message")
							.equalsIgnoreCase(
									"Please check your email and confirm your registration.")) {
				{
				
					setFocusable(false, view);
					forgotPassword();
					/*
					 * IS_LOGGED_IN = true; Utils.showToast(this,
					 * hashMap.get("message")); BuyOpic buyOpic = (BuyOpic)
					 * getApplication(); buyOpic.setRegistered(true);
					 * buyOpic.setHashMap(hashMap); Intent intent = new
					 * Intent(this, HomePageTabActivity.class); ;
					 * startActivity(intent); finish();
					 */

				}

			} else if (hashMap != null && hashMap.containsKey("message")) {
				Utils.showToast(this, hashMap.get("message"));
			}

			break;
		case Constants.REQUEST_MERCHANT_UPDATE:
			mSaveButton.setEnabled(true);
			hashMap = JsonResponseParser
					.parseMerchantStoreRegisterResponse((String) object);
			;

			if (hashMap != null
					&& hashMap.containsKey("message")
					&& hashMap.get("message").equalsIgnoreCase(
							"Merchant details updated successfully.")) {
				{
					Utils.showToast(this, hashMap.get("message"));
					BuyOpic buyOpic = (BuyOpic) getApplication();
					buyOpic.setRegistered(true);
					buyOpic.setHashMap(hashMap);
					finish();
				}
			}
			break;
		case Constants.REQUEST_ZONES:
			mZones = JsonResponseParser
					.parseZonesOfStoreInfoResponse((String) object);

			if (role != null && !role.isEmpty()) {

				if (mZones != null && !mZones.isEmpty()) {
				if (!role.equalsIgnoreCase("basic")
						&& !mZones.get(0).getmZoneId().equalsIgnoreCase("NA")) {
					findViewById(R.id.layout_merchant_create_zone_header_layout)
							.setVisibility(View.VISIBLE);
					listView.setAdapter(new CustomEditZoneAdapter(this, -1,
							mZones));
					updateListViewHeight(listView);
				}
				}
			}
		default:
			break;
		}
	}

	private void setFocusable(boolean b, View view) {
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			for (int i = 0; i < vg.getChildCount(); i++) {
				View child = vg.getChildAt(i);
				setFocusable(b, child);
			}
		} else if (view instanceof EditText) {
			EditText editText = (EditText) view;
			editText.setFocusable(b);
		}
	}

	public static void updateListViewHeight(ListView myListView) {
		CustomEditZoneAdapter myListAdapter = (CustomEditZoneAdapter) myListView
				.getAdapter();
		if (myListAdapter == null) {
			return;
		}
		// get listview height
		int totalHeight = 0;
		int adapterCount = myListAdapter.getCount();
		for (int size = 0; size < adapterCount; size++) {
			View listItem = myListAdapter.getView(size, null, myListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		// Change Height of ListView
		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight
				+ (myListView.getDividerHeight() * (adapterCount - 1));
		myListView.setLayoutParams(params);
	}

	@Override
	public void onFailure(int requestCode, String message) {
		dismissProgressDialog();
	}

	private void startDialog() {

		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		myAlertDialog.setTitle("Upload");
		myAlertDialog.setMessage("How do you want to add the Picture?");

		myAlertDialog.setPositiveButton("Gallery",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						Intent pictureActionIntent = new Intent(
								Intent.ACTION_GET_CONTENT, null);
						pictureActionIntent.setType("image/*");
						pictureActionIntent.putExtra("return-data", true);
						startActivityForResult(Intent.createChooser(
								pictureActionIntent, "Select Picture"),
								GALLERY_PICTURE);
					}
				});

		myAlertDialog.setNegativeButton("Camera",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						Intent pictureActionIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						File f = createImageFile();
						mCurrentFilePath = f.getAbsolutePath();
						pictureActionIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(f));
						startActivityForResult(pictureActionIntent,
								CAMERA_PICTURE);
					}
				});
		myAlertDialog.show();
	}

	private File createImageFile() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		File image = null;
		try {
			image = File.createTempFile(imageFileName, /* prefix */
					".jpg", /* suffix */
					storageDir /* directory */
			);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case GALLERY_PICTURE:
				Uri selectedImageUri = data.getData();
				mCurrentFilePath = getPath(selectedImageUri);

			case CAMERA_PICTURE:
				if (mCurrentFilePath != null) {
					imageLoader.displayImage("file:///" + mCurrentFilePath,
							mProductImage, configureOptions());
				}

				break;
			}
		} else if (resultCode == Utils.RESULT_MAP) {

			mapData = (MapData) data.getExtras().getSerializable("mapdata");
			addressImagePath = (String) data.getExtras().getSerializable(
					"imagepath");
			//mMerchantName.setActivated(false);
			//mMerchantName.setFocusable(false);
			Log.i("map data is ", "map  " + mapData);
			/*
			 * if (mapData != null) { if (mapData.getmStreet() == null) {
			 * mapData.setmStreet(""); } if (mapData.getmPostalCode() == null) {
			 * mapData.setmPostalCode(""); } if (mapData.getmCity() == null) {
			 * mapData.setmCity(""); } if (mapData.getmCountry() == null) {
			 * mapData.setmCountry(""); }
			 *//*
				 * mAddress.setText(mapData.getmStreet() + "\n" +
				 * mapData.getmPostalCode() + "\n" + mapData.getmCity() + " " +
				 * mapData.getmCountry());
				 */

			// }
		}

	}

	private String getPath(Uri selectedImageUri) {
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		String path = null;
		CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri,
				projection, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(projection[0]));
			cursor.close();
		}
		return path;
	}

	private String getEncodedImageString() {
		if (mCurrentFilePath != null) {
			byte[] byteArray = decodeSampledBitmapFromFile(mCurrentFilePath);
			return byteArray != null ? Base64.encodeToString(byteArray,
					Base64.URL_SAFE) : "Unable to Encode Image With Base64";
		}
		return "Unable to Encode Image With Base64";

	}

	private int getRotation() {
		ExifInterface exif;
		try {
			exif = new ExifInterface(mCurrentFilePath);

			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {

			case ExifInterface.ORIENTATION_ROTATE_270:
				return 270;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private byte[] convertFileIntoBase64(String mCurrentFilePath) {

		if (mCurrentFilePath != null) {
			File file = new File(mCurrentFilePath);
			byte[] data = new byte[(int) file.length()];
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			FileInputStream fileInputStream = null;

			try {

				int nRead;
				fileInputStream = new FileInputStream(file);
				while ((nRead = fileInputStream.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
				buffer.flush();
				return buffer.toByteArray();

			} catch (FileNotFoundException e) {
				System.out.println("File Not Found.");
				e.printStackTrace();
			} catch (IOException e1) {
				System.out.println("Error Reading The File.");
				e1.printStackTrace();
			} finally {
				try {
					if (fileInputStream != null) {
						fileInputStream.close();
					}
				} catch (IOException ioe) {

				}
			}
		}
		return null;

	}

	private DisplayImageOptions configureOptions() {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(android.R.color.transparent)
				.showImageForEmptyUri(R.drawable.ic_placeholder_image)
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
				.considerExifParams(true)
				.showImageOnFail(R.drawable.ic_placeholder_image)
				.cacheOnDisc(true).build();
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further.
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	private String getEncodedImageString(String path) {
		if (path != null) {
			byte[] byteArray = decodeSampledBitmapFromFile(path);
			return byteArray != null ? Base64.encodeToString(byteArray,
					Base64.URL_SAFE) : "Unable to Encode Image With Base64";
		}
		return "Unable to Encode Image With Base64";

	}

	public synchronized byte[] decodeSampledBitmapFromFile(String path) {

		byte[] bytes = convertFileIntoBase64(path);
		if (bytes != null && bytes.length > 0) {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, SCALE_WIDTH,
					SCALE_HEIGHT);
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length, options);
			Matrix matrix = new Matrix();
			matrix.postRotate(getRotation(path));
			Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			rotated.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();

			return byteArray;
		}
		return null;
	}

	private int getRotation(String path) {
		ExifInterface exif;
		try {
			exif = new ExifInterface(path);

			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {

			case ExifInterface.ORIENTATION_ROTATE_270:
				return 270;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
