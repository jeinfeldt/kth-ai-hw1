package kth.ai16.hw1.main;

public class HMM1 {
	public static void main(String[] args) {
		Kattio io = new Kattio(System.in, System.out);
		Matrix a = null;
		Matrix b = null;
		Matrix pi = null;
		// reading input
		int current = 0;
		double[] values;
		while (io.hasMoreTokens()) {
			int rows = io.getInt();
			int columns = io.getInt();
			values = new double[rows * columns];
			for (int i = 0; i < values.length; i++) {
				double d = io.getDouble();
				values[i] = d;
			}
			if (current == 0) {
				a = new Matrix(rows, columns, values);
			} else if (current == 1) {
				b = new Matrix(rows, columns, values);
			} else if (current == 2) {
				pi = new Matrix(rows, columns, values);
				break;
			}
			current += 1;
		}
		io.flush();
		// output
		HMM myHmm = new HMM(pi, a, b);
		System.out.println(myHmm.next(-1));
		io.close();
	}
}
