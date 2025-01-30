import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;



//private static String loggedInUserEmail;
/**
 * FXML Controller class
 */
public class SearchController implements Initializable {

    static void setLoggedInUserEmail(String email) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @FXML
    private ComboBox<String> area_search;
    @FXML
    private ComboBox<String> thana_search;
    @FXML
    private ComboBox<String> district_search;
    @FXML
    private ComboBox<String> blood_search;
    @FXML
    private TreeTableColumn<Donor, String> table_name;
    @FXML
    private TreeTableColumn<Donor, String> table_blood;
    @FXML
    private TreeTableColumn<Donor, String> table_phone;
    @FXML
    private TreeTableColumn<Donor, String> table_area;
    @FXML
    private TreeTableColumn<Donor, String> table_thana;
    @FXML
    private TreeTableColumn<Donor, String> table_district;
    @FXML
    private TreeTableView<Donor> tableView;
    @FXML
    private Button search_to_register_;

     private static String loggedInUserEmail_;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateComboBoxes();
        // Binding the columns to the respective properties in Donor
        table_name.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        table_blood.setCellValueFactory(param -> param.getValue().getValue().bloodGroupProperty());
        table_phone.setCellValueFactory(param -> param.getValue().getValue().phoneProperty());
        table_area.setCellValueFactory(param -> param.getValue().getValue().areaProperty());
        table_thana.setCellValueFactory(param -> param.getValue().getValue().thanaProperty());
        table_district.setCellValueFactory(param -> param.getValue().getValue().districtProperty());
    }
    
    public static void setLoggedInUserEmail_(String email) {
        loggedInUserEmail_ = email;  // Set the static variable
    }


    private void populateComboBoxes() {
        // **Static Data for ComboBoxes**
        blood_search.setItems(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"));
        district_search.setItems(FXCollections.observableArrayList("District 1", "District 2", "District 3"));
        area_search.setItems(FXCollections.observableArrayList("Area 1", "Area 2", "Area 3"));
        thana_search.setItems(FXCollections.observableArrayList("Thana 1", "Thana 2", "Thana 3"));

        // **Fetch dynamic data from database**
        populateComboBoxFromDB("SELECT DISTINCT district FROM users", district_search);
        populateComboBoxFromDB("SELECT DISTINCT area FROM users", area_search);
        populateComboBoxFromDB("SELECT DISTINCT thana FROM users", thana_search);
    }

    private void populateComboBoxFromDB(String query, ComboBox<String> comboBox) {
        String dbUrl = "jdbc:mysql://localhost:3306/blood_db";
        String dbUser = "root";
        String dbPassword = "";

        ObservableList<String> items = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String value = rs.getString(1);
                if (!items.contains(value)) {
                    items.add(value);
                }
            }

            comboBox.getItems().addAll(items);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void donor_search_press(ActionEvent event) {
        String dbUrl = "jdbc:mysql://localhost:3306/blood_db";
        String dbUser = "root";
        String dbPassword = "";

        String selectedBlood = blood_search.getValue();
        String selectedArea = area_search.getValue();
        String selectedThana = thana_search.getValue();
        String selectedDistrict = district_search.getValue();

        String query = "SELECT name, blood_group, phone, area, thana, district FROM users WHERE 1=1";
        
        if (selectedBlood != null) query += " AND blood_group = ?";
        if (selectedArea != null) query += " AND area = ?";
        if (selectedThana != null) query += " AND thana = ?";
        if (selectedDistrict != null) query += " AND district = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            if (selectedBlood != null) stmt.setString(paramIndex++, selectedBlood);
            if (selectedArea != null) stmt.setString(paramIndex++, selectedArea);
            if (selectedThana != null) stmt.setString(paramIndex++, selectedThana);
            if (selectedDistrict != null) stmt.setString(paramIndex++, selectedDistrict);

            ResultSet rs = stmt.executeQuery();
            TreeItem<Donor> root = new TreeItem<>(new Donor("Root", "", "", "", "", "")); // Dummy root

            while (rs.next()) {
                Donor donor = new Donor(
                    rs.getString("name"),
                    rs.getString("blood_group"),
                    rs.getString("phone"),
                    rs.getString("area"),
                    rs.getString("thana"),
                    rs.getString("district")
                );
                root.getChildren().add(new TreeItem<>(donor));
            }

            tableView.setRoot(root);
            tableView.setShowRoot(false); // Hide the dummy root

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void search_to_register_press(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SignUp");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void search_to_login(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // **Donor Class for Table Representation**
    public static class Donor {
        private final StringProperty name;
        private final StringProperty bloodGroup;
        private final StringProperty phone;
        private final StringProperty area;
        private final StringProperty thana;
        private final StringProperty district;

        public Donor(String name, String bloodGroup, String phone, String area, String thana, String district) {
            this.name = new SimpleStringProperty(name);
            this.bloodGroup = new SimpleStringProperty(bloodGroup);
            this.phone = new SimpleStringProperty(phone);
            this.area = new SimpleStringProperty(area);
            this.thana = new SimpleStringProperty(thana);
            this.district = new SimpleStringProperty(district);
        }

        public StringProperty nameProperty() { return name; }
        public StringProperty bloodGroupProperty() { return bloodGroup; }
        public StringProperty phoneProperty() { return phone; }
        public StringProperty areaProperty() { return area; }
        public StringProperty thanaProperty() { return thana; }
        public StringProperty districtProperty() { return district; }

        // Getters
        public String getName() { return name.get(); }
        public String getBloodGroup() { return bloodGroup.get(); }
        public String getPhone() { return phone.get(); }
        public String getArea() { return area.get(); }
        public String getThana() { return thana.get(); }
        public String getDistrict() { return district.get(); }
    }
    
@FXML
private void home_to_profile(ActionEvent event) {
    // Ensure loggedInUserEmail_ is not null and not empty
    if (loggedInUserEmail_ == null || loggedInUserEmail_.isEmpty()) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("userProfile.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


}

