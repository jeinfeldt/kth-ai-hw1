package kth.ai16.hw1.test;

import static org.junit.Assert.*;
import org.junit.Test;
import kth.ai16.hw1.objects.Matrix;

public class TestMatrixOperations {
	
	@Test
	public void testInit(){
		double [] testValues = {2, 4, 1, 3}; 
		Matrix testMatrix = new Matrix(2, 2, testValues);
		assertEquals(testMatrix.get(0, 0), 2, 0.0);
		assertEquals(testMatrix.get(0, 1), 4, 0.0);
		assertEquals(testMatrix.get(1, 0), 1, 0.0);
		assertEquals(testMatrix.get(1, 1), 3, 0.0);
	}
	
	@Test
	public void testSet(){
		double [] testValues = {2, 4, 1, 3}; 
		Matrix testMatrix = new Matrix(2, 2, testValues);
		testMatrix.set(0, 0, 0.1);
		assertEquals(testMatrix.get(0, 0), 0.1, 0.0);
		assertEquals(testMatrix.get(0, 1), 4, 0.0);
		assertEquals(testMatrix.get(1, 0), 1, 0.0);
		assertEquals(testMatrix.get(1, 1), 3, 0.0);
	}
	
	@Test
	public void testMulitply(){
		double [] v1 = {2, 4, 1, 3}; 
		Matrix m1 = new Matrix(2, 2, v1);
		double [] v2 = {3, 4, 1, 2};
		Matrix m2 = new Matrix(2, 2, v2);
		double [] result = {10, 16, 6, 10};
		Matrix resultMatrix = new Matrix(2, 2, result);
		assertTrue(m1.multiply(m2).equals(resultMatrix));
	}
	
	@Test
	public void testGetRow(){
		Matrix expected = new Matrix(1,2,new double[]{1, 3});
		Matrix m1 = new Matrix(2, 2, new double[]{2, 4, 1, 3});
		Matrix row = m1.getRow(1);
		assertTrue(expected.equals(row));
		assertTrue(expected.toString().equals(row.toString()));
	}
}
