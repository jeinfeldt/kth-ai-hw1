package kth.ai16.hw1.test;

import java.math.RoundingMode;
import java.text.DecimalFormat;

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
	
	@Test
	public void testObservationSequencePropapility(){
		Matrix pi = new Matrix(1, 4, new double[]{1.0, 0, 0, 0}); 
		Matrix a = new Matrix(4, 4, new double[]{0, 0.8, 0.1, 0.1, 0.1, 0, 0.8, 0.1, 0.1, 0.1, 0, 0.8, 0.8, 0.1, 0.1, 0});
		Matrix b = new Matrix(4, 4, new double[]{0.9, 0.1, 0, 0, 0, 0.9, 0.1, 0, 0, 0, 0.9, 0.1, 0.1, 0, 0, 0.9});
		int[] observations = {0,1,2,3,0,1,2,3};
		double expectation = 0.090276;
		HMM myHmm = new HMM(pi, a,  b);
		double result = myHmm.alphaPass(observations);
		System.out.println(result);
		DecimalFormat df = new DecimalFormat("#.######");
		df.setRoundingMode(RoundingMode.CEILING);
		String d  = "" + result;
		result = Double.parseDouble(d);
		System.out.println(result);
		assertTrue(expectation == result);
	}
	
	public void testDecode(){
		Matrix pi = new Matrix(1, 4, new double[]{1.0, 0.0, 0.0, 0.0}); 
		Matrix a = new Matrix(4, 4, new double[]{0.0, 0.8, 0.1, 0.1, 0.1, 0.0, 0.8, 0.1, 0.1, 0.1, 0.0, 0.8, 0.8, 0.1, 0.1, 0.0});
		Matrix b = new Matrix(4, 4, new double[]{0.9, 0.1, 0.0, 0.0, 0.0, 0.9, 0.1, 0.0, 0.0, 0.0, 0.9, 0.1, 0.1, 0.0, 0.0, 0.9});
		HMM myHmm = new HMM(pi, a, b);
		int [] observations = {1, 1, 2, 2};
		myHmm.decode(observations);
	}

}
