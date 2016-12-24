package bgu.spl.a2;

import java.lang.Thread.State;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {
	
	private ConcurrentLinkedDeque<Task<?>>[] listofqueue;
	private Thread[] listofprocessors;
	private VersionMonitor monitor;
	private boolean shutDown=false;

    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     *
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     * thread pool
     */
    public WorkStealingThreadPool(int nthreads) {
    	this.monitor=new VersionMonitor();
    	this.listofqueue=new ConcurrentLinkedDeque[nthreads];
    	this.listofprocessors=new Thread[nthreads];
    	
        for(int i=0;i<nthreads;i++){
        	this.listofqueue[i]=new ConcurrentLinkedDeque<Task<?>>();
        	this.listofprocessors[i]=new Thread(new Processor(i, this));
        }
        
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {
    	int i=(int)(Math.random()*listofprocessors.length);
        this.listofqueue[i].add(task);
        this.monitor.inc();
    }
    


    
    ConcurrentLinkedDeque<Task<?>> getQueue(int id){
    	return this.listofqueue[id];
    }
    
    VersionMonitor getMonitor(){
    	return this.monitor;
    }
    
    boolean steal(int id){
    	int i=(id+1)%this.listofprocessors.length;
    	while(i!=id){
			int n=this.listofqueue[i].size()/2;
			if(n>0){
				Task<?> t=this.listofqueue[i].pollLast();
    			for(int j=0; j<n && t!=null ; j++){
    				this.listofqueue[id].addFirst(t);
    				if(j<n-1) t=this.listofqueue[i].pollLast();
    			}
    			if(!this.listofqueue[id].isEmpty())return true;
    		}
    		i=(i+1)%this.listofprocessors.length;
    	}
    	return false;
    	
    }
    
    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     *
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is
     * interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     * shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {
    	for (Thread thread : this.listofprocessors) {
        	if(thread.getId()==Thread.currentThread().getId()) throw new UnsupportedOperationException("Tried to shut down from one of the processors");
        	 
        }
        this.shutDown=true;
        for (Thread thread : this.listofprocessors) {
        	if(thread.getState()==State.WAITING) thread.interrupt();
        	 
        }
        for (Thread thread : this.listofprocessors) {
        	thread.join();
        }
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
    	int n=this.listofprocessors.length;
       for(int i=0;i<n;i++){
    	   this.listofprocessors[i].start();
       }
    }
    
    boolean shutDown(){
    	return this.shutDown;
    }

}
