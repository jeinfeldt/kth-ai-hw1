package kth.ai16.hw1.objects;

import java.util.Arrays;

/**
 * Represents a Hidden Markov Model with capability to
 * calculate probability for hidden states based on observations
 * @author jöric
 *
 */
public class HMM {
	
	private Matrix pi;
	private Matrix a;
	private Matrix b;
	
	/**
	 * 
	 * @param initialProbability
	 * @param transitionMatrix
	 * @param observationMatrix
	 */
	public HMM(Matrix initialProbability, Matrix transitionMatrix, Matrix observationMatrix){
		pi = initialProbability;
		a = transitionMatrix;
		b = observationMatrix;
	}
	
	/**
	 * Predicts next obervation matrix based on state
	 * @param state
	 * @return
	 */
	public Matrix next(int state){
		Matrix result = null;
		// initial computation
		if(state == -1){
			result = pi.multiply(a).multiply(b);
		} else {
			Matrix row = a.getRow(state);
			result = row.multiply(a).multiply(b);
		}
		return result;
	}
	
	/**
	 * Returns the likelihood of given observation sequence
	 * @param observationSequence sequence of observations
	 * @return double likelihood
	 */
	public Matrix forwardPropability(int[] observationSequence){
		
		//Variables for current and next alpha
		Matrix alphaNow = new Matrix(pi.getRows(), pi.getColumns());
		Matrix alphaNext = new Matrix(pi.getRows(), pi.getColumns());
		Matrix allAlphas = new Matrix(observationSequence.length, pi.getColumns());
		
		
		//Iterating over all observations
		for(int i = 0; i < observationSequence.length; i++){
			Matrix observProb = b.getColumn(observationSequence[i]);
			
			//special treatment for t = 0
			if(i == 0){
				for(int j = 0; j < pi.getColumns(); j++){
					alphaNext.set(i, j, pi.get(0, j) * observProb.get(i, j));
					allAlphas.set(i, j, pi.get(0, j) * observProb.get(i, j));
				}
			}
			else{
				Matrix calculateHelp = alphaNow.multiply(a);
					for(int j = 0; j < pi.getColumns(); j++){
						alphaNext.set(0, j, calculateHelp.get(0, j) * observProb.get(0, j));
						allAlphas.set(i, j, calculateHelp.get(0, j) * observProb.get(0, j));
					}
				}
			alphaNow = alphaNext;
		}
		return allAlphas;
	}	
	
	public double likelyhood(Matrix alpha){
		
		Matrix alphaNow = alpha.getRow(alpha.getRows() - 1);
		double result = 0.0;
		
		for(int i = 0; i < alphaNow.getColumns(); i++){
			result += alphaNow.get(0, i);
		}
		return result;
	}
		
	/**
	 * Returns the most likely sequence of states based on given observation sequence
	 * @param observationSequence sequence of observations
	 * @return sequence of states
	 */
	public int [] decode(int [] observationSequence){
		// init calculation helpers
		int [] states = new int[observationSequence.length];
		int possibleStates = a.getRows();
		Matrix viterbi = new Matrix(possibleStates, observationSequence.length);
		int [][] indices = new int[possibleStates][observationSequence.length];
		for(int i=0; i<indices.length; i++){
			Arrays.fill(indices[i], -1);
		}
		// viterbi initialization step 
		Matrix deltas = pi.multiply(b.getColumn(observationSequence[0]));
		for(int state=0; state<possibleStates; state++){
			viterbi.set(0, state, deltas.get(0, state));
		}
		// viterbi iteration step
		for(int t=1; t<observationSequence.length; t++){
			for(int state=0; state<possibleStates; state++){
				Matrix lastT = viterbi.getRow(t-1); 
				Matrix stateTrans = a.getColumn(state);
				double obs = b.get(state, observationSequence[t]) ;
				Matrix tmp = (lastT.multiply(stateTrans)).multiply(obs);
				viterbi.set(t, state, tmp.getMax());
				indices[t][state] = tmp.getMaxIndex();
			}
		}
		// backtracking for state sequence
		Matrix rowVector = viterbi.getRow(possibleStates-1);
		int index = rowVector.getMaxIndex();
		states[states.length-1] = index;
		for(int i=states.length-2; i>=0; i--){
			states[i] = indices[i+1][states[i+1]];
		}
		return states;
	}
	
	/** 
	 * Calculates backward probability matrix based on squence of obervations
	 * and possible states of the model with beta-pass (backward) algorithm
	 * Beta(i,j): stores the probability of observing the rest of the sequence after time step i 
	 * given that at time step i we are in state k in the HMM 
	 * @param observationSequence
	 * @param possibleStates
	 * @return beta matrix
	 */
	private Matrix backwardProbability(int [] observationSequence, int possibleStates){
		Matrix beta = new Matrix(observationSequence.length, possibleStates);
		// initialisation
		for(int i=0; i<possibleStates; i++){
			beta.set(observationSequence.length-1, i, 1.0);
		}
		// iteration
		for(int t=observationSequence.length-2; t>=0; t--){
			for(int i=0; i<possibleStates; i++){
				double currentBeta = 0.0;
				for(int j=0; j<possibleStates; j++){
					currentBeta = beta.get(t+1, j)*b.get(j, observationSequence[t+1])*a.get(i, j);
				}
				beta.set(t, i, currentBeta);
			}
		}
		
		return beta;
	}
}

