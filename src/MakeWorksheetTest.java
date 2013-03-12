import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

	private static final String addProb = "{" + 
			"probType: \"intAdd\"," + 
			"description: {" +
				"hasCarry: true," + 
				"numDigits: 5," + 
				"numTerms: 4" + 
			"}" + 
		"}";
	
	private static String[] problemJSONs = {addProbWithCarry, addProbNoCarry, addProb};
	
	public static void main(String[] args) throws IOException {
		JsonParser parser = new JsonParser();
		Map<ProblemGenerator, Integer> problems = new HashMap<ProblemGenerator,Integer>();
		for (String probJSON : problemJSONs) {
			JsonObject description = parser.parse(probJSON).getAsJsonObject();
			ProblemGenerator pg = new ProblemGenerator(description);
			problems.put(pg, 5);
		}
		
		Worksheet ws = new Worksheet(problems, System.out);
		ws.writeWorksheet();
		
	}
	
	
	
}
