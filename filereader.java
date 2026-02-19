import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ChatLoggingSystem {

    private static final String FILE_NAME = "chatlog.txt";
    private static final Object lock = new Object();
    private static boolean writingDone = false;

    public static void main(String[] args) {

        String[] messages = {
                "Hello, how are you?",
                "I am fine, thanks!",
                "Are you attending class today?",
                "Yes, see you there."
        };

        // Sender Thread
        Thread sender = new Thread(() -> {
            synchronized (lock) {
                try (FileWriter writer = new FileWriter(FILE_NAME)) {
                    for (String msg : messages) {
                        writer.write(msg + "\n");
                        System.out.println("Sent: " + msg);
                        Thread.sleep(500);
                    }
                    writingDone = true;
                    lock.notifyAll();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Receiver Thread
        Thread receiver = new Thread(() -> {
            synchronized (lock) {
                try {
                    while (!writingDone) {
                        lock.wait(); // wait until writing finishes
                    }

                    System.out.println("\nReceiving Messages:\n");

                    try (FileReader reader = new FileReader(FILE_NAME)) {
                        int ch;
                        StringBuilder message = new StringBuilder();

                        while ((ch = reader.read()) != -1) {
                            if (ch == '\n') {
                                System.out.println("Received: " + message);
                                message.setLength(0);
                            } else {
                                message.append((char) ch);
                            }
                        }
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Moderator Thread
        Thread moderator = new Thread(() -> {
            synchronized (lock) {
                try {
                    while (!writingDone) {
                        lock.wait();
                    }
                    System.out.println("\nModerator: All messages have been successfully recorded.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        sender.start();
        receiver.start();
        moderator.start();
    }
}
