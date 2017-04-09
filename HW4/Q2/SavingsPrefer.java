/*
 * This implementation has two different withdraw, ordinary withdraw and
 * preferred withdraw.
 * Test:
 * Thread 1: PW(100) D(20)
 * Thread 2: D(60), OW(20)
 * Thread 3: D(50), PW(10)
 * Thread 4: D(20)
 */

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


public class SavingsPrefer {
  public static void main(String[] args) throws InterruptedException {
    SavingsPrefer account = new SavingsPrefer();
    Thread thrd1 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          account.preWithdraw(100);
        } catch (InterruptedException e) {}
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
          account.ordWithdraw(20);
        } catch (InterruptedException e) {}
        account.getbalance();
      }
    });
    thrd2.setName("2");
    thrd2.start();

    Thread thrd3 = new Thread(new Runnable() {
      @Override
      public void run() {
        account.deposit(50);
        try {
          account.preWithdraw(10);
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
  private int prefer;
  private ReentrantLock lock;
  private Condition withdraw;

  public SavingsPrefer() {
    balance = 0.0;
    prefer = 0;
    lock = new ReentrantLock();
    withdraw = lock.newCondition();
  }

  public void deposit(double k) {
    lock.lock();
    try {
      balance += k;
      System.out.println("Thread [" + Thread.currentThread().getName() + "] " +
          "deposits $" + k);
      withdraw.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public void preWithdraw(double k) throws InterruptedException {
    lock.lock();
    try {
      prefer++;
      while (balance < k) {
        withdraw.await();
      }
      balance -= k;
      System.out.println("Thread [" + Thread.currentThread().getName() + "] " +
          "preferred withdraws $" + k);
      prefer--;
      withdraw.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public void ordWithdraw(double k) throws InterruptedException {
    lock.lock();
    try {
      while (prefer > 0 || balance < k) {
        withdraw.await();
      }
      balance -= k;
      System.out.println("Thread [" + Thread.currentThread().getName() + "] " +
          "ordinary withdraws $" + k);
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
