/*
 * Problem 1
 */

class Counter implements Runnable {

  /*
   * count: The counter variable
   */
  private static int count;

  public void run() {

    // Increment the counter variable for 100 times
    for (int i = 0; i < 100; i++) {
      increment();
    }
    System.out.println(Thread.currentThread().getName() + " is done!");
  }

  /*
   * Increment the global variable, count
   */
  private void increment() {
    count++;
  }

  /*
   * Return the global variable, count
   */
  public static int getCount() {
    return count;
  }
}
