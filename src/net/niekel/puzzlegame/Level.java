package net.niekel.puzzlegame;

import java.util.ArrayList;

@SuppressWarnings("unused")

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
		int empty = find_empty();
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
		int empty = find_empty();
		
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
	
	private void swap(int p1, int p2) {
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
		int size = (int) Math.sqrt(size());
		
		int neighbour = pos + 1;
		if ((neighbour % size) != 0) { // right neighbour
			neighbours.add(neighbour);
		}
		
		neighbour = pos + size;
		if (neighbour < size()) { // down neighbour
			neighbours.add(neighbour);
		}
		
		neighbour = pos - 1;
		if ((pos % size) != 0) { // left neighbour
			neighbours.add(neighbour);
		}
		neighbour = pos - size;
		if (neighbour >= 0) { // up neighbour
			neighbours.add(neighbour);
		}
		return neighbours;
	}

	private int find_empty() {
		for (int i = 0; i < size(); i++) {
			if (get(i) == size()-1) {
				return i;
			}
		}
		return -1;
	}
	
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
