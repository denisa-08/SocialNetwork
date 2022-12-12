package com.example.socialnetwork;
import com.example.socialnetwork.controller.UserController;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.validators.FriendshipValidator;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.repository.RepositoryFriendshipDB;
import com.example.socialnetwork.repository.RepositoryUserDB;
import com.example.socialnetwork.service.ServiceObservable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repository.Repository;

import java.io.IOException;
import java.util.UUID;
public class HelloApplication extends Application {

    Repository<UUID, User> userRepository;
    ServiceObservable service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        String username = "postgres";
        String password = "denisa2003";
        String url = "jdbc:postgresql://localhost:5432/socialNetwork";

        Repository<UUID, User> userRepository = new RepositoryUserDB(url, username, password, new UserValidator());
        Repository<Long, Friendship> repoFriendship = new RepositoryFriendshipDB(url, username, password, new FriendshipValidator());


        userRepository.findAll().forEach(x -> System.out.println(x));
        service = new ServiceObservable(userRepository, repoFriendship);
        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/userView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UserController userController = fxmlLoader.getController();
        userController.setUtilizatorService(service);
    }
}
