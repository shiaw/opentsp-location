package com.navinfo.opentsp.platform.location.kit.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FixedArray<E> {
	private int size;
	private Queue<E> queue = new ConcurrentLinkedQueue<E>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	public FixedArray(int size) {
		super();
		this.size = size;
	}

	@Override
	public String toString() {
		return "FixedArray [size=" + size + ", queue=" + queue + "]";
	}

	/**
	 * 往集合中添加一个元素<br>
	 * 如果集合大于指定大小<code>size</code>
	 * ,则移除第一个元素,将当前元素添加到末尾.并将移除的元素返回,如果未达到定大小,则直接将元素添加到末尾,返回null
	 * 
	 * @param e
	 * @return
	 */
	public E add(E e) {
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		E ret = null;
		if (this.queue.size() == this.size) {
			ret = this.queue.poll();
		}
		this.queue.add(e);
		writeLock.unlock();
		return ret;
	}

	public List<E> get() {
		Lock readLock = lock.readLock();
		readLock.lock();
		List<E> ret = new ArrayList<E>();
		for (E e : this.queue) {
			ret.add(e);
		}
		readLock.unlock();
		return ret;
	}
	
	public void clear(){
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		queue.clear();
		writeLock.unlock();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
