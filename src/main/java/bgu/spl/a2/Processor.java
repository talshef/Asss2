package bgu.spl.a2;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool pool;
    private final int id;

    /**
     * constructor for this class
     *
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id - the processor id (every processor need to have its own unique
     * id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.pool = pool;
    }
    
    void addToMyQueue(Task<?>... task){
    	for(int i=0;i<task.length;i++){
    		this.pool.getQueue(this.id).addFirst(task[i]);
    		this.pool.getMonitor().inc();
    	}
    }
    
    @Override
    public void run() {
    	ConcurrentLinkedDeque<Task<?>> myqueue=this.pool.getQueue(id);
    	VersionMonitor monitor=this.pool.getMonitor();
    	while(!this.pool.shutDown()){      ////TODO 
	    	while(!myqueue.isEmpty()&&!this.pool.shutDown()){
	    		myqueue.pollFirst().handle(this);
	    	}
	    	if(!pool.steal(this.id)){
		    	try{
		    		monitor.await(monitor.getVersion());
		    	}
		    	catch (InterruptedException e){
		    		//System.out.println("fuck");
		    	}
	    	}
    	}
    }

}
