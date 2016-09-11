package kth.ai16.hw1.questions;

import kth.ai16.hw1.objects.Matrix;

/**
 * Illustrates initialisation of custom matrix and vector class
 * @author jöric
 *
 */
public class Question1 {
	
	public static void main(String [] args){
		double []piValues = {0.5, 0.5};
		Matrix pi = new Matrix(1,2,piValues);
		double []transitionValues = {0.5, 0.5, 0.5, 0.5};
		Matrix a = new Matrix(2, 2, transitionValues);
		double []observationValues = {0.9, 0.1, 0.5, 0.5};
		Matrix b = new Matrix(2, 2, observationValues);
		
		System.out.println("Initial Row Vector Pi:");
		System.out.println(pi.show());
		System.out.println("Transition Matrix A:");
		System.out.println(a.show());
		System.out.println("Observation Matrix B:");
		System.out.println(b.show());
	}

}
