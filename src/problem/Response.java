package problem;
import java.util.Map;

/**
 * Encapsulates information about a response on a multiple choice worksheet
 *
 * @param <K> the type of the answer
 */
public class Response<K> implements Map.Entry<K, String> {
	private K answer;
	
	/* this field is only relevant on incorrect solutions and in those situations, it describes the
	 * error that was made in arriving at that solution
	 */
	private String explanation;
	
	/**
	 * Constructs a new Repsonse with the given solution and explanation
	 * 
	 * @param a the answer
	 * @param e the explanation
	 */
	public Response(K a, String e) {
		answer = a;
		explanation = e;
	}
	
	/**
	 * @return the answer
	 */
	public K getKey() {
		return answer;
	}

	/**
	 * @return the explanation
	 */
	public String getValue() {
		return explanation;
	}

	/**
	 * Sets the explanation of this Response
	 * 
	 * @param the explanation to use
	 */
	public String setValue(String explanation) {
		String res = this.explanation;
		this.explanation = explanation;
		return res;
	}
}
