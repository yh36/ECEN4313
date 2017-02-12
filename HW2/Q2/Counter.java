/*
 * This test class, Counter, uses Bakery lock to synchronize the update of
 * the variable "value" inside the function getAndIncrement(). Class Counter
 * implements Runnable interface. So all running threads will use the same
 * lock object to coordinate function calls among them.
 */


public class Counter implements Runnable {
  public static void main(String[] args) {
    int n = 64;    // *****Number of threads to be run******
    Counter job = new Counter(n); // Create a runnable Counter object
    Thread[] worker = new Thread[n];
    for (int i = 0; i < n; i++) {
      worker[i] = new Thread(job);
      // The thread's name will be used as ThreadID in Bakery algorithm
      worker[i].setName(Integer.toString(i));
      worker[i].start();
    }

    // main thread waits for all threads to complete before exiting
    for (int j = 0; j < n; j++) {
      try {
        worker[j].join();
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
    System.out.println(" ");
  }

  private long value;
  private Lock lock;

  public Counter(int n) {
    if (n <= 0) {
      System.out.println("Invalid number of threads!");
      System.exit(0);
    }
    value = 0;
    lock = new TreeLock(n);	// *****Change Lock here*****
  }

  public void run() {
     // *****Repeat times for a thread to call getAndIncrement()*****
    int repeat = 10;
    for (int i = 0; i < repeat; i++) {
      System.out.println("Thread(" + Thread.currentThread().getName() + "):\t"
        + getAndIncrement());
    }
  }

  private long getAndIncrement() {
    long temp;
    lock.lock();  // Enter critical section
    try {
      temp = value;
      value++;
    } finally {
      lock.unlock();  // Leave critical section
    }
    return temp;
  }
}
