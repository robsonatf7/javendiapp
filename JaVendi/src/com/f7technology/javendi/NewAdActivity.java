package com.f7technology.javendi;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.f7technology.javendi.R;
import com.f7technology.javendi.adapters.MainListAdapter;
import com.f7technology.javendi.models.CategoryModel;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;

public class NewAdActivity extends SharedCode {
	
	Button createAd;
	Context context;
	String feedUrl = "http://192.168.1.15:3000/categories.json";
	ArrayList<SpinnerCategory> spinnerCategories = new ArrayList<SpinnerCategory>();
	ArrayList<String> spinnerCategoryNames = new ArrayList<String>();
	ArrayList<Integer> spinnerCategoryIds = new ArrayList<Integer>();
	
	private DrawerLayout drawerLayout;
	private ListView featuresList;
	private String[] features;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_ad);
		
		setTitle("New ad");
		setDrawer();
		setAdMob();
		
		context = this;
		NewAdSpinnerTask loaderTask = new NewAdSpinnerTask();
		loaderTask.execute();
		
		onClickCreateAd();
		
	}
	
	public class NewAdSpinnerTask extends AsyncTask<Void, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... params) {
			CategoryModel jParser = new CategoryModel();
			JSONArray json = jParser.getJSONFromUrl(feedUrl);
			System.out.println(feedUrl);
			try {
				for (int i = 0; i < json.length(); i++) {
					
					JSONObject category = json.getJSONObject(i);
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
			
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
					final Spinner mySpinner = (Spinner) findViewById(R.id.new_ad_category);
					mySpinner.setAdapter (new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerCategoryNames));
//					mySpinner.post(new Runnable() {
//					    public void run() {
//					    	mySpinner.setOnItemSelectedListener(new SpinnerClickHandler());
//					    }
//					});
				}
				
			});
			
			return json;
		}
		
		protected void onPostExecute(JSONArray json){
//			dialog.dismiss();
			super.onPostExecute(json);
		}
		
	}
	
	private void onClickCreateAd() {
		
		final Context context = this;
		createAd = (Button) findViewById(R.id.new_ad_button);
		createAd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Spinner adCategory = (Spinner)findViewById(R.id.new_ad_category);
				String categoryName = (String) adCategory.getItemAtPosition(adCategory.getSelectedItemPosition());
				int position = -1;
				for (String s : spinnerCategoryNames) {
					position++;
					if (s.equals(categoryName)) {
						break;
					}
				}
				String value1 = spinnerCategoryIds.get(position).toString();
				
				TextView adTitle = (TextView)findViewById(R.id.new_ad_title);
				String value2 = adTitle.getText().toString();
				
				TextView adPrice = (TextView)findViewById(R.id.new_ad_price);
				String value3 = adPrice.getText().toString();
				
				TextView adDescription = (TextView)findViewById(R.id.new_ad_description);
				String value4 = adDescription.getText().toString();
				
				Intent getUserData = getIntent();
				String value5 = getUserData.getStringExtra("userEmail");
				
				Intent intent = new Intent(context, AddPhotoActivity.class);
				Bundle extras = new Bundle();
				
				extras.putString("categoryId", value1);
				extras.putString("title", value2);
				extras.putString("price", value3);
				extras.putString("description", value4);
				extras.putString("userEmail", value5);
				intent.putExtras(extras);
				
				startActivity(intent);
			}
		});
	}
}
