package net.niekel.puzzlegame;


import java.util.ArrayList;

import android.util.Log;

public class Level extends ArrayList<Integer> {
	
	private static final long serialVersionUID = 1L;

	public Level() {
	}
	
	public void copy(ArrayList<Integer> list) {
		clear();
		for (int i = 0; i < list.size(); i++) {
			add(i, list.get(i));
		}
	}
	
	public void build(int size) {
		//creates an ordered level with size pieces
		clear();
		for (int i = 0; i < size; i++) {
			add(i, i);
		}
	}
	
	public int slide(int position) {
		//slides one or more pieces when possible, returns number of steps
		int empty = get_empty();
		int size = (int) Math.sqrt(size());

		if ((position / size) == (empty / size)) {
			return slide_horizontal(position, empty);
		} else {
			if ((position % size) == (empty % size)) {
				return slide_vertical(position, empty);
			}
		}
		return 0;
	}
	
	public void shuffle(int steps) {
		//shuffles the current state of the level by steps random steps
		int empty = get_empty();
		
		ArrayList<Integer> neighbours;
		int rand;
		int neighbour;
		
		for (int i = 0; i < steps; i++) {
			neighbours = find_neighbours(empty);
			rand = (int) (Math.random() * neighbours.size());
			neighbour = neighbours.get(rand);
			swap(empty, neighbour);
			empty = neighbour;
		}
	}
	
	public boolean solved() {
		//true if level state is start state, else false
		for (int i = 0; i < size(); i++) {
			if (get(i) != i) return false;
		}
		return true;
	}
	
	public int get_left(int position) {
		int size = (int) Math.sqrt(size());
		
		if (position % size != 0) {
			return position - 1;
		}
		return -1;
	}
	
	public int get_right(int position) {
		int size = (int) Math.sqrt(size());
		
		if ((position + 1) % size != 0) {
			return position + 1;
		}
		return -1;
	}
	
	public int get_up(int position) {
		int size = (int) Math.sqrt(size());
		
		if (position >= size) {
			return position - size;
		}
		return -1;
	}	
	
	public int get_down(int position) {
		int size = (int) Math.sqrt(size());
		
		if ((position + size) < size()) {
			return position + size;
		}
		return -1;
	}

	public int get_empty() {
		for (int i = 0; i < size(); i++) {
			if (get(i) == size()-1) {
				return i;
			}
		}
		return -1;
	}
	
	public void swap(int p1, int p2) {
		int tmp = get(p1);
		set(p1, get(p2));
		set(p2, tmp);
	}
	
	private int slide_horizontal(int pos, int empty) {
		int steps = 0;
		
		while (empty < pos) {
			swap(empty, empty+1);
			steps++;
			empty++;
		}
		while (empty > pos) {
			swap(empty, empty-1);
			steps++;
			empty--;
		}
		return steps;
	}
	
	private int slide_vertical(int pos, int empty) {
		int steps = 0;
		int size = (int) Math.sqrt(size());
		
		while (empty < pos) {
			swap(empty, empty+size);
			steps++;
			empty += size;
		}
		while (empty > pos) {
			swap(empty, empty-size);
			steps++;
			empty -= size;
		}
		return steps;
	}	
	
	private ArrayList<Integer> find_neighbours(int pos) {
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		int neighbour;
		
		neighbour = get_right(pos);
		if (neighbour != -1) {
			neighbours.add(neighbour);
		}
		neighbour = get_left(pos);
		if (neighbour != -1) {
			neighbours.add(neighbour);
		}
		neighbour = get_up(pos);
		if (neighbour != -1) {
			neighbours.add(neighbour);
		}
		neighbour = get_down(pos);
		if (neighbour != -1) {
			neighbours.add(neighbour);
		}
				
		return neighbours;
	}

	
	@SuppressWarnings("unused")
	private int moveable(int position) {
		ArrayList<Integer> neighbours = find_neighbours(position);

		for (int n : neighbours) {
			if (get(n) == size()-1) {
				return n;
			}
		}
		return -1;
	}
}
