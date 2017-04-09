package sharedCounter;

public class Barrier1 implements Runnable {
  public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();
    int n = 64;
    Barrier1 job = new Barrier1(n);
    Thread[] worker = new Thread[n];
    for (int i = 0; i < n; i++) {
      worker[i] = new Thread(job);
      worker[i].start();
    }

    for (int j = 0; j < n; j++) {
      worker[j].join();
    }
    System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));
  }


  private int N;
  private int counter;
  private TTASLock lock;

  public Barrier1(int n) {
    N = n;
    counter = 0;
    lock = new TTASLock();
  }

  @Override
  public void run() {
    try {
      foo();
      barrier();
      bar();
    } catch(InterruptedException e) {
      System.out.println("InterruptedException: " + e.getMessage());
    }
  }

  public void foo() throws InterruptedException {
    System.out.println("foo");
    lock.lock();
    counter++;
//    System.out.println("counter: " + counter);
    lock.unlock();
    Thread.sleep(20);
  }

  public void bar() throws InterruptedException {
    System.out.println("bar");
    Thread.sleep(20);
  }

  public void barrier() throws InterruptedException {
    while (counter != N) {
      Thread.sleep(1);
    }
  }
}
