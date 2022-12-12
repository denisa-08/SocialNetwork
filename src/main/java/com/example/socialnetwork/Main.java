package com.example.socialnetwork;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.repository.RepositoryUserDB;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        System.out.println("ok");
        System.out.println("Reading data from file");
        String username="postgres";
        String password="denisa2003";
        String url="jdbc:postgresql://localhost:5432/socialNetwork";
        Repository<UUID, User> userFileRepository = new RepositoryUserDB(url, username, password, new UserValidator());
        userFileRepository.findAll().forEach(x -> System.out.println(x));
    }
}
