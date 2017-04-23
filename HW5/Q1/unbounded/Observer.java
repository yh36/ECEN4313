package unbounded;

public class Observer {
  public static void main(String[] args) throws InterruptedException {
    // Observer(home_team, enemy_team, xp)
    new Observer(32, 32, 10000).start();
  }

  private Empire game;
  private int homes, enemys;

  public Observer(int _homes, int _enemys, int xp) {
    homes = _homes;
    enemys = _enemys;
    game = new Empire(xp);
  }

  public void start() throws InterruptedException {
    int i;
    Thread[] home = new Thread[homes];
    for (i = 0; i < homes; i++) {
      home[i] = new Thread(new Runnable() {
        @Override
        public void run() {
          while (true) {
            game.build();
          }
        }
      });
    }

    Thread[] enemy = new Thread[enemys];
    for (i = 0; i < enemys; i++) {
      enemy[i] = new Thread(new Runnable() {
        @Override
        public void run() {
          while (true) {
            game.destroy();
          }
        }
      });
    }

    for (Thread thrd : home) {
      thrd.start();
    }
    for (Thread thrd : enemy) {
      thrd.start();
    }

    int res;
    for (i = 1; i < 6; i++) {
      Thread.sleep(1000);
      res = game.hasWinner();
      if (res != 0) {
        System.out.println(res > 0 ? "HOME TEAM WINS" : "ENEMY TEAM WINS");
        System.exit(0);
      } else {
        if (i == 5) {
          System.out.println("TIE");
          System.exit(0);
        } else {
          System.out.println("Home: " + game.getHmPts() + " pts, Enemy: " +
              game.getEmPts() + " pts");
        }
      }
    }
  }
}
