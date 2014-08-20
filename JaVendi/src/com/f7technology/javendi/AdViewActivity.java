package com.f7technology.javendi;

import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.f7technology.javendi.R;
import com.f7technology.javendi.models.AdModel;
import com.facebook.Session;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class AdViewActivity extends SharedCode implements AsyncResponse {

	Button buy, back;
	Context context;
	String position = null;
	String feedUrl = null;
	TextView viewDescription;
	String description;
	TextView viewPrice;
	String price;
	ImageView img;
	String image;
	AdModel jParser = new AdModel();
	String mailTo = "";
	String categoryName;
	int adId, id;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_view);
		
		viewDescription = (TextView) findViewById(R.id.ad_view_description);
		viewPrice = (TextView) findViewById(R.id.ad_view_price);
		img = (ImageView)findViewById(R.id.ad_view_photo_image);
		
		Intent ii = getIntent();
		Bundle ee = ii.getExtras();
		categoryName = ee.getString("categoryName");
		
		setTitle(categoryName);
		setDrawer();
		setAdMob();
		
		context = this;
		AdViewTask loaderTask = new AdViewTask();
		loaderTask.delegate = this;
		loaderTask.execute();
		
		onClickBuy();
		onClickBack();
	}

	public class AdViewTask extends AsyncTask<Void, Void, JSONArray> {
		
		ImageView img1 = (ImageView)findViewById(R.id.ad_view_photo_image);
		Bitmap newBitmap;
		
		public AsyncResponse delegate = null;
		ProgressDialog dialog;
		String sellerEmail = "";
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setTitle("Loading ads");
			dialog.show();
			super.onPreExecute();
		}
		
		private float getBitmapScalingFactor(Bitmap bm) {
			
			DisplayMetrics displaymetrics = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int screenWidth = displaymetrics.widthPixels;
			
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.img1.getLayoutParams();
			int leftMargin = layoutParams.leftMargin;
			int rightMargin = layoutParams.rightMargin;
			
			int imageViewWidth = screenWidth - (leftMargin + rightMargin);
			
			return ((float) imageViewWidth / (float) bm.getWidth());
		}
		
		@Override
		protected JSONArray doInBackground(Void... params) {
			
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			adId = extras.getInt("adId");
			
			AdModel jParser = new AdModel();
			JSONObject jsonObj = jParser.getJSONObjFromUrl("http://192.168.1.15:3000/ads/"+ String.valueOf(adId) +".json");
			
			try {
				JSONObject ad = jsonObj;
				id = ad.getInt("id");
				price = ad.getString("price");
				description = ad.getString("description");
				image = ad.getString("image");
				sellerEmail = ad.getString("user_email");
			} catch (JSONException ex) {
				ex.printStackTrace();
			}

			final String imageURL = "http://192.168.1.15:3000" + image;
			try {
		        URL url = new URL(imageURL);
		        HttpGet httpRequest = null;
		        httpRequest = new HttpGet(url.toURI());
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpResponse response = (HttpResponse) httpclient
		                .execute(httpRequest);

		        HttpEntity entity = response.getEntity();
		        BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
		        InputStream input = b_entity.getContent();

		        Bitmap bitmap = BitmapFactory.decodeStream(input);
		        float scalingFactor = this.getBitmapScalingFactor(bitmap);
		        newBitmap = ScaleImage.ScaleBitmap(bitmap, scalingFactor);
		        
		        
		    } catch (Exception ex) {
		    	ex.printStackTrace();
		    }
			
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
					viewPrice.setText(price);
					viewDescription.setText(description);
					img.setImageBitmap(newBitmap);
					
				}
				
			});
			
			return null;
		}

		@Override
		protected void onPostExecute(JSONArray json) {
			dialog.dismiss();
			delegate.processFinish(sellerEmail);
		}
	}
	
	@Override
	public void processFinish(String output) {
		mailTo = output;
	}
	
	private void onClickBuy() {
		final Context context = this;
		buy = (Button) findViewById(R.id.ad_view_buy);
		buy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { mailTo });
				email.setType("message/rfc822");
				
				startActivity(Intent.createChooser(email, "Choose an Email client :"));
			}
		});
	}
	

	private void onClickBack() {
		final Context context = this;
		back = (Button) findViewById(R.id.ad_view_back);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(context, AdListActivity.class);
				intent.putExtra("categoryName", categoryName);
				startActivity(intent);
				
			}
		});
	}

}