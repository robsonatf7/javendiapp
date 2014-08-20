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
import com.f7technology.javendi.adapters.CategoryListAdapter;
import com.f7technology.javendi.models.CategoryModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryListActivity extends SharedCode {

		CategoryListAdapter categoryListAdapter;
		JSONArray categoriesJson = new JSONArray();
		Context context;
		String feedUrl = "http://192.168.1.15:3000/categories.json";
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.category_list);
			
			setTitle("Categories");
			setDrawer();
			setAdMob();
			
			context = this;
			CategoryListTask loaderTask = new CategoryListTask();
			loaderTask.execute();
		
		}
		
		public class CategoryListTask extends AsyncTask<Void, Void, JSONArray> {
			
			ProgressDialog dialog;
			String img;
			Bitmap bmp;
			ArrayList<String> categoriesNamesArray = new ArrayList<String>();
			ArrayList<Bitmap> categoriesBitmapsArray = new ArrayList<Bitmap>();
			ArrayList<String> imageUrls = new ArrayList<String>();
			
			@Override
			protected void onPreExecute() {
				dialog = new ProgressDialog(context);
				dialog.setTitle("Loading categories");
				dialog.show();
				super.onPreExecute();
			}

			@Override
			protected JSONArray doInBackground(Void... params) {
				
				CategoryModel jParser = new CategoryModel();
				JSONArray json = jParser.getJSONFromUrl(feedUrl);
				
				try{
					for (int i = 0; i < json.length(); i++) {
						
						JSONObject category = json.getJSONObject(i);
						String name = category.getString("name");
						categoriesNamesArray.add(name);
						img = category.getString("image");
						imageUrls.add(img);
						
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
					
					categoriesBitmapsArray.add(bmp);
				}
			
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						CategoryListAdapter categoryListAdapter = new CategoryListAdapter(context, categoriesNamesArray, categoriesBitmapsArray);	
						GridView gridView = (GridView) findViewById(R.id.category_grid_view);
						gridView.setAdapter(categoryListAdapter);
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
		
		private class ListClickHandler implements OnItemClickListener {
			@Override
			public void onItemClick(AdapterView<?> Adapter, View view, int position, long arg3) {
				
				TextView listText = (TextView) view.findViewById(R.id.category_row_text);
				String categoryName = listText.getText().toString();

				Intent intent = new Intent(context, AdListActivity.class);
				intent.putExtra("categoryName", categoryName);
				startActivity(intent);
			}
		}
}