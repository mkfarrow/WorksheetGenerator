package descriptor;

import java.util.HashSet;
import java.util.Set;

import digit.DigitGenerator;

import problem.Problem;

/**
 * ProblemDescriptor is an abstract class that provides the blueprint for objects that are used to
 * describe and generate various different types of math problems. All ProblemDescriptors must
 * implement a makeProblem() method that generates an instance of the Problem they describe.
 * 
 * @author Mikey Farrow
 */
public abstract class ProblemDescriptor {
	
	/**
	 * Generates a single Problem that conforms to the constraints described by the 
	 * ProblemDescriptor
	 * 
	 * @return a Problem that satisfies the description encapsulated by the ProblemDescriptor
	 */
	public abstract Problem makeProblem();
	
	/**
	 * Given an array of digits (where only the digit at index 0 can be negative), returns the
	 * int represented by that array. For example, the array [3,4,5] returns 345.
	 * 
	 * @param data the array of digits
	 * @return the int represented by the array of digits
	 */
	public static int intFromArray(int[] data) {
		String result = "";
		for (int i = 0; i < data.length; i++)
			result += "" + data[i];
		return Integer.parseInt(result);
	}
	
	/**
	 * Returns a Set of randomly chosen indices (usually for use in choosing which digits in a
	 * problem need to satisfy a certain property such as where carries or borrows will occur)
	 * 
	 * @param needed the number of indices to choose
	 * @param length the upper bound of the range of indices to choose from
	 * @return a set of n indices between 0 and length (exclusive)
	 */
	public static Set<Integer> chooseIndices(int n, int length) {
		return chooseIndices(n, 0, length);
	}
	
	/**
	 * Randomly chooses n unique numbers in the range [min, max) and returns them as a Set
	 * 
	 * @param n the number of unique integers to select
	 * @param min the lower bound of the range (inclusive)
	 * @param max the upper bound of the range (exclusive)
	 * @return a Set containing n unique integers in the range specified
	 */
	static Set<Integer> chooseIndices(int n, int min, int max) {
		Set<Integer> result = new HashSet<Integer>();
		while (result.size() < n)
			result.add(DigitGenerator.inRange(min, max));
		return result;
	}
}
