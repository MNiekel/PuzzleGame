package org.softnez.slidingpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.util.Log;

@SuppressWarnings("unused")

public class RasterView extends LinearLayout {

	private int rows = 0;
	private Context context;
	private Bitmap[] puzzle3x3 = new Bitmap[9];
	private Bitmap[] puzzle4x4 = new Bitmap[16];

	public RasterView(Context context) {
		super(context);
		this.context = context;
	}
	
	public RasterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	public void init(int s) {
		rows = s;
	}
	
	public void build() {
		
		if (getChildCount() > 0) {
			removeAllViews();
		}
				    
		setWeightSum(rows);
		
		for (int x = 0; x < rows; x++) {
			ColView col = new ColView(context, rows, x, (OnTouchListener) context);
			addView(col);
		}
	}
	
	public void update(Level level, Bitmap[] puzzle) {
		if (level.size() != puzzle.length) {
			Log.e("RasterView", "Uncompatible puzzle and level");
			return;
		}
		for (int i = 0; i < level.size(); i++) {
			ImageView v = (ImageView) findViewById(i);
			v.setImageBitmap(puzzle[level.get(i)]);
		}
	}
	
	public void reset(Level level, Bitmap[] puzzle) {
		int s = (int) Math.sqrt(level.size());
		if (s != rows) {
			rows = s;
			build();
		}
		update(level, puzzle);
	}

	private class ColView extends LinearLayout {
		
		public ColView(Context context, int size, int row, OnTouchListener listener) {
			super(context);
			setOrientation(VERTICAL);
			setWeightSum(size);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
			setLayoutParams(params);
			
			for (int x = 0; x < size; x++) {
				int id = x*size + row;
				ImageView image = new ImageView(context);
				params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
				image.setId(id);
				image.setAdjustViewBounds(true);
				image.setScaleType(ScaleType.FIT_CENTER);
				image.setLayoutParams(params);
				image.setOnTouchListener(listener);
				addView(image);
			}
		}
	}	
}
