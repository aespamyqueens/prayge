*Q) Implement a Hospital Appointment System using Java (Generics + Collection Framework) fully integrated with JavaFX.

JAVAFX - 18M 💀

The application should include a JavaFX-based GUI and support the following functionalities:

1) Register Appointment
   - Allow users to add a new appointment with details such as Patient ID, Patient Name, Doctor Name, Date, and Time.

2) View All Appointments
   - Display all stored appointments in a structured format (e.g., TableView).

3) Search Appointment by Patient ID
   - Retrieve and display appointment details based on a given Patient ID.

4) Search Appointment by Doctor Name
   - Retrieve and display all appointments associated with a specific doctor.

5) Cancel Appointment
   - Remove an appointment using Patient ID or any unique identifier.

6) Exception Handling
   - Implement proper exception handling for invalid inputs, missing data, and runtime errors.

Requirements:
- Use Java Generics where applicable.
- Use Java Collection Framework (e.g., ArrayList, HashMap).
- Integrate all functionalities within a JavaFX GUI.
- Ensure user-friendly interface and proper validation.
- */

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;
import java.time.LocalDate;

public class Main extends Application {

    class Appointment {
        int patient_id;
        String patient_name;
        String doctor_name;
        String app_date; // keep as String (simple)
        String app_time;

        Appointment(int patient_id, String patient_name,
                    String doctor_name, String app_date, String app_time) {
            this.patient_id = patient_id;
            this.patient_name = patient_name;
            this.doctor_name = doctor_name;
            this.app_date = app_date;
            this.app_time = app_time;
        }

        public String toString() {
            return patient_id + " " + patient_name + " " +
                   doctor_name + " " + app_date + " " + app_time;
        }
    }

    HashMap<Integer, Appointment> map = new HashMap<>();

    int getInt(TextField tf) throws Exception {
        if (tf.getText().isEmpty())
            throw new Exception("Empty Field");
        return Integer.parseInt(tf.getText());
    }

    public void start(Stage stage) {

        TextField patient_id = new TextField();
        patient_id.setPromptText("Patient Id");

        TextField patient_name = new TextField();
        patient_name.setPromptText("Patient Name");

        TextField doctor_name = new TextField();
        doctor_name.setPromptText("Doctor Name");

        DatePicker app_date = new DatePicker();
        app_date.setPromptText("Select Date");

        TextField app_time = new TextField();
        app_time.setPromptText("Appointment Time");

        TextArea out = new TextArea();

        Button Register = new Button("Register");
        Button view = new Button("View");
        Button Search_patient = new Button("Search by Patient ID");
        Button Search_doctor = new Button("Search by Doctor Name");
        Button Cancel = new Button("Cancel");

        Register.setOnAction(e -> {
            try {
                int p_id = getInt(patient_id);

                if (map.containsKey(p_id))
                    throw new Exception("Duplicate");

                LocalDate date = app_date.getValue();
                if (date == null)
                    throw new Exception("Select Date");

                map.put(p_id, new Appointment(
                        p_id,
                        patient_name.getText(),
                        doctor_name.getText(),
                        date.toString(),
                        app_time.getText()
                ));

                out.setText("Appointment Registered");

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        view.setOnAction(e -> {
            out.setText(map.values().toString());
        });

        Search_patient.setOnAction(e -> {
            try {
                int p_id = getInt(patient_id);
                Appointment app = map.get(p_id);

                if (app == null)
                    throw new Exception("Not Found");

                out.setText(app.toString());

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        Search_doctor.setOnAction(e -> {
            try {
                String res = "";

                for (Appointment a : map.values()) {
                    if (a.doctor_name.equalsIgnoreCase(doctor_name.getText())) {
                        res += a + "\n";
                    }
                }

                if (res.isEmpty())
                    throw new Exception("Not Found");

                out.setText(res);

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        Cancel.setOnAction(e -> {
            try {
                int p_id = getInt(patient_id);

                if (map.remove(p_id) == null)
                    throw new Exception("Not Found");

                out.setText("Appointment Cancelled");

            } catch (Exception ex) {
                out.setText(ex.getMessage());
            }
        });

        VBox vbox = new VBox(10, patient_id, patient_name, doctor_name, app_date, app_time, Register, view, Search_patient, Search_doctor, Cancel, out);

        stage.setTitle("Hospital Appointment Management System");
        stage.setScene(new Scene(vbox, 320, 450));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
