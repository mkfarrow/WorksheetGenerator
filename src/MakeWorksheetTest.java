import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		List<Problem> problemList = new ArrayList<Problem>();
		JsonParser parser = new JsonParser();
		
		for (String probJSON : problemJSONs) {
			JsonObject description = parser.parse(probJSON).getAsJsonObject();
			ProblemGenerator pg = new ProblemGenerator(description);
			for (int i = 0; i < 5; i++) {
				problemList.add(pg.next());
			}
		}
		
		Collections.shuffle(problemList);

		
		// get a huge string of HTML for all the problems
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < problemList.size(); i++) {
			sb.append(problemList.get(i).toHTML());
		}
		
		// parse the actual template and grab the innerHTML of the div that
		// the problems go into
		File worksheetHtmlFile = new File("src/pages/worksheet.html");
		Document template = Jsoup.parse(worksheetHtmlFile, null);
		Element divForProblems = template.getElementById("problemspace");
		
		// write the problems' HTML inside the problems div
		divForProblems.append(sb.toString());
		
		// write all the html out to a file
		File outFile = new File("src/pages/example.html");
		PrintStream out = new PrintStream(outFile);
		
		out.println(template.html());
		
	}
	
	
	
}
