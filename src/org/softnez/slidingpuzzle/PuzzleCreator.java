package org.softnez.slidingpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

public class PuzzleCreator {
	private static final double percentage = 0.85;
	
	public static void make_pieces(Context context, int id, Bitmap[] puzzle) {
		int rows = (int) Math.sqrt(puzzle.length);
		int cols = rows;
		
		int scaled_size =
				(int) Math.floor((context.getResources().getDisplayMetrics().widthPixels) * PuzzleCreator.percentage);
		
		int piece_size = scaled_size / rows;
				
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
		
	    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, scaled_size, scaled_size, false);
			    
	    Log.v("PuzzleCreator", "size of scaled = " +scaled.getHeight());
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int nr = i*rows + j;
				puzzle[nr] = Bitmap.createBitmap(scaled, j*piece_size, i*piece_size, piece_size, piece_size);
			}
		}
		
		puzzle[puzzle.length-1].eraseColor(Color.DKGRAY);
	}
}
