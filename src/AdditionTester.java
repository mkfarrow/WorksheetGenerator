import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AdditionTester {

	/*
	 * Creates many different addition problems with various numbers of carries and digits per
	 * operand and tests to see if they have the correct number of carries.
	 */
	@Test
	public void test() {
		for (int i = 0; i < 1000; i++)
			for (int digs = 0; digs < 6; digs++)
				for (int carries = 0; carries < digs; carries++) {
					ProblemDescriptor pd = new IntAddDescriptor(carries, digs);
					ProblemGenerator pg = new ProblemGenerator(pd);
					IntAddProblem p = (IntAddProblem) pg.next();
					assertEquals(carries, numCarries(p.getTerm1(), p.getTerm2(), digs));
				}
	}

	/*
	 * Given two operands and the number of digits in the bigger number, returns the number of
	 * carries one performs to add the two numbers.
	 */
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

	/*
	 * Converts an int into an array of digits. For example, 1234 becomes [1,2,3,4]
	 */
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
