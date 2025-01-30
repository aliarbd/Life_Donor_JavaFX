import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

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
    @FXML
    private Label info_available;
    @FXML
    private RadioButton available_radio_yes_edit;
    @FXML
    private RadioButton available_radio_no_edit;

    private static String loggedInUserEmail;
    @FXML
    private ToggleGroup available_radio_edit;
    @FXML
    private Label info_blood;
    @FXML
    private ComboBox<String> blood_edit;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (loggedInUserEmail != null && !loggedInUserEmail.isEmpty()) {
            populateUserData();
        } else {
            showAlert("Error", "Logged in email is not set.");
        }
    }

    public static void setLoggedInUserEmail(String email) {
        loggedInUserEmail = email;
    }

    private void populateUserData() {
        String dbUrl = "jdbc:mysql://localhost:3306/blood_db";
        String dbUser = "root";
        String dbPassword = "";
        
        blood_edit.setItems(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"));
        district_edit.setItems(FXCollections.observableArrayList("District 1", "District 2", "District 3"));
        area_edit.setItems(FXCollections.observableArrayList("Area 1", "Area 2", "Area 3"));
        thana_edit.setItems(FXCollections.observableArrayList("Thana 1", "Thana 2", "Thana 3"));

        String query = "SELECT name, email, password, phone, area, thana, district, availability, blood_group FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, loggedInUserEmail);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                info_name.setText(rs.getString("name"));
                info_email.setText(rs.getString("email"));
                info_pass.setText("********");
                info_phone.setText(rs.getString("phone"));
                info_area.setText(rs.getString("area"));
                info_thana.setText(rs.getString("thana"));
                info_district.setText(rs.getString("district"));
                info_available.setText(rs.getBoolean("availability") ? "Yes" : "No");
                info_blood.setText(rs.getString("blood_group"));

                name_edit.setText(rs.getString("name"));
                email_edit.setText(rs.getString("email"));
                phone_edit.setText(rs.getString("phone"));
                password_edit.setText(rs.getString("password"));
                area_edit.getSelectionModel().select(rs.getString("area"));
                thana_edit.getSelectionModel().select(rs.getString("thana"));
                district_edit.getSelectionModel().select(rs.getString("district"));
                blood_edit.getSelectionModel().select(rs.getString("blood_group"));
                if (rs.getBoolean("availability")) {
                    available_radio_yes_edit.setSelected(true);
                } else {
                    available_radio_no_edit.setSelected(true);
                }
            } else {
                showAlert("Error", "No user data found for the given email.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch user data: " + e.getMessage());
        }
    }

    @FXML
private void update_pressed() {
    String updatedName = name_edit.getText();
    String updatedEmail = email_edit.getText();
    String updatedPhone = phone_edit.getText();
    String updatedPassword = password_edit.getText();
    String updatedArea = area_edit.getValue();
    String updatedThana = thana_edit.getValue();
    String updatedDistrict = district_edit.getValue();
    boolean updatedAvailability = available_radio_yes_edit.isSelected();
    String updatedBlood = blood_edit.getValue();

    // Validate input fields
    if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedPhone.isEmpty() || updatedPassword.isEmpty()) {
        showAlert("Error", "Please fill out all fields.");
        return;
    }

    // Validate email format
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    if (!updatedEmail.matches(emailRegex)) {
        showAlert("Validation Error", "Invalid email format. Please enter a valid email address.");
        return;
    }

    // Validate phone number (must be exactly 11 digits)
    if (!updatedPhone.matches("\\d{11}")) {
        showAlert("Validation Error", "Phone number must be exactly 11 digits.");
        return;
    }

    String dbUrl = "jdbc:mysql://localhost:3306/blood_db";
    String dbUser = "root";
    String dbPassword = "";

    // Update user details
    String updateQuery = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, area = ?, thana = ?, district = ?, availability = ? , blood_group = ? WHERE email = ?";

    try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
         PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

        stmt.setString(1, updatedName);
        stmt.setString(2, updatedEmail);
        stmt.setString(3, updatedPassword);
        stmt.setString(4, updatedPhone);
        stmt.setString(5, updatedArea);
        stmt.setString(6, updatedThana);
        stmt.setString(7, updatedDistrict);
        stmt.setBoolean(8, updatedAvailability);
        stmt.setString(9, updatedBlood);
        stmt.setString(10, loggedInUserEmail); // Using old email to find the record

        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated > 0) {
            showAlert("Success", "User data updated successfully.");

            // Update loggedInUserEmail **only if email was changed**
            if (!loggedInUserEmail.equals(updatedEmail)) {
                loggedInUserEmail = updatedEmail;
            }

            // Refresh the UI with updated data
            populateUserData();
        } else {
            showAlert("Error", "Failed to update user data.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Error", "Database error: " + e.getMessage());
    }
}

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void profile_to_home(ActionEvent event) {
        
                try {
            Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SignUp");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
