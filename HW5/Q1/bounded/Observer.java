package bounded;

public class Observer {

  public static void main(String[] args) throws InterruptedException {
    // Observer(home_team, enemy_team, area)
    new Observer(32, 64, 20).start();
  }

  private int home, enemy, area;
  private Empire empire;

  public Observer(int _home, int _enemy, int _area) {
    home = _home;
    enemy = _enemy;
    area = _area;
    empire = new Empire(area);
  }

  public void start() throws InterruptedException {
    int i;
    Thread[] homes = new Thread[home];
    for (i = 0; i < home; i++) {
      homes[i] = new Thread (new Runnable() {
        @Override
        public void run() {
          while (true) {
            try {
              empire.build();
            } catch (InterruptedException e) {
              // Ignore
            }
          }
        }
      });
    }

    Thread[] enemys = new Thread[enemy];
    for (i = 0; i < enemy; i++) {
      enemys[i] = new Thread (new Runnable() {
        @Override
        public void run() {
          while (true) {
            try {
              empire.destroy();
            } catch (InterruptedException e) {
              // Ignore
            }
          }
        }
      });
    }

    for (Thread thrd : homes) {
      thrd.start();
    }
    for (Thread thrd : enemys) {
      thrd.start();
    }

    for (i = 1; i < 5; i++) {
      Thread.sleep(1000);
      System.out.println("Result@" + i + " second(s) is " + empire.getBuilt());
    }
    Thread.sleep(1000);
    int n = empire.getBuilt();
    if (n == 0) {
      System.out.println("ENEMY TEAM WINS");
    } else if (n == area) {
      System.out.println("HOME TEAM WINS");
    } else {
      System.out.println("TIE");
    }

    System.exit(0);
  }
}
