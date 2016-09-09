package kth.ai16.hw1.objects;

/**
 * This class represents a basic vector object with elementary operations
 * @author jöric
 *
 */
public class Vector {
	
	private double [] values;
	
	public Vector(int length){
		this.values = new double[length];
		this.values = fillWithValue(this.values, 0.0);
	}
	
	public Vector(int length, double defaultValue){
		this.values = new double[length];
		this.values = fillWithValue(this.values, defaultValue);
	}
	
	public Vector(double [] values){
		this.values = values;
	}
	
	public double get(int index){
		return values[index];
	}
	
	public void set(int index, double value){
		values[index] = value;
	}
	
	public double dot(Vector otherVector){
		double scalar = 0.0;
		for(int i=0; i<otherVector.size(); i++){
			scalar += values[i] * otherVector.get(i); 
		}
		return scalar;
	}
	
	public Vector multiply(Vector otherVector){
		Vector result = new Vector(otherVector.size());
		for(int i=0; i<otherVector.size(); i++){
			double current = values[i] * otherVector.get(i);
			result.set(i, current);
		}
		return result;
	}
	
	public int size(){
		return this.values.length;
	}
	
	public void setValues(double[] values){
		this.values = values;
	}
	
	public double[] getValues(){
		return this.values;
	}
	
	private double[] fillWithValue(double [] vector, double value){
		for(int i=0; i<vector.length; i++){
			vector[i] = value;
		}
		return vector;
	}
}	
