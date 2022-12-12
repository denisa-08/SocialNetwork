package com.example.socialnetwork.service;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.FriendshipValidator;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.domain.validators.ValidationException;
import com.example.socialnetwork.repository.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Class that coordinates the behaviour of the application.
 */
public class Service {

    //private final InMemoryRepository<Long, User> repository;
    //private final Repository<UUID, User> userRepository;
    private final RepositoryUserDB userRepository;

    //private final Repository<Long, Friendship> friendshipRepository;
    private final RepositoryFriendshipDB friendshipRepository;

    public Service() {
        //IN MEMORY
        //this.repository = new InMemoryRepository<>(new UserValidator());

        //FILE
        //this.userRepository = new UserFile(Config.getProperties().getProperty("Users"), new UserValidator());
        //this.friendshipRepository = new FriendshipFile(Config.getProperties().getProperty("Friendships"), new FriendshipValidator());

        //DATABASE
        this.userRepository = new RepositoryUserDB("jdbc:postgresql://localhost:5432/socialNetwork", "postgres", "denisa2003", new UserValidator());
        this.friendshipRepository = new RepositoryFriendshipDB("jdbc:postgresql://localhost:5432/socialNetwork", "postgres", "denisa2003", new FriendshipValidator());
    }


    /**
     * Adds a user to the repository. It creates a user based on the information provided as parameters and sets its ID
     * to the next value.
     * If user already exists, it won't be added another one
     *
     * @param FirstName User's first name
     * @param LastName  User's last name
     * @param Email     User's email
     * @throws IllegalArgumentException if the data is not in the correct format
     * @throws ValidationException      if the User does not meet the defined requirements
     */
    public void addUser(String FirstName, String LastName, String Email) throws IllegalArgumentException, ValidationException {
        User newUser = new User(FirstName, LastName, Email);
        //String uniqueID = UUID.randomUUID().toString();
        newUser.setId(UUID.randomUUID());

        User savedUser = this.userRepository.save(newUser);

        if (savedUser == null) {
            System.out.println(newUser + " has been successfully added.");
        } else {
            /*if (savedUser.isDeleted() && savedUser != newUser) {
                //savedUser.setDeleted(false);
                //System.out.println(savedUser + "recovered the account.");
            }*/
            System.out.println(newUser + "has not been added.");
        }

    }

    /**
     * If the user with the id exists, it marks the user as deleted. The user is not removed from the repository
     *
     * @param ID User's Id
     * @throws IllegalArgumentException if the data read is not in the correct format
     */
    public void removeUser(UUID ID) throws IllegalArgumentException {
        User removedUser = this.searchUser(ID);
        if (removedUser != null) {
            //removedUser.setDeleted(true);
            //this.userRepository.update(removedUser);

            ArrayList<Long> ids = new ArrayList<Long>();

            this.friendshipRepository.findAll().forEach(friendship -> {
                if (friendship.getIdUser1().equals(removedUser.getId()) || friendship.getIdUser2().equals(removedUser.getId())) {
                   ids.add(friendship.getId());
                }
            });
            for (Long id : ids) this.friendshipRepository.delete(id);

            this.userRepository.delete(ID);
        }
    }


    /**
     * Searches a user with the ID
     *
     * @param ID User's ID
     * @return User if the user exists, and it is not deleted, null otherwise
     * @throws IllegalArgumentException if the data read is not in the correct format
     */
    public User searchUser(UUID ID) throws IllegalArgumentException {
        User searchedUser = this.userRepository.findOne(ID);
        if (searchedUser == null) {
            System.out.println("The user with the ID " + ID.toString() + " could not been found.");
        }
            return searchedUser;
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
        for (Friendship searchedFriendship : this.friendshipRepository.findAll()) {
            if ((searchedFriendship.getIdUser1().equals(ID1) && searchedFriendship.getIdUser2().equals(ID2)) ||
                    (searchedFriendship.getIdUser1().equals(ID2) && searchedFriendship.getIdUser2().equals(ID1))) {
                return searchedFriendship;
            }
        }
        return null;
    }


