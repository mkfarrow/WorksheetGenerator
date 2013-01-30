import java.util.Random;

public class DigitGenerator {
  private static final Random rand = new Random();

  /**
   * Given a single digit integer n, returns a number between 0 and 9 (inclusive) that, when added
   * to n would yield a sum of <10
   *
   * @param n an integer between 0 and 9 inclusive
   * @return a digit x where x + n < 10
   */
  public static int noCarry(int n) {
    checkArg(n);
    return rand.nextInt(10 - n);
  }

  /**
   * Given a single digit integer n, returns a number between 0 and 9 (inclusive) that, when added
   * to n would yield a sum of >=10
   *
   * @param n an integer between 0 and 9 inclusive
   * @return a digit x where n + x > 10 or if a carry is impossible, -1
   */
  public static int withCarry(int n) {
    checkArg(n);
    if (n == 0)
      return -1;
    return rand.nextInt(n) + (10 - n);
  }

  /**
   * Given a single digit integer n, returns a number between 0 and 9 (inclusive) that, when
   * subtracted from n would yield a non-negative difference
   *
   * @param n an integer between 0 and 9 inclusive
   * @return a digit x where n - x >= 0
   */
  public static int noBorrow(int n) {
    checkArg(n);
    return rand.nextInt(n + 1);
  }

  /**
   * Given a single digit integer n, returns a number between 0 and 9 (inclusive) that, when
   * subtracted from n would yield a negative difference
   *
   * @param n an integer between 0 and 9 inclusive
   * @return a digit x where n - x < 0 or if a borrow is impossible, -1
   */
  public static int withBorrow(int n) {
    checkArg(n);
    if (n == 9)
      return -1;
    return rand.nextInt(9 - n) + n + 1;
  }

  /**
   * Returns a random, positive, single digit
   *
   * @returns an int in the range [1, 9]
   */
  public static int randomNonZeroDigit() {
    return 1 + (int) (Math.random() * 9);
  }

  /**
   * Returns a random, nonnegative, single digit
   *
   * @returns an int in the range [0, 9]
   */
  public static int randomDigit() {
    return (int) (Math.random() * 10);
  }

  /**
   * Checks to see if the given n is a legal digit
   *
   * @throws IllegalArgumentException if n < 0 || n > 9
   */
  private static void checkArg(int n) {
    if (n < 0 || n > 9)
      throw new IllegalArgumentException();
  }
}
