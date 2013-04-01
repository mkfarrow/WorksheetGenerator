package descriptor;

import java.util.Arrays;
import java.util.Set;

import digit.DigitGenerator;

import problem.IntSubProblem;
import problem.Problem;

/**
 * This only works for problems with two operands, but otherwise it is analogous to 
 * IntAddDescriptor but for subtraction problems.
 */
public class IntSubDescriptor extends ProblemDescriptor {
	int borrows;
	int[] numDigits;
	boolean borrowAcrossZero;
	
	/** needed for JSON parser */
	public IntSubDescriptor() { }
	
	/**
	 * @see ProblemDescriptor#makeProblem()
	 */
	public Problem makeProblem() {
		if (numDigits.length != 2)
			throw new IllegalArgumentException("can only make subtraction probs with two operands");
		if (Math.max(numDigits[0], numDigits[1]) <= borrows)
			throw new IllegalArgumentException("too many borrows for the number of digits");

		return borrowAcrossZero ? acrossZero() : noAcrossZero();
	}
	
	/**
	 * Makes a Problem that satisfies the description in the case that there are no borrows across
	 * zero.
	 * 
	 * @return a Problem fitting the description of this IntSubDescriptor
	 */
	private Problem noAcrossZero() {		
		int[] first = new int[Math.max(numDigits[0], numDigits[1])];
		int[] second = new int[first.length];
		int smaller = Math.min(numDigits[0], numDigits[1]);
		Set<Integer> borrowIndices = chooseIndices(borrows, Math.max(first.length - smaller, 1), second.length);
		
		first[0] = DigitGenerator.randomNonZeroDigit();
		for (int i = 1; i < first.length; i++) {
			if (borrowIndices.contains(i) && !borrowIndices.contains(i + 1))
				first[i] = DigitGenerator.inRange(0, 9);
			else if (borrowIndices.contains(i + 1))
				first[i] = DigitGenerator.randomNonZeroDigit();
			else
				first[i] = DigitGenerator.randomDigit();
		}
		
		int[] afterBorrows = new int[first.length];
		for (int i = 0; i < afterBorrows.length; i++)
			afterBorrows[i] = borrowIndices.contains(i + 1) ? first[i] - 1 : first[i];
		
		for (int i = 1; i <= smaller; i++) {
			if (borrowIndices.contains(second.length - i))
				second[second.length - i] = DigitGenerator.withBorrow(afterBorrows[first.length - i]);
			else
				second[second.length - i] = DigitGenerator.noBorrow(afterBorrows[first.length - i]);
		}
		
		return new IntSubProblem(intFromArray(first), intFromArray(second));
	}
	
	/**
	 * Makes a Problem that satisfies the description in the case that there are borrows across
	 * zero.
	 * 
	 * @throws 	IllegalArgumentException if borrowAcrossZero is true but borrows is 0 or if there is
	 * 			a borrow across zero requested and there are less than 3 digits in the problem description
	 * @return a Problem fitting the description of this IntSubDescriptor
	 */
	private Problem acrossZero() {
		if (borrows == 0)
			throw new IllegalArgumentException("borrowAcrossZero is true, but borrows is 0");
		int larger = Math.max(numDigits[0], numDigits[1]);
		if (larger < 3)
			throw new IllegalArgumentException("cannot perform borrows across zero with less than 3 digits");
		
		int[] first = new int[larger];
		int[] second = new int[first.length];
		int smaller = Math.min(numDigits[0], numDigits[1]);
		
		int starter = DigitGenerator.inRange(Math.max(first.length - smaller, 2), second.length);
		int end = DigitGenerator.inRange(1, starter);
		first[0] = DigitGenerator.randomNonZeroDigit();

		for (int i = 1; i < first.length; i++) {
			if (i == starter)
				first[i] = DigitGenerator.inRange(1, 9);
			else if (i < starter && i >= end)
				first[i] = 0;
			else
				first[i] = DigitGenerator.inRange(1, 9);
		}
		
		for (int i = 1; i <= smaller; i++) {
			if (i == starter)
				second[second.length - i] = DigitGenerator.withBorrow(first[first.length - i]);
			else if (i < starter && i >= end)
				second[second.length - i] = DigitGenerator.randomNonZeroDigit();
			else if (end == second.length - i + 1)
				second[second.length - i] = DigitGenerator.noBorrow(first[first.length - i] - 1);
			else
				second[second.length - i] = DigitGenerator.noBorrow(first[first.length - i]);
		}
		
		
		return new IntSubProblem(intFromArray(first), intFromArray(second));
	}
	
	
}