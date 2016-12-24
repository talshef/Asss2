package bgu.spl.a2.sim;

import java.util.LinkedList;
import java.util.List;

/**
 * A class that represents a product produced during the simulation.
 */
public class Product {
	final long startId;
	final String name;
	long finalId;
	List<Product> parts;
	/**
	* Constructor 
	* @param startId - Product start id
	* @param name - Product name
	*/
    public Product(long startId, String name){
    	this.startId=startId;
    	this.name=name;
    	this.finalId=0;
    	this.parts=new LinkedList<Product>();
    }

	/**
	* @return The product name as a string
	*/
    public String getName(){
    	return this.name;
    }

	/**
	* @return The product start ID as a long. start ID should never be changed.
	*/
    public long getStartId(){
    	return this.startId;
    }
    
	/**
	* @return The product final ID as a long. 
	* final ID is the ID the product received as the sum of all UseOn(); 
	*/
    public long getFinalId(){
    	return this.finalId;
    }

	/**
	* @return Returns all parts of this product as a List of Products
	*/
    public List<Product> getParts(){
    	return this.parts;
    }

	/**
	* Add a new part to the product
	* @param p - part to be added as a Product object
	*/
    public void addPart(Product p){
    	this.parts.add(p);
    }

    public void SetfinalId(long num){
    	this.finalId+=num;
    }

}
