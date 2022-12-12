package com.example.socialnetwork.controller;

import com.example.socialnetwork.HelloApplication;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.ServiceObservable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private ServiceObservable service;
    @FXML
    TextField emailText;
    @FXML
    PasswordField passwordText;
    @FXML
    Button loginButton;

    public void setLoginService(ServiceObservable service) {
        this.service = service;
    }

    @FXML
    public void loginButtonClicked() throws IOException {
        String email = emailText.getText();
        String password = passwordText.getText();

        emailText.clear();
        passwordText.clear();
        User foundUser = service.searchUserByEmail(email);
        if (foundUser != null && foundUser.getPassword().equals(password)) {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/userView.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(loader.load(), 576, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            User2Controller userCtrl = loader.getController();
            userCtrl.setService(service, foundUser);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Hello, " + foundUser.getFirstName() + " " + foundUser.getLastName() + " ! These are your friends:");
            stage.show();

            Stage thisStage = (Stage) loginButton.getScene().getWindow();
            thisStage.hide();
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Email or password incorrect!");
            alert.show();
        }
    }
}
