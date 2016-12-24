package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

public class NextPrimeHammer implements Tool {

	@Override
	public String getType() {
		return "Next Prime Hammer";
	}

	@Override
	public long useOn(Product p) {
		long result=NextPrime(p.getStartId());
		return result;
	}
	
	private long NextPrime(long num){
		boolean found=false;
		if(num%2==0) num++;
		else num+=2;
		while(!found){
			boolean IsPrime=true;
			for(long i=3;i<=Math.sqrt(num) && IsPrime;i++){
				if(num%i==0) IsPrime=false;
			}
			if(IsPrime==true) return num;
			num+=2;
		}
		return 0;
	}
}
