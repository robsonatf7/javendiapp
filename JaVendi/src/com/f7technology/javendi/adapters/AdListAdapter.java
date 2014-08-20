package com.f7technology.javendi.adapters;

import java.util.ArrayList;

import com.f7technology.javendi.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdListAdapter extends ArrayAdapter<String> {
	
	private final Context context;
	private final ArrayList<String> titles;
	private final ArrayList<Double> prices;
	private final ArrayList<String> locations;
	private final ArrayList<Bitmap> bitmaps;
	
	public AdListAdapter(Context context, ArrayList<String> titles,
						 ArrayList<Double> prices, ArrayList<String> locations, ArrayList<Bitmap> bitmaps) {
		super(context, R.layout.ad_row, titles);
		this.context = context;
		this.titles = titles;
		this.prices = prices;
		this.locations = locations;
		this.bitmaps = bitmaps;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.ad_row, parent, false);
		
		TextView textTitle = (TextView) rowView.findViewById(R.id.ad_row_title);
//		String limited = values.get(position).substring(0,7) + "...";
		textTitle.setText(titles.get(position));
		
		TextView textPrice = (TextView) rowView.findViewById(R.id.ad_row_price);
		textPrice.setText("R$ " + prices.get(position).toString());
		
		TextView textLocation = (TextView) rowView.findViewById(R.id.ad_row_location);
		textLocation.setText(locations.get(position));
		
		ImageView imageView = (ImageView)rowView.findViewById(R.id.ad_row_img);
		imageView.setImageBitmap(bitmaps.get(position));
		
		return rowView;
	}

}