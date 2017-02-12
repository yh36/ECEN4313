/*
 * This test class, Counter, uses Bakery lock to synchronize the update of
 * the variable "value" inside the function getAndIncrement(). Class Counter
 * implements Runnable interface. So all running threads will use the same
 * lock object to coordinate function calls among them.
 */


public class Counter implements Runnable {
  public static void main(String[] args) {
    int n = 10;    // Number of threads to be run
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
  }

  private long value;
  private Lock lock;    // Use lock implemented in Backery algorithm

  public Counter(int n) {
    if (n <= 0) {
      System.out.println("Invalid number of threads!");
      System.exit(0);
    }
    value = 0;
    lock = new Bakery(n);
  }

  public void run() {
    int repeat = 20;    // Repeat times for a thread calling getAndIncrement()
    for (int i = 0; i < repeat; i++) {
      System.out.println("Thread(" + Thread.currentThread().getName() + ")--" +
        "Run(" + i + "):\t" + getAndIncrement());
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
