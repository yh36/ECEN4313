public class Bakery implements Lock {
  private boolean[] flag;
  private int[] label;

  public Bakery(int n) {
    flag = new boolean[n];
    label = new int[n];
    for (int i = 0; i < n; i++) {
      flag[i] = false;
      label[i] = 0;
    }
  }

  public void lock() {
    /*
     * Doorway Section
     */
    // Get the thread ID
    int i = Integer.parseInt(Thread.currentThread().getName());
    // Raise the flag of this thread
    flag[i] = true;
    // Get the label number
    label[i] = maxArray(label) + 1;

    /*
     * Wait section
     */
     for (int k = 0; k < flag.length; k++) {
       if (k == i) {  // Skip checking itself
         continue;
       }

       while (flag[k]) {
         if (label[i] < label[k] || i < k) {
           // If there are no raising flags with smaller labels, we can proceed
           break;
         }
       }
     }
  }

  public void unlock() {
    int i = Integer.parseInt(Thread.currentThread().getName());
    flag[i] = false;
  }

  /*
   * Helper function to find the maximum value in an array
   */
  private int maxArray(int[] arr) {
    int max = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (max < arr[i]) {
        max = arr[i];
      }
    }
    return max;
  }
}
