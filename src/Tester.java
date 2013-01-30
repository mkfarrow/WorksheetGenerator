import java.util.Arrays;

public class Tester {
  public static final int NUM_DIGITS = 5;
  public static final int NUM_CARRIES = 3;

  public static void main(String[] args) {
    ProblemGenerator gen = new ProblemGenerator();
    int[] freq = new int[200];
    for (int i = 0; i < 10000; i++) {
      Problem p = ProblemGenerator.makeAdditionProb(NUM_DIGITS, NUM_CARRIES, 100000, 0);
      //System.out.println(p);
      //freq[p.solve()]++;
      int carries = numCarries(p.getOp1(), p.getOp2());
      if (carries != NUM_CARRIES) {
        System.out.println(carries);
        System.out.println("BAD!: " + p);
      }
    }
    //System.out.println(Arrays.toString(freq));
  }

  public static int numCarries(int a, int b) {
    int[] first = arrayFromInt(a);
    int[] second = arrayFromInt(b);
    int carries = 0;
    int carriedIn = 0;
    for (int i = Math.max(first.length, second.length) - 1; i >= 0; i--) {
      if (first[i] + second[i] + carriedIn >= 10)
        carries++;
      carriedIn = (first[i] + second[i] + carriedIn) / 10;
    }
    return carries;
  }

  public static int[] arrayFromInt(int num) {
    String data = "" + num;
    while (data.length() < NUM_DIGITS)
      data = "0" + data;
    int[] result = new int[data.length()];
    for (int i = 0; i < result.length; i++)
      result[i] = data.charAt(i) - '0';
    return result;
  }
}
