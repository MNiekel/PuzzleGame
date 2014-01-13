package net.niekel.puzzlegame;

import java.util.ArrayList;

import android.util.Log;

public class PuzzleSolver {
	
	private Level start = new Level();
	private int max_depth;
	private boolean solvable = false;
	private ArrayList<Level> searched = new ArrayList<Level>();

	public PuzzleSolver(int max) {
		// TODO Auto-generated constructor stub
		max_depth = max + 1;
		//max_depth = 11;
	}
	
	private Level get_left_branch(Level l) {
		int empty = l.get_empty();
		int neighbour = l.get_left(empty);
		if (neighbour != -1) {
			l.swap(empty,  neighbour);
			return l;
		} else {
			return null;
		}
	}
	
	private Level get_right_branch(Level l) {
		int empty = l.get_empty();
		int neighbour = l.get_right(empty);
		if (neighbour != -1) {
			l.swap(empty,  neighbour);
			return l;
		} else {
			return null;
		}
	}	
	private Level get_up_branch(Level l) {
		int empty = l.get_empty();
		int neighbour = l.get_up(empty);
		if (neighbour != -1) {
			l.swap(empty,  neighbour);
			return l;
		} else {
			return null;
		}
	}
	
	private Level get_down_branch(Level l) {
		int empty = l.get_empty();
		int neighbour = l.get_down(empty);
		if (neighbour != -1) {
			l.swap(empty,  neighbour);
			return l;
		} else {
			return null;
		}
	}		
	private void test(Level l, int steps) {
		//Log.d("test", "steps = " +steps);
		if (l == null) {
			return;
		}
		
		if (steps >= max_depth) {
			return;
		}
		
		if (searched.contains(l)) {
			//Log.d("test", "level in searched " +l);
			return;
		}
		
		searched.add(l);
		
		if (l.solved()) {
			Log.v("test", "level solved in steps: " +steps);
			solvable = true;
			max_depth = Math.min(steps, max_depth);
			return;
		}
		
		steps++;
		
		Level right = new Level();
		right.copy(l);
		Log.v("test", "testing right " +steps);
		test(get_right_branch(right), steps);
			
		if (steps >= max_depth) return;
		
		Level down = new Level();
		down.copy(l);
		Log.v("test", "testing down " +steps);
		test(get_down_branch(down), steps);

		if (steps >= max_depth) return;
		
		Level left = new Level();
		left.copy(l);
		Log.v("test", "testing left " +steps);
		test(get_left_branch(left), steps);
		
		if (steps >= max_depth) return;
		
		Level up = new Level();
		up.copy(l);
		Log.v("test", "testing up " +steps);
		test(get_up_branch(up), steps);
			
		return;
	}
	
	public int solve(Level state) {
		
		if (state.solved()) {
			return 0;
		}
		start.copy(state);
		
		test(start, 0);
		
		Log.d("solve", "searched = " +searched);
		
		if (solvable) {
			return max_depth;
		}
		return -1;
	}
}
