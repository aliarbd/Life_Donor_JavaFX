import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.scene.control.RadioButton;

public class SignupController {

    @FXML
    private ComboBox<String> area;

    @FXML
    private ToggleGroup available_radio;

    @FXML
    private ComboBox<String> district;

    @FXML
    private TextField email;

    @FXML
    private TextField name;

    @FXML
    private TextField password;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> thana;

    private Connection connection;
    @FXML
    private RadioButton available_radio_no;
    @FXML
    private RadioButton available_radio_yes;
    @FXML
    private ComboBox<String> blood;
    @FXML
    private RadioButton somker_radio_yes;
    @FXML
    private ToggleGroup somker_radio;
    @FXML
    private RadioButton somker_radio_no;

    // Initialize method to set up database connection and populate ComboBoxes
    public void initialize() {
        try {
            // Establish connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood_db", "root", "");

            // Populate district, area, and thana ComboBoxes (example values)
            blood.setItems(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"));
            district.setItems(FXCollections.observableArrayList("District 1", "District 2", "District 3"));
            area.setItems(FXCollections.observableArrayList("Area 1", "Area 2", "Area 3"));
            thana.setItems(FXCollections.observableArrayList("Thana 1", "Thana 2", "Thana 3"));

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not connect to the database.");
        }
    }

    
@FXML
public void signupButtonClicked(ActionEvent event) {
    try {
        // Validate input fields
        if (name.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty() || phone.getText().isEmpty()) {
            showAlert("Validation Error", "All fields must be filled.");
            return;
        }

        // Check if the user is a smoker
        if (somker_radio_yes.isSelected()) {
            showAlert("Registration Denied", "Sorry, smokers are not allowed to register as donors.");
            return; // Stop execution here
        }

        // Validate phone number (must be exactly 11 digits)
        String phoneNumber = phone.getText();
        if (!phoneNumber.matches("\\d{11}")) {  // Check if phone number contains exactly 11 digits
            showAlert("Validation Error", "Phone number must be exactly 11 digits.");
            return;
        }

        // Validate email format using a regular expression
        String emailAddress = email.getText();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!emailAddress.matches(emailRegex)) {
            showAlert("Validation Error", "Invalid email format. Please enter a valid email address.");
            return;
        }

        // Determine availability (true for Yes, false for No)
        boolean isAvailable = available_radio_yes.isSelected();

        // Prepare SQL query to insert user data
        String query = "INSERT INTO users (name, email, password, phone, district, area, thana, availability, blood_group) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name.getText());
        statement.setString(2, email.getText());
        statement.setString(3, password.getText());
        statement.setString(4, phone.getText());
        statement.setString(5, district.getValue());
        statement.setString(6, area.getValue());
        statement.setString(7, thana.getValue());
        statement.setBoolean(8, isAvailable); // Store boolean value for availability
        statement.setString(9, blood.getValue());

        // Execute the query
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            showAlert("Success", "Signup successful! You can now log in.");
            clearFields();

            // Redirect to login screen
            try {
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login Screen");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Signup failed. Please try again.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Error", "Email Already Exited. Please try with different mail.");
    }
}

    // Method to switch to login screen
    @FXML
    public void login_link_clicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to display alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to clear all fields
    private void clearFields() {
        name.clear();
        email.clear();
        password.clear();
        phone.clear();
        district.setValue(null);
        area.setValue(null);
        thana.setValue(null);
    }

    @FXML
    private void available_radio_reg(ActionEvent event) {
    }

    @FXML
    private void register_to_home(ActionEvent event) {
        
                try {
            Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
