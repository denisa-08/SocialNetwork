package com.example.socialnetwork;

import com.example.socialnetwork.controller.LoginController;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.FriendshipValidator;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.repository.RepositoryFriendshipDB;
import com.example.socialnetwork.repository.RepositoryUserDB;
import com.example.socialnetwork.service.ServiceObservable;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.UUID;

public class TestLogin extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/loginView.fxml"));
        AnchorPane root = loader.load();

        String username = "postgres";
        String password = "denisa2003";
        String url = "jdbc:postgresql://localhost:5432/socialNetwork";

        Repository<UUID, User> repoUser = new RepositoryUserDB(url, username, password, new UserValidator());
        Repository<Long, Friendship> repoFriendship = new RepositoryFriendshipDB(url, username, password, new FriendshipValidator());

        LoginController ctrl = loader.getController();
        ctrl.setLoginService(new ServiceObservable(repoUser, repoFriendship));

        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
