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
	
	public void train(int [] oSeq){
		int numStates = a.getRows();
		Matrix alpha = forwardPropability(oSeq);
		Matrix beta = backwardProbability(oSeq);
		int T = oSeq.length;
		
		// calculate diGamma denominator
		double diGammaDenom = 0.0;
		for(int i=0; i<numStates; i++){
			diGammaDenom += alpha.get(T-1, i);
		}
				
		// reestimate transition matrix A
		for(int i=0; i<numStates; i++){
			for(int j=0; j<numStates; j++){
				double num = 0.0;
				double denom = 0.0;
				for(int t=0; t<T-1; t++){
					num += calculateDiGamma(t, i, j, alpha, beta, oSeq, diGammaDenom);
					denom += calculateGamma(t, j, alpha, beta, oSeq, diGammaDenom, numStates);
				}
				a.set(i, j, num/denom);
			}
		}
		// reestiamate transition matrix B
		for(int j=0; j<numStates; j++){
			for(int k=0; k<b.getColumns(); k++){
				double num = 0.0;
				double denom = 0.0;
				for(int t=0; t<T-2; t++){
					num += indicator(oSeq[t], k) * calculateGamma(t, j, alpha, beta, oSeq, diGammaDenom, numStates);
					denom += calculateGamma(t, j, alpha, beta, oSeq, diGammaDenom, numStates);
				}
				b.set(j, k, num/denom);
			}
		}
		// reestimate initial transition PI
		for(int i=0; i<numStates; i++){
			pi.set(0, i, calculateGamma(0, i, alpha, beta, oSeq, diGammaDenom, numStates));
		}
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
	private Matrix forwardPropability(int[] observationSequence){
		
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
	
	public double likelihood(int [] oSeq){
		Matrix alpha = forwardPropability(oSeq);
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
	 * @param numStates
	 * @return beta matrix
	 */
	private Matrix backwardProbability(int [] observationSequence){
		int numStates = a.getRows();
		Matrix beta = new Matrix(observationSequence.length-1, numStates);
		// initialisation
		for(int i=0; i<numStates; i++){
			beta.set(observationSequence.length-1, i, 1.0);
		}
		// iteration
		for(int t=observationSequence.length-2; t>=0; t--){
			for(int i=0; i<numStates; i++){
				double currentBeta = 0.0;
				for(int j=0; j<numStates; j++){
					currentBeta += beta.get(t+1, j)*b.get(j, observationSequence[t+1])*a.get(i, j);
				}
				beta.set(t, i, currentBeta);
			}
		}
		
		return beta;
	}
	
	/**
	 * 
	 * @param o
	 * @param k
	 * @return
	 */
	private int indicator(int o, int k){
		if(o == k){
			return 1;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param t
	 * @param i
	 * @param j
	 * @param alpha
	 * @param beta
	 * @param oSeq
	 * @param diGammaDenom
	 * @return
	 */
	private double calculateDiGamma(int t, int i, int j, Matrix alpha, Matrix beta, int [] oSeq, double diGammaDenom){
		double diGammaNum = alpha.get(t, i)*a.get(i, j)*b.get(j, oSeq[t+1])*beta.get(t+1, j);
		return diGammaNum/diGammaDenom;
	}
	
	/**
	 * 
	 * @param t
	 * @param i
	 * @param alpha
	 * @param beta
	 * @param oSeq
	 * @param diGammaDenom
	 * @param numStates
	 * @return
	 */
	private double calculateGamma(int t, int i, Matrix alpha, Matrix beta, int [] oSeq, double diGammaDenom, int numStates){
		double gamma = 0.0;
		for(int k=0; k<numStates; k++){
			gamma += calculateDiGamma(t, i, k, alpha, beta, oSeq, diGammaDenom);
		}
		return gamma;
	}
}

