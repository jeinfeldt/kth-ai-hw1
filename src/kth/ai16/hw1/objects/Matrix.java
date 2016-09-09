package kth.ai16.hw1.objects;

public class Matrix {
	
	private double[][] values;
	private int rows;
	private int columns;
	
	public Matrix(int rows, int columns, double[] values){
		this.rows = rows;
		this.columns = columns;
		this.values = new double[rows][columns];
		
		int rowPosition = 0;
		int columnPosition = 0;
		for(int i=0; i<values.length; i++){
			if(i!=0 && i%columns == 0){
				rowPosition += 1;
				columnPosition = 0;
			}
			this.values[rowPosition][columnPosition] = values[i];
			columnPosition += 1;
		}
	}
	
	public String toString(){
		String s = "";
		for(int i=0; i<values.length; i++){
			for(int j=0; j<values[i].length; j++){
				s += values[i][j] + " ";
			}
			s += "\n";
		}
		return s;
	}

}
