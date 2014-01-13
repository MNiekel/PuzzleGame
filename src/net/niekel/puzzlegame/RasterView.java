package net.niekel.puzzlegame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class RasterView extends LinearLayout {

	private int rows = 0;
	private Context context;
	private int[] puzzle4x4 = new int[16];
	private int[] puzzle3x3 = new int[9];

	public RasterView(Context context) {
		super(context);
		this.context = context;
	}
	
	public RasterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	public RasterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	public void init(int s) {
		rows = s;
		build_puzzles();
	}
	
	public void build() {
		
		if (getChildCount() > 0) {
			Log.d("RasterView:build", "Delete all children");
			removeAllViews();
		}
		    
		setWeightSum(rows);
		
		for (int x = 0; x < rows; x++) {
			ColView col = new ColView(context, rows, x, (OnTouchListener) context);
			addView(col);
		}
	}
	
	public void update(Level level) {
		for (int i = 0; i < level.size(); i++) {
			ImageView v = (ImageView) findViewById(i);
			v.setImageResource(puzzle4x4[level.get(i)]);
		}
	}
	
	public void reset(Level level) {
		//TODO: implement other sized puzzles
		int s = (int) Math.sqrt(level.size());
		if (s == rows) {
			update(level);
		} else {
			Log.d("RasterView:reset", "New puzzle size differs from current size: Not yet implemented");
		}
	}
	
	private void build_puzzles() {
		puzzle4x4[0] = R.drawable.piece_4x4_0;
		puzzle4x4[1] = R.drawable.piece_4x4_1;
		puzzle4x4[2] = R.drawable.piece_4x4_2;
		puzzle4x4[3] = R.drawable.piece_4x4_3;
		puzzle4x4[4] = R.drawable.piece_4x4_4;
		puzzle4x4[5] = R.drawable.piece_4x4_5;
		puzzle4x4[6] = R.drawable.piece_4x4_6;
		puzzle4x4[7] = R.drawable.piece_4x4_7;
		puzzle4x4[8] = R.drawable.piece_4x4_8;
		puzzle4x4[9] = R.drawable.piece_4x4_9;
		puzzle4x4[10] = R.drawable.piece_4x4_10;
		puzzle4x4[11] = R.drawable.piece_4x4_11;
		puzzle4x4[12] = R.drawable.piece_4x4_12;
		puzzle4x4[13] = R.drawable.piece_4x4_13;
		puzzle4x4[14] = R.drawable.piece_4x4_14;
		puzzle4x4[15] = R.drawable.piece_4x4_15;
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