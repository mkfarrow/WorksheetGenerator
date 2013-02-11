import java.util.Random;
import java.util.Set;

public class IntAddProblem extends Problem {
	private static final Random rand = new Random();
	private int term1;
	private int term2;
	private int solution;
	private int[] first;
	private int[] second;
	
	
	private enum ErrorType {
		NO_CARRY_RESET, SAME_COLUMN_CARRY, IGNORE_CARRIES, IGNORE_UNITS_DIGIT, ADD_WRONG;

		public static ErrorType randomError() {
			ErrorType[] values = ErrorType.values();
			return values[DigitGenerator.inRange(0, values.length)];
		}
	}
	
	public IntAddProblem(int t1, int t2) {
		term1 = t1;
		term2 = t2;
		solution = term1 + term2;
		
		int length = Math.max(("" + term1).length(), ("" + term2).length());
		first = arrayFromInt(term1, length);
		second = arrayFromInt(term2, length);
	}
	
	public int getSolution() {
		return solution;
	}
	
	public int getTerm1() {
		return term1;
	}
	
	public int getTerm2() {
		return term2;
	}
	
	public int getWrongAnswer() {
		
		/*
		ErrorType mistake = ErrorType.randomError();
		switch (mistake) {
			case NO_CARRY_RESET: return noCarryReset();
			case SAME_COLUMN_CARRY: return sameColumnCarry();
			case IGNORE_UNITS_DIGIT: return ignoreUnitsDigit();
			case ADD_WRONG: return addColumnWrong(1);
			case IGNORE_CARRIES: return ignoreCarries();
			default: return 0;
		}
		*/
		
		return 0;
	}

	/**
	 * Generates an incorrect solution to this Problem by ignoring all the carries, except for
	 * carries that occur in the last column.
	 * 
	 * @return an incorrect solution to this Problem
	 */
	public int ignoreCarries() {
		int[] result = new int[first.length + 1];
		for (int i = 1; i < result.length; i++)
			result[result.length - i] = (first[first.length - i] + second[second.length - i]) % 10;
		if (first[0] + second[0] > 10)
			result[0] = 1;
	
		return ProblemDescriptor.intFromArray(result);
	}

	/**
	 * Generates an incorrect solution to this Problem by performing the addition problem from
	 * right to left without ever resetting the carry register.
	 * 
	 * @return an incorrect solution to this Problem
	 */
	public int noCarryReset() {
		int carriedIn = 0;
		int[] result = new int[first.length + 1];
		
		int columnSum = 0;
		for (int i = 1; i < result.length; i++) {
			columnSum = first[first.length - i] + second[second.length - i] + carriedIn;
			if (columnSum >= 10)
				carriedIn++;
			result[result.length - i] = columnSum % 10;
		}
		result[0] = columnSum / 10;
		
		return ProblemDescriptor.intFromArray(result);
	}
	
	/**
	 * Generates an incorrect solution by adding the carry digit into the same column that caused
	 * the carry (instead of carrying to the next column)
	 * 
	 * @return an incorrect solution to this Problem
	 */
	public int sameColumnCarry() {
		return sameColumnCarryGeneral(false);
	}
	
	/**
	 * Generates an incorrect solution by adding the carry digit into the same column that caused
	 * the carry (instead of carrying to the next column). The last column is carried correctly.
	 * 
	 * @return an incorrect solution to this Problem
	 */
	public int sameColumnCarryLastColCorrect() {
		return sameColumnCarryGeneral(true);
	}
	
	/**
	 * Generates an incorrect solution by adding the carry digit into the same column that caused
	 * the carry (instead of carrying to the next column). Whether or not the last column is added
	 * correctly is determined by lastCorrect.
	 * 
	 * @param lastCorrect whether or not the last column should be carried correctly
	 * @return an incorrect solution to this Problem
	 */
	private int sameColumnCarryGeneral(boolean lastCorrect) {
		int[] result = new int[first.length + 1];
		int columnSum = 0;
		for (int i = 1; i < result.length - 1; i++) {
			columnSum = first[first.length - i] + second[second.length - i];
			result[result.length - i] = (columnSum / 10) + (columnSum % 10);
		}
		
		if (lastCorrect) {
			columnSum = first[0] + second[0];
			result[1] = columnSum % 10;
			result[0] = columnSum / 10;
		} else {
			columnSum = first[0] + second[0];
			result[1] = (columnSum / 10) + (columnSum % 10);
		}
		
		return ProblemDescriptor.intFromArray(result);
	}

	/**
	 * Generates an incorrect solution by ignore the units (tens) digit for every carry. In other
	 * words, every column that would cause a carry becomes a 1 in the solution.
	 * 
	 * @return an incorrect solution to this Problem
	 */
	public int ignoreUnitsDigit() {
		int[] result = new int[first.length];
		for (int i = 0; i < result.length; i++) {
			int columnSum = first[i] + second[i];
			if (columnSum >= 10)
				result[i] = 1;
			else
				result[i] = columnSum;
		}
		
		return ProblemDescriptor.intFromArray(result);
	}

	/**
	 * Generates an incorrect solution to this Problem by performing numColsWrongs single digit
	 * additions incorrectly. Additions will be off by n where n is chosen randomly from 
	 * -2 <= n <= 2, n != 0, with equal probabilities for each value of n.
	 * 
	 * @param numColsWrong the number of columns that should be added incorrectly
	 * @return an incorrect solution to this Problem
	 */
	public int addColumnWrong(int numColsWrong) {
		int[] result = new int[first.length + 1];
		Set<Integer> mistakeCols = ProblemDescriptor.chooseIndices(numColsWrong, first.length);
		
		int carriedIn = 0;
		int columnSum = 0;
		for (int i = 1; i < result.length; i++) {
			columnSum = first[first.length - i] + second[second.length - i] + carriedIn;
			if (mistakeCols.contains(first.length - i))
				columnSum = mangle(columnSum);
			carriedIn = columnSum / 10;
			result[result.length - i] = columnSum % 10;
		}
		result[0] = carriedIn;
		
		return ProblemDescriptor.intFromArray(result);
	}
	
	/**
	 * Obscures a number by randomly adding -2, -1, 1, or 2 to it
	 * 
	 * @param n the number to mangle
	 * @return a number that is at most 2 higher or lower than n
	 */
	private static int mangle(int n) {
		int delta = rand.nextInt(2) + 1;
		if (rand.nextBoolean())
			n += delta;
		else
			n -= delta;
		return n;
	}
}
