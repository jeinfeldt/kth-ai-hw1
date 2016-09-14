package kth.ai16.hw1.questions;

import kth.ai16.hw1.main.Matrix;

public class Question3 {

	public static void main(String [] args){
		double []piValues = {0.5, 0.5};
		Matrix pi = new Matrix(1,2,piValues);
		double []transitionValues = {0.5, 0.5, 0.5, 0.5};
		Matrix a = new Matrix(2, 2, transitionValues);
		double []observationValues = {0.9, 0.1, 0.5, 0.5};
		Matrix b = new Matrix(2, 2, observationValues);
		
		System.out.println("Current probabilities for observation:");
		System.out.println(pi.multiply(a).multiply(b).show());
	}
}
