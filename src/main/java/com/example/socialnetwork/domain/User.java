package com.example.socialnetwork.domain;

import java.util.Objects;
import java.util.UUID;

/**
 *  Class for the users from the social network
 *  It extends Entity class with type parameter <Long>
 *
 */
public class User extends Entity<UUID>{
    /**
     * All the attributes for every user.
     */
    private String firstName;
    private String lastName;
    private String email;

    private String password;
    //private boolean deleted = false;
    private boolean inCommunity = false;

    //A HashSet that contains the friends of the user
    //private final HashSet<User> friends = new HashSet<>();

    public User(){}

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }*/

    public boolean isInCommunity() {
        return inCommunity;
    }

    public void setInCommunity(boolean inCommunity) {
        this.inCommunity = inCommunity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "id='" + this.getId().toString() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) && getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getEmail());
    }

}
