public class IntSubProblem extends Problem {
	private int term1, term2;
	private int solution;
	
	public IntSubProblem(int t1, int t2) {
		term1 = t1;
		term2 = t2;
		solution = t1 - t2;
	}
	
	public int getWrongAnswer() {
		return 0;
	}
	
	public int getSolution() {
		return solution;
	}
	
	public int getTerm1() {
		return term1;
	}
	
	public int getTerm2() {
		return term2;
	}
	
	public String toString() {
		return term1 + " - " + term2 + " = " + solution;
	}

}
