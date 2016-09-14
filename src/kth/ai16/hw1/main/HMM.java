package kth.ai16.hw1.main;

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

	public void train(int [] oSeq, int maxIters, double oldLog){
		
		double oldLogProb = oldLog;
		int iters = 0;
		
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
				for(int t=0; t<T; t++){
					num += calculateDiGamma(t, i, j, alpha, beta, oSeq, diGammaDenom);
					denom += calculateGamma(t, j, alpha, beta, oSeq, diGammaDenom, numStates);
				}
				a.set(i, j, divide(num,denom));
			}
		}
		// reestiamate transition matrix B
		for(int j=0; j<numStates; j++){
			for(int k=0; k<b.getColumns(); k++){
				double num = 0.0;
				double denom = 0.0;
				for(int t=0; t<T; t++){
					num += indicator(oSeq[t], k) * calculateGamma(t, j, alpha, beta, oSeq, diGammaDenom, numStates);
					denom += calculateGamma(t, j, alpha, beta, oSeq, diGammaDenom, numStates);
				}
				b.set(j, k, divide(num,denom));
			}
		}
		// reestimate initial transition PI
		for(int i=0; i<numStates; i++){
			pi.set(0, i, calculateGamma(0, i, alpha, beta, oSeq, diGammaDenom, numStates));
		}
		
		double logProb = 0.0;
		for (int t = 0; t < T; t++){
			double alphaT = 0.0;
			for (int n = 0; n < numStates; n++){
				 alphaT += alpha.getRow(t).get(0, n);
			}
			logProb += Math.log(1/alphaT);
		}
		logProb = -logProb;
		
		if(iters < maxIters && logProb > oldLogProb){
			oldLogProb = logProb;
			train(oSeq, maxIters-1, oldLogProb);
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
		for(int t = 0; t < observationSequence.length; t++){
			Matrix observProb = b.getColumn(observationSequence[t]);
			
			double c0 = 0.0;
			double ct = 0.0;
			
			//special treatment for t = 0
			if(t == 0){
				for(int j = 0; j < pi.getColumns(); j++){
					alphaNext.set(t, j, pi.get(0, j) * observProb.get(t, j));
					c0 += alphaNext.get(t, j);
				}
				
				c0 = 1/c0;
				
				for(int j = 0; j < pi.getColumns(); j++){
					alphaNext.set(t, j, alphaNext.get(t, j)*c0);
					allAlphas.set(t, j, alphaNext.get(t, j));
				}
				

			}
			else{
				Matrix calculateHelp = alphaNow.multiply(a);
					for(int j = 0; j < pi.getColumns(); j++){
						alphaNext.set(0, j, calculateHelp.get(0, j) * observProb.get(0, j));
						ct += alphaNext.get(0, j);
					}
					
					ct = 1/ct;
					
					for(int j = 0; j <pi.getColumns(); j++){
						alphaNext.set(0, j, alphaNext.get(0, j) * ct);
						allAlphas.set(t, j, alphaNext.get(0, j));
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
	 * @param oSeq sequence of observations
	 * @return sequence of states
	 */
	public int [] decode(int [] oSeq){
		// init calculation helpers
		int numStates = a.getRows();
		int [][] indices = new int[oSeq.length][numStates];
		for(int i=0; i<indices.length; i++){
			Arrays.fill(indices[i], -1);
		}
		Matrix viterbi = new Matrix(oSeq.length, numStates);
		// viterbi initialization step 
		for(int i=0; i<numStates; i++){
			double delta = b.get(i, oSeq[0]) * pi.get(0, i);
			viterbi.set(0, i, delta);
		}
		// update subsequent deltas
		for(int t=1; t<oSeq.length; t++){
			for(int i=0; i<numStates; i++){
				double delta = 0.0;
				int index = -1;
				for(int j=0; j<numStates; j++){
					double tmp = a.get(j, i)*viterbi.get(t-1, j)*b.get(i, oSeq[t]);
					if(tmp > delta){
						delta = tmp;
						index = j;
					}
				}
				viterbi.set(t, i, delta);
				indices[t][i] = index;
			}
		}
		// backtracking for state sequence
		int [] states = new int[oSeq.length];
		Matrix rowVector = viterbi.getRow(numStates-1);
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
	 * @param oSeq
	 * @param numStates
	 * @return beta matrix
	 */
	private Matrix backwardProbability(int [] oSeq){
		int numStates = a.getRows();
		Matrix beta = new Matrix(oSeq.length, numStates);
		// initialisation
		for(int i=0; i<numStates; i++){
			beta.set(oSeq.length-1, i, 1.0);
		}
		// normalize
		for(int i=0; i<numStates; i++){
			double current = beta.get(oSeq.length-1, i);
			beta.set(oSeq.length-1, i, normalize(current, beta, oSeq.length-1));
		}
		// iteration
		for(int t=oSeq.length-2; t>=0; t--){
			for(int i=0; i<numStates; i++){
				double currentBeta = 0.0;
				for(int j=0; j<numStates; j++){
					currentBeta += beta.get(t+1, j)*b.get(j, oSeq[t+1])*a.get(i, j);
				}
				beta.set(t, i, currentBeta);
			}
			// normalize
			for(int n=0; n<numStates; n++){
				double current = beta.get(t, n);
				beta.set(t, n, normalize(current, beta, t));
			}
		}
		return beta;
	}
	
	public Matrix getTransition(){
		return this.a;
	}
	
	public Matrix getEmission(){
		return this.b;
	}
	
	public Matrix getInitial(){
		return this.pi;
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
		double diGammaNum = 0.0;
		if(t == oSeq.length - 1){
			diGammaNum = alpha.get(t, i) * a.get(i, j);
		}else{
			diGammaNum = alpha.get(t, i)*a.get(i, j)*b.get(j, oSeq[t+1])*beta.get(t+1, j);
		}
		return divide(diGammaNum,diGammaDenom);
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
	
	private double divide(double num, double denom){
		if(num == 0){
			return 0;
		}
		else{
			return num/denom;
		}
	}
	private double normalize(double value, Matrix m, int t){
		double denom = 0.0;
		for(int i=0; i<m.getColumns(); i++){
			denom += m.get(t, i);
		}
		return divide(value, denom);
	}
	
}
