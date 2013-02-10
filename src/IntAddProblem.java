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
		ErrorType mistake = ErrorType.randomError();
		switch (mistake) {
			case NO_CARRY_RESET: return noCarryReset();
			case SAME_COLUMN_CARRY: return sameColumnCarry();
			case IGNORE_UNITS_DIGIT: return ignoreUnitsDigit();
			case ADD_WRONG: /*return addColumnWrong(); */
			case IGNORE_CARRIES: return ignoreCarries();
			default: return 0;
		}
	}

	/**
	 * Doesn't ignore last carry
	 */
	public int ignoreCarries() {
		int[] result = new int[first.length + 1];
		for (int i = 1; i < result.length; i++)
			result[result.length - i] = (first[first.length - i] + second[second.length - i]) % 10;
		if (first[0] + second[0] > 10)
			result[0] = 1;
	
		return ProblemDescriptor.intFromArray(result);
	}

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
	
	// gets last column wrong
	public int sameColumnCarry() {
		return sameColumnCarryGeneral(false);
	}
	
	// gets last column right
	public int sameColumnCarryLastColCorrect() {
		return sameColumnCarryGeneral(true);
	}
	
	/**
	 * Adds the carry to the same column it came from
	 * Does the last (most-sig) column correct
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
	 * any column that has a carry becomes 1 in the column that caused the carry
	 */
	private int ignoreUnitsDigit() {
		int[] result = new int[first.length];
		for (int i = 0; i < result.length; i++) {
			int columnSum = first[i] + second[i];
			if (columnSum >= 10)
				result[i] = 1;
			else
				result[i] = columnSum / 10;
		}
		
		return ProblemDescriptor.intFromArray(result);
	}

	
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
	
	/*
	 * mangles a number, trying to mimic clerical errors or mistakes in adding two numbers
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
