public abstract class Problem {

	public abstract int getWrongAnswer();
	public abstract String toMathML(String id);
	
	public String toMathML() {
		return toMathML(null);
	}
	
	static int[] arrayFromInt(int num, int numDig) {
		String data = "" + num;
		while (data.length() < numDig)
			data = "0" + data;
		int[] result = new int[data.length()];
		for (int i = 0; i < result.length; i++)
			result[i] = data.charAt(i) - '0';
		return result;
	}
	
}
