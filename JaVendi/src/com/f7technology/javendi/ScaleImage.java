package com.f7technology.javendi;

import android.graphics.Bitmap;

public class ScaleImage {

	public static Bitmap ScaleBitmap(Bitmap bm, float scalingFactor) {
		
		int scaleHeight = (int) (bm.getHeight() * scalingFactor);
		int scaleWidth = (int) (bm.getWidth() * scalingFactor);
		
		return Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, true);
	}
}
