package bgu.spl.a2;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class DeferredTest {
	Deferred<Integer> deferred;
	
	@Rule
	public Timeout GlobalTimeout=new Timeout(10000);
	
	
	@Before
	public void setUp() throws Exception {
		this.deferred=new Deferred<Integer>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test 
	public void testGet() {
		Integer i=new Integer(5);
		this.deferred.resolve(i);
		assertEquals(i,this.deferred.get());
		
	}
	@Test (expected = IllegalStateException.class) 
	public void testGet1() {
		this.deferred.get();
		
	}
	@Test
	public void testIsResolved() {
		
		assertFalse(this.deferred.isResolved());
		Integer i=new Integer(5);
		this.deferred.resolve(i);
		assertTrue(this.deferred.isResolved());
	}

	@Test (expected = IllegalStateException.class) 
	public void testResolve() {
		AtomicInteger counter=new AtomicInteger(0);
		this.deferred.whenResolved(new RunWithCall(counter));
		this.deferred.whenResolved(new RunWithCall(counter));
		this.deferred.whenResolved(new RunWithCall(counter));
		this.deferred.whenResolved(new RunWithCall(counter));
		this.deferred.whenResolved(new RunWithCall(counter));
		this.deferred.resolve(5);
		synchronized (counter) {
		while (counter.intValue() != 5)
	        {
	            try {
	                counter.wait();
	            } catch (InterruptedException e) {}
	        }
		}
		assertTrue(this.deferred.isResolved());
		assertEquals(5,this.deferred.get().intValue());
		this.deferred.resolve(5);
	}

	@Test
	public void testWhenResolved() {
		AtomicInteger counter=new AtomicInteger(0);
		this.deferred.whenResolved(new RunWithCall(counter));
		this.deferred.whenResolved(new RunWithCall(counter));
		this.deferred.whenResolved(new RunWithCall(counter));
		this.deferred.whenResolved(new RunWithCall(counter));
		this.deferred.whenResolved(new RunWithCall(counter));
	

		this.deferred.resolve(5);
		synchronized (counter) {
			while (counter.intValue() != 5)
	        {
	            try {
	            	 
	            	counter.wait();
	              
	            } catch (InterruptedException e) {}
	        }
		}
		
	}
	
	public class RunWithCall implements Runnable {
		AtomicInteger i;
		  RunWithCall(AtomicInteger _i){
				   this.i=_i; 
		   }

		    public void run() {
		    	synchronized(this.i){
		    		this.i.incrementAndGet();
		    		i.notifyAll();
		    	}

		}
		  
	}
}
	



