public class Problem {
  private int operand1, operand2;
  private int solution;
  private char operation;

  public Problem(int op1, int op2, char operation) {
    this.operation = operation;
    operand1 = op1;
    operand2 = op2;
    solution = operand1+operand2;
  }

  public int getOp1() {
    return operand1;
  }

  public int getOp2() {
    return operand2;
  }

  public int solve() {
    return solution;
  }

  public char getOperator() {
    return operation;
  }

  public String toString() {
    return operand1 + " " + operation + " " + operand2 + " = " + solution + "\n";
  }
}
