/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;

    public MergeSort(int[] array) {
        this.array = array;
    }

    @Override
    protected void start() {
        
    	if (this.array.length<=1) 
    		{
    		this.complete(array);
    	
    		}
    	else
    	{	
    		int [] arrayL;
    		int [] arrayR;
    		if(array.length%2==0)
    		{
    			arrayL= new int[array.length/2];
    			arrayR= new int[array.length/2];
    		}
    		else
    		{
    			arrayL= new int[array.length/2];
        		arrayR= new int[array.length/2+1];
    		}
    		for(int i=0;i<arrayL.length;i++) arrayL[i]=array[i];
    		for(int i=0;i<arrayR.length;i++) arrayR[i]=array[i+arrayL.length];
    		
    		MergeSort left = new MergeSort(arrayL);
    		MergeSort right = new MergeSort(arrayR);
    		spawn(left);
    		spawn(right);
    		ArrayList<Task<int[]>> m=new ArrayList<>();
    		m.add(left);
    		m.add(right);
    				
    		whenResolved(m,()->{
    			int[] leftA=left.getResult().get();
    			int[] rightA=right.getResult().get();
    			int l=0;
    			int r=0;
    			while(l<leftA.length && r<rightA.length)
    			{
    				if (leftA[l]<=rightA[r])
    				{
    					array[l+r]=leftA[l];
    					l++;
    				}
    				else {
    					array[l+r]=rightA[r];
    					r++;
    					}
    			}
    			if(l<leftA.length) for(int j=l;j<leftA.length;j++) array[r+j]=leftA[j];
    			if(r<rightA.length) for(int j=r;j<rightA.length;j++) array[l+j]=rightA[j];
//    			for(int i=0;i<array.length;i++){
//    				System.out.print(" "+ array[i]);
//    			}
//    			System.out.println(" ");
    			complete(array);
    		});
    		
    	}
    }

    public static void main(String[] args) throws InterruptedException {
    	for (int i=0;i<1000;i++){
        WorkStealingThreadPool pool = new WorkStealingThreadPool(10);
        int n = 100; //you may check on different number of elements if you like
        int[] array = new Random().ints(n).toArray();

        MergeSort task = new MergeSort(array);

        //CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(task);
        System.out.println("koko");
        task.getResult().whenResolved(() -> {
           // warning - a large print!! - you can remove this line if you wish
            System.out.println(Arrays.toString(task.getResult().get()));
 //           l.countDown();
        });

//        l.await();
        Thread.sleep(30);
        
        pool.shutdown();
        System.out.println(i);
    	}
    	
    }

}
