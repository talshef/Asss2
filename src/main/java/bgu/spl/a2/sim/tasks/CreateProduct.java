package bgu.spl.a2.sim.tasks;

import java.util.ArrayList;
import java.util.List;


import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

public class CreateProduct extends Task<Product> {
	private ManufactoringPlan plan;
	private Warehouse warehouse;
	private Product product;
	
	public CreateProduct(ManufactoringPlan plan,Warehouse w,long id) {
		this.plan=plan;
		this.warehouse=w;
		this.product=new Product(id, this.plan.getProductName());
	}

	@Override
	protected void start() {
		List<Task<Product>> tasks = new ArrayList<>();
		for(int i=0;i<this.plan.getParts().length;i++){
			ManufactoringPlan m=this.warehouse.getPlan(this.plan.getParts()[i]);
			Task<Product> subtask=new CreateProduct(m, this.warehouse, this.product.getStartId());
			this.spawn(subtask);
			tasks.add(subtask);
		}
		whenResolved(tasks,()->{
			for(int i=0;i<tasks.size();i++){
				this.product.addPart(tasks.get(i).getResult().get());
			}
			long sum=0;
			for(int i=0;i< plan.getTools().length;i++){
				Deferred<Tool> d=this.warehouse.acquireTool(plan.getTools()[i]);
				while(!d.isResolved());
				sum+=d.get().useOn(this.product);
				this.warehouse.releaseTool(d.get());
			}
			this.product.SetfinalId(sum);
			complete(this.product);	
			
			}
		);
		
	}

}
