package com.example.socialnetwork.repository;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.validators.FriendshipValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RepositoryFriendshipDB implements Repository<Long, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final FriendshipValidator validator;

    public RepositoryFriendshipDB(String url, String username, String password, FriendshipValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Friendship findOne(Long id) {
        for (Friendship fr : this.findAll()) {
            if(fr.getId() == id) {
                return fr;
            }
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id_f");
                //String id = resultSet.getString("id");
                String id_user1 = resultSet.getString("id_user1");
                String id_user2 = resultSet.getString("id_user2");
                String form = resultSet.getString("form");
                LocalDateTime form_parsed = LocalDateTime.parse(form);

                Friendship friendship = new Friendship(UUID.fromString(id_user1),UUID.fromString(id_user2),form_parsed);
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;

    }

    @Override
    public Friendship save(Friendship entity) {

        String sql = "insert into friendships (id_f, id_user1, id_user2, form) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getId().toString());
            ps.setString(2, entity.getIdUser1().toString());
            ps.setString(3, entity.getIdUser2().toString());
            ps.setString(4, entity.getFriendsForm().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long id) {
        String sql = "delete from friendships where id_f = ?";

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
    public Friendship update(Friendship entity) {
        String sql = "update friendships set id_user1 = ?, id_user2 = ?, form = ? where id_f = ?";


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getIdUser1().toString());
            ps.setString(2, entity.getIdUser2().toString());
            ps.setString(3, entity.getFriendsForm().toString());
            ps.setString(4, entity.getId().toString());

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
        for (Friendship fr : findAll()) {
            s++;
        }
        return s;
    }
}