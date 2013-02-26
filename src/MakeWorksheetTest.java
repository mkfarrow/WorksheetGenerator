import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MakeWorksheetTest {
	
	private static final String addProbWithCarry = "{" + 
			"probType: \"intAdd\"," + 
			"description: {" +
				"hasCarry: true," + 
				"numDigits: 3," + 
				"numTerms: 3" + 
			"}" + 
		"}";
	
	private static final String addProbNoCarry = "{" + 
			"probType: \"intAdd\"," + 
			"description: {" +
				"hasCarry: false," + 
				"numDigits: 4," + 
				"numTerms: 2" + 
			"}" + 
		"}";

	private static final String subProb = "{" + 
			"probType: \"intSub\"," +
			"description: {" + 
				"borrows: 0," + 
				"numDigits: [3,3]," +
				"borrowAcrossZero: false" +
			"}" + 
		"}";
	
	private static String[] problemJSONs = {addProbWithCarry, addProbNoCarry, subProb};
	
	public static void main(String[] args) {
		List<Problem> problemList = new ArrayList<Problem>();
		JsonParser parser = new JsonParser();
		
		for (String probJSON : problemJSONs) {
			JsonObject description = parser.parse(probJSON).getAsJsonObject();
			ProblemGenerator pg = new ProblemGenerator(description);
			for (int i = 0; i < 5; i++)
				problemList.add(pg.next());
		}
		
		System.out.println(problemList.get(1).toMathML());
		
		
		
	}
	
	
	
}
