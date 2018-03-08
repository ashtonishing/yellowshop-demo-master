package com.buyopicadmin.admin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MerchantStoreSetupActivity extends BaseActivity implements
		OnClickListener, BuyopicNetworkCallBack {

	private EditText mMerchantStoreId;
	private EditText mMerchantId;
	private EditText mAddress;
	private EditText mCity;
	private EditText mState;
	private EditText mCountry;
	private EditText mLatitude;
	private EditText mLongitude;
	private EditText mStoreName;
	private EditText mPincode;
	private EditText mYelpId;
	protected String mCurrentFilePath;
	protected static final int GALLERY_PICTURE = 1000;
	protected static final int CAMERA_PICTURE = 1001;
	public static final int SCALE_WIDTH = 500;
	public static final int SCALE_HEIGHT = 500;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView mStoreLogo;
	private ProgressDialog mProgressDialog;

	public MerchantStoreSetupActivity() {
		super(R.string.action_settings);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_create_store);
		prepareViews();

	}

	private void prepareViews() {
		BaseActivity baseActivity = this;
		baseActivity.setBeaconActionBar("Merchant Store Setup", 2);
		mMerchantStoreId = (EditText) findViewById(R.id.layout_store_store_id);
		mMerchantId = (EditText) findViewById(R.id.layout_store_merchant_id);
		mAddress = (EditText) findViewById(R.id.layout_store_store_address);
		mCity = (EditText) findViewById(R.id.layout_store_city);
		mState = (EditText) findViewById(R.id.layout_store_state);
		mCountry = (EditText) findViewById(R.id.layout_store_country);
		mLatitude = (EditText) findViewById(R.id.layout_store_lat);
		mLongitude = (EditText) findViewById(R.id.layout_store_longitude);
		mStoreName = (EditText) findViewById(R.id.layout_store_store_name);
		mPincode = (EditText) findViewById(R.id.layout_store_pincode);
		mYelpId = (EditText) findViewById(R.id.layout_store_yelp_id);
		findViewById(R.id.layout_store_cancel_button).setOnClickListener(this);
		findViewById(R.id.layout_store_submit_button).setOnClickListener(this);
		mStoreLogo = (ImageView) findViewById(R.id.layout_store_store_image);
		mStoreLogo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_store_cancel_button:
			finish();
			break;
		case R.id.layout_store_store_image:
			startDialog();
			break;
		case R.id.layout_store_submit_button:
			submitDataToServer();

			break;

		default:
			break;
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

	private void submitDataToServer() {

		if (!isEmpty(mMerchantStoreId) && !isEmpty(mAddress) && !isEmpty(mCity)
				&& !isEmpty(mCountry) && !isEmpty(mLatitude)
				&& !isEmpty(mLongitude) && !isEmpty(mPincode)
				&& !isEmpty(mMerchantId) && !isEmpty(mState)
				&& !isEmpty(mMerchantStoreId) && !isEmpty(mStoreName)
				&& !isEmpty(mYelpId)) {

			if (mCurrentFilePath != null) {
				String imageName = new File(mCurrentFilePath).getName();
				String imageFile = getEncodedImageString();
				BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
						.getInstance(this);
				showProgressDialog();
				buyopicNetworkServiceManager.sendsetUpStoreRequest(
						Constants.REQUEST_MERCHANT_SETUP, mMerchantStoreId
								.getText().toString(), mMerchantId.getText()
								.toString(), mStoreName.getText().toString(),
						mAddress.getText().toString(), mCity.getText()
								.toString(), mState.getText().toString(),
						mCountry.getText().toString(), mPincode.getText()
								.toString(), mYelpId.getText().toString(),
						mLatitude.getText().toString(), mLongitude.getText()
								.toString(), imageName, imageFile, this);

			} else {
				Utils.showToast(this, "Please add the Store Image");
			}

		} else {
			Utils.showToast(this, "Please fill the all fields");
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
							mStoreLogo, configureOptions());
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
				.showImageForEmptyUri(android.R.color.transparent)
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
				.considerExifParams(true)
				.showImageOnFail(android.R.color.transparent).cacheOnDisc(true)
				.build();
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
		if (requestCode == Constants.REQUEST_MERCHANT_SETUP) {
			String message = JsonResponseParser
					.parseCreateAlertResponse((String) object);
			Utils.showToast(this, message);
		}
		/*
		 * { "status": "ok", "message": "Store setup completed successfully",
		 * "alert": { "buyopic_store_id": "232342", "buyopic_retailer_id":
		 * "235235" } }
		 */
	}

	@Override
	public void onFailure(int requestCode, String message) {
		dismissProgressDialog();
	}

}
