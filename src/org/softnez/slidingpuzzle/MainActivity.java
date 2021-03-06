package org.softnez.slidingpuzzle;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchListener {
	
	static final String STATE_SIZE = "size";
	static final String STATE_TURN = "turn";
	static final String STATE_LEVEL = "level";
	static final String STATE_SOLVED = "solved";
	static final String STATE_IMAGE = "image";
	
	private Level level = new Level();
	
	private int steps = 30;
	
	private int size;
	private int turn;
	private boolean solved;
	private Bitmap[] puzzle;
	private int image_id;
	private RasterView raster;
	
	private MediaPlayer click_sound;
	private MediaPlayer slide_sound;
	private MediaPlayer solved_sound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		if (savedInstanceState == null) {
			size = 3;
			turn = 0;
			solved = true;
			level.build(size*size);
			image_id = R.drawable.klaproos_256;
		} else {
			solved = savedInstanceState.getBoolean(STATE_SOLVED);
			turn = savedInstanceState.getInt(STATE_TURN);
			size = savedInstanceState.getInt(STATE_SIZE);
			level.copy(savedInstanceState.getIntegerArrayList(STATE_LEVEL));
			image_id = savedInstanceState.getInt(STATE_IMAGE);
		}
		
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		puzzle = new Bitmap[size*size];
		PuzzleCreator.make_pieces(this, image_id, puzzle);
						
		raster = (RasterView) findViewById(R.id.rasterView);
		raster.init((int) Math.sqrt(level.size()));
		raster.build();
		raster.update(level, puzzle);
		
		update_button();
	}
	
	@Override
	public void onResume() {
		click_sound = MediaPlayer.create(this, R.raw.click_sound);
		slide_sound = MediaPlayer.create(this, R.raw.slide_sound);
		solved_sound = MediaPlayer.create(this, R.raw.solved_sound);
		
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
				
		click_sound.stop();
		click_sound.release();
		solved_sound.stop();
		solved_sound.release();
		slide_sound.stop();
		slide_sound.release();
	}
    
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
    	savedInstanceState.putBoolean(STATE_SOLVED, solved);
    	savedInstanceState.putInt(STATE_SIZE, size);
    	savedInstanceState.putInt(STATE_TURN, turn);
    	savedInstanceState.putIntegerArrayList(STATE_LEVEL, (ArrayList<Integer>) level);
    	savedInstanceState.putInt(STATE_IMAGE, image_id);
        super.onSaveInstanceState(savedInstanceState);
    }  
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	        case R.id.resize:
	            resize();
	            return true;
	            
		    /*    
	        case R.id.reset:
	        	reset();
	            return true;
	        case R.id.dev:
	        	dev();
	        	return true;
	        */

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void end() {
		solved_sound.start();
		
		String message = getResources().getString(R.string.message);
		TextView text = (TextView) findViewById(R.id.textView);
		
		message = message + " in " +turn+ " turns.";
		text.setText(message);
		update_button();
	}
	
	private void update_button() {
		Button button = (Button) findViewById(R.id.clickButton);
		if (solved) {
			button.setText(getResources().getString(R.string.start));
		} else {
			button.setText(getResources().getString(R.string.reset));
		}
	}
	
	private void clear_message() {
		TextView text = (TextView) findViewById(R.id.textView);
		
		text.setText("");
	}
	
	private void reset() {
		level.build(size*size);
		
		puzzle = new Bitmap[size*size];
		PuzzleCreator.make_pieces(this, image_id, puzzle);
		
		raster.reset(level, puzzle);
		solved = true;
		turn=0;
		
		update_button();
		clear_message();
	}
	
	private void shuffle() {
		level.shuffle(steps * size);
		raster.update(level, puzzle);
		turn = 0;
		solved = level.solved();
		update_button();
		clear_message();
	}
	
	public void button_click(View v) {
		click_sound.start();
		if (level.solved()) {
			shuffle();
		} else {
			reset();
		}
	}
	
	private void resize() {
		if (size == 3) { 
			size = 4;
			image_id = R.drawable.dalek_256;
		} else {
			size = 3;
			image_id = R.drawable.klaproos_256;
		}
		
		reset();
	}
	
	@SuppressWarnings("unused")
	private void dev() {
		steps = 1;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int id = view.getId();
		Log.d("MainActivity:onTouch", "id = "+id);
				
		if (solved) return false;
		
		int position = view.getId();
		int steps = level.slide(position);
				
		if (steps == 0) return false;
				
		turn += steps;
		Log.d("onTouch", "Turns taken so far: " +turn);
		
		raster.update(level, puzzle);
		
		if (position == level.size()-1) {
			solved = level.solved();
			if (solved) {
				end();
				return false;
			}
		}
		
		slide_sound.start();
		
		return false;
	}
}