import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Describes and generates addition problems with integer operands. IntAddDescriptors encapsulate
 * the number of carries in the problem and the number of digits per operand. 
 * 
 * JSON can be used to represent IntAddDescriptors as follows:
 * "{probType: "intAdd", description: {numCarries: <some_int>, numDigits: <some_int>} }"
 * 
 * @author Mikey Farrow
 */
public class IntAddDescriptor extends ProblemDescriptor {
	boolean hasCarry;
	int numDigits; // number of digits in each operator	
	int numTerms;
	
	/**
	 * The only purpose this serves is that the JSON parser needs a zero-argument constructor in
	 * order to create instances of this object on the fly.
	 */
	public IntAddDescriptor() { }

	public IntAddDescriptor(boolean c, int nd, int nt) {
		hasCarry = c;
		//numDigits = Arrays.copyOf(nd, nd.length);
		numDigits = nd;
		numTerms = nt;
	}
	
	public Problem makeProblem() {
		return hasCarry ? carryProblem() : noCarryProblem();
	}
	
	/**
	 * Makes a problem that has carries in it.
	 * 
	 * @return an IntAddProblem with carries
	 */
	private IntAddProblem carryProblem() {
		List<int[]> nums = new ArrayList<int[]>();
		
		for (int i = 0; i < numTerms; i++)
			nums.add(new int[numDigits]);
		
		Set<Integer> carryIndices = chooseIndices(DigitGenerator.inRange(1, numDigits), numDigits);
		
		nums.get(0)[0] = DigitGenerator.randomNonZeroDigit();
		for (int i = 1; i < numDigits; i++)
			if (carryIndices.contains(i))
				nums.get(0)[i] = DigitGenerator.randomNonZeroDigit();
			else
				nums.get(0)[i] = DigitGenerator.randomDigit();
		
		for (int i = numDigits - 1; i >= 0; i--) {
			int carryTerm = DigitGenerator.inRange(0, numTerms); // force a carry here
			for (int j = 1; j < numTerms; j++) {
				int[] currTerm = nums.get(j);
				if (j == carryTerm && carryIndices.contains(i))
					currTerm[i] = DigitGenerator.withCarry(Math.max(nums.get(j - 1)[i], 1));
				else
					currTerm[i] = DigitGenerator.randomDigit();
			}
		}
		
		List<Integer> terms = new ArrayList<Integer>();
		for (int i = 0; i < nums.size(); i++)
			terms.add(intFromArray(nums.get(i)));
		
		return new IntAddProblem(terms);
	}
	
	/**
	 * Makes a problem with no carries in it. Each number 0 through 9 has an equal probability
	 * of appearing as the sum of a column in the problems returned by this method.
	 * 
	 * @return an IntAddProblem without any carries
	 */
	private IntAddProblem noCarryProblem() {
		List<int[]> nums = new ArrayList<int[]>();
		
		for (int i = 0; i < numTerms; i++)
			nums.add(new int[numDigits]);
		
		for (int dig = numDigits - 1; dig >= 0; dig--) {
			int[] values = numsWithSumLessThan10(numTerms);
			for (int i = 0; i < numTerms; i++) {
				int[] curr = nums.get(i);
				curr[dig] = values[i];
			}
		}
		
		List<Integer> terms = new ArrayList<Integer>();
		for (int i = 0; i < nums.size(); i++)
			terms.add(intFromArray(nums.get(i)));
		
		return new IntAddProblem(terms);
	}
	
	/**
	 * Generates and returns an array of n ints (all between 0 and 10 exclusive) whose sum is a
	 * random number that is less than 10
	 * 
	 * @param n the number of ints to use
	 * @return an array with n ints that sum to a random number less than 10
	 */
	private int[] numsWithSumLessThan10(int n) {
		return numsWithSum(n, DigitGenerator.randomDigit());
	}
	
	/**
	 * Generates and returns an array of n ints (all between 0 and 10 exclusive) whose sum is equal
	 * to desiredSum
	 * 
	 * @param n the number of ints to use
	 * @param desiredSum the sum to reach
	 * @return an array with n ints whose sum is desiredSum
	 */
	public int[] numsWithSum(int n, int desiredSum) {
		double[] temp = new double[n];
		int[] result = new int[n];
		
		double arraySum = 0;
		for (int i = 0; i < temp.length; i++) {
			temp[i] = DigitGenerator.randomDigit();
			arraySum += temp[i];
		}
		
		double scale = arraySum / 9;
		
		for (int i = 0; i < temp.length; i++) {
			temp[i] /= scale;
			result[i] = (int) Math.round(temp[i]);
		}
		
		arraySum = 0;
		for (int i = 0; i < result.length; i++)
			arraySum += result[i];
				
		
		// these while loops hone in on the desiredSum... at the expense of really messing the number of digits up.
		while (arraySum > desiredSum) {
			int index = DigitGenerator.inRange(0, n);
			if (result[index] > 0) {
				result[index]--;
				arraySum--;
			}
		}
		
		while (arraySum < desiredSum) {
			int index = DigitGenerator.inRange(0, n);
			if (result[index] < 9) {
				result[index]++;
				arraySum++;
			}
		}
		
		return result;
	}
	
	
//	/**
//	 * Generates and returns a Problem that satisfies the description encapsulated by this
//	 * IntAddDescriptor.
//	 * 
//	 * @return a Problem that satisfies the description encapsulated by this IntAddDescriptor
//	 */
//	public Problem makeProblem() {
//		if (numCarries > numDigits || numDigits <= 0)
//			throw new IllegalArgumentException();
//		int[] first = new int[numDigits]; // this is the first operand
//		int[] second = new int[numDigits]; // second operand
//
//		Set<Integer> carryIndices = chooseIndices(numCarries, first.length);
//
//		// fill in digits for first operand
//		for (int i = 0; i < first.length; i++) {
//			first[i] = DigitGenerator.randomNonZeroDigit();
//			if (carryIndices.contains(i + 1) && first[i] == 9 && !carryIndices.contains(i))
//				first[i]--; // a carry in the digit to the right would cause an unwanted carry here
//		}
//
//		// fill in digits for second operand
//		int carriedIn = 0;
//		for (int i = second.length - 1; i >= 0; i--) {
//			int addend = Math.min(9, first[i] + carriedIn);
//			if (carryIndices.contains(i))
//				second[i] = DigitGenerator.withCarry(addend);
//			else
//				second[i] = DigitGenerator.noCarry(addend);
//			carriedIn = (first[i] + second[i] + carriedIn) / 10;
//		}
//		
//		return new IntAddProblem(intFromArray(first), intFromArray(second));
//	}
}