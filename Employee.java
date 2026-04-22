import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

public class Main extends Application {

    // 🔹 Employee class
    class Employee {
        int id;
        String name;
        double salary;

        Employee(int id, String name, double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }

        public String toString() {
            return id + " " + name + " " + salary;
        }
    }

    // 🔹 Collection + Generics
    HashMap<Integer, Employee> map = new HashMap<>();

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

        TextField id = new TextField(); id.setPromptText("Employee ID");
        TextField name = new TextField(); name.setPromptText("Name");
        TextField sal = new TextField(); sal.setPromptText("Salary");

        TextArea out = new TextArea();

        Button add = new Button("Add Employee");
        Button update = new Button("Update Salary");
        Button view = new Button("View");
        Button search = new Button("Search");
        Button delete = new Button("Delete");

        // ➕ ADD EMPLOYEE
        add.setOnAction(e -> {
            try {
                int i = getInt(id);
                double s = getDouble(sal);

                if (map.containsKey(i)) throw new Exception("Duplicate ID");

                map.put(i, new Employee(i, name.getText(), s));
                out.setText("Employee Added");

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        // 🔁 UPDATE SALARY
        update.setOnAction(e -> {
            try {
                int i = getInt(id);
                double s = getDouble(sal);

                Employee emp = map.get(i);
                if (emp == null) throw new Exception("Not Found");

                emp.salary = s; // overwrite salary
                out.setText("Salary Updated");

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

                Employee emp = map.get(i);
                if (emp == null) throw new Exception("Not Found");

                out.setText(emp.toString());

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
                id, name, sal,
                add, update,
                view, search, delete,
                out
        );

        stage.setTitle("Employee Payroll System");
        stage.setScene(new Scene(vbox, 320, 450));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
