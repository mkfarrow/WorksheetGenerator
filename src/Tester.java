import java.util.Arrays;
import com.google.gson.*;

public class Tester {
	public static final int NUM_DIGITS = 5;
	public static final int NUM_CARRIES = 3;

	public static void main(String[] args) {
		String addProb = 	"{" + 
								"probType: \"intAdd\"," + 
								"description: {" +
									"hasCarry: true," + 
									"numDigits: 3," + 
									"numTerms: 3" + 
								"}" + 
							"}";

		String subProb = 	"{" + 
								"probType: \"intSub\"," +
								"description: {" + 
									"borrows: 0," + 
									"numDigits: [3,3]," +
									"borrowAcrossZero: false" +
								"}" + 
							"}";

		String fracAdd = 	"{" + 
								"probType: \"fracAdd\"," +
								"description: {" +
									"denomToConvert: 1," +
									"sumAboveOne: false" + 
								"}" +
							"}";

		
		
		JsonParser parser = new JsonParser();
		JsonObject description = parser.parse(addProb).getAsJsonObject();
		ProblemGenerator pg = new ProblemGenerator(description);
		
		for (int i = 0; i < 100; i++) {
			IntAddProblem prob = (IntAddProblem) pg.next();
			System.out.println(prob);
			System.out.println(prob.addColumnWrong(0));
			//System.out.println(prob.noDecrementOnBorrow());
			System.out.println();
		}
		
		
		/*
		int[] avg = new int[10];
		IntAddDescriptor d = new IntAddDescriptor();
		for (int i = 0; i < 50000; i++) {
			int[] curr = d.numsWithSumLessThan10(6);
			int sum = 0;
			for (int j = 0; j < curr.length; j++)
				avg[curr[j]]++;
			
		}
		System.out.println(Arrays.toString(avg));
		*/
	}
	
	private int[] arrayFromInt(int num, int numDig) {
		String data = "" + num;
		while (data.length() < numDig)
			data = "0" + data;
		int[] result = new int[data.length()];
		for (int i = 0; i < result.length; i++)
			result[i] = data.charAt(i) - '0';
		return result;
	}
}
