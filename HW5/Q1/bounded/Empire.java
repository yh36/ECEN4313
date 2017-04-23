package bounded;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Empire {
  private int area;
  private AtomicInteger built;
  private Building head, tail;
  private ReentrantLock enqLock, deqLock;
  private Condition notFull, notEmpty;

  public Empire(int _area) {
    area = _area;
    built = new AtomicInteger(0);
    head = tail = new Building();
    enqLock = new ReentrantLock();
    notFull = enqLock.newCondition();
    deqLock = new ReentrantLock();
    notEmpty = deqLock.newCondition();
  }

  public void build() throws InterruptedException {
    boolean wakeDequeuers = false;
    enqLock.lock();
    try {
      while (built.get() == area) {
        // Lost-wakeup: avoiding dequeuer signalling between two steps here
        notFull.await();
      }
      tail.next = new Building();
      tail = tail.next;

      if (built.getAndIncrement() == 0) {
        wakeDequeuers = true;
      }
    } finally {
      enqLock.unlock();
    }

    if (wakeDequeuers) {
      deqLock.lock();
      try {
        notEmpty.signalAll();
      } finally {
        deqLock.unlock();
      }
    }
  }

  public void destroy() throws InterruptedException {
    boolean wakeEnqueuers = false;
    deqLock.lock();
    try {
      while (built.get() == 0) {
        notEmpty.await();
      }
      head = head.next;
      if (built.getAndDecrement() == area) {
        wakeEnqueuers = true;
      }
    } finally {
      deqLock.unlock();
    }

    if (wakeEnqueuers) {
      enqLock.lock();
      try {
        notFull.signalAll();
      } finally {
        enqLock.unlock();
      }
    }
  }

  public int getBuilt() {
    return built.get();
  }

  private class Building {
    private Building next;

    public Building() {
      next = null;
    }
  }
}
