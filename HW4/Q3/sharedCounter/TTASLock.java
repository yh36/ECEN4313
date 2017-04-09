package sharedCounter;

import java.util.concurrent.locks.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TTASLock implements Lock {
  private AtomicBoolean state = new AtomicBoolean(false);

  @Override
  public void lock() {
    while (true) {
      while (state.get()) {;}
      if (!state.getAndSet(true)) {
        return;
      }
    }
  }

  @Override
  public void unlock() {
    state.set(false);
  }

  @Override
  public void lockInterruptibly() {
    // Ignore
  }

  @Override
  public Condition newCondition() {
    return null;
  }

  @Override
  public boolean tryLock() {
    return false;
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) {
    return tryLock();
  }
}
