import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SensorDataRecorder {

    private static final String FILE_NAME = "sensordata.txt";
    private static final Object lock = new Object();
    private static boolean writingDone = false;

    public static void main(String[] args) {

        // Sensor Thread (writes data)
        Thread sensorThread = new Thread(() -> {
            synchronized (lock) {
                try (FileWriter writer = new FileWriter(FILE_NAME)) {

                    for (int i = 1; i <= 5; i++) {
                        int temperature = 20 + i;  // 21 to 25
                        String reading = "Reading " + i + ": Temperature = " + temperature + " C";

                        writer.write(reading + "\n");
                        System.out.println("Sensor Recorded: " + reading);

                        Thread.sleep(500);
                    }

                    writingDone = true;
                    lock.notifyAll();

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Monitor Thread (reads data)
        Thread monitorThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    while (!writingDone) {
                        lock.wait(); // Wait until writing is finished
                    }

                    System.out.println("\nMonitoring Sensor Readings:\n");

                    try (FileReader reader = new FileReader(FILE_NAME)) {
                        int ch;
                        StringBuilder line = new StringBuilder();

                        while ((ch = reader.read()) != -1) {
                            if (ch == '\n') {
                                System.out.println("Monitored: " + line);
                                line.setLength(0);
                            } else {
                                line.append((char) ch);
                            }
                        }
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Controller Thread
        Thread controllerThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    while (!writingDone) {
                        lock.wait();
                    }
                    System.out.println("\nController: All sensor readings have been successfully recorded.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        sensorThread.start();
        monitorThread.start();
        controllerThread.start();
    }
}
