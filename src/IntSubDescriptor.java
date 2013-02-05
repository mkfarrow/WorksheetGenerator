import java.util.Arrays;
import java.util.Set;

public class IntSubDescriptor extends ProblemDescriptor {
	int borrows;
	int[] numDigits;
	boolean borrowAcrossZero;
	
	public IntSubDescriptor() { }
	
	/**
	 * currently only generates probs with positive diffs
	 * 
	 * doesn't actually work yet
	 */
	public Problem makeProblem() {
		//check the fields and make sure they are a legal description
		if (borrowAcrossZero) {
			throw new UnsupportedOperationException("borrow across zero not yet implemented");
			// return makeProbWithAcrossZero()...
		} else {
			int[] first = new int[numDigits[0]];
			int[] second = new int[numDigits[1]];
			
			Set<Integer> borrowIndices = chooseIndices(borrows, second.length);
			for (int i = 0; i < first.length; i++) {
				if (borrowIndices.contains(i) && borrowIndices.contains(i + 1)) // check for other cases too!
					first[i] = DigitGenerator.randomNonZeroDigit();
				else if (borrowIndices.contains(i) != borrowIndices.contains(i + 1))
					first[i] = DigitGenerator.inRange(1,9);
				else
					first[i] = DigitGenerator.randomDigit();
			}
			
			for (int i = 1; i <= second.length; i++) {
				if (borrowIndices.contains(second.length - i))
					second[second.length - i] = DigitGenerator.withBorrow(first[first.length - i]);
				else
					second[second.length - i] = DigitGenerator.noBorrow(first[first.length - i]);
			}
			
			/*
			if (borrowIndices.contains(1)) 
				second[0] = DigitGenerator.inRange(0, );
			*/
			
			System.out.println(borrowIndices);
			System.out.println(Arrays.toString(first));
			System.out.println(Arrays.toString(second));
			
			int op1 = intFromArray(first);
			int op2 = intFromArray(second);
			
			System.out.println(op1 - op2);
			System.out.println();
			
			return null;
		}
	}
	
}