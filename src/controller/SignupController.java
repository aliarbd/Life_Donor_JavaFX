/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class SignupController {

    @FXML
    private ComboBox<?> area;

    @FXML
    private ToggleGroup available_radio;

    @FXML
    private ComboBox<?> district;

    @FXML
    private TextField email;

    @FXML
    private TextField name;

    @FXML
    private TextField password;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<?> thana;

    @FXML
    void register_press(ActionEvent event) {

    }

}
