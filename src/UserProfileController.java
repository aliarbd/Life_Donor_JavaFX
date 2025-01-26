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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ur
 */
public class UserProfileController implements Initializable {

    @FXML
    private TextField name_edit;
    @FXML
    private TextField email_edit;
    @FXML
    private TextField phone_edit;
    @FXML
    private ComboBox<?> area_edit;
    @FXML
    private ComboBox<?> thana_edit;
    @FXML
    private ComboBox<?> district_edit;
    @FXML
    private ToggleGroup available_radio;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
