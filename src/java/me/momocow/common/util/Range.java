package me.momocow.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Range extends ArrayList<Integer>{

	private static final long serialVersionUID = 1756855152704925973L;

	public Range () {
		this(0);
	}
	
	public Range (int size) {
		this(0, size);
	}
	
	public Range (int start, int size) {
		super();
		
		for (int i = start; i < start + size; i++) {
			this.add(i);
		}
	}
	
	@Override
	public boolean add(Integer e) {
		return false;
	}
	
	@Override
	public void add(int index, Integer element) { }
	
	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		return false;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends Integer> c) {
		return false;
	}
	
	@Override
	public boolean remove(Object o) {
		return false;
	}
	
	@Override
	public void ensureCapacity(int minCapacity) { }
	
	@Override
	public Integer remove(int index) {
		return null;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}
	
	@Override
	public boolean removeIf(Predicate<? super Integer> filter) {
		return false;
	}
	
	@Override
	public void sort(Comparator<? super Integer> c) { }
	
	@Override
	public void replaceAll(UnaryOperator<Integer> operator) { }
	
	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}
	
	/**
	 * Convenience utility to fetch a Range without the <i>new</i> statement.
	 * @return
	 */
	public static Range $ () {
		return new Range();
	}
	
	/**
	 * Convenience utility to fetch a Range without the <i>new</i> statement.
	 * @return
	 */
	public static Range $ (int size) {
		return new Range(size);
	}
	
	/**
	 * Convenience utility to fetch a Range without the <i>new</i> statement.
	 * @return
	 */
	public static Range $ (int start, int size) {
		return new Range(start, size);
	}
}
