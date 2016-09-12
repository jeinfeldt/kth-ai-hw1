package kth.ai16.hw1.objects;

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
	
	/**
	 * Returns the most likely sequence of states based on given observation sequence
	 * @param observations sequence of observations
	 * @return sequence of states
	 */
	public int [] decode(int [] observations){
		int [] states = new int[observations.length];
		int possibleStates = a.getRows();
		Matrix viterbi = new Matrix(possibleStates, observations.length);
		// initialization step 
		for(int state=0; state<possibleStates; state++){
			double delta = b.get(state, observations[0])*pi.get(0, state);
			viterbi.set(state, 0, delta);
		}
		// recursion step
		for(int t=1; t<observations.length; t++){
			for(int state=0; state<possibleStates; state++){
				double delta = 0.0;
				viterbi.set(state, t, delta);
			}
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
		int maxIndex = 0;
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
}
