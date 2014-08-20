package com.f7technology.javendi.adapters;

import com.f7technology.javendi.R;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout.LayoutParams;

public class ImageSwipeAdapter extends PagerAdapter {
	
	Context context;
	private int[] GalImages = new int[] { R.drawable.one,
										  R.drawable.two,
										  R.drawable.three,
										  R.drawable.four };
	
	public ImageSwipeAdapter (Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return GalImages.length;
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
//		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imageView.setImageResource(GalImages[position]);
		((ViewGroup) container).addView(imageView, 0);
		return imageView;
	}
	
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewGroup) container).removeView((ImageView) object);
	}
}
