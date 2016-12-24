package bgu.spl.a2.test;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;

public class main {
	 public static void main(String[] args) throws InterruptedException {
		 WorkStealingThreadPool pool = new WorkStealingThreadPool(10);
		 int[][] array = new int[100][10];
		 for(int i=0;i<100;i++){
			 for(int j=0;j<10;j++){
				 array[i][j]=i+j;
				 System.out.print(i+j+" ");
			 }
			 System.out.println("");
		 }
		 // some stuff
		 SumMatrix myTask = new SumMatrix(array);
		 pool.start();
		 pool.submit(myTask);
		 //some stuff
		 while(!myTask.getResult().isResolved());
		 pool.shutdown(); //stopping all the threads

	 }
	 
}
