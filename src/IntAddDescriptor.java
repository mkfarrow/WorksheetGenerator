import java.util.HashSet;
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
	int numCarries, numDigits; // number of digits in each operator	
	
	/**
	 * The only purpose this serves is that the JSON parser needs a zero-argument constructor in
	 * order to create instances of this object on the fly.
	 */
	public IntAddDescriptor() { }

	public IntAddDescriptor(int nc, int nd) {
		numCarries = nc;
		numDigits = nd;
	}
	
	/**
	 * Generates and returns a Problem that satisfies the description encapsulated by this
	 * IntAddDescriptor.
	 * 
	 * @return a Problem that satisfies the description encapsulated by this IntAddDescriptor
	 */
	public Problem makeProblem() {
		if (numCarries > numDigits || numDigits <= 0)
			throw new IllegalArgumentException();
		int[] first = new int[numDigits]; // this is the first operand
		int[] second = new int[numDigits]; // second operand

		Set<Integer> carryIndices = chooseIndices(numCarries, first.length);

		// fill in digits for first operand
		for (int i = 0; i < first.length; i++) {
			first[i] = DigitGenerator.randomNonZeroDigit();
			if (carryIndices.contains(i + 1) && first[i] == 9 && !carryIndices.contains(i))
				first[i]--; // a carry in the digit to the right would cause an unwanted carry here
		}

		// fill in digits for second operand
		int carriedIn = 0;
		for (int i = second.length - 1; i >= 0; i--) {
			int addend = Math.min(9, first[i] + carriedIn);
			if (carryIndices.contains(i))
				second[i] = DigitGenerator.withCarry(addend);
			else
				second[i] = DigitGenerator.noCarry(addend);
			carriedIn = (first[i] + second[i] + carriedIn) / 10;
		}
		
		return new Problem(intFromArray(first), intFromArray(second), '+');
	}
}