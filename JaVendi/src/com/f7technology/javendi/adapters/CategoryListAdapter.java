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


public class CategoryListAdapter extends ArrayAdapter<String> {
	
	private final Context context;
	private final ArrayList<String> values;
	private final ArrayList<Bitmap> bitmaps;
	
	
	public CategoryListAdapter(Context context, ArrayList<String> values, ArrayList<Bitmap> bitmaps) {
		super(context, R.layout.category_row, values);
		this.context = context;
		this.values = values;
		this.bitmaps = bitmaps;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.category_row, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.category_row_text);
		textView.setText(values.get(position));
		
		ImageView imageView = (ImageView)rowView.findViewById(R.id.category_row_img);
		imageView.setImageBitmap(bitmaps.get(position));
		
		return rowView;
	}

}
