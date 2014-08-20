package com.f7technology.javendi.adapters;

import java.util.ArrayList;

import com.f7technology.javendi.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SwipeAdapter extends PagerAdapter {

	Context context;
	ArrayList<Bitmap> bitmaps;
	
	public SwipeAdapter (Context context, ArrayList<Bitmap> bitmaps) {
		this.context = context;
		this.bitmaps = bitmaps;
	}
	
	@Override
	public int getCount() {
		return bitmaps.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	@Override
	public Object instantiateItem (ViewGroup container, int position) {
		ImageView imageView = new ImageView (context);
		int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_none);
		imageView.setPadding(padding, padding, padding, padding);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imageView.setImageBitmap(bitmaps.get(position));
		((ViewGroup) container).addView(imageView, 0);
		return imageView;
	}
	
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewGroup) container).removeView((ImageView) object);
	}
	
}
