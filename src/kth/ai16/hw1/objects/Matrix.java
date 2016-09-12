package kth.ai16.hw1.objects;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Basic Matrix Class for probability (double) values
 * @author jöric
 *
 */
public class Matrix {
	
	private double[][] values;
	private int rows;
	private int columns;
	
	/**
	 * Initialises matrix with given dimension and values
	 * @param rows - Number of rows
	 * @param columns - Number of columns
	 * @param values - Flat array representing values
	 */
	public Matrix(int rows, int columns, double[] values){
		this.rows = rows;
		this.columns = columns;
		this.values = new double[rows][columns];
		
		// init matrix with values
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
	
	/**
	 * Initialises matrix with given dimensions and default value 0.0
	 * @param rows - Number of rows
	 * @param columns - Number of columns
	 */
	public Matrix(int rows, int columns){
		this.rows = rows;
		this.columns = columns;
		this.values = new double[rows][columns];
	}
	
	/**
	 * Mutiplies two matrices and return result
	 * @param m2 - Matrix to multiply with
	 * @return Product matrix
	 */
	public Matrix multiply(Matrix m2){
		Matrix result = new Matrix(rows, m2.getColumns());
		for(int k = 0; k < rows; k++)
			for(int i=0; i<m2.getColumns(); i++){
				double current = 0.0;
				for(int j=0; j<m2.getRows(); j++){
					current += values[k][j] * m2.get(j, i);
				}
				result.set(k, i, current);
			}
		return result;
	}
	
	/**
	 * Get value at given position
	 * @param x - x1 value
	 * @param y - x2 value
	 * @return Value at position
	 */
	public double get(int x, int y){
		return this.values[x][y];
	}
	
	/**
	 * Set value at given position
	 * @param x - x1 value
	 * @param y - x2 value
	 * @param Value at position
	 */
	public void set(int x, int y, double value){
		this.values[x][y] = value;
	}
	
	/**
	 * Returns a flat representation of the matrix
	 * @return Single dimension array containing values
	 */
	public double[] toArray(){
		double [] result = new double[rows*columns];
		int k = 0;
		for(int i=0; i<values.length; i++){
			for(int j=0; j<values[i].length; j++){
				result[k] = values[i][j];
				k += 1;
			}
		}
		return result;
	}
	
	/**
	 * Output method for debug purposes
	 * @return Formated String
	 */
	public String show(){
		StringBuilder s = new StringBuilder();
		for(int i=0; i<values.length; i++){
			if(i>0){
				s.append("\n");
			}
			for(int j=0; j<values[i].length; j++){
				s.append(values[i][j]);
				s.append("  ");
			}
		}
		return s.toString();
	}
	
	/**
	 * Returns row specified by index
	 * @param index of row
	 * @return selected row
	 */
	public Matrix getRow(int index){
		Matrix row = new Matrix(1, columns);
		for(int i=0; i<columns; i++){
			row.set(0, i, values[index][i]);
		}
		return row;
	}
	
	
	// GETTERS AND SETTERS
	public int getRows(){
		return this.rows;
	}
	
	public int getColumns(){
		return this.columns;
	}
	
	// OBJECT FUNCTIONS
	public String toString(){
		NumberFormat formatter = new DecimalFormat("#0.0");
		StringBuilder s = new StringBuilder();
		s.append(this.rows + " ");
		s.append(this.columns + " ");
		for(double currentValue : toArray()){
			s.append(formatter.format(currentValue) + " ");
		}
		return s.toString();
	}
	
	public Matrix clone(){
		double [] values = toArray();
		return new Matrix(rows, columns, values);
	}
	
	public boolean equals(Matrix m2){
		if(m2 == this){
			return true;
		}
		if(m2.getRows() != rows){
			return false;
		}
		if(m2.getColumns() != columns){
			return false;
		}
		double [] m1Values = toArray();
		double [] m2Values = m2.toArray();
		
		for(int i=0; i<m1Values.length; i++){
			if(m1Values[i] != m2Values[i]){
				return false;
			}
		}
		return true;
	}
}
