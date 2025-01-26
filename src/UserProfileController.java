import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UserProfileController implements Initializable {

    @FXML
    private TextField name_edit;
    @FXML
    private TextField email_edit;
    @FXML
    private TextField phone_edit;
    @FXML
    private ComboBox<String> area_edit;
    @FXML
    private ComboBox<String> thana_edit;
    @FXML
    private ComboBox<String> district_edit;
    @FXML
    private TextField password_edit;
    @FXML
    private Label info_name;
    @FXML
    private Label info_email;
    @FXML
    private Label info_pass;
    @FXML
    private Label info_phone;
    @FXML
    private Label info_area;
    @FXML
    private Label info_thana;
    @FXML
    private Label info_district;

    private static String loggedInUserEmail; // Static variable to store the logged-in email

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (loggedInUserEmail != null && !loggedInUserEmail.isEmpty()) {
            populateUserData();
        } else {
            showAlert("Error", "Logged in email is not set.");
        }
    }

    // Method to set the logged-in user's email (can be called from other controllers)
    public static void setLoggedInUserEmail(String email) {
        loggedInUserEmail = email;
    }

    private void populateUserData() {
        // Database connection details
        String dbUrl = "jdbc:mysql://localhost:3306/blood_db"; // Update with your database URL
        String dbUser = "root"; // Update with your database username
        String dbPassword = ""; // Update with your database password

        // Query to fetch user data from the database
        String query = "SELECT name, email, password, phone, area, thana, district FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, loggedInUserEmail); // Use the logged-in email to query the database

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Populate fields with user data
                info_name.setText(rs.getString("name"));
                info_email.setText(rs.getString("email"));
                info_pass.setText("********"); // Mask the password
                info_phone.setText(rs.getString("phone"));
                info_area.setText(rs.getString("area"));
                info_thana.setText(rs.getString("thana"));
                info_district.setText(rs.getString("district"));

                // Optionally, set default values for editable fields
                name_edit.setText(rs.getString("name"));
                email_edit.setText(rs.getString("email"));
                phone_edit.setText(rs.getString("phone"));
                password_edit.setText(rs.getString("password"));
                area_edit.getSelectionModel().select(rs.getString("area"));
                thana_edit.getSelectionModel().select(rs.getString("thana"));
                district_edit.getSelectionModel().select(rs.getString("district"));
            } else {
                showAlert("Error", "No user data found for the given email.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch user data: " + e.getMessage());
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
