import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private TextField log_email;
    @FXML
    private TextField log_password;

    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood_db", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void login_press(ActionEvent event) throws IOException {
        String email = log_email.getText();
        String password = log_password.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email or Password cannot be empty.");
            return;
        }

        try {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                // If a match is found, login successful
                showAlert("Success", "Login successful!");
                
                // Redirect to the next screen (e.g., dashboard or main page)
                Parent root = FXMLLoader.load(getClass().getResource("userProfile.fxml")); // Replace with your main page
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.show();
            } else {
                // If no match is found, show error
                showAlert("Error", "Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error. Please try again later.");
        }
    }

    @FXML
    private void signup_link_press(ActionEvent event) {
        try {
            // Load the signup.fxml file
            Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Signup Screen");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Log the exception for debugging
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
