import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import com.google.gson.*;

public class ProblemGenerator {

	public ProblemGenerator() {
		
	}
	
	public ProblemGenerator(JsonObject probInfo) {
		String type = probInfo.get("probType").getAsString();
		JsonObject description = probInfo.getAsJsonObject("description");
		Gson parser = new Gson();
		
		if (type.equals("intAdd")) {
			IntAddDescriptor probDesc = parser.fromJson(description, IntAddDescriptor.class);
			System.out.println(probDesc.numCarries);
			System.out.println(Arrays.toString(probDesc.digitsPerOp));
		} else if (type.equals("fracAdd")) {
			
		} else if (type.equals("intSub")) {
			
		} else if (type.equals("fracSub")) {
			
		} else if (type.equals("intMult")) {
			
		} else if (type.equals("intDiv")) {
			
		} else {
			System.err.println("problem type not supported");
			System.exit(1);
		}
	}

	public Problem next() {

		return null;
	}

	public Problem[] nextN(int n) {
		Problem[] problems = new Problem[n];
		for (int i = 0; i < n; i++)
			problems[i] = next();
		return problems;
	}


	public static Problem makeAdditionProb(int numDigits, int carries) {
		if (carries > numDigits || numDigits <= 0)
			throw new IllegalArgumentException();
		int[] first = new int[numDigits]; // this is the first operand
		int[] second = new int[numDigits]; // second operand

		// pick which indices there will be carries at
		Set<Integer> carryIndices = new HashSet<Integer>();
		while (carryIndices.size() < carries)
			carryIndices.add((int) (Math.random() * first.length));

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

		int op1 = intFromArray(first);
		int op2 = intFromArray(second);
		return new Problem(op1, op2, '+');
	}

	private static int intFromArray(int[] data) {
		String result = "";
		for (int i = 0; i < data.length; i++)
			result += "" + data[i];
		return Integer.parseInt(result);
	}
	
	
	
	/**************************************************************
	 * The following descriptor classes encapsulate the parameters
	 * necessary to generate different types of problems.
	 **************************************************************/
	public class IntAddDescriptor {
		int numCarries;
		int[] digitsPerOp; // number of digits in each operator	
		
		public IntAddDescriptor() { }
	}
	
	public class FracAddDescriptor {
		int denomsToConvert; // 0, 1, or 2
		boolean sumOverOne;
	}
		
	public class IntSubDescriptor {
		int borrows;
		int[] numDigits;
		boolean borrowAcrossZero;
		boolean canBeNeg;
	}
	
	public class FracSubDescriptor {
		int denomsToConvert;
		boolean negativeDiff;
	}

}
