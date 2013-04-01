import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import problem.IntAddProblem;
import problem.Problem;
import problem.Response;
import problem.*;

/**
 * Encapsulates information vital to a multiple choice math worksheet where each question is
 * associated with four responses, one of which is correct.
 * 
 * @author Mikey Farrow
 *
 */
public class Worksheet {
	/* maps each problem to all of the responses to that problem and their letter (A|B|C|D) */
	private Map<Problem, Map<Choice, Response<Integer>>> problems;
	
	/* maps problems --> correct choice (A|B|C|D) */
	private Map<Problem, Choice> answers;
	
	/* where the Worksheet will be printed */
	private PrintStream out;
	
	/**
	 * Enumerates the possible choices for a single problem
	 */
	public enum Choice {
		A, B, C, D;
	}
	
	/**
	 * Constructs a worksheet based off of the ProblemGenerator -> number of problems map
	 * 
	 * @param pds maps ProblemGenerators to a number of problems to generate using that generator
	 * @param out the PrintStream to print the worksheet to
	 */
	public Worksheet(Map<ProblemGenerator, Integer> pds, PrintStream out) {
		problems = new HashMap<Problem, Map<Choice, Response<Integer>>>();
		answers = new HashMap<Problem, Choice>();
		this.out = out;
		
		for (Map.Entry<ProblemGenerator, Integer> entry : pds.entrySet()) {
			ProblemGenerator pg = entry.getKey();
			int num = entry.getValue();
			
			for (int i = 0; i < num; i++) {
				Problem currProb = pg.next(); // the current problem
				
				// four incorrect responses
				List<Response<Integer>> wrongResponses = 
											((IntAddProblem) currProb).getFourChoices();
				
				// (A|B|C|D) --> the response corresponding to that letter
				Map<Choice, Response<Integer>> multipleChoice = 
										new HashMap<Choice, Response<Integer>>();
				
				for (int j = 0; j < wrongResponses.size(); j++) {
					Choice abcd = Choice.values()[j];
					multipleChoice.put(abcd, wrongResponses.get(j));
					if (wrongResponses.get(j).getValue().equals("correct"))
						answers.put(currProb, abcd);
				}
				
				// update the master map
				problems.put(currProb, multipleChoice);
			
			}
		}
		
	}
	
	/**
	 * Writes the html representation of this worksheet to the PrintStream used to construct this
	 * Worksheet
	 * 
	 * @throws IOException
	 */
	public void writeWorksheet() throws IOException {
		// get a huge string of HTML for all the problems		
		
		// parse the actual template and grab the innerHTML of the div that
		// the problems go into
		File worksheetHtmlFile = new File("src/pages/worksheet.html");
		Document template = Jsoup.parse(worksheetHtmlFile, null);
		Element divForProblems = template.getElementById("problemspace");
		
		for (Problem p : problems.keySet()) {
			StringBuilder sb = new StringBuilder();
			
			String choices = makeChoicesHTML(p);
			String mathProb = p.toHTML();
			
			sb.append("<div class=probandchoice>");
			sb.append(mathProb);
			sb.append(choices);
			sb.append("</div>");
			
			// write the problems' HTML inside the problems div
			divForProblems.append(sb.toString());
		}
		
		// write it to the output
		out.println(template.html());
	}
	
	/**
	 * Returns the correct choice on the worksheet for the Problem passed in
	 * 
	 * @param p the Problem whose answer to find
	 * @return the correct choice (A|B|C|D) for p
	 */
	public Choice rightAnswer(Problem p) {
		return answers.get(p);
	}

	private String makeChoicesHTML(Problem p) {
		StringBuilder sb = new StringBuilder();
		Map<Choice, Response<Integer>> responses = problems.get(p);
		
		sb.append("<div><table class=\"choices\">");
		
		
		for (Choice choice : Choice.values()) {
			Response<Integer> r = responses.get(choice);
			sb.append("<tr>");
			sb.append("<td>").append(choice.name()).append("</td>");
			sb.append("<td>").append(r.getKey()).append("</td>");
			sb.append("</tr>");
		}
		
		sb.append("</div>");
		
		return sb.toString();
	}
}
