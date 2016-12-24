package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

public class VersionMonitorTest {
	VersionMonitor versionmonitor;
	
	@Rule
	public Timeout GlobalTimeout=new Timeout(10000);
	
	@Before
	public void setUp() throws Exception {
		this.versionmonitor=new VersionMonitor();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetVersion() {
		assertEquals(0, this.versionmonitor.getVersion());
		this.versionmonitor.inc();
		assertEquals(1, this.versionmonitor.getVersion());
	}

	@Test
	public void testInc() {
		int i=this.versionmonitor.getVersion();
		this.versionmonitor.inc();
		assertEquals(i+1, this.versionmonitor.getVersion());
	}

	
//	@Test
//	public void testAwait() {
//		
//		Thread t1 = new Thread(new waitV(this.versionmonitor));
//		Thread t2 = new Thread(new incV(this.versionmonitor));
//		
//		
//		t1.start();
//		
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//		}
//		t2.start();
//		
//		
//		while(t1.getState()!=Thread.State.TERMINATED);		
//
//	}


	
	public class incV implements Runnable {
	   VersionMonitor v;
	   incV(VersionMonitor _v){
		   this.v=_v;
	   }

	    public void run() {
	       v.inc();
	    }
	}
	
	public class waitV implements Runnable {
		   VersionMonitor v;
		   waitV(VersionMonitor _v){
			   this.v=_v;
		   }

		    public void run() {
		   	int i=v.getVersion();
		   	try {
				this.v.await(i);
		   	} catch (InterruptedException e) {
			}
				
		}
	}
}
