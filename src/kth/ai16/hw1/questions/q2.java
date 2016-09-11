package kth.ai16.hw1.questions;

import kth.ai16.hw1.objects.Matrix;

/**
 * Illustrates basic math operations with custom matrix, vector and HMM class 
 * @author jöric
 *
 */
public class q2 {
	
	public static void main(String [] args){
		
		// checking general matrix operation
		double [] v1 = {2, 4, 1, 3}; 
		Matrix m1 = new Matrix(2, 2, v1);
		double [] v2 = {3, 4, 1, 2};
		Matrix m2 = new Matrix(2, 2, v2);
		
		System.out.println("Matrix 1:");
		System.out.println(m1);
		System.out.println("Matrix 2:");
		System.out.println(m2);
		System.out.println("Product Matrix:");
		System.out.println(m1.multiply(m2) + "\n");
		
		// checking hmm1 example
		double [] v3 = {0.5, 0.5}; 
		m1 = new Matrix(1, 2, v3);
		double [] v4 = {0.9, 0.1, 0.5, 0.5};
		m2 = new Matrix(2, 2, v4);
		
		System.out.println("Matrix 1:");
		System.out.println(m1);
		System.out.println("Matrix 2:");
		System.out.println(m2);
		System.out.println("Product Matrix:");
		System.out.println(m1.multiply(m2) + "\n");	
		
		// checking hmm1 kattis input
		double [] v5 = {0.2, 0.5, 0.3, 0.0, 0.1, 0.4, 0.4, 0.1, 0.2, 0.0, 0.4, 0.4, 0.2, 0.3, 0.0, 0.5}; 
		m1 = new Matrix(4, 4, v5);
		double [] v6 = {1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.2, 0.6, 0.2};
		m2 = new Matrix(4, 3, v6);
		double [] v7 = {0.0, 0.0, 0.0, 1.0};
		Matrix pi = new Matrix(1, 4, v7);
		
		System.out.println("Matrix 1:");
		System.out.println(m1);
		System.out.println("Matrix 2:");
		System.out.println(m2);
		System.out.println("Pi multiplied transition:");
		Matrix r1 = pi.multiply(m1);
		System.out.println(r1);
		System.out.println("Result wird observation");
	}

}
