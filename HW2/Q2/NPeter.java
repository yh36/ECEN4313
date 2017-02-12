import java.util.Stack;

public class NPeter implements Lock {
  private int n;            // Number of concurrent threads
  private boolean[] flag;   // Mean we want to access
  private int[] victim;
  private Stack<Integer> path;

  public NPeter(int n) {
    this.n = n;
    flag = new boolean[n * 2]; // Only use [1, 2n-1]
    victim = new int[n];  // Use [1, n-1]
    path = new Stack<>();

    for (int i = 0; i < flag.length; i++) {
      flag[i] = false;
    }

    for (int i = 0; i < victim.length; i++) {
      victim[i] = -1;
    }
  }

  public void lock() {
    // Get thread id
    int id = Integer.parseInt(Thread.currentThread().getName());

    /*
     * Doorway section
     */
     int i = id + n;
     // Raise flag to show interesting
     flag[i] = true;
     // Mark itself as a victim in one upper layer
     victim[i / 2] = id;

    /*
     * Wait section
     */
     while (i > 1) {
       int sib = i % 2 == 1 ? i - 1 : i + 1;
       while (flag[sib] && victim[i / 2] == id) {
         ;
       }
       i /= 2;
       flag[i] = true;
       if (i > 1) {
         victim[i / 2] = id;
       }
     }
  }

  public void unlock() {
    int id = Integer.parseInt(Thread.currentThread().getName());
    int i = id + n;

    while (i > 0) {
      path.push(i);
      i /= 2;
    }

    while (!path.empty()) {
      flag[path.pop()] = false;
    }
  }
}
