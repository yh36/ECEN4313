public class Bakery implements Lock {
  private boolean[] flag;
  private int[] label;

  public Bakery(int n) {
    if (n <= 0) {
      System.out.println("Number of threads must be larger than 0!");
      System.exit(0);
    }
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
         // If the current thread has a smaller lexicographical order, proceed
         if (label[i] < label[k] || (label[i] == label[k] && i < k)) {
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
    int maxVal = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (maxVal < arr[i]) {
        maxVal = arr[i];
      }
    }
    return maxVal;
  }
}
