import java.util.concurrent.atomic.AtomicInteger;
import java.util.EmptyStackException;

public class DancePair {
  public static void main(String[] args) throws InterruptedException {
    DancePair dance = new DancePair();
    Thread[] girls = new Thread[20];
    Thread[] boys = new Thread[20];
    int i;
    for (i = 0; i < 4; i++) {
      girls[i] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.drop(6);
        }
      });
      boys[i] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.pick(10);
        }
      });
      girls[i + 4] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.drop(7);
        }
      });
      boys[i + 4] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.pick(9);
        }
      });
      girls[i + 8] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.drop(8);
        }
      });
      boys[i + 8] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.pick(8);
        }
      });
      girls[i + 12] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.drop(9);
        }
      });
      boys[i + 12] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.pick(7);
        }
      });
      girls[i + 16] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.drop(10);
        }
      });
      boys[i + 16] = new Thread(new Runnable() {
        @Override
        public void run() {
          dance.pick(6);
        }
      });
    }

    for (Thread thrd : girls) {
      thrd.start();
    }
    for (Thread thrd : boys) {
      thrd.start();
    }
    for (Thread thrd : boys) {
      thrd.join();
    }
  }

  private LockFreeStack<Student> lot;
  private AtomicInteger order;

  public DancePair() {
    lot = new LockFreeStack<Student>();
    order = new AtomicInteger(0);
  }

  public void pick(int grade) {
    while (true) {
      try {
        Student stud = lot.pop();
        if (stud.grade == grade) {
          System.out.println("My grade is " + grade + ", my girl partner is No."
            + stud.number);
          return;
        } else {
          lot.push(stud);
        }
      } catch (EmptyStackException ex) {
        // IGNORE
      }
    }
  }

  public void drop(int grade) {
    Student stud = new Student(order.getAndIncrement(), grade);
    System.out.println("Girl has grade " +grade + ", No." + stud.number);
    lot.push(stud);
  }

  private class Student {
    int number;
    int grade;

    public Student(int num, int grd) {
      number = num;
      grade = grd;
    }
  }
}
