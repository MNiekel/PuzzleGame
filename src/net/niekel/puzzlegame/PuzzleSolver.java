package net.niekel.puzzlegame;

import java.util.ArrayList;

import android.util.Log;

public class PuzzleSolver {
	
	private Level start = new Level();
	private int max_depth;
	private int min_steps = -1;
	private boolean solvable = false;
	private ArrayList<Level> searched = new ArrayList<Level>();

	public PuzzleSolver(int max) {
		// TODO Auto-generated constructor stub
		max_depth = max + 1;
		max_depth = 5;
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
	
	private boolean test2(Level l, int steps) {
		//Log.d("test", "steps = " +steps);
		if (l == null) {
			return false;
		}
		
		if (steps > max_depth) {
			return false;
		}
		
		if (searched.contains(l)) {
			//Log.d("test", "level in searched " +l);
			return false;
		}
		
		searched.add(l);
		
		if (l.solved()) {
			Log.v("test", "level solved in steps: " +steps);
			solvable = true;
			max_depth = Math.min(steps, max_depth);
			return true;
		}
		
		steps++;
		
		Level right = new Level();
		right.copy(l);
		Log.v("test", "testing right " +steps);
		if (!test2(get_right_branch(right), steps)) {
			Level down = new Level();
			down.copy(l);
			Log.v("test", "testing down " +steps);
			if (!test2(get_down_branch(down), steps)) {
				Level left = new Level();
				left.copy(l);
				Log.v("test", "testing left " +steps);
				if (!test2(get_left_branch(left), steps)) {
					Level up = new Level();
					up.copy(l);
					Log.v("test", "testing up " +steps);
					if (!test2(get_up_branch(up), steps)) {
						return false;
					}
				}
			}
		}
		return true;
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
		
		//test(start, 0);
		boolean result = test2(start, 0);
		
		if (result) {
			Log.d("solve", "found solution");
		} else {
			Log.d("solve", "no solution");
		}
		
		//Log.d("solve", "searched = " +searched);
		
		if (solvable) {
			return max_depth;
		}
		return min_steps;
	}
	
	public boolean find_solution(Level level, int depth) {
		if (level == null) return false;
		if (level.solved()) {
			Log.d("find_solution", "solved level, depth = " +depth);
			return true;
		}
		
		if (depth >= max_depth) return false;
		
		depth++;
				
		Level right = new Level();
		right.copy(level);
		Level left = new Level();
		left.copy(level);
		Level up = new Level();
		up.copy(level);
		Level down = new Level();
		down.copy(level);
		
		Level[] children = {get_right_branch(right), get_left_branch(left), get_down_branch(down), get_up_branch(up)};
		//Level[] children = {get_right_branch(right) };
		
		Log.v("find_solution", "children = " +children);
		
		for (Level child : children) {
			if (child != null) {
				if (child.solved()) {
					Log.d("find_solution", "solved level, depth = " +depth);
					max_depth = depth;
					min_steps = depth;
					return true;
				}
			}
		}
		
		for (Level child : children) {
			Log.v("find_solution", "child = " +child);
			
			if (find_solution(child, depth)) {
				Log.v("find_solution", "depth = " +depth);
				min_steps = Math.max(depth, min_steps);
				return true;
			}
		}
		
	return false;
	}
}
