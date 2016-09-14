package kth.ai16.hw1.main;

public class HMM4 {
	public static void main(String[] args) {
		Kattio io = new Kattio(System.in, System.out);
		Matrix a = null;
		Matrix b = null;
		Matrix pi = null;
		int [] oSeq = null;
		// reading input
		int current = 0;
		while (io.hasMoreTokens()) {
			if (current == 0) {
				a = readMatrix(io);
			} else if (current == 1) {
				b = readMatrix(io);
			} else if (current == 2) {
				pi = readMatrix(io);
			} else if (current == 3) {
				oSeq = readIntArray(io);
				break;
			}
			current += 1;
		}
		io.flush();
		// output
		HMM myHmm = new HMM(pi, a, b);
		System.out.println(myHmm.likelihood(oSeq));
		io.close();
	}
	public static Matrix readMatrix(Kattio io){
		int rows = io.getInt();
		int columns = io.getInt();
		double[] values = new double[rows * columns];
		for (int i = 0; i < values.length; i++) {
			double d = io.getDouble();
			values[i] = d;
		}
		return new Matrix(rows, columns, values);
	}
	
	public static int[] readIntArray(Kattio io){
		int length = io.getInt();
		int [] oSeq = new int[length];
		for(int i=0; i<length; i++){
			oSeq[i] = io.getInt();
		}
		return oSeq;
	}
}
