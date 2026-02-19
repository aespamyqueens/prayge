// Interface
interface ItemOperations {
    void checkout() throws InvalidTransactionException;
    void returnItem() throws InvalidTransactionException;
}

// Custom Exception
class InvalidTransactionException extends Exception {
    public InvalidTransactionException(String message) {
        super(message);
    }
}

// Base Class
class LibraryItem {
    protected int itemID;
    protected String title;
    protected String author;
    protected boolean isAvailable;

    public LibraryItem(int itemID, String title, String author) {
        this.itemID = itemID;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }
}

// Derived Class
class StudentMember extends LibraryItem implements ItemOperations {

    public StudentMember(int itemID, String title, String author) {
        super(itemID, title, author);
    }

    // Synchronized checkout
    @Override
    public synchronized void checkout() throws InvalidTransactionException {
        if (!isAvailable) {
            throw new InvalidTransactionException("Book is already borrowed!");
        }
        isAvailable = false;
        System.out.println("Book Checked Out: " + title);
    }

    // Synchronized return
    @Override
    public synchronized void returnItem() throws InvalidTransactionException {
        if (isAvailable) {
            throw new InvalidTransactionException("Book was not borrowed!");
        }
        isAvailable = true;
        System.out.println("Book Returned: " + title);
    }

    public void displayStatus() {
        System.out.println("Book: " + title + " | Available: " + isAvailable);
    }
}

// Main Class
public class LibrarySystem {

    public static void main(String[] args) {

        StudentMember book = new StudentMember(101, "Java Programming", "James Gosling");

        // Thread 1: Student borrowing
        Thread studentThread = new Thread(() -> {
            try {
                book.checkout();
            } catch (InvalidTransactionException e) {
                System.out.println("Borrow Error: " + e.getMessage());
            }
        });

        // Thread 2: Librarian returning
        Thread librarianThread = new Thread(() -> {
            try {
                Thread.sleep(2000); // Delay to simulate real scenario
                book.returnItem();
            } catch (InvalidTransactionException e) {
                System.out.println("Return Error: " + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start Threads
        studentThread.start();
        librarianThread.start();

        // Wait for threads to finish
        try {
            studentThread.join();
            librarianThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final Status
        System.out.println("\nFinal Library Status:");
        book.displayStatus();
    }
}
