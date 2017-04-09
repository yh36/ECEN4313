package booleanArray;

import java.util.concurrent.atomic.AtomicBoolean;

public class Barrier2 implements Runnable {
  public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();
    int n = 4;
    Barrier2 job = new Barrier2(n);
    Thread[] worker = new Thread[n];
    for (int i = 0; i < n; i++) {
      worker[i] = new Thread(job);
      worker[i].setName(Integer.toString(i));
      worker[i].start();
    }

    for (int j = 0; j < n; j++) {
      worker[j].join();
    }
    System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));
  }

  private AtomicBoolean[] arr;
  private int N;

  public Barrier2(int n) {
    N = n;
    arr = new AtomicBoolean[n];
    for (int i = 0; i < n; i++) {
      arr[i] = new AtomicBoolean(false);
    }
  }

  @Override
  public void run() {
    try {
      foo();
      barrier();
      bar();
    } catch (InterruptedException e) {
      System.out.println("InterruptedException: " + e.getMessage());
    }
  }

  public void foo() throws InterruptedException {
    System.out.println("foo");
    Thread.sleep(20);
  }

  public void bar() throws InterruptedException {
    System.out.println("bar");
    Thread.sleep(20);
  }

  public void barrier() throws InterruptedException {
    int id = Integer.parseInt(Thread.currentThread().getName());
    if (0 == id) {
      arr[0].set(true);
    } else {
      while (!arr[id - 1].get()) {
        Thread.sleep(1);
      }
      arr[id].set(true);
    }
    while (!arr[N - 1].get()) {
      Thread.sleep(1);
    }
  }
}
