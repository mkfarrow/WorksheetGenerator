import java.util.Arrays;
import com.google.gson.*;

public class Tester {
	public static final int NUM_DIGITS = 5;
	public static final int NUM_CARRIES = 3;

	public static void main(String[] args) {
		String addProb = 	"{" + 
								"probType: \"intAdd\"," + 
								"description: {" + 
									"numCarries: 3," + 
									"digitsPerOp: [5,5]" + 
								"}" + 
							"}";

		String subProb = 	"{" + 
								"probType: \"intSub\"," +
								"description: {" + 
									"numBorrows: 3," + 
									"digitsPerOp: [5,3]," +
									"accrossZero: false," +
									"canBeNeg: false" +
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
	}
}
