package com.f7technology.javendi;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

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
import com.f7technology.javendi.adapters.MainListAdapter;
import com.f7technology.javendi.models.AdModel;
import com.f7technology.javendi.models.CategoryModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends SharedCode {
	
	Context context;
	String feedUrl, catName;
	Button sell, more;
	ArrayList<SpinnerCategory> spinnerCategories = new ArrayList<SpinnerCategory>();
	ArrayList<String> spinnerCategoryNames = new ArrayList<String>();
	ArrayList<Integer> spinnerCategoryIds = new ArrayList<Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		setTitle("Ja Vendi!");
//		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
//		TextView abTitle = (TextView) findViewById(titleId);
//		abTitle.setTextColor(Color.WHITE);
		setDrawer();
		setAdMob();
		
		onClickMore();
		onClickSell();
	
		context = this;
		MainListTask loaderTask = new MainListTask();
		loaderTask.execute();
		
	}

	private class ListClickHandler implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> Adapter, View view, int position, long id) {

//			Intent y = getIntent();
//			catName = y.getStringExtra("categoryName");
//			String catUrl = catName.replaceAll("\\s", "%20");
//			String feedUrl = "http://192.168.0.11:3000/ads.json?category_name="+ catUrl;
//			
//			Intent intent = new Intent(context, AdViewActivity.class);
//			Bundle extras = new Bundle();
//			extras.putString("feedUrl", feedUrl);
//			extras.putString("position", String.valueOf(id));
//			extras.putString("categoryUrl", catUrl);
//			extras.putString("categoryName", catName);
//			intent.putExtras(extras);
//			startActivity(intent);
		}
	}
	
	public class MainListTask extends AsyncTask<Void, Void, JSONArray> {
		
		ProgressDialog dialog;
		Bitmap bmp;
		ArrayList<String> adsTitlesArray = new ArrayList<String>();
		ArrayList<Bitmap> adsBitmapsArray = new ArrayList<Bitmap>();
		ArrayList<String> imageUrls = new ArrayList<String>();
		String adsUrl = "http://192.168.1.15:3000/ads.json";
		String catsUrl = "http://192.168.1.15:3000/categories.json";
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setTitle("Loading ads");
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected JSONArray doInBackground(Void... params) {

			CategoryModel jCatsParser = new CategoryModel();
			JSONArray catsJson = jCatsParser.getJSONFromUrl(catsUrl);
			
			try {
				for (int i = 0; i < catsJson.length(); i++) {
					
					JSONObject category = catsJson.getJSONObject(i);
					SpinnerCategory spinnerCategory = new SpinnerCategory();
					
					spinnerCategory.setName(category.optString("name"));
					spinnerCategoryNames.add(category.optString("name"));
					
					spinnerCategory.setId(category.optInt("id"));
					spinnerCategoryIds.add(category.optInt("id"));
					
					spinnerCategories.add(spinnerCategory);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			AdModel jParser = new AdModel();
			JSONArray json = jParser.getJSONFromUrl(adsUrl);
			
			try {
				for (int i = 0; i < json.length(); i++) {
					JSONObject ad = json.getJSONObject(i);
					String title = ad.getString("title");
					adsTitlesArray.add(title);
					String image = ad.getString("image");
					imageUrls.add(image);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < imageUrls.size(); i++) {
				
				String imgUrl = "http://192.168.1.15:3000" + imageUrls.get(i);
				
				try {
			        URL url = new URL(imgUrl);
			        HttpGet httpRequest = null;
			        httpRequest = new HttpGet(url.toURI());
			        HttpClient httpclient = new DefaultHttpClient();
			        HttpResponse response = (HttpResponse) httpclient
			                .execute(httpRequest);

			        HttpEntity entity = response.getEntity();
			        BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
			        InputStream input = b_entity.getContent();

			        bmp = BitmapFactory.decodeStream(input);
			        
			    } catch (Exception ex) {
			    	ex.printStackTrace();
			    }
				
				adsBitmapsArray.add(bmp);
			}

			
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
//					ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//					SwipeAdapter adapter = new SwipeAdapter(getApplicationContext(), adsBitmapsArray);
//					viewPager.setAdapter(adapter);
					
					final Spinner mySpinner = (Spinner) findViewById(R.id.main_spinner);
					mySpinner.setAdapter (new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerCategoryNames));
					mySpinner.post(new Runnable() {
					    public void run() {
					    	mySpinner.setOnItemSelectedListener(new SpinnerClickHandler());
					    }
					});
					
					MainListAdapter mainListAdapter = new MainListAdapter(context, adsTitlesArray, adsBitmapsArray);
					GridView gridView = (GridView) findViewById(R.id.main_list);
					gridView.setAdapter(mainListAdapter);
			
					gridView.setOnItemClickListener(new ListClickHandler());
				}
				
			});
			
			return json;
			
		}
		
		@Override
		protected void onPostExecute(JSONArray json) {
			dialog.dismiss();
			super.onPostExecute(json);
		}
	}
	
	private class SpinnerClickHandler implements OnItemSelectedListener {
		
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			TextView listText = (TextView) view.findViewById(android.R.id.text1);
			String categoryName = listText.getText().toString();

			Intent intent = new Intent(context, AdListActivity.class);
			intent.putExtra("categoryName", categoryName);
			startActivity(intent);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private void onClickSell() {
		
		sell = (Button) findViewById(R.id.main_sell);
		sell.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
      
//				final Context context = getApplicationContext();
				Intent intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	private void onClickMore() {
		
		more = (Button) findViewById(R.id.main_more);
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
      
//				final Context context = getApplicationContext();
				Intent intent = new Intent(context, AdListActivity.class);
				startActivity(intent);
				
			}
		});
	}
}
