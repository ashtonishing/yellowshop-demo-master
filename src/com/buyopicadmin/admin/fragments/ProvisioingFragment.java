package com.buyopicadmin.admin.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.buyopic.android.network.BuyopicNetworkCallBack;
import com.buyopic.android.network.BuyopicNetworkServiceManager;
import com.buyopic.android.network.Constants;
import com.buyopic.android.network.JsonResponseParser;
import com.buyopic.android.network.Utils;
import com.buyopicadmin.admin.BBIDSetupActivity;
import com.buyopicadmin.admin.BuyOpic;
import com.buyopicadmin.admin.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ProvisioingFragment extends Fragment implements OnClickListener, BuyopicNetworkCallBack {

	private EditText merchantYelpId;
	private EditText merchantIdView;
	private EditText storeIdView;
	private EditText bbidSpinnerView;
	private Activity context;
	private BBIDSetupActivity bbidSetupActivity;
	protected String mCurrentFilePath;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView mStoreLogoView;
	private String userid;
	protected static final int GALLERY_PICTURE = 1000;
	protected static final int CAMERA_PICTURE = 1001;
	public static final int SCALE_WIDTH = 500;
	public static final int SCALE_HEIGHT = 500;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BuyOpic buyOpic = (BuyOpic) context.getApplication();
		userid = buyOpic.getmMerchantId();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_provisioning, null);
		Utils.overrideFonts(context, view);
		merchantIdView = (EditText) view
				.findViewById(R.id.layout_provisioing_merchant_id);
		storeIdView = (EditText) view
				.findViewById(R.id.layout_provisioing_store_id);
		merchantYelpId = (EditText) view
				.findViewById(R.id.layout_provisioing_yelp_id);
		bbidSpinnerView = (EditText) view
				.findViewById(R.id.layout_provisioing_spinner_bbid);
		view.findViewById(R.id.layout_provisioing_button_save)
				.setOnClickListener(this);
		view.findViewById(R.id.layout_provisioing_button_clear)
				.setOnClickListener(this);
		view.findViewById(R.id.layout_provisioing_image).setOnClickListener(
				this);
		mStoreLogoView = (ImageView) view
				.findViewById(R.id.layout_provisioing_image);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
		bbidSetupActivity = (BBIDSetupActivity) activity;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_provisioing_button_save:
			submitDataToServer();
			break;
		case R.id.layout_provisioing_image:
			startDialog();
			break;
		case R.id.layout_provisioing_button_clear:
			Activity activity=context;
			activity.finish();
		default:
			break;
		}
	}

	private void submitDataToServer() {
		if (!bbidSetupActivity.isEmpty(merchantIdView)
				&& !bbidSetupActivity.isEmpty(bbidSpinnerView)
				&& !bbidSetupActivity.isEmpty(storeIdView)) {
			BuyopicNetworkServiceManager buyopicNetworkServiceManager = BuyopicNetworkServiceManager
					.getInstance(context);
			bbidSetupActivity.showProgressDialog();
//			File file=new File(mCurrentFilePath);
			
			buyopicNetworkServiceManager.sendProvisioningRequest(
					Constants.REQUEST_PROVISION_ING, bbidSpinnerView
							.getText().toString(),storeIdView
							.getText().toString(), merchantIdView.getText()
							.toString(), userid, merchantYelpId.getText()
							.toString(),"",
					"", this);
		} else {
			Utils.showToast(context, "Please fill all the fields");
		}

	}

	private void startDialog() {

		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
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

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case GALLERY_PICTURE:
				Uri selectedImageUri = data.getData();
				mCurrentFilePath = getPath(selectedImageUri);

			case CAMERA_PICTURE:
				if (mCurrentFilePath != null) {
					imageLoader.displayImage("file:///" + mCurrentFilePath,
							mStoreLogoView, configureOptions());
				}

				break;
			}
		}
	}

	private String getPath(Uri selectedImageUri) {
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		String path = null;
		CursorLoader cursorLoader = new CursorLoader(context, selectedImageUri,
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
			byte[] byteArray = decodeSampledBitmapFromFile();
			return byteArray != null ? Base64.encodeToString(byteArray,
					Base64.URL_SAFE) : "Unable to Encode Image With Base64";
		} else {
			return null;
		}

	}

	private byte[] convertFileIntoBase64() {

		if (mCurrentFilePath != null) {
			File file = new File(mCurrentFilePath);
			byte[] data = new byte[(int) file.length()];
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			FileInputStream fileInputStream = null;
			BitmapDrawable bitmapDrawable = (BitmapDrawable) mStoreLogoView
					.getDrawable();
			bitmapDrawable.getBitmap();

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
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(false)
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
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
		bbidSetupActivity.dismissProgressDialog();
		switch (requestCode) {
		case Constants.REQUEST_PROVISION_ING:
			HashMap<String,String> hashMap=JsonResponseParser.parsePreprovisioingResponse((String)object);
			if(hashMap!=null && hashMap.containsKey("message"))
			{
				Utils.showToast(context, hashMap.get("message"));
			}
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onFailure(int requestCode, String message) {
		// TODO Auto-generated method stub
		bbidSetupActivity.dismissProgressDialog();
	}

}
