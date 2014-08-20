package com.f7technology.javendi;

import com.facebook.Session;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SharedCode extends ActionBarActivity implements OnItemClickListener {

	private DrawerLayout drawerLayout;
	private ListView featuresList;
	private String[] features;
	private ActionBarDrawerToggle drawerListener;
	
/** Navigation Drawer -----------------------------------------------------*/
	public void setDrawer() {
		Session session = Session.getActiveSession();
		if(session != null && session.isOpened()) {
			features = getResources().getStringArray(R.array.loggedin);
		} else {
			features = getResources().getStringArray(R.array.loggedout);
		}
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		featuresList = (ListView) findViewById(R.id.left_drawer);
		featuresList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_view, features));
		featuresList.setOnItemClickListener(this);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
	    drawerLayout.setDrawerListener(drawerListener);
	    getSupportActionBar().setHomeButtonEnabled(true);
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		} else {
			onSearchRequested();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	drawerListener.syncState();
    }
	
	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		
		TextView text = (TextView) arg1.findViewById(R.id.text1);
		String string = text.getText().toString();
		System.out.println(string);
		
		if (string.equals("Categories")) {
			
			final Context context = this;
			Intent intent = new Intent(context, CategoryListActivity.class);
			startActivity(intent);
			
		} else if (string.equals("Log in")){
			
			final Context context = this;
			Intent intent = new Intent(context, LoginActivity.class);
			startActivity(intent);
		
		} else if (string.equals("New ad")){
			
			final Context context = this;
			
			Intent getUserData = getIntent();
			String userEmail = getUserData.getStringExtra("user_email");
			
			Intent intent = new Intent(context, NewAdActivity.class);
			intent.putExtra("userEmail", userEmail);
			startActivity(intent);
			
		} else if (string.equals("My ads")){
			
			final Context context = this;

			Intent getUserData = getIntent();
			String userEmail = getUserData.getStringExtra("user_email");
					
			Intent intent = new Intent(context, AdListActivity.class);
			intent.putExtra("userEmail", userEmail);
			startActivity(intent);
			
		} else if (string.equals("Log out")) {
			Session session = Session.getActiveSession();
			session.closeAndClearTokenInformation();
			
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
		}
		
	}
/** Navigation Drawer End------------------------------------------------------*/
	
	public void setTitle (String title) {
		getSupportActionBar().setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public void setAdMob() {
		AdView adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
	}
	
}
