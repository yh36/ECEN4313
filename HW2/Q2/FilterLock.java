public class FilterLock implements Lock {
  private int[] victim;   // Mark victim of each level
  private int[] level;    // Record the level of current thread

  public FilterLock(int n) {
    victim = new int[n];
    level = new int[n];   // Use 1..n-1
    for (int i = 0; i < n; i++) {
      level[i] = 0;
    }
  }

  public void lock() {
    int me = Integer.parseInt(Thread.currentThread().getName());

    for (int i = 1; i < victim.length; i++) {
      // Doorway section
      level[me] = i;
      victim[i] = me;   // victim will trap this thread if there are other
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
  }

  public void unlock() {
    int me = Integer.parseInt(Thread.currentThread().getName());
    level[me] = 0;
  }
}