    /**
     * Creates a friendship between 2 users
     *
     * @param ID  One of the user's Id
     * @param IDf Other's user Id
     * @throws IllegalArgumentException if the data read is not in the correct format
     */
    public void addFriend(UUID ID, UUID IDf) throws IllegalArgumentException {
        User user1 = this.searchUser(ID);
        User user2 = this.searchUser(IDf);

        /*if(user1 != null && user2 != null) {
            user1.getFriends().add(user2);
            this.repository.update(user1);

            user2.getFriends().add(user1);
            this.repository.update(user2);
            System.out.println("Friendship created!");
        }*/
        if(this.searchFriendship(ID, IDf) == null) {
            if (user1 != null && user2 != null) {
                Friendship newFriendship = new Friendship(ID, IDf, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                newFriendship.setId((long) newFriendship.hashCode());
                this.friendshipRepository.save(newFriendship);
                System.out.println("Friendship created between these 2 users!");
            } else {
                System.out.println("The users don't exist!");
            }
        }
        else
            System.out.println("This friendship already exists!");
    }

    /**
     * Ends a friendship between 2 users
     *
     * @param ID  One of the user's Id
     * @param IDf Other's user Id
     * @throws IllegalArgumentException if the data read is not in the correct format
     */
    public void removeFriend(UUID ID, UUID IDf) throws IllegalArgumentException {
        User user1 = this.searchUser(ID);
        User user2 = this.searchUser(IDf);

        /*if(user1 != null && user2 != null) {
            user1.getFriends().remove(user2);
            System.out.println(user1.getFriends());
            this.repository.update(user1);

            user2.getFriends().remove(user1);
            this.repository.update(user2);
            System.out.println("Friendship removed!");
        }*/
        if (user1 != null && user2 != null) {
            Friendship friendship = this.searchFriendship(ID, IDf);
            if (friendship != null) {
                this.friendshipRepository.delete(friendship.getId());
                System.out.println("Friendship deleted!");}
            else
                System.out.println("The friendship does not exist yet!");

        }
    }

    /**
     * DFS algorithm to find each community
     *
     * @param user the user from we start the iteration
     */
    private void DFS(User user) {
        user.setInCommunity(true);

        /*user.getFriends().forEach(friend -> {
            if (!friend.isInCommunity() && !friend.isDeleted()) {
                DFS(friend);
            }
        });*/
        this.friendshipRepository.findAll().forEach(friendship -> {
            if (friendship.getIdUser1().equals(user.getId())) {
                User friend = this.searchUser(friendship.getIdUser2());
                if (friend != null && !friend.isInCommunity()) {
                    DFS(friend);
                }
            } else if (friendship.getIdUser2().equals(user.getId())) {
                User friend = this.searchUser(friendship.getIdUser1());
                if (friend != null && !friend.isInCommunity()) {
                    DFS(friend);
                }
            }
        });
    }

    /**
     * Counts the number of communities of users in the social network. Each user of the social network is \
     * added in the same community as his friends.
     *
     * @return int- the number of communities
     */
    public int numberofCommunities() {
        int communities = 0;
        for (User user : this.userRepository.findAll()) {
            System.out.println(user.getFirstName());
            if (!user.isInCommunity()) {
                ++communities;
                DFS(user);
            }
        }
        this.userRepository.findAll().forEach(user -> {
            user.setInCommunity(false);
        });
        return communities;
    }


    /**
     * Prints all the users that are in the social network without their friends
     */
    public void printAll() {
        this.userRepository.findAll().forEach(user -> {
                System.out.println(user);
        });
    }

    /**
     * Prints all the users that are in the social network with their friends
     */
    public void printAllWithFriends() {
        this.userRepository.findAll().forEach(user -> {
                System.out.println();
                System.out.println(user);
                System.out.println("Their friends: ");
                this.friendshipRepository.findAll().forEach(friendship -> {
                    //System.out.println(friendship.getIdUser1());
                    //System.out.println(friendship.getIdUser2());
                    if (friendship.getIdUser1().equals(user.getId())) {
                        User searchedUser = this.searchUser(friendship.getIdUser2());
                        if (searchedUser != null) {
                            System.out.println(this.searchUser(friendship.getIdUser2()));
                        }
                    } else if (friendship.getIdUser2().equals(user.getId())) {
                        User searchedUser = this.searchUser(friendship.getIdUser1());
                        if (searchedUser != null) {
                            System.out.println(this.searchUser(friendship.getIdUser1()));
                        }
                    }
                });
        });
    }
}
