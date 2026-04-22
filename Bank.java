import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

public class Main extends Application {

    // 🔹 Account class (like Item/Book)
    class Account {
        int id;
        String name;
        double balance;

        Account(int id, String name, double balance) {
            this.id = id;
            this.name = name;
            this.balance = balance;
        }

        public String toString() {
            return id + " " + name + " " + balance;
        }
    }

    // 🔹 Collection + Generics
    HashMap<Integer, Account> map = new HashMap<>();

    // 🔒 validation
    int getInt(TextField tf) throws Exception {
        if (tf.getText().isEmpty()) throw new Exception("Empty Field");
        return Integer.parseInt(tf.getText());
    }

    double getDouble(TextField tf) throws Exception {
        if (tf.getText().isEmpty()) throw new Exception("Empty Field");
        return Double.parseDouble(tf.getText());
    }

    public void start(Stage stage) {

        TextField id = new TextField(); id.setPromptText("Account ID");
        TextField name = new TextField(); name.setPromptText("Name");
        TextField amt = new TextField(); amt.setPromptText("Amount");

        TextArea out = new TextArea();

        Button create = new Button("Create");
        Button deposit = new Button("Deposit");
        Button withdraw = new Button("Withdraw");
        Button view = new Button("View");
        Button search = new Button("Search");
        Button delete = new Button("Delete");

        // ➕ CREATE ACCOUNT
        create.setOnAction(e -> {
            try {
                int i = getInt(id);
                double b = getDouble(amt);

                if (map.containsKey(i)) throw new Exception("Duplicate ID");

                map.put(i, new Account(i, name.getText(), b));
                out.setText("Account Created");

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        // 💰 DEPOSIT
        deposit.setOnAction(e -> {
            try {
                int i = getInt(id);
                double a = getDouble(amt);

                Account acc = map.get(i);
                if (acc == null) throw new Exception("Not Found");

                acc.balance += a;
                out.setText("Deposited");

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        // 💸 WITHDRAW
        withdraw.setOnAction(e -> {
            try {
                int i = getInt(id);
                double a = getDouble(amt);

                Account acc = map.get(i);
                if (acc == null) throw new Exception("Not Found");

                if (acc.balance < a) throw new Exception("Insufficient Balance");

                acc.balance -= a;
                out.setText("Withdrawn");

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        // 👁 VIEW
        view.setOnAction(e -> {
            out.setText(map.values().toString());
        });

        // 🔍 SEARCH
        search.setOnAction(e -> {
            try {
                int i = getInt(id);

                Account acc = map.get(i);
                if (acc == null) throw new Exception("Not Found");

                out.setText(acc.toString());

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        // ❌ DELETE
        delete.setOnAction(e -> {
            try {
                int i = getInt(id);

                if (map.remove(i) == null)
                    throw new Exception("Not Found");

                out.setText("Deleted");

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        VBox vbox = new VBox(10,
                id, name, amt,
                create, deposit, withdraw,
                view, search, delete,
                out
        );

        stage.setTitle("Banking System");
        stage.setScene(new Scene(vbox, 320, 450));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
