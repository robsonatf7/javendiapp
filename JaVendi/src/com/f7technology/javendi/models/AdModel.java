package com.f7technology.javendi.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.media.Image;
import android.widget.ImageView;

import com.f7technology.javendi.R;

public class AdModel {
	
	JSONArray adsJson = new JSONArray();
	JSONObject object = new JSONObject();
	
	public AdModel() {
	}
	
	public JSONArray getJSONFromUrl(String url){
		
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		
		try {
			HttpResponse response = client.execute(getRequest);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			
			if (statusCode != 200) {
				return null;
			}
			
			InputStream jsonStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
			StringBuilder builder = new StringBuilder();
			String line;
			
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			String jsonData = builder.toString();
			
			JSONArray json = new JSONArray(jsonData);
			for (int i = 0; i < json.length(); i++) {
				JSONObject object = json.getJSONObject(i);
				JSONObject ad = object.getJSONObject("ad");
				adsJson.put(ad);
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return adsJson;
	}

	public JSONObject getJSONObjFromUrl(String url){
		
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		
		try {
			HttpResponse response = client.execute(getRequest);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			
			if (statusCode != 200) {
				return null;
			}
			
			InputStream jsonStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
			StringBuilder builder = new StringBuilder();
			String line;
			
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			String jsonData = builder.toString();
			
			object = new JSONObject(jsonData);
			
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return object;
	}
	
}