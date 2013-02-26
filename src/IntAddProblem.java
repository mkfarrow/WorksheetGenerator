import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class IntAddProblem extends Problem {
	private static final Random rand = new Random();
	private int solution;
	List<Integer> terms;
	List<int[]> termsArrays;
	int maxLength;
	
	public IntAddProblem(List<Integer> nums) {
		terms = new ArrayList<Integer>(nums);
		termsArrays = new ArrayList<int[]>();
		
		// find the max length and calculate the solution
		int maxLength = 0;
		for (int i = 0; i < terms.size(); i++) {
			maxLength = Math.max(maxLength, (terms.get(i) + "").length());
			solution += terms.get(i);
		}
		
		this.maxLength = maxLength;
		
		for (int i = 0; i < terms.size(); i++)
			termsArrays.add(arrayFromInt(terms.get(i), maxLength));
	}
	
	public int getSolution() {
		return solution;
	}
	
	public int getTerm(int n) {
		if (n >= terms.size() || n < 0)
			throw new IllegalArgumentException();
		return terms.get(n);
	}
	
	private enum ErrorType {
		NO_CARRY_RESET, SAME_COLUMN_CARRY, IGNORE_CARRIES, IGNORE_UNITS_DIGIT, ADD_WRONG;

		public static ErrorType randomError() {
			ErrorType[] values = ErrorType.values();
			return values[DigitGenerator.inRange(0, values.length)];
		}
	}
	
	public int getWrongAnswer() {
		ErrorType mistake = ErrorType.randomError();
		switch (mistake) {
			case NO_CARRY_RESET: return noCarryReset();
			case SAME_COLUMN_CARRY: return sameColumnCarry();
			case IGNORE_UNITS_DIGIT: return ignoreUnitsDigit();
			case ADD_WRONG: return addColumnWrong(DigitGenerator.inRange(1, maxLength + 1));
			case IGNORE_CARRIES: return ignoreCarries();
			default: return 0;
		}
	}
	
	public int[] getNWrongAnswers(int n) {
		return null;
	}
	
	private int[] listToArray(List<Integer> list) {
		int[] res = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
			res[i] = list.get(i);
		return res;
	}

	/**
	 * Generates an incorrect solution to this Problem by ignoring all the carries, except for
	 * carries that occur in the last column.
	 * 
	 * @return an incorrect solution to this Problem
	 */
	public int ignoreCarries() {
		List<Integer> result = new LinkedList<Integer>(); // because we'll add to the front a lot.
		
		int columnSum = 0;
		for (int row = 1; row <= maxLength; row++) {
			columnSum = 0;
			for (int col = 0; col < termsArrays.size(); col++) {
				columnSum += termsArrays.get(col)[maxLength - row];
			}
			result.add(0, columnSum % 10);
		}
		
		columnSum /= 10;
		while (columnSum > 0) {
			result.add(0, columnSum % 10);
			columnSum /= 10;
		}
	
		return ProblemDescriptor.intFromArray(listToArray(result));
	}

	/**
	 * Generates an incorrect solution to this Problem by performing the addition problem from
	 * right to left without ever resetting the carry register.
	 * 
	 * @return an incorrect solution to this Problem
	 */
	public int noCarryReset() {
		List<Integer> result = new LinkedList<Integer>();
		int carriedIn = 0;
		
		int columnSum = 0;
		for (int row = 1; row <= maxLength; row++) {
			columnSum = carriedIn;
			for (int col = 0; col < termsArrays.size(); col++) {
				columnSum += termsArrays.get(col)[maxLength - row];
			}
			carriedIn += columnSum / 10;
			
			result.add(0, columnSum % 10);
		}
		
		columnSum /= 10;
		while (columnSum > 0) {
			result.add(0, columnSum % 10);
			columnSum /= 10;
		}
				
		return ProblemDescriptor.intFromArray(listToArray(result));
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
	int sameColumnCarryGeneral(boolean lastCorrect) {
		List<Integer> result = new LinkedList<Integer>();
		int carriedIn = 0;
		
		int columnSum = 0;
		for (int row = 1; row < maxLength; row++) {
			columnSum = 0;
			for (int col = 0; col < termsArrays.size(); col++) {
				columnSum += termsArrays.get(col)[maxLength - row];
			}
			carriedIn = columnSum / 10;
			
			result.add(0, columnSum % 10 + carriedIn);
		}
		
		columnSum = 0;
		for (int col = 0; col < termsArrays.size(); col++) {
			columnSum += termsArrays.get(col)[0];
		}
		
		if (lastCorrect) {
			
			while (columnSum > 0) {
				result.add(0, columnSum % 10);
				columnSum /= 10;
			}
		} else {
			int digitSum = 0;
			while (columnSum > 0) {
				digitSum += columnSum % 10;
				columnSum /= 10;
			}
			result.add(0, digitSum);
		}
		

		return ProblemDescriptor.intFromArray(listToArray(result));
	}

	/**
	 * Generates an incorrect solution by ignore the units (tens) digit for every carry. In other
	 * words, every column that would cause a carry becomes a 1 in the solution.
	 * 
	 * @return an incorrect solution to this Problem
	 */
	public int ignoreUnitsDigit() {
		List<Integer> result = new LinkedList<Integer>();
		int carriedIn = 0;
		
		int columnSum = 0;
		for (int row = 1; row <= maxLength; row++) {
			columnSum = carriedIn;
			for (int col = 0; col < termsArrays.size(); col++) {
				columnSum += termsArrays.get(col)[maxLength - row];
			}
			carriedIn = columnSum / 10;
			
			result.add(0, columnSum % 10);
		}
		
		columnSum /= 10;
		while (columnSum > 0) {
			result.add(0, columnSum % 10);
			columnSum /= 10;
		}
				
		return ProblemDescriptor.intFromArray(listToArray(result));
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
		numColsWrong = Math.min(numColsWrong, maxLength);
		
		List<Integer> result = new LinkedList<Integer>();
		Set<Integer> mistakeCols = ProblemDescriptor.chooseIndices(numColsWrong, maxLength);
		
		int carriedIn = 0;
		int columnSum = 0;
		for (int row = 1; row <= maxLength; row++) {
			columnSum = carriedIn;
			for (int col = 0; col < termsArrays.size(); col++) {
				columnSum += termsArrays.get(col)[maxLength - row];
			}
			carriedIn = columnSum / 10;
			
			if (mistakeCols.contains(maxLength - row))
				columnSum = mangle(columnSum);
			result.add(0, columnSum % 10);
		}
		
		columnSum /= 10;
		while (columnSum > 0) {
			result.add(0, columnSum % 10);
			columnSum /= 10;
		}
		
		return ProblemDescriptor.intFromArray(listToArray(result));
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
	
	public String toMathML(String id) {
		StringBuilder result = new StringBuilder();
		
		if (id == null)
			result.append("<math>");
		else
			result.append("<math id=" + id + ">");
		
		result.append("<mstack>");
		
		for (int i = 0; i < terms.size() - 1; i++) {
			result.append("<mn>").append(terms.get(i)).append("</mn>");
		}
		
		result.append("<msrow>");
		result.append("<mo>+</mo><none/>");
		result.append("<mn>").append(terms.get(terms.size() - 1)).append("</mn>");
		
		result.append("</msrow>");
		result.append("<msline/>");
		result.append("</mstack>");
		
		result.append("</math>");
		
		return result.toString();
	}
	
	public String toString() {
		String result ="";
		for (int i = 0; i < terms.size(); i++) {
			int sp = 3 - (terms.get(i) + "").length();
			for (int j = 0; j < sp; j++)
				result += " ";
			result += (terms.get(i) + "\n");
		}
		result += "---\n" + solution;
		return result;
	}
}
