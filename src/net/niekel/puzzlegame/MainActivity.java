package net.niekel.puzzlegame;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	static final String STATE_SIZE = "size";
	static final String STATE_TURN = "turn";
	static final String STATE_LEVEL = "level";
	static final String STATE_SOLVED = "solved";
	
	private Level level = new Level();
	
	private int steps = 100;
	
	private int size;
	private int num_cells;
	private int turn;
	private boolean solved;

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
		
		num_cells = size * size;				
		setContentView(R.layout.main_layout);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		build_raster();
		update_raster();
		update_turns(turn);
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
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
	
	private void build_raster() {
		FrameLayout fl = (FrameLayout) findViewById(R.id.containerFrameLayout);
		GridLayout raster;
		
		fl.removeAllViews();
		
		if (size == 4) {
			raster = (GridLayout) getLayoutInflater().inflate(R.layout.layout4x4, null);
		} else {
			raster = (GridLayout) getLayoutInflater().inflate(R.layout.layout3x3, null);
			
		}
		
		//LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.);

		
		for (int i = 0; i < num_cells; i++){
			Button b = new Button(this);
			b.setId(i);
			b.setBackgroundResource(R.drawable.square_icon);
			b.setOnClickListener(this);
			
			raster.addView(b);
		}
		
		fl.addView(raster);
	}	
	
	private void update_raster() {
		Log.d("MainActivity:update_raster", "level = " +level);
		for (int i = 0; i < level.size(); i++) {
			Button b = (Button) findViewById(i);
			int tag = level.get(i);
			
			b.setTag(tag);
			if (tag == -1) {
				b.setText("");
			} else {
				b.setText(Integer.toString(tag));
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		Log.d("MainActivity:onClick", "clicked on position " +v.getId());
		
		if (solved) return;
		
		int position = v.getId();
		int move_to = level.moveable(position);
		
		if (move_to == -1) return;
				
		update_turns(++turn);
		
		level.swap(position, move_to);
		update_raster();
		
		if (position == num_cells-1) {
			solved = level.solved();
			if (solved) {
				Log.v("Puzzle Game", "You solved the puzzle!");
				game_ends();
			}
		}
	}

	private void game_ends() {
		TextView message = (TextView) findViewById(R.id.messageTextView);
		message.setText(R.string.message2);
	}
	
	public void shuffle(View v) {
		TextView message = (TextView) findViewById(R.id.messageTextView);
		message.setText("");
		
		if (!solved) {
			String text = "The puzzle is already shuffled!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(this, text, duration);
			
			toast.show();
			return;
		}
		
		level.shuffle(steps);
		update_raster();
		update_turns(0);
		solved = level.solved();
	}

	private void update_turns(int t) {
		TextView text = (TextView) findViewById(R.id.turnsTextView2);
		text.setText(Integer.toString(t));
		turn = t;
	}
	
	private void reset_raster() {
		level.build(size*size);
		build_raster();
		update_raster();
		solved = true;
	}
	
	public void change_size(View v) {
		if (size == 3) {
			size = 4;
		} else {
			size = 3;
		}
		num_cells = size*size;
		reset_raster();
	}
	
	public void dev() {
		steps = 1;
	}
}