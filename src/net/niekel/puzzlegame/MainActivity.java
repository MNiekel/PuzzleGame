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
import android.widget.Toast;

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
	            change_size(null);
	            return true;
	        case R.id.reset:
	            reset_raster();
	            return true;
	        case R.id.dev:
	            dev();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void game_ends() {
		String text = "You solved the puzzle in " +turn+ " turns!";
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(this, text, duration);
		
		toast.show();
		return;
	}
	
	public void shuffle(View v) {
		Button button = (Button) findViewById(R.id.shuffleButton);
		button.setText("Shuffle");
		
		if (!solved) {
			String text = "The puzzle is already shuffled!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(this, text, duration);
			
			toast.show();
			return;
		}
		
		level.shuffle(steps * size);
		raster.update(level);
		turn = 0;
		solved = level.solved();
	}
	
	private void reset_raster() {
		level.build(size*size);
		raster.reset(level);
		solved = true;
	}
	
	public void change_size(View v) {
		if (size == 3) { 
			size = 4;
		} else {
			size = 3;
		}
		reset_raster();
	}
	
	public void dev() {
		steps = 1;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int id = view.getId();
		Log.v("MainActivity:onTouch", "id = "+id);
				
		if (level.solved()) return false;
		
		int position = view.getId();
		int steps = level.slide(position);
				
		if (steps == 0) return false;
		
		turn += steps;
		Log.v("onTouch", "steps = " +steps);
		Log.v("onTouch", "Turns taken so far: " +turn);
		
		raster.update(level);
		
		if (position == level.size()-1) {
			solved = level.solved();
			if (solved) {
				Log.v("Puzzle Game", "You solved the puzzle!");
				game_ends();
			}
		}
		return false;
	}
}