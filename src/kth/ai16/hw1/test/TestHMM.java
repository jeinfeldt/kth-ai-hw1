package kth.ai16.hw1.test;

import kth.ai16.hw1.objects.HMM;
import kth.ai16.hw1.objects.Matrix;
import static org.junit.Assert.*;

import org.junit.Test;

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
		String myOutput = myHmm.predictObservation(-1).toString(); 
		System.out.println(myOutput);
		assertTrue(output.equals(myOutput));
	}

}
