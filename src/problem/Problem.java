package problem;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

/**
 * Encapsulates a problem. Problems can do things like generate HTML representations of themselves,
 * come up with seemingly correct solutions that are actually wrong (to make viable multiple choice
 * options).
 * 
 * @author Mikey Farrow
 */
public abstract class Problem {

	/**
	 * Returns a plausible but incorrect solution to a problem
	 * 
	 * @return an incorrect solution to this problem as a Response
	 */
	public abstract Response getWrongAnswer();
	
	/**
	 * Returns an html representation of this Problem
	 * 
	 * @return html text that can be used to display this Problem
	 */
	public abstract String toHTML();
	
	/**
	 * Turns a number into an equivalent array of digits
	 * 
	 * @param num the number to turn into an array
	 * @param numDig the max number of digits that an operand in this problem has
	 * @return an int[] representing the number 
	 */
	static int[] arrayFromInt(int num, int numDig) {
		String data = "" + num;
		while (data.length() < numDig)
			data = "0" + data;
		int[] result = new int[data.length()];
		for (int i = 0; i < result.length; i++)
			result[i] = data.charAt(i) - '0';
		return result;
	}
	
}
