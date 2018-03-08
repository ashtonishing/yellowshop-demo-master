package com.buyopicadmin.admin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.models.Alert;
import com.buyopicadmin.admin.models.Zone;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MerchantCreateOfferActivity extends BaseActivity implements
		OnClickListener, BuyopicNetworkCallBack {

	private EditText mOfferTitle;
	private EditText mOfferDescription;
	private EditText mOfferPrice;
	private EditText mOfferValid;
	private EditText mOfferValidTo;
	private CheckBox mActivateView;
	protected String mCurrentFilePath = null;
	private ImageView mProductImage;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ProgressDialog mProgressDialog;
	private String mStoreId;
	private String mRetailerId;
	private String mMerchantId;
	private boolean isInEditMode;
	private String alertId = null;
	private BuyOpic buyOpic;
	private MerchantCreateOfferActivity baseActivity;
	private Spinner mZonesSpinner;
	private BuyopicNetworkServiceManager buyopicNetworkServiceManager;
	private ImageView mStoreLogoView;
	private TextView mStoreNameView;
	private TextView mStoreAddressView;
	private List<Zone> mZones;
	private TextView mValidFromText;
	private TextView mUntilText;
	private TextView mZoneText;
	private FinishActivityReceiver activityReceiver;
	private TextView mOfferPriceText;
	private Button mDeleteButton;
	protected static final int GALLERY_PICTURE = 1000;
	protected static final int CAMERA_PICTURE = 1001;
	public static final int SCALE_WIDTH = 500;
	public static final int SCALE_HEIGHT = 500;
	private static final int START_DATE = 1;
	private static final int END_DATE = 2;

	private long offerStartTime = -1;

	public static final String KEY_EXTRA_ALERT = "alert";

	public MerchantCreateOfferActivity() {
		super(R.string.action_settings);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver();
		View view = LayoutInflater.from(this).inflate(
				R.layout.layout_create_offer, null);
		setContentView(view);
		Utils.overrideFonts(this, view);
		buyOpic = (BuyOpic) getApplication();
		prepareViews();
		Bundle bundle = getIntent().getExtras();
		buyopicNetworkServiceManager = BuyopicNetworkServiceManager
				.getInstance(this);
		buyopicNetworkServiceManager.sendZonesOfStoreRequest(
				Constants.REQUEST_ZONES, buyOpic.getmMerchantId(),
				buyOpic.getmRetailerId(), buyOpic.getmStoreId(), this);

		if (bundle != null && bundle.containsKey(KEY_EXTRA_ALERT)) {
			isInEditMode = true;
			showProgressDialog();
			alertId = bundle.getString(KEY_EXTRA_ALERT);
			buyopicNetworkServiceManager.sendAlertDetailsRequest(
					Constants.REQUEST_EDIT_ALERT, mMerchantId, alertId, this);

		}
		if (buyOpic.getHashMap()!=null && bundle != null && bundle.containsKey("name")
				&& bundle.containsKey("address") && bundle.containsKey("logo")) {
			mStoreNameView.setText(buyOpic.getHashMap().get("store_name"));
			String storeAddress = buyOpic.getHashMap().get("store_address1") + ", "
					+ buyOpic.getHashMap().get("store_city") + ", "
					+ buyOpic.getHashMap().get("store_state");
			mStoreAddressView.setText(storeAddress);
			if (!TextUtils.isEmpty(bundle.getString("logo"))) {
				imageLoader.displayImage(buyOpic.getHashMap().get("store_image_url"),
						mStoreLogoView, configureOptions());
			} else {
				imageLoader.displayImage("drawable://"
						+ R.drawable.ic_placeholder_image, mStoreLogoView,
						configureOptions());
			}
		}

		mStoreId = buyOpic.getmStoreId();
		mRetailerId = buyOpic.getmRetailerId();
		mMerchantId = buyOpic.getmMerchantId();

	}
	
	

	private void bindAlertDetailsToViews(Alert alert) {
		if (baseActivity != null) {
			baseActivity.setBeaconActionBar("Edit Current Listing", 2);
		}
		mOfferTitle.setText(alert.getmOfferTitle());
		mOfferDescription.setText(alert.getmOfferMessage());
		mOfferPrice.setText(alert.getmPrice());
		mOfferValid.setText(getFormattedTime(alert.getmStartDate()));
		mOfferValidTo.setText(getFormattedTime(alert.getmEndDate()));
		imageLoader.displayImage(alert.getmThumbnailUrl(), mProductImage,
				configureOptions());
		mActivateView.setChecked(alert.ismIsActivated());
		mCurrentFilePath = alert.getmThumbnailUrl();
		if (alert.getmZones() != null
				&& !alert.getmZones().equalsIgnoreCase("null")) {
			mZonesSpinner.setSelection(selectedZonePos(alert.getmZones()));
		}
	}

	private int selectedZonePos(String zoneNumber) {
		int selectedPos = 0;
		try {
			if (mZones != null && mZones.isEmpty()) {
				for (Zone zone : mZones) {
					if (zone.getmZoneNumber().equalsIgnoreCase(zoneNumber)) {
						selectedPos = mZones.indexOf(zone);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectedPos;
	}

	private String getFormattedTime(String string) {
		// 2014-04-16 20:51:00.0
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.US);
		try {
			Date date = dateFormat.parse(string);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat(
					"MM/dd/yyyy hh:mm a", Locale.US);
			return dateFormat2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void prepareViews() {
		String postText = "<font color='#EE0000'>*</font>";
		mZonesSpinner = (Spinner) findViewById(R.id.layout_create_offer_spinner_zones);
		mOfferTitle = (EditText) findViewById(R.id.layout_create_offer_title);
		String preText = "Offer Name";
		mOfferTitle.setHint(Html.fromHtml(preText + postText));

		mOfferPriceText = (TextView) findViewById(R.id.offerpricetext);
		preText = "Offer Price:";
		mOfferPriceText.setText(Html.fromHtml(preText + postText));

		mOfferDescription = (EditText) findViewById(R.id.layout_create_offer_description);
		mOfferPrice = (EditText) findViewById(R.id.layout_create_offer_price);

		preText = "Offer Price";
		// mOfferPrice.setHint(Html.fromHtml(preText + postText));

		mValidFromText = (TextView) findViewById(R.id.validfromtext);
		mValidFromText.setText(Html.fromHtml("Valid From:" + postText));

		mUntilText = (TextView) findViewById(R.id.untiltext);
		mUntilText.setText(Html.fromHtml("Until:" + postText));

		mZoneText = (TextView) findViewById(R.id.zoneText);
		mZoneText.setText(Html.fromHtml("Zone No.:" + postText));

		mOfferValid = (EditText) findViewById(R.id.layout_create_offer_valid_from);
		mOfferValidTo = (EditText) findViewById(R.id.layout_create_offer_valid_to);
		mActivateView = (CheckBox) findViewById(R.id.layout_create_offer_checkbox_activate);
		mProductImage = (ImageView) findViewById(R.id.layout_create_offer_product_image);
		mProductImage.setOnClickListener(this);
		mDeleteButton=(Button)findViewById(R.id.layout_create_offer_delete_button);
		mDeleteButton.setVisibility(View.INVISIBLE);
		findViewById(R.id.layout_create_offer_linear_layout_zones)
				.setOnClickListener(this);
		findViewById(R.id.layout_create_offer_down_arrow_zone)
				.setOnClickListener(this);

		findViewById(R.id.layout_create_offer_submit_button)
				.setOnClickListener(this);
		findViewById(R.id.layout_create_offer_cancel_button)
				.setOnClickListener(this);
		mOfferValid.setOnClickListener(this);
		mOfferValidTo.setOnClickListener(this);
		baseActivity = this;
		if (baseActivity != null) {
			baseActivity.setBeaconActionBar("Create New Listing", 2);
		}

		mStoreLogoView = (ImageView) findViewById(R.id.layout_offer_desc_store_logo);
		mStoreNameView = (TextView) findViewById(R.id.layout_offer_desc_store_name);
		mStoreAddressView = (TextView) findViewById(R.id.layout_offer_desc_store_address);
		// mSwitchView=(Switch)findViewById(R.id.create_offer_switch);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.layout_create_offer_product_image:
			startDialog();
			break;
		case R.id.layout_create_offer_valid_from:
			showDateTimeDialog((EditText) arg0, START_DATE);
			break;
		case R.id.layout_create_offer_valid_to:
			showDateTimeDialog((EditText) arg0, END_DATE);
			break;

		case R.id.layout_create_offer_submit_button:
			submitDataToServer();
			break;
		case R.id.layout_create_offer_cancel_button:
			cancelData();
			break;
		case R.id.layout_create_offer_down_arrow_zone:
		case R.id.layout_create_offer_linear_layout_zones:
			mZonesSpinner.performClick();
			break;
		default:
			break;
		}
	}

	private void cancelData() {
		finish();
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

	private void submitDataToServer() {
		if (!isEmpty(mOfferTitle) && !isEmpty(mOfferDescription)
				&& !isEmpty(mOfferPrice) && !isEmpty(mOfferValid)
				&& !isEmpty(mOfferValidTo)) {
			if (mCurrentFilePath != null) {
				showProgressDialog();
				DecimalFormat decimalFormat = new DecimalFormat("#0.00");
				String mPrice = decimalFormat.format(Double.valueOf(mOfferPrice
						.getText().toString()));
				BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
						.getInstance(this);
				String imageString = null;
				String mthubmailPath="";
				if (mCurrentFilePath.trim().startsWith("https:")) {
					imageString = mCurrentFilePath;
					mthubmailPath=mCurrentFilePath;
				} else {
					imageString = getEncodedImageString();
				}
			
				Zone mZone = (Zone) mZonesSpinner.getSelectedItem();
				String imageName = new File(mCurrentFilePath).getName();
				buyopicNetworkServiceManager.sendCreateOffersDataRequest(
						Constants.REQUEST_CREATE_ALERT, alertId, isInEditMode,
						mStoreId, mRetailerId,
						mOfferTitle.getText().toString(), mOfferDescription
								.getText().toString(), "specialoffer",
						mOfferValid.getText().toString(), mOfferValidTo
								.getText().toString(), mZone.getmZoneId(),
						mMerchantId, mPrice,
						mActivateView.isChecked() ? "active" : "inactive",
						imageName, imageString,mthubmailPath, this);
			} else {
				Utils.showToast(this, "Please select the product Picture");
			}
		} else {
			Utils.showToast(this, "Please fill all the fields");
		}
	}

	private boolean isEmpty(EditText editText) {
		return TextUtils.isEmpty(editText.getText().toString().trim());
	}

	private void startDialog() {

		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		myAlertDialog.setTitle("Upload");
		myAlertDialog.setMessage("How do you want to add the Picture?");

		myAlertDialog.setPositiveButton("Gallery",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						/*Intent pictureActionIntent = new Intent(
								Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						pictureActionIntent.setType("image/*");
						pictureActionIntent.putExtra("return-data", true);
						startActivityForResult(Intent.createChooser(
								pictureActionIntent, "Select Picture"),
								GALLERY_PICTURE);*/
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
						intent.setType("image/*");
						intent.putExtra("return-data", true);

						startActivityForResult(intent, 511);

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

	private void showDateTimeDialog(final EditText edittext, final int which) {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
				.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as
		// expected though)
		final String timeS = android.provider.Settings.System.getString(
				getContentResolver(),
				android.provider.Settings.System.TIME_12_24);
		final boolean is24h = !(timeS == null || timeS.equals("12"));

		// Update demo TextViews when the "OK" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mDateTimePicker.clearFocus();

						String dateTime = ((mDateTimePicker.get(Calendar.MONTH)) + 1)
								+ "/"
								+ mDateTimePicker.get(Calendar.DAY_OF_MONTH)
								+ "/"
								+ mDateTimePicker.get(Calendar.YEAR)
								+ " "
								+ mDateTimePicker.get(Calendar.HOUR)
								+ ":"
								+ mDateTimePicker.get(Calendar.MINUTE)
								+ " "
								+ (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ? "AM"
										: "PM");

						Calendar calendar = Calendar.getInstance();
						String currentDateTime = (calendar.get(Calendar.MONTH) + 1)
								+ "/"
								+ calendar.get(Calendar.DAY_OF_MONTH)
								+ "/"
								+ calendar.get(Calendar.YEAR)
								+ " "
								+ calendar.get(Calendar.HOUR)
								+ ":"
								+ calendar.get(Calendar.MINUTE)
								+ " "
								+ (calendar.get(Calendar.AM_PM) == 0 ? "AM"
										: "PM");
						if (which == START_DATE) {
							offerStartTime = convertDateToMilliSeconds(dateTime);
							boolean isValidDate = compareDates(offerStartTime,
									convertDateToMilliSeconds(currentDateTime));
							if (isValidDate) {
								edittext.setText(dateTime);
								mDateTimeDialog.dismiss();
							} else {
								Utils.showToast(
										MerchantCreateOfferActivity.this,
										"Invalid Time");
							}
						} else if (which == END_DATE) {
							boolean isValidDate = compareDates(
									convertDateToMilliSeconds(dateTime),
									offerStartTime);
							if (isValidDate) {
								edittext.setText(dateTime);
								mDateTimeDialog.dismiss();
							} else {
								Utils.showToast(
										MerchantCreateOfferActivity.this,
										"Invalid Time");
							}
						}
					}
				});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimeDialog.cancel();
					}
				});

		// Reset Date and Time pickers when the "Reset" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimePicker.reset();
					}
				});

		// Setup TimePicker
		mDateTimePicker.setIs24HourView(is24h);
		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();
	}

	private long convertDateToMilliSeconds(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"MM/dd/yyyy hh:mm a", Locale.US);
		try {
			Date convertedDate = dateFormat.parse(date);
			return convertedDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private boolean compareDates(long start, long end) {
		if (end != -1) {
			return end <= start;
		} else {
			Utils.showToast(this, "Plese give offer start Time");
			return false;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 511:
				Uri selectedImageUri = data.getData();
				mCurrentFilePath = getPath(selectedImageUri);

			case CAMERA_PICTURE:
				if (mCurrentFilePath != null) {
					imageLoader.displayImage("file:///" + mCurrentFilePath,
							mProductImage, configureOptions());
				}

				break;
			}
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
		byte[] byteArray = decodeSampledBitmapFromFile();
		return byteArray != null ? Base64.encodeToString(byteArray,
				Base64.URL_SAFE) : "Unable to Encode Image With Base64";

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

	private byte[] convertFileIntoBase64() {

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

	public synchronized byte[] decodeSampledBitmapFromFile() {
		byte[] bytes = convertFileIntoBase64();
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, SCALE_WIDTH,
				SCALE_HEIGHT);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
				options);
		Matrix matrix = new Matrix();
		matrix.postRotate(getRotation());
		Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		rotated.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		return byteArray;
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

	@Override
	public void onSuccess(int requestCode, Object object) {
		dismissProgressDialog();
		if (object != null && object instanceof String) {
			switch (requestCode) {
			case Constants.REQUEST_CREATE_ALERT:
				String message = JsonResponseParser
						.parseCreateAlertResponse((String) object);
				if (message.equalsIgnoreCase("Listing created successfully.")
						|| message
								.equalsIgnoreCase("Listing updated successfully.")) {
					Intent intent = new Intent(this,
							MerchantOfferListActivity.class);
					intent.putExtra("a", Constants.SUCCESS);
					setResult(RESULT_OK, intent);
					finish();
				}
				Utils.showToast(this, message);
				break;
			case Constants.REQUEST_EDIT_ALERT:
				Alert alert = JsonResponseParser
						.parseAlertDetailsResponse((String) object);
				if (alert != null) {
					bindAlertDetailsToViews(alert);
				}
				break;
			case Constants.REQUEST_ZONES:
				mZones = JsonResponseParser
						.parseZonesOfStoreInfoResponse((String) object);
				bindZonesToSpinner(mZones);

				break;
			default:
				break;
			}

		}
	}

	private void bindZonesToSpinner(List<Zone> mZones) {
		if (mZones != null && !mZones.isEmpty()) {
			Utils.showLog("Only ONe Zone");
			mZonesSpinner.setAdapter(new ArrayAdapter<Zone>(this,
					R.layout.spinner_row1, R.id.zonenumber, mZones));
		}
	}

	@Override
	public void onFailure(int requestCode, String message) {
		dismissProgressDialog();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ImageView imageView=new ImageView(this);
		imageView.setImageResource(R.drawable.ic_add_new_listings);
		imageView.setColorFilter(getResources().getColor(R.color.screen_bg_color));
		Drawable drawable=imageView.getDrawable();
		menu.add(0, 1, 0, "Add").setIcon(drawable)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 2, 0, "Settings").setIcon(R.drawable.ic_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
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

}
