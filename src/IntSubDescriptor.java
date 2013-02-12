import java.util.Arrays;
import java.util.Set;

public class IntSubDescriptor extends ProblemDescriptor {
	int borrows;
	int[] numDigits;
	boolean borrowAcrossZero;
	
	/** needed for JSON parser */
	public IntSubDescriptor() { }
	
	/**
	 * currently only generates probs with positive diffs
	 * 
	 * I think this works for the most part now
	 */
	public Problem makeProblem() {
		if (numDigits.length != 2)
			throw new IllegalArgumentException("can only make subtraction probs with two operands");
		if (Math.max(numDigits[0], numDigits[1]) <= borrows)
			throw new IllegalArgumentException("too many borrows for the number of digits");
		
		if (borrowAcrossZero) {
			throw new UnsupportedOperationException("borrow across zero not yet implemented");
			// return makeProbWithAcrossZero()...
		} else {
			return noAcrossZero();
		}
	}
	
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
	
	private Problem acrossZero() {
		if (borrows == 0)
			throw new IllegalArgumentException("borrowAcrossZero is true, but borrows is 0");
		
		int[] first = new int[Math.max(numDigits[0], numDigits[1])];
		int[] second = new int[first.length];
		int smaller = Math.min(numDigits[0], numDigits[1]);
		
		int borrowsAcross = DigitGenerator.inRange(1, borrows);
		int borrowsLeft = borrows - borrowsAcross;
		
		first[0] = DigitGenerator.randomNonZeroDigit();
		for (int i = 1; i < borrowsAcross; i++)
			;
		
		
		return null;
	}
	
	
}