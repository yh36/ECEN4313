/*
 * This implementation only has ordinary withdraw().
 * Test:
 * Thread 1: W(100), D(20)
 * Thread 2: D(60), W(20)
 * Thread 3: D(50), W(10)
 * Thread 4: D(20)
 */

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Savings {
  public static void main(String[] args) throws InterruptedException {
    Savings account = new Savings();
    Thread thrd1 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          account.withdraw(100);
        } catch (InterruptedException e) {}
        account.getbalance();
        account.deposit(20);
      }
    });
    thrd1.setName("1");
    thrd1.start();

    Thread thrd2 = new Thread(new Runnable() {
      @Override
      public void run() {
        account.deposit(60);
        try {
          account.withdraw(20);
        } catch (InterruptedException e) {}
      }
    });
    thrd2.setName("2");
    thrd2.start();

    Thread thrd3 = new Thread(new Runnable() {
      @Override
      public void run() {
        account.deposit(50);
        try {
          account.withdraw(10);
        } catch (InterruptedException e) {}
      }
    });
    thrd3.setName("3");
    thrd3.start();

    Thread thrd4 = new Thread(new Runnable() {
      @Override
      public void run() {
        account.deposit(20);
      }
    });
    thrd4.setName("4");
    thrd4.start();

    thrd1.join();
    thrd2.join();
    thrd3.join();
    thrd4.join();
  }

  private double balance;
  private ReentrantLock lock;
  private Condition withdrawBlock;

  public Savings() {
    lock = new ReentrantLock();
    withdrawBlock = lock.newCondition();
    balance = 0.0;
  }

  public void deposit(double k) {
    lock.lock();
    try {
      balance += k;
      System.out.println("Thread [" + Thread.currentThread().getName() + "] " +
          "deposits $" + k);
      withdrawBlock.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public void withdraw(double k) throws InterruptedException {
    lock.lock();
    try {
      while (balance < k) {
        withdrawBlock.await();
      }
      balance -= k;
      System.out.println("Thread [" + Thread.currentThread().getName() + "] " +
          "withdraws $" + k);
    } finally {
      lock.unlock();
    }
  }

  public double getbalance() {
    double m;
    lock.lock();
    try {
      m = balance;
      System.out.println("Thread [" + Thread.currentThread().getName() + "] " +
          "checks the balance with $" + m);
    } finally {
      lock.unlock();
    }
    return m;
  }
}
