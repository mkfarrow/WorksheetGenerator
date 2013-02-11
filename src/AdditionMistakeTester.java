
public class AdditionMistakeTester {
	public static void main(String[] args) {
		
//		IntAddProblem p1 = new IntAddProblem(41, 9);
//		System.out.println(p1.noCarryReset());
//		IntAddProblem p2 = new IntAddProblem(328, 917);
//		System.out.println(p2.noCarryReset());
//		IntAddProblem p3 = new IntAddProblem(989, 52);
//		System.out.println(p3.noCarryReset());
//		IntAddProblem p4 = new IntAddProblem(66, 887);
//		System.out.println(p4.noCarryReset());
//		IntAddProblem p5 = new IntAddProblem(216, 13);
//		System.out.println(p5.noCarryReset());
//		
//		System.out.println();
//		p1 = new IntAddProblem(41, 9);
//		System.out.println(p1.ignoreCarries());
//		p2 = new IntAddProblem(328, 917);
//		System.out.println(p2.ignoreCarries());
//		p3 = new IntAddProblem(989, 52);
//		System.out.println(p3.ignoreCarries());
//		p4 = new IntAddProblem(66, 887);
//		System.out.println(p4.ignoreCarries());
//		p5 = new IntAddProblem(216, 13);
//		System.out.println(p5.ignoreCarries());
//		
//		System.out.println();
//		p1 = new IntAddProblem(41, 9);
//		System.out.println(p1.sameColumnCarry());
//		p2 = new IntAddProblem(328, 917);
//		System.out.println(p2.sameColumnCarry());
//		p3 = new IntAddProblem(989, 52);
//		System.out.println(p3.sameColumnCarry());
//		p4 = new IntAddProblem(66, 887);
//		System.out.println(p4.sameColumnCarry());
//		p5 = new IntAddProblem(216, 13);
//		System.out.println(p5.sameColumnCarry());
//		
//		System.out.println();
//		p1 = new IntAddProblem(41, 9);
//		System.out.println(p1.sameColumnCarryLastColCorrect());
//		p2 = new IntAddProblem(328, 917);
//		System.out.println(p2.sameColumnCarryLastColCorrect());
//		p3 = new IntAddProblem(989, 52);
//		System.out.println(p3.sameColumnCarryLastColCorrect());
//		p4 = new IntAddProblem(66, 887);
//		System.out.println(p4.sameColumnCarryLastColCorrect());
//		p5 = new IntAddProblem(216, 13);
//		System.out.println(p5.sameColumnCarryLastColCorrect());
//		
//		System.out.println();
//		p1 = new IntAddProblem(41, 9);
//		System.out.println(p1.addColumnWrong(1));
//		p2 = new IntAddProblem(328, 917);
//		System.out.println(p2.addColumnWrong(1));
//		p3 = new IntAddProblem(989, 52);
//		System.out.println(p3.addColumnWrong(1));
//		p4 = new IntAddProblem(66, 887);
//		System.out.println(p4.addColumnWrong(1));
//		p5 = new IntAddProblem(216, 13);
//		System.out.println(p5.addColumnWrong(1));
		
		System.out.println();
		IntAddProblem p1 = new IntAddProblem(41, 9);
		System.out.println(p1.getSolution());
		IntAddProblem p2 = new IntAddProblem(328, 917);
		System.out.println(p2.getSolution());
		IntAddProblem p3 = new IntAddProblem(989, 52);
		System.out.println(p3.getSolution());
		IntAddProblem p4 = new IntAddProblem(66, 887);
		System.out.println(p4.getSolution());
		IntAddProblem p5 = new IntAddProblem(216, 13);
		System.out.println(p5.getSolution());
		
		System.out.println();
		p1 = new IntAddProblem(41, 9);
		System.out.println(p1.ignoreUnitsDigit());
		p2 = new IntAddProblem(328, 917);
		System.out.println(p2.ignoreUnitsDigit());
		p3 = new IntAddProblem(989, 52);
		System.out.println(p3.ignoreUnitsDigit());
		p4 = new IntAddProblem(66, 887);
		System.out.println(p4.ignoreUnitsDigit());
		p5 = new IntAddProblem(216, 13);
		System.out.println(p5.ignoreUnitsDigit());
	}
}
