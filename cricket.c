// Interface
interface BattingOperations {
    void playShot(int runs);
    void getOut();
}

// Enum for Shot Types
enum ShotType {
    SINGLE(1),
    DOUBLE(2),
    FOUR(4),
    SIX(6),
    OUT(0);

    private int runs;

    ShotType(int runs) {
        this.runs = runs;
    }

    public int getRuns() {
        return runs;
    }
}

// Base Class
class CricketPlayer {
    protected String name;
    protected int totalRuns;
    protected boolean isOut;

    public CricketPlayer(String name) {
        this.name = name;
        this.totalRuns = 0;
        this.isOut = false;
    }
}

// Derived Class
class Batsman extends CricketPlayer implements BattingOperations {

    public Batsman(String name) {
        super(name);
    }

    // Thread-safe score update
    @Override
    public synchronized void playShot(int runs) {
        if (!isOut) {
            totalRuns += runs;
            System.out.println("Runs Scored: " + runs);
            System.out.println("Current Total Score: " + totalRuns);
        }
    }

    // Thread-safe dismissal
    @Override
    public synchronized void getOut() {
        if (!isOut) {
            isOut = true;
            System.out.println("Batsman " + name + " is OUT!");
        }
    }

    public synchronized boolean isOut() {
        return isOut;
    }

    public synchronized int getTotalRuns() {
        return totalRuns;
    }
}

// Main Class
public class CricketMatch {

    public static void main(String[] args) {

        Batsman batsman = new Batsman("Virat");

        // Batting Thread
        Thread battingThread = new Thread(() -> {
            ShotType[] shots = ShotType.values();

            while (!batsman.isOut()) {
                int random = (int) (Math.random() * 4); // Avoid OUT here
                ShotType shot = shots[random];

                System.out.println("\nShot Played: " + shot);
                batsman.playShot(shot.getRuns());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Fielding Thread
        Thread fieldingThread = new Thread(() -> {
            try {
                Thread.sleep(5000); // After some time, attempt dismissal
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!batsman.isOut()) {
                System.out.println("\nFielding Team appeals for OUT!");
                batsman.getOut();
            }
        });

        // Umpire Thread
        Thread umpireThread = new Thread(() -> {
            while (!batsman.isOut()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("\nUmpire Decision: OUT!");
            System.out.println("Final Score of " + batsman.name + ": " + batsman.getTotalRuns());
        });

        // Start Threads
        battingThread.start();
        fieldingThread.start();
        umpireThread.start();
    }
}
