import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public abstract class Problem {

	public abstract Response getWrongAnswer();
	
	public abstract String toHTML();
	
	static int[] arrayFromInt(int num, int numDig) {
		String data = "" + num;
		while (data.length() < numDig)
			data = "0" + data;
		int[] result = new int[data.length()];
		for (int i = 0; i < result.length; i++)
			result[i] = data.charAt(i) - '0';
		return result;
	}
	
	/**
	 * Encapsulates information about a response on a multiple choice worksheet
	 *
	 * @param <K> the type of the answer
	 */
	public class Response<K> implements Map.Entry<K, String> {
		private K answer;
		private String explanation;
		
		
		public Response(K a, String e) {
			answer = a;
			explanation = e;
		}
		
		public K getKey() {
			return answer;
		}

		public String getValue() {
			return explanation;
		}

		public String setValue(String explanation) {
			String res = this.explanation;
			this.explanation = explanation;
			return res;
		}
	}	
	
}
