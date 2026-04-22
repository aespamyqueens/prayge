import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;
import java.time.LocalDate;

public class Main extends Application {

    // 🔁 CHANGE THIS CLASS PER QUESTION
    class Item {
        int id;
        String name;
        String extra;      // author / dept / etc.
        double value;      // salary / balance etc.
        String date;       // for DatePicker usage

        Item(int id, String name, String extra, double value, String date) {
            this.id = id;
            this.name = name;
            this.extra = extra;
            this.value = value;
            this.date = date;
        }

        public String toString() {
            return id + " " + name + " " + extra + " " + value + " " + date;
        }
    }

    HashMap<Integer, Item> map = new HashMap<>();

    // 🔒 VALIDATION
    int getInt(TextField tf) throws Exception {
        if (tf.getText().isEmpty()) throw new Exception("Empty Field");
        return Integer.parseInt(tf.getText());
    }

    double getDouble(TextField tf) throws Exception {
        if (tf.getText().isEmpty()) throw new Exception("Empty Field");
        return Double.parseDouble(tf.getText());
    }

    String getText(TextField tf) throws Exception {
        if (tf.getText().isEmpty()) throw new Exception("Empty Field");
        return tf.getText();
    }

    public void start(Stage stage) {

        // 🔹 INPUTS
        TextField id = new TextField(); id.setPromptText("ID");
        TextField name = new TextField(); name.setPromptText("Name");
        TextField extra = new TextField(); extra.setPromptText("Extra");
        TextField val = new TextField(); val.setPromptText("Value");

        DatePicker dp = new DatePicker(); // ✅ DatePicker

        TextArea out = new TextArea();

        // 🔹 BUTTONS
        Button add = new Button("Add");
        Button view = new Button("View");
        Button search = new Button("Search");
        Button delete = new Button("Delete");
        Button update = new Button("Update");
        Button sort = new Button("Sort by Name"); // ✅ SORT

        // ➕ ADD
        add.setOnAction(e -> {
            try {
                int i = getInt(id);
                String n = getText(name);

                if (map.containsKey(i)) throw new Exception("Duplicate ID");

                double v = 0;
                try { v = getDouble(val); } catch(Exception ex) {}

                String d = "";
                if (dp.getValue() != null)
                    d = dp.getValue().toString();

                map.put(i, new Item(i, n, extra.getText(), v, d));
                out.setText("Added");

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        // 👁 VIEW
        view.setOnAction(e -> {
            out.setText(map.values().toString());
        });

        // 🔍 SEARCH (by ID)
        search.setOnAction(e -> {
            try {
                int i = getInt(id);
                Item it = map.get(i);

                if (it == null) throw new Exception("Not Found");

                out.setText(it.toString());

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

        // 🔁 UPDATE VALUE
        update.setOnAction(e -> {
            try {
                int i = getInt(id);
                double v = getDouble(val);

                Item it = map.get(i);
                if (it == null) throw new Exception("Not Found");

                it.value = v;
                out.setText("Updated");

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        // 🔃 SORT (by name)
        sort.setOnAction(e -> {
            List<Item> list = new ArrayList<>(map.values());

            list.sort((a, b) -> a.name.compareToIgnoreCase(b.name));

            out.setText(list.toString());
        });

        VBox vbox = new VBox(10,
                id, name, extra, val, dp,
                add, view, search, delete, update, sort,
                out
        );

        stage.setTitle("Universal System");
        stage.setScene(new Scene(vbox, 320, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
