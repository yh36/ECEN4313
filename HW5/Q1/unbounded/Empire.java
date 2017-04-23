package unbounded;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Empire {
  private AtomicReference<Building> head, tail;
  private AtomicInteger built, dstry, winner;
  private int XP;

  public Empire(int xp) {
    Building sentinel = new Building();
    head = new AtomicReference<Building>(sentinel);
    tail = new AtomicReference<Building>(sentinel);
    built = new AtomicInteger(0);
    dstry = new AtomicInteger(0);
    winner = new AtomicInteger(0);
    XP = xp;
  }

  public void build() {
    Building buld = new Building();
    while (true) {
      Building last = tail.get();
      Building next = last.next.get();
      if (last == tail.get()) {
        if (next == null) {
          if (last.next.compareAndSet(next, buld)) {
            tail.compareAndSet(last, buld);
            if (built.getAndAdd(5) + 5 > XP) {
              winner.compareAndSet(0, 1);
            }
            return;
          }
        } else {
          tail.compareAndSet(last, next);
        }
      }
    }
  }

  public void destroy() {
    while (true) {
      Building first = head.get();
      Building last = tail.get();
      Building next = first.next.get();
      if (first == head.get()) {
        if (first == last) {
          if (next == null) {
            return;
          }
          tail.compareAndSet(last, next);
        } else {
          if (head.compareAndSet(first, next)) {
            if (dstry.getAndAdd(5) + 5 > XP) {
              winner.compareAndSet(0, -1);
            }
            return;
          }
        }
      }
    }
  }

  public int hasWinner() {
    return winner.get();
  }

  public int getHmPts() {
    return built.get();
  }

  public int getEmPts() {
    return dstry.get();
  }

  private class Building {
    public AtomicReference<Building> next;

    public Building() {
      next = new AtomicReference<Building>(null);
    }
  }
}
