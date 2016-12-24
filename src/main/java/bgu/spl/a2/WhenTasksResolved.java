package bgu.spl.a2;

import java.util.concurrent.atomic.AtomicInteger;

public class WhenTasksResolved implements Runnable {
	AtomicInteger counter;
	Task<?> task;
	//Runnable callback;
	
	public WhenTasksResolved(AtomicInteger n,Task<?> t) {
		this.counter=n;
		this.task=t;
		//this.callback=callback;
		
	}
	@Override
	public void run() {
		counter.decrementAndGet();
		if(counter.get()==0){
			//this.task.AddContinuesTask(callback);
			task.spawn(task);
		}

	}

}
