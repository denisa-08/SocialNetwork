package com.example.socialnetwork.repository;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.domain.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RepositoryUserDB implements Repository<UUID, User> {
    private final String url;
    private final String username;
    private final String password;
    private final UserValidator validator;

    public RepositoryUserDB(String url, String username, String password, UserValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findOne(UUID uuid) {
        for (User user : this.findAll()) {
            if(user.getId().equals(uuid)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User utilizator = new User(firstName, lastName, email);
                UUID id_converted = UUID.fromString(id);
                utilizator.setId(id_converted);
                utilizator.setPassword(password);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {

        String sql = "insert into users (id, first_name, last_name, email ) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setString(4, entity.getEmail());
            ps.setString(5, entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(UUID id) {
        String sql = "delete from users where id = ?";


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
                 ps.setString(1,id.toString());

                 ps.executeUpdate();
             }
             catch (SQLException e) {
                 e.printStackTrace();
             }
             return null;

    }

    @Override
    public User update(User entity) {
        String sql = "update users set first_name = ?, last_name = ?, email = ? where id = ?";


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getId().toString());
            ps.setString(5, entity.getPassword());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int size() {
        int s = 0;
        for (User user : findAll()) {
            s++;
        }
        return s;
    }
}