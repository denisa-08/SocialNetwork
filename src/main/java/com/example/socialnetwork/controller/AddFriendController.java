package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.ServiceObservable;
import com.example.socialnetwork.utils.events.UserEntityChangeEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddFriendController implements Observer<UserEntityChangeEvent> {
    private ServiceObservable service;

    ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TableColumn<User, String> tableColumnEmail;

    @FXML
    TextField textFirstName;

    @FXML
    TextField textLastName;

    public void setService(ServiceObservable service) {
        this.service = service;
        service.addObserver(this);
        //initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        //tableView.setItems(users);
    }

    private void initModel() {
        Iterable<User> users_iterable = service.searchUserByName(textFirstName.getText(), textLastName.getText());
        List<User> users_list = StreamSupport.stream(users_iterable.spliterator(), false)
                .collect(Collectors.toList());
        users.setAll(users_list);
    }

    public void handleSendRequest(ActionEvent actionEvent) {

    }

    @Override
    public void update(UserEntityChangeEvent utilizatorEntityChangeEvent) {
        initModel();
    }
}
