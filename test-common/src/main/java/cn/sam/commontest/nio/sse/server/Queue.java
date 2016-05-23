package cn.sam.commontest.nio.sse.server;

import java.util.LinkedList;

/**
 * @author tcowan
 */
public class Queue extends LinkedList
{
	private int waitingThreads = 0;

	public synchronized void insert(Object obj)
	{
		addLast(obj);
		notify();
	}

	public synchronized Object remove()
	{
		if ( isEmpty() ) {
			try	{ waitingThreads++; wait();} 
			catch (InterruptedException e)	{Thread.interrupted();}
			waitingThreads--;
		}
		return removeFirst();
	}

	public boolean isEmpty() {
		return 	(size() - waitingThreads <= 0);
	}
}