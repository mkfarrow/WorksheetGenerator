import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import com.google.gson.*;

public class ProblemGenerator {
	
	/*
	 * Maps problem type Strings (as in the JSON description object) to an empty instance of the
	 * class used to store that type. Used to get the Class object associated with the problem type
	 */
	private static final Map<String, ProblemDescriptor> typeToClass = getTypeMap();
	 
	/*
	 * Parses JSON objects (see Google's API for Gson and the related classes here:
	 * <a href="http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/index.html" />
	 */ 
	private static final Gson parser = new Gson();
	
	/*
	 * This will probably change once a single instance of ProblemGenerator can generate multiple
	 * problem types
	 */
	private ProblemDescriptor pd;
		
	/**
	 * Constructs a ProblemGenerator that can generate random problems of the type described by
	 * the ProblemDescriptor passed in
	 * 
	 * @param pd the type of problem the ProblemGenerator will generate
	 */
	public ProblemGenerator(ProblemDescriptor pd) {	
		this.pd = pd;
	}
	
	/**
	 * Constructs a ProblemGenerator that can generate random problems of the type described by
	 * the JsonObject passed in
	 * 
	 * @param probInfo a JSON object with the fields probType: <some problem type>,
	 * description: {<js object that describes the fields of that problem>}
	 */
	public ProblemGenerator(JsonObject probInfo) {
		String type = probInfo.get("probType").getAsString();
		JsonObject jsonDescription = probInfo.getAsJsonObject("description");	
		
		ProblemDescriptor className = typeToClass.get(type);
		
		if (className == null)
			throw new IllegalArgumentException("problem type not supported");
		
		pd = parser.fromJson(jsonDescription, className.getClass());
	}
	
	public Problem next() {
		return pd.makeProblem();
	}

	public Problem[] nextN(int n) {
		Problem[] problems = new Problem[n];
		for (int i = 0; i < n; i++)
			problems[i] = next();
		return problems;
	}

	/**
	 * Initializes the type map to store all of the different problem types, indexed by the String
	 * they are referred to by in their JSON representation.
	 * 
	 * @return a Map from type name to an empty instance of the class the name refers to
	 */
	private static Map<String, ProblemDescriptor> getTypeMap() {
		Map<String, ProblemDescriptor> result = new HashMap<String, ProblemDescriptor>();
		
		// populate the map with all the problem types
		result.put("intAdd", new IntAddDescriptor());
		result.put("intSub", new IntSubDescriptor());
		result.put("fracAdd", new FracAddDescriptor());
		result.put("fracSub", new FracSubDescriptor());
		
		return result;
	}
}
