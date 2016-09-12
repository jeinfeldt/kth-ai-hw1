package kth.ai16.hw1.main;

public class HMM1 {
	public static void main(String[] args) {
		Kattio io = new Kattio(System.in, System.out);
		double[] values;
		while (io.hasMoreTokens()) {
			int i1 = io.getInt();
			int i2 = io.getInt();
			values = new double[i1*i2];
			for(int i=0; i<values.length; i++){
				double d = io.getDouble();
				values[i] = d;
			}
			System.out.println("N: " + i1);
			System.out.println("M: "+ i2);
			System.out.print("D:");
			for(double currentValue: values){
				System.out.print(" " + currentValue);
			}
			
		}
	}
}
