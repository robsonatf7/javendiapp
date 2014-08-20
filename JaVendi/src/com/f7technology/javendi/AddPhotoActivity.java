package com.f7technology.javendi;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.ContentBody;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import com.f7technology.javendi.R;
import com.facebook.Session;

//import com.adapp.CategoryListActivity.CategoryListTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class AddPhotoActivity extends SharedCode {

	Button finishAd;
	Button addPicture;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	static File mediaFile;
	
	private DrawerLayout drawerLayout;
	private ListView featuresList;
	private String[] features;
	
	private static Uri getUri(int type){
		
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraOutput");
		
		if (! mediaStorageDir.exists()) {
			if (! mediaStorageDir.mkdirs()) {
				Log.d("CameraOutput", "Nao foi possivel criar o diretorio");
				return null;
			}
		}
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		//File mediaFile;
		
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}
		return Uri.fromFile(mediaFile);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_photo);
		
		setTitle("New ad");
		setDrawer();
		setAdMob();
		
		onClickAddPicture();
		onClickFinishAd();
	}

	private void onClickAddPicture(){
		
		addPicture = (Button)findViewById(R.id.add_photo_add_picture);
		addPicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = getUri(MEDIA_TYPE_IMAGE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "imagem salva", Toast.LENGTH_SHORT).show();
				ImageView imageView = (ImageView)findViewById(R.id.add_photo_caught_picture);
				imageView.setImageURI(fileUri);
			} else if(resultCode == RESULT_CANCELED) {
				
			} else {
				
			}
		}
	}
	
	private class ImageUploader extends AsyncTask<Void, Void, String> {
		
		@Override
		protected String doInBackground(Void... params) {
			
			String result = "";
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPostRequest = new HttpPost("http://192.168.0.11:3000/ads");
			try {

				Intent i = getIntent();
				String adCategory = i.getStringExtra("categoryId");
				String adTitle = i.getStringExtra("title");
				String adPrice = i.getStringExtra("price");
				String adDescription = i.getStringExtra("description");
				String adUserEmail = i.getStringExtra("userEmail");
				int catId = Integer.parseInt(adCategory);

				JSONObject json = new JSONObject();
				json.put("category_id", adCategory);
				json.put("title", adTitle);
				json.put("price", adPrice);
				json.put("description", adDescription);
				json.put("user_email", adUserEmail);
				StringEntity adParams = new StringEntity(json.toString());
				adParams.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				httpPostRequest.setEntity(adParams);

				// Execute POST request to the given URL
				HttpResponse httpResponse = null;
				httpResponse = httpClient.execute(httpPostRequest);

				// receive response as inputStream
				InputStream inputStream = null;
				inputStream = httpResponse.getEntity().getContent();

				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";
				return result;
			} catch (Exception e) {
				return null;
			}

			// return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}
 
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}
	
	private void onClickFinishAd() {
		
		final Context context = this;
		finishAd = (Button) findViewById(R.id.ad_photo_finish_button);
		finishAd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
      
				ImageUploader loaderTask = new ImageUploader();
				loaderTask.execute();
				
				Intent intent = new Intent(context, AdListActivity.class);
				startActivity(intent);
			}
		});
	}
}