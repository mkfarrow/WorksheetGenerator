import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class ProblemGenerator {
  //private String probType;

  public ProblemGenerator(/*Problem type*/) {
    //probType = type.getClass().getName();
  }

  public Problem next() {
    /*
    switch (probType) {
      case "AddTwoDig":
        return nextAddTwoDig();
      case "AddFourDig":
        return null;
      case "SubOneFromTwo":
        return null;
      case "SubTwoFromTwo":
        return null;
      default:
        return null;
    }
    */
    return null;
  }

  public Problem[] nextN(int n) {
    Problem[] problems = new Problem[n];
    for (int i = 0; i < n; i++)
      problems[i] = next();
    return problems;
  }

  
  public static Problem makeAdditionProb(int numDigits, int carries, int maxSum, int minSum) {
    if (carries > numDigits || maxSum < minSum || numDigits <= 0)
      throw new IllegalArgumentException();
    int[] first = new int[numDigits]; // this is the first operand
    int[] second = new int[numDigits]; // second operand

    // pick which indices there will be carries at
    Set<Integer> carryIndices = new HashSet<Integer>();
    while (carryIndices.size() < carries)
      carryIndices.add((int) (Math.random() * first.length));

    // fill in digits for first operand
    for (int i = 0; i < first.length; i++) {
      first[i] = DigitGenerator.randomNonZeroDigit();
      if (carryIndices.contains(i + 1) && first[i] == 9 && !carryIndices.contains(i))
        first[i]--; // a carry in the digit to the right would cause an unwanted carry here
    }

    // fill in digits for second operand
    int carriedIn = 0;
    for (int i = second.length - 1; i >= 0; i--) {
      int addend = Math.min(9, first[i] + carriedIn);
      if (carryIndices.contains(i))
        second[i] = DigitGenerator.withCarry(addend);
      else
        second[i] = DigitGenerator.noCarry(addend);
      carriedIn = (first[i] + second[i] + carriedIn) / 10;
    }

    int op1 = intFromArray(first);
    int op2 = intFromArray(second);
    return new Problem(op1, op2, '+');
  }
  
  private static int intFromArray(int[] data) {
    String result = "";
    for (int i = 0; i < data.length; i++)
      result += "" + data[i];
    return Integer.parseInt(result);

  }
  
  /*
  public static Problem nextAddTwoDig() {
    int op1OnesDig, op1TensDig, op2OnesDig, op2TensDig;

    op1OnesDig = 1 + (int) (Math.random() * 9);
    op1TensDig = 1 + (int) (Math.random() * 9);
    int op1 = op1TensDig * 10 + op1OnesDig;

    op2OnesDig = DigitGenerator.withCarry(op1OnesDig);
    op2TensDig = DigitGenerator.withCarry(Math.min(9, op1TensDig));
    int op2 = op2TensDig * 10 + op2OnesDig;
    
    return new Problem(op1, op2, '+');
  }
  */

}
