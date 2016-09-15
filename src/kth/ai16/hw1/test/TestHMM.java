package kth.ai16.hw1.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import kth.ai16.hw1.main.HMM;
import kth.ai16.hw1.main.Matrix;

public class TestHMM {
	
	@Test
	public void testHMM1Input(){
		Matrix pi = new Matrix(1, 4, new double[]{0.0, 0.0, 0.0, 1.0}); 
		Matrix a = new Matrix(4, 4, new double[]{ 0.2, 0.5, 0.3, 0.0, 0.1, 0.4, 0.4,
				   0.1, 0.2, 0.0, 0.4, 0.4, 0.2, 0.3, 0.0, 0.5});
		Matrix b = new Matrix(4, 3, new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0,
				1.0, 0.2, 0.6, 0.2});
		Matrix result = new Matrix(1,3, new double[]{0.3, 0.6, 0.1});
		HMM myHmm = new HMM(pi, a,  b);
		String output = result.toString();
		String myOutput = myHmm.next(-1).toString(); 
		assertTrue(output.equals(myOutput));
	}
	
	@Test
	public void testEvaluate(){
		Matrix pi = new Matrix(1, 4, new double[]{1.0, 0, 0, 0}); 
		Matrix a = new Matrix(4, 4, new double[]{0, 0.8, 0.1, 0.1, 0.1, 0, 0.8, 0.1, 0.1, 0.1, 0, 0.8, 0.8, 0.1, 0.1, 0});
		Matrix b = new Matrix(4, 4, new double[]{0.9, 0.1, 0, 0, 0, 0.9, 0.1, 0, 0, 0, 0.9, 0.1, 0.1, 0, 0, 0.9});
		int[] observations = {0,1,2,3,0,1,2,3};
		double expectation = 0.090276;
		HMM myHmm = new HMM(pi, a,  b);
		double resultA = myHmm.evaluateA(observations);
		double resultB = myHmm.evaluateB(observations);
		assertTrue(Math.round(resultA*1000000)/1000000.0 == Math.round(resultB*1000000)/1000000.0);
		assertTrue(expectation == Math.round(resultA*1000000)/1000000.0);
		assertTrue(expectation == Math.round(resultB*1000000)/1000000.0);
	}
	
	@Test
	public void testDecode(){
		// numbers from lesson
		Matrix pi = new Matrix(1, 4, new double[]{0.5, 0.0, 0.0, 0.5}); 
		Matrix a = new Matrix(4, 4, new double[]{0.6, 0.1, 0.1, 0.2, 0.0, 0.3, 0.2, 0.5, 0.8, 0.1, 0.0, 0.1, 0.2, 0.0, 0.1, 0.7});
		Matrix b = new Matrix(4, 4, new double[]{0.6, 0.2, 0.1, 0.1, 0.1, 0.4, 0.1, 0.4, 0.0, 0.0, 0.7, 0.3, 0.0, 0.0, 0.1, 0.9});
		int [] observations1 = {2, 0, 3, 1};
		HMM myHmm = new HMM(pi, a, b);
		int [] states1 = myHmm.decode(observations1);
		int [] expectation1 = new int[]{0,0,0,0};
		assertTrue(Arrays.equals(states1, expectation1));
		// numbers from kattis
		pi = new Matrix(1, 4, new double[]{1.0, 0.0, 0.0, 0.0}); 
		a = new Matrix(4, 4, new double[]{0.0, 0.8, 0.1, 0.1, 0.1, 0.0, 0.8, 0.1, 0.1, 0.1, 0.0, 0.8, 0.8, 0.1, 0.1, 0.0});
		b = new Matrix(4, 4, new double[]{0.9, 0.1, 0.0, 0.0, 0.0, 0.9, 0.1, 0.0, 0.0, 0.0, 0.9, 0.1, 0.1, 0.0, 0.0, 0.9});
		int [] observations2 = {1, 1, 2, 2};
		myHmm = new HMM(pi, a, b);
		int [] states2 = myHmm.decode(observations2);
		int [] expectation2 = new int[]{0,1,2,1};
		assertTrue(Arrays.equals(states2, expectation2));
	}
	
	@Test
	public void testTraining(){
		Matrix pi = new Matrix(1, 4, new double[]{0.241896, 0.266086, 0.249153, 0.242864});
		Matrix a = new Matrix(4, 4, new double[]{0.4, 0.2, 0.2, 0.2, 0.2, 0.4, 0.2, 0.2, 0.2, 0.2, 0.4, 0.2, 0.2, 0.2, 0.2, 0.4});
		Matrix b = new Matrix(4, 4, new double[]{0.4, 0.2, 0.2, 0.2, 0.2, 0.4, 0.2, 0.2, 0.2, 0.2, 0.4, 0.2, 0.2, 0.2, 0.2, 0.4});
		int [] oSeq = new int[]{0,1,2,3,3,0,0,1,1,1,2,2,2,3,0,0,0,1,1,1,2,3,3,0,0,0,1,1,1,2,3,3,0,1,2,3,0,1,1,1,2,3,3,0,1,2,2,3,0,0,0,1,1,2,2,3,0,1,1,2,3,0,1,2,2,2,2,3,0,0,1,2,3,0,1,1,2,3,3,3,0,0,1,1,1,1,2,2,3,3,3,0,1,2,3,3,3,3,0,1,1,2,2,3,0,0,0,0,0,0,0,0,0,1,1,1,1,1,2,2,2,3,3,3,3,0,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,3,3,3,0,1,2,3,0,1,1,1,2,3,0,1,1,2,2,2,2,2,3,0,1,1,1,2,2,2,2,3,0,0,0,0,0,1,1,1,1,2,2,3,3,0,1,2,3,3,0,0,0,0,0,0,1,1,2,2,3,0,0,1,1,1,1,1,1,2,3,3,0,0,1,1,1,2,3,0,0,1,2,3,0,1,1,2,3,3,0,0,0,1,2,3,3,3,0,1,1,1,1,2,3,3,3,3,3,3,0,1,2,2,2,2,2,2,3,0,1,1,1,2,2,3,3,3,3,0,1,2,3,0,0,0,1,1,2,2,3,0,0,0,0,0,0,0,1,2,2,2,3,3,3,3,0,0,1,2,2,2,3,3,3,0,0,1,2,2,3,0,0,0,0,1,1,1,2,3,3,3,3,3,3,3,3,0,1,2,3,0,0,1,2,3,3,3,0,0,0,0,0,1,1,1,1,2,3,0,0,0,1,2,2,3,3,0,0,0,1,1,1,1,1,2,3,3,3,3,0,1,1,1,2,2,3,0,1,2,3,3,3,3,0,0,0,0,1,2,3,3,0,1,2,2,3,3,0,0,1,1,2,3,3,0,1,2,2,3,3,3,0,0,1,1,2,3,3,3,3,0,0,1,1,2,3,3,0,1,2,3,0,1,1,2,2,3,0,1,2,3,3,0,1,1,1,2,2,2,3,3,0,0,1,1,1,1,1,2,3,3,3,0,1,1,2,2,2,2,3,3,0,0,1,2,3,0,1,1,2,2,2,2,3,0,0,1,2,2,3,0,0,0,0,0,1,1,1,2,3,0,0,1,2,3,3,0,0,0,1,2,2,2,3,3,0,0,0,1,2,2,2,2,2,3,0,1,1,2,3,0,0,1,1,1,2,2,3,0,0,0,0,1,1,1,2,2,3,0,1,1,1,2,2,2,3,3,0,0,1,2,2,3,3,3,0,1,1,2,3,0,0,0,0,0,1,2,2,2,3,3,3,0,0,0,1,2,3,0,1,1,2,3,3,3,0,1,2,2,2,3,0,0,1,1,1,1,2,3,3,0,0,0,0,1,2,3,3,3,0,0,0,1,1,2,3,0,1,1,1,1,2,2,2,2,2,2,3,0,0,0,0,1,2,2,2,2,3,0,1,2,2,3,0,1,2,3,0,1,2,3,0,0,0,1,1,2,2,3,3,0,1,1,1,1,2,2,3,3,0,1,1,1,2,2,2,3,3,3,0,1,1,2,3,3,0,1,2,3,0,0,0,0,1,2,3,0,0,0,0,0,0,1,2,2,3,3,0,0,1,2,3,0,1,2,2,3,0,0,0,1,1,2,2,2,2,2,3,3,3,3,3,0,1,2,2,3,3,3,3,3,0,0,1,1,2,2,3,0,0,1,2,2,3,3,3,0,0,0,1,2,2,2,2,3,3,0,1,2,3,0,0,1,1,1,2,2,3,0,0,1,1,2,2,2,3,3,0,0,1,1,1,1,1,2,3,3,3,0,1,2,2,2,2,3,3,3,3,3,3,0,0,0,0,0,0,1,2,3,0,0,1,1,1,2,3,0,0,1,1,2,2,2,2,3,3,3,0,1,1,2,2,2,3,3,0,0,0,0,0,0,1,2,2,3,3,0,0,0,0,0,0,1,2,3,3,3,0,1,1,1,2,2,2,2,2,3,3,3,0,1,2,2,2,3,3,3,3,0,0,0,0,1,2,3,3,3,3,3,3,0,0,1,1,1,1,2,3,0,1,2,3,0,1,1,2,3,3,3,0,0,0,0,1,1,2,3,3,3,3,0,0,1,1,1,2,2,2,2,2,2,3,3,0,0,0,1,2,3,0,0,1,1,2,2,3,3,3,3,3,0,0,1,2,2,2,2,3,0,0,1,1,1,1,1,2,3,3,0,0,1,1,1,2,3,3,3,0,0};
		HMM myHmm = new HMM(pi, a, b);
		myHmm.train(oSeq, 500, Double.NEGATIVE_INFINITY);
		System.out.println("Transition");
		System.out.println(myHmm.getTransition().show());
		System.out.println("Emission");
		System.out.println(myHmm.getEmission().show());
	}

}
