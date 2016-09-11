package kth.ai16.hw1.main;

public class HMM1 {
	public static void main(String[] args) {
		Kattio io = new Kattio(System.in, System.out);
		while (io.hasMoreTokens()) {
			int i1 = io.getInt();
			int i2 = io.getInt();
			System.out.println(i1);
			System.out.println(i2);
		}
	}
}
