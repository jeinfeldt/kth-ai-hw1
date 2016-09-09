package kth.ai16.hw1.test;

import kth.ai16.hw1.objects.Matrix;

public class Start {

	public static void main(String[] args) {
		
		double []testValues = {0.1, 0.4, 0.5, 0.6};
		Matrix m = new Matrix(2, 2, testValues);
		System.out.println(m);
	}

}
