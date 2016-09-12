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
	public Matrix predictObservation(int state){
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
	
	public double alphaPass(int[] observationSequence){
		
		Matrix alphaNow = new Matrix(pi.getRows(), pi.getColumns());
		Matrix alphaNext = new Matrix(pi.getRows(), pi.getColumns());
		
		for(int i = 0; i < observationSequence.length; i++){
			Matrix observProb = b.getColumn(observationSequence[i]);
			
			if(i == 0){
				for(int j = 0; j < pi.getColumns(); j++){
					alphaNext.set(0, i, pi.get(0, i) * observProb.get(0, i));
				}
			}
			else{
				Matrix calculateHelp = alphaNow.multiply(a);
					for(int j = 0; j < pi.getColumns(); j++){
						alphaNext.set(0, j, calculateHelp.get(0, j) * observProb.get(0, j));
					}
				}
			alphaNow = alphaNext;
		}
		double result = 0.0;
		
		for(int i = 0; i < alphaNow.getColumns(); i++){
			result += alphaNow.get(0, i);
		}
		return result;
	}		
		
	/**
	 * Returns the most likely sequence of states based on given observation sequence
	 * @param observations sequence of observations
	 * @return sequence of states
	 */
	public int [] decode(int [] observations){
		// init calculation helpers
		int [] states = new int[observations.length];
		int possibleStates = a.getRows();
		Matrix viterbi = new Matrix(possibleStates, observations.length);
		int [][] indices = new int[possibleStates][observations.length];
		for(int i=0; i<indices.length; i++){
			Arrays.fill(indices[i], -1);
		}
		// viterbi initialization step 
		Matrix deltas = pi.multiply(b.getColumn(observations[0]));
		for(int state=0; state<possibleStates; state++){
			viterbi.set(0, state, deltas.get(0, state));
		}
		// viterbi iteration step
		for(int t=1; t<observations.length; t++){
			for(int state=0; state<possibleStates; state++){
				Matrix lastT = viterbi.getRow(t-1); 
				Matrix stateTrans = a.getColumn(state);
				double obs = b.get(state, observations[t]) ;
				Matrix tmp = (lastT.multiply(stateTrans)).multiply(obs);
				viterbi.set(t, state, findMaxValue(tmp));
				indices[t][state] = findMaxIndex(tmp);
			}
		}
		// backtracking for state sequence
		for(int i=possibleStates-1; i>=0; i--){
			Matrix rowVector = viterbi.getRow(i);
			int index = findMaxIndex(rowVector);
			int state = indices[i][index];
			states[states.length-1-i] = state;
		}
		return states;
	}
	
	/**
	 * Fetches max index value for given row vector
	 * @param rowVector
	 * @return max index
	 */
	private int findMaxIndex(Matrix rowVector){
		double max = 0.0;
		int maxIndex = -1;
		// TODO multiple max values
		double [] values = rowVector.toArray();
		for(int i=0; i<values.length; i++){
			if(values[i]>max){
				max = values[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	private double findMaxValue(Matrix rowVector){
		double max = 0.0;
		double [] values = rowVector.toArray();
		for(int i=0; i<values.length; i++){
			if(values[i]>max){
				max = values[i];
			}
		}
		return max;		
	}
}

