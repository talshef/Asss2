package bgu.spl.a2.sim.tools;



import bgu.spl.a2.sim.Product;

public class GcdScrewDriver implements Tool {

	@Override
	public String getType() {
		return "GCD ScrewDriver";
	}

	@Override
	public long useOn(Product p) {
		long num1=p.getStartId();
		long num2=Reverse(p.getStartId());
		long result=GCD(num1,num2);
		return result;
	}
	
	private long GCD(long a1,long a2){return a2==0 ? a1 : GCD(a2, a1%a2);}
	private long Reverse(long num){
		long result=0;
		while(num!=0){
			long lastDigit= num%10;
			result=result*10+lastDigit;
			num=num/10;
		}
		return result;
	}
}
