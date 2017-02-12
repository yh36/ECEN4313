/*
 * M-exclusion problem
 * To use n-thread Filter Lock algorithm to implemnt M-exlusion, a variable,
 * cnt, is added to track how many threads in the critical section. If the
 * number of threads is below the allowed number, the thread just entering into
 * the critical section should release the flag in order to allow more threads
 * to come in.As the variable cnt is increased inside the critical section, the
 * mutual exclusion of updating cnt is guaranteed. So it satisfies M-Exlusion.
 * As the underlying algorithm is based on n-thread Filter Lock algorithm, it
 * also meets the requirement of M-Starvation-Freedom.
 */

public class MExclusion implements Lock {
  private int[] victim;   // Mark victim of each level
  private int[] level;    // Record the level of current thread
  private int maxThrd;    // Maximum threads allowed in critical section
  private int cnt;        // Current number of threads inside critical section

  /*
   * n: Number of maximum allowed concurrent threads
   * m: m exclusion
   */
  public MExclusion(int n, int m) {
    if (n < m) {
      System.out.println("Invalid arguments!");
      System.exit(0);
    }
    maxThrd = m;
    cnt = 0;
    victim = new int[n];
    level = new int[n];   // Use 1..n-1
    for (int i = 0; i < n; i++) {
      level[i] = 0;
    }
  }

  public void lock() {
    int me = Integer.parseInt(Thread.currentThread().getName());

    for (int i = 1; i < victim.length; i++) {
      /*
       * Doorway section
       */
      level[me] = i;
      victim[i] = me;   // victim will trap current thread if there are other
                        // running threads at the same or higher level

      /*
       * Wait section
       * Check if other threads enter into the same or higher level but ahead
       * of me. If so, I should stay at this level and let them go first.
       */
      for (int k = 0; k < level.length; k++) {
        if (k == me) {
          continue;
        }
        while (level[k] >= i && victim[i] == me) {
          try {
            Thread.sleep(1);
          } catch (InterruptedException ex) {
            System.out.println(ex);
          }
        }
      }
    }

    /*
     * Citical Section
     * This part is added for M-Exclusion algorithm.
     */
    cnt++;      // One more thread goes into CS section, increase cnt
    System.out.println("Thread[" + me + "-" + cnt + "] inside function.");
    if (cnt < maxThrd) {  // If more space available for CS section, we
                          // should unlock and let more threads come in
      level[me] = 0;
    }
    else {
      System.out.println("");
    }
  }

  public void unlock() {
    int me = Integer.parseInt(Thread.currentThread().getName());
    cnt--;
    level[me] = 0;
  }
}
