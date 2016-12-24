package bgu.spl.a2.sim.tools;

import java.util.Random;

import bgu.spl.a2.sim.Product;

public class RandomSumPliers implements Tool {

	
	
	
	@Override
	public String getType() {
		return "RandomSumPliers";
	}

	@Override
	public long useOn(Product p) {
		Random Gen= new Random(p.getStartId());
		long n=p.getStartId()%10000;
		long sum=0;
		for(int i=0;i<n;i++)sum+=Gen.nextLong();
		return sum;
	}

}
