package net.niekel.puzzlegame;

import java.util.ArrayList;

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
	
	public void swap(int p1, int p2) {
		int tmp = get(p1);
		set(p1, get(p2));
		set(p2, tmp);
	}
	
	public void build(int size) {
		clear();
		for (int i = 0; i < (size-1); i++) {
			add(i, i+1);
		}
		add(size-1, -1);
	}
	
	public ArrayList<Integer> find_neighbours(int pos) {
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
	
	public int moveable(int position) {
		ArrayList<Integer> neighbours = find_neighbours(position);

		for (int n : neighbours) {
			if (get(n) == -1) {
				return n;
			}
		}
		return -1;
	}
	
	public boolean move(int position) {
		int dest = moveable(position);
		
		if (dest == -1) return false;
		swap(position, dest);
		return true;
	}
	
	public void shuffle(int steps) {
		int empty = 0;
		
		for (int i = 0; i < size(); i++) {
			if (get(i) == -1) {
				empty = i;
			}
		}
		
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
		for (int i = 0; i < size()-1; i++) {
			if (get(i) != i+1) return false;
		}
		return true;
	}

}
