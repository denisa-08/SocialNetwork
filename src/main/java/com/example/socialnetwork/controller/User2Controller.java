package com.example.socialnetwork.controller;

import com.example.socialnetwork.HelloApplication;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.service.ServiceObservable;
import com.example.socialnetwork.utils.events.UserEntityChangeEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class User2Controller implements Observer<UserEntityChangeEvent> {
    private ServiceObservable service;

    ObservableList<User> friends = FXCollections.observableArrayList();

    User user = new User();

    @FXML
    TableView<User> friendsTable;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TableColumn<User, String> tableColumnEmail;

    public void setService(ServiceObservable service, User user) {
        this.service = service;
        service.addObserver(this);
        this.user = user;
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        friendsTable.setItems(friends);
    }

    private void initModel() {
        Iterable<User> users_iterable = service.searchFriends(user);
        List<User> users = StreamSupport.stream(users_iterable.spliterator(), false)
                .collect(Collectors.toList());
        friends.setAll(users);
    }

    @Override
    public void update(UserEntityChangeEvent utilizatorEntityChangeEvent) {
        initModel();
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        User user_friend= friendsTable.getSelectionModel().getSelectedItem();
        if (user_friend!=null) {
            service.removeFriend(user_friend.getId(), this.user.getId());
            friends.remove(user_friend);
        }
    }

    public void handleAddFriend(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/addFriendsView.fxml"));
        Scene scene  = new Scene(loader.load(), 576, 400);

        AddFriendController addCtrl = loader.getController();
        addCtrl.setService(service);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

}
