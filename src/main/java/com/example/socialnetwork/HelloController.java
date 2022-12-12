package com.example.socialnetwork;

import com.example.socialnetwork.domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Arrays;
import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button btnHello;

    @FXML
    ListView<User> listView;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        btnHello.setText("alt text");
        List<User> list = List.of(new User("dan", "ana", "dan23@gmail.com"));
        ObservableList<User> observableList = FXCollections.observableList(list);
        listView.setItems(observableList);
    }
}