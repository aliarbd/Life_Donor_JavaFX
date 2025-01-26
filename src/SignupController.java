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

    // Initialize method to set up database connection and populate ComboBoxes
    public void initialize() {
        try {
            // Establish connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood_db", "root", "");

            // Populate district, area, and thana ComboBoxes (example values)
            district.setItems(FXCollections.observableArrayList("District 1", "District 2", "District 3"));
            area.setItems(FXCollections.observableArrayList("Area 1", "Area 2", "Area 3"));
            thana.setItems(FXCollections.observableArrayList("Thana 1", "Thana 2", "Thana 3"));

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not connect to the database.");
        }
    }

    // Method to handle signup form submission
    @FXML
    public void signupButtonClicked(ActionEvent event) {
        try {
            // Validate input fields
            if (name.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty() || phone.getText().isEmpty()) {
                showAlert("Validation Error", "All fields must be filled.");
                return;
            }

            // Prepare SQL query to insert user data
            String query = "INSERT INTO users (name, email, password, phone, district, area, thana) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name.getText());
            statement.setString(2, email.getText());
            statement.setString(3, password.getText());
            statement.setString(4, phone.getText());
            statement.setString(5, district.getValue());
            statement.setString(6, area.getValue());
            statement.setString(7, thana.getValue());

            // Execute the query
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Success", "Signup successful! You can now log in.");
                clearFields();
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
            showAlert("Error", "Could not save your information. Please try again.");
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
}
