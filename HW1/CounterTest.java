/*
 * Test Program
 * The following test program will test two classes, Counter and CounterSync.
 * For each class, it makes an instance and let 128 threads increment their
 * shared count variable.
 *
 * In the class CounterSync, the keyword "synchronized" is added to its
 * increment() method. As there's only one lock for an object, all 128 threads
 * have to aqcuire the lock before entering into this method. So there's only
 * one thread increasing the count variable each time.
 */

public class CounterTest {

  public static void main(String[] args) {

    // Make an instance variable for class Counter and CounterSync respectively
    Runnable[] jobs = new Runnable[2];
    jobs[0] = new Counter();
    jobs[1] = new CounterSync();

    // Allocate an array to trace all 128 threads
    Thread[] workers = new Thread[128];

    for (int j = 0; j < jobs.length; j++) {
      System.out.println("Start test \"" + jobs[j].getClass() + "\"");

      // Create 128 threads to run on the same instance object
      for (int i = 0; i < 128; i++) {
        workers[i] = new Thread(jobs[j]);
        workers[i].setName(Integer.toString(i + 1));
        workers[i].start();
      }

      // Wait for all 128 threads to complete before testing the next class
      for (int k = 0; k < 128; k++) {
        try {
          workers[k].join();
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }

      }
      System.out.println("Test \"" + jobs[j].getClass() + "\" are finished!\n");
    }

    System.out.println("Final count number without synchronization: "
      + Counter.getCount());
    System.out.println("Final count number without synchronization: "
      + CounterSync.getCount());
  }
}
