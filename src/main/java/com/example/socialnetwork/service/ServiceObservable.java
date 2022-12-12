package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.utils.events.ChangeEventType;
import com.example.socialnetwork.utils.observer.Observable;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.utils.events.UserEntityChangeEvent;
import com.example.socialnetwork.utils.observer.Observer;


import java.util.*;

public class ServiceObservable implements Observable<UserEntityChangeEvent> {
    private final Repository<UUID, User> repoUser;

    private final Repository<Long, Friendship> repoFriendship;
    private final List<Observer<UserEntityChangeEvent>> observers = new ArrayList<>();

    public ServiceObservable(Repository<UUID, User> repoUser, Repository<Long, Friendship> repoFriendship){
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;
    }

    public User addUser(User user) {
        if(repoUser.save(user) == null) {
            UserEntityChangeEvent event = new UserEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public User deleteUser(UUID id) {
        User user = repoUser.delete(id);
        if (user != null) {
            notifyObservers(new UserEntityChangeEvent(ChangeEventType.DELETE, user));
            return user;
        }
        return null;
    }

    public Iterable<User> getAll() {
        return repoUser.findAll();
    }

    /**
     * Search a user by his email
     * @param email
     * @return
     * @throws IllegalArgumentException
     */
    public User searchUserByEmail(String email) throws IllegalArgumentException {
        Iterable<User> users = this.getAll();
        for (User user : users) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    /**
     * Search a user by his name
     * @param firstName
     * @param lastName
     * @return
     * @throws IllegalArgumentException
     */
    public Iterable<User> searchUserByName(String firstName, String lastName) throws IllegalArgumentException{
        Iterable<User> users = this.getAll();
        Set<User> foundUsers = new HashSet<>();
        for (User user : users) {
            if(user.getFirstName().equals(firstName) || user.getLastName().equals(lastName))
                foundUsers.add(user);
        }
        return foundUsers;
    }

    /**
     * Search all the friends for a user
     * @param user
     * @return
     * @throws IllegalArgumentException
     */
    public Iterable<User> searchFriends(User user) throws IllegalArgumentException{
        Set<User> friends = new HashSet<>();
        Iterable<Friendship> friendships = repoFriendship.findAll();
        friendships.forEach(friendship -> {
            if (friendship.getIdUser1().equals(user.getId())) {
                friends.add(repoUser.findOne(friendship.getIdUser2()));
            } else if (friendship.getIdUser2().equals(user.getId())) {
                friends.add(repoUser.findOne(friendship.getIdUser1()));
            }
        });
        return friends;
    }


    /**
     * Searches a friendship between 2 user with ID1 and ID2
     *
     * @param ID1 User 1's ID
     * @param ID2 User 2's ID
     * @return Friendship if the friendship exists, and it is not deleted, null otherwise
     * @throws IllegalArgumentException if the data read is not in the correct format
     */
    public Friendship searchFriendship(UUID ID1, UUID ID2) throws IllegalArgumentException {
        for (Friendship searchedFriendship : this.repoFriendship.findAll()) {
            if ((searchedFriendship.getIdUser1().equals(ID1) && searchedFriendship.getIdUser2().equals(ID2)) ||
                    (searchedFriendship.getIdUser1().equals(ID2) && searchedFriendship.getIdUser2().equals(ID1))) {
                return searchedFriendship;
            }
        }
        return null;
    }

    /**
     * Ends a friendship between 2 users
     *
     * @param ID  One of the user's Id
     * @param IDf Other's user Id
     * @throws IllegalArgumentException if the data read is not in the correct format
     */
    public void removeFriend(UUID ID, UUID IDf) throws IllegalArgumentException {
        User user1 = repoUser.findOne(ID);
        User user2 = repoUser.findOne(IDf);

        if (user1 != null && user2 != null) {
            Friendship friendship = this.searchFriendship(ID, IDf);
            if (friendship != null) {
                this.repoFriendship.delete(friendship.getId());
                System.out.println("Friendship deleted!");}
            else
                System.out.println("The friendship does not exist yet!");
        }
    }

    @Override
    public void addObserver(Observer<UserEntityChangeEvent> e)
    {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserEntityChangeEvent> e){
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEntityChangeEvent t) {
        observers.stream().forEach(x -> x.update(t));
    }

}
