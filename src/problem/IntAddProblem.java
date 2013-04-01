package problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import descriptor.ProblemDescriptor;
import digit.DigitGenerator;

/**
 * Encapsulates an integer addition problem with any number of terms.
 * 
 * @author Mikey Farrow
 *
 */
public class IntAddProblem extends Problem {
	private static final Random rand = new Random();
	private int solution;
	private List<Integer> terms;
	private List<int[]> termsArrays;
	private int maxLength;
	
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
	
	/**
	 * Returns the answer to this Problem
	 * 
	 * @return the solution to this Problem as an int
	 */
	public int getSolution() {
		return solution;
	}
	
	/**
	 * Returns the nth term of this Problem
	 * 
	 * @param n the index of the term to get (0 is first)
	 * @throws IllegalArgumentException if there is no nth term
	 */
	public int getTerm(int n) {
		if (n >= terms.size() || n < 0)
			throw new IllegalArgumentException();
		return terms.get(n);
	}
	
	/**
	 * This is a way of doing some bookkeeping regarding what error was made in order to produce a
	 * wrong solution. This enum type keeps track of all the possible types of errors that are
	 * generated and a String that describes the mistake.
	 */
	private enum ErrorType {
		NO_CARRY_RESET		("student didn't reset the carry digit"), 
		SAME_COLUMN_CARRY 	("student added carries back into the same column"), 
		IGNORE_CARRIES		("student ignored all carries"), 
		IGNORE_UNITS_DIGIT	("student ignored the units digits"), 
		ADD_WRONG			("student probably carried correctly but made a mistake adding a column");
		
		String message;
		ErrorType(String m) {
			message = m;
		}
		
		/**
		 * Chooses a random member of this enum type and returns it
		 * 
		 * @return a random type of error of type ErrorType
		 */
		public static ErrorType randomError() {
			ErrorType[] values = ErrorType.values();
			return values[DigitGenerator.inRange(0, values.length)];
		}
	}
	
	/**
	 * Returns a Response (see problem.Response) that encapsulates an incorrect answer to this
	 * Problem by choosing a random mistake to make and then calculating an answer using that
	 * mistake
	 */
	public Response<Integer> getWrongAnswer() {
		ErrorType mistake = ErrorType.randomError();
		switch (mistake) {
			case NO_CARRY_RESET: 
				return new Response<Integer>(noCarryReset(), ErrorType.NO_CARRY_RESET.message);
			case SAME_COLUMN_CARRY: 
				return new Response<Integer>(sameColumnCarry(), 
							ErrorType.SAME_COLUMN_CARRY.message);
			case IGNORE_UNITS_DIGIT: 
				return new Response<Integer>(ignoreUnitsDigit(), ErrorType.NO_CARRY_RESET.message);
			case ADD_WRONG: 
				return new Response<Integer>(addColumnWrong(DigitGenerator.inRange(1, maxLength)),
																ErrorType.NO_CARRY_RESET.message);
			case IGNORE_CARRIES: 
				return new Response<Integer>(ignoreCarries(), ErrorType.NO_CARRY_RESET.message);
			
			default: return null;
		}
	}
	
	/**
	 * Returns a list of incorrect solutions to this Problem as Response<Integer>
	 * 
	 * @returns a List of four incorrect solutions to this Problem
	 */
	public List<Response<Integer>> getFourChoices() {
		Set<Response<Integer>> set = new TreeSet<Response<Integer>>(new Comparator<Response<Integer>>() {
					public int compare(Response<Integer> a, Response<Integer> b) {
						return a.getKey() - b.getKey();
					}
				});
		
		while (set.size() < 3) {
			Response<Integer> r = getWrongAnswer();

			if (r.getKey() != solution) 
				set.add(r);				
		}
		
		List<Response<Integer>> list = new ArrayList<Response<Integer>>();
		int i = 0;
		for (Response<Integer> ans : set)
			list.add(ans);
		list.add(new Response<Integer>(solution, "correct"));
		
		Collections.shuffle(list);
		
		return list;
	}
	
	/**
	 * Converts a list to an array
	 * 
	 * @param list
	 * @return an array form of list
	 */
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
		return Math.max(n,0);
	}
	
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div><table class=\"problem\">");
		
		for (int i = 0; i < termsArrays.size(); i++) {
			int[] curr = termsArrays.get(i);
			sb.append("<tr>");
			sb.append("<td>");
			if (i == termsArrays.size() - 1)
				sb.append("+");
			sb.append("</td>");
			
			boolean seenNonZero = false;
			for (int j = 0; j < curr.length; j++) {
				
				sb.append("<td>");
				if (curr[j] != 0)
					seenNonZero = true;
				
				if (seenNonZero)
					sb.append(curr[j]);
				
				sb.append("</td>");
				
			}
			sb.append("</tr>");
		}
		
		
		sb.append("</table></div>");
		return sb.toString();
		
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
