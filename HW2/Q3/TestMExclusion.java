import java.util.Random;

public class TestMExclusion implements Runnable {
  public static void main(String[] args) {
    System.out.println("NOTE:\nThread[3-2] means there are two threads inside" +
      " the critical section after thread3 enterred\n");
    int n = 6;    // Number of concurrent threads
    int m = 3;    // Number of m exclusion
    TestMExclusion job = new TestMExclusion(n, m);
    Thread[] worker = new Thread[n];
    for (int i = 0; i < n; i++) {
      worker[i] = new Thread(job);
      worker[i].setName(Integer.toString(i));
      worker[i].start();
    }
    for (int i = 0; i < n; i++) {
      try {
        worker[i].join();
      } catch(InterruptedException e) {
        System.out.println(e);
      }
    }
  }

  private Random rand;
  private Lock lock;

  public TestMExclusion(int n, int m) {
    lock = new MExclusion(n, m);
    rand = new Random();
  }

  public void run() {
    for (int i = 0; i < 6; i++) {
      lock.lock();
      try { // Make this current stay in the critical section for a while
        Thread.sleep((rand.nextInt(3) + 1) * 1000);
      } catch(InterruptedException e) {
        System.out.println(e);
      }
      lock.unlock();
    }
  }
}
