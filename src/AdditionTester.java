import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class AdditionTester {

	@Test
	public void test() {
		for (int i = 0; i < 1000; i++)
			for (int digs = 0; digs < 6; digs++)
				for (int carries = 0; carries < digs; carries++)
					Problem p = ProblemGenerator.makeAdditionProb(digs, carries);
	}

	private int numCarries(int a, int b, int numDig) {
		int[] first = arrayFromInt(a, numDig);
		int[] second = arrayFromInt(b, numDig);
		int carries = 0;
		int carriedIn = 0;
		for (int i = Math.max(first.length, second.length) - 1; i >= 0; i--) {
			if (first[i] + second[i] + carriedIn >= 10)
				carries++;
			carriedIn = (first[i] + second[i] + carriedIn) / 10;
		}
		return carries;
	}

	private int[] arrayFromInt(int num, int numDig) {
		String data = "" + num;
		while (data.length() < numDig)
			data = "0" + data;
		int[] result = new int[data.length()];
		for (int i = 0; i < result.length; i++)
			result[i] = data.charAt(i) - '0';
		return result;
	}
}
