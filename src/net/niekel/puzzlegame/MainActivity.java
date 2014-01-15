package net.niekel.puzzlegame;

import java.util.ArrayList;

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
	
	private Level level = new Level();
	
	private int steps = 20;
	
	private int size;
	private int turn;
	private boolean solved;
	private RasterView raster;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			size = 3;
			turn = 0;
			solved = true;
			level.build(size*size);
		} else {
			solved = savedInstanceState.getBoolean(STATE_SOLVED);
			turn = savedInstanceState.getInt(STATE_TURN);
			size = savedInstanceState.getInt(STATE_SIZE);
			level.copy(savedInstanceState.getIntegerArrayList(STATE_LEVEL));
		}
		
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public void onStart() {
		super.onStart();
				
		raster = (RasterView) findViewById(R.id.rasterView);
		raster.init((int) Math.sqrt(level.size()));
		raster.build();
		raster.update(level);
		
		update_button();
	}
    
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
    	savedInstanceState.putBoolean(STATE_SOLVED, solved);
    	savedInstanceState.putInt(STATE_SIZE, size);
    	savedInstanceState.putInt(STATE_TURN, turn);
    	savedInstanceState.putIntegerArrayList(STATE_LEVEL, (ArrayList<Integer>) level);
        super.onSaveInstanceState(savedInstanceState);
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
	        */
	        case R.id.dev:
	        	dev();
	        	return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void end() {
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
		raster.reset(level);
		solved = true;
		turn=0;
		
		update_button();
		clear_message();
	}
	
	private void shuffle() {
		level.shuffle(steps * size);
		raster.update(level);
		turn = 0;
		solved = level.solved();
		update_button();
		clear_message();
	}
	
	public void button_click(View v) {
		if (level.solved()) {
			shuffle();
		} else {
			reset();
		}
	}
	
	private void resize() {
		if (size == 3) { 
			size = 4;
		} else {
			size = 3;
		}
		reset();
	}
	
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
		
		raster.update(level);
		
		if (position == level.size()-1) {
			solved = level.solved();
			if (solved) {
				end();
			}
		}
		return false;
	}
}