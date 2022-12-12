package com.example.socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Class for managing the friendship between 2 users
 */
public class Friendship extends Entity<Long>{
    private UUID idUser1, idUser2;
    private LocalDateTime friendsForm;

    public Friendship(UUID idUser1, UUID idUser2, LocalDateTime friendsForm) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.friendsForm = friendsForm;
    }

    public UUID getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(UUID idUser1) {
        this.idUser1 = idUser1;
    }

    public UUID getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(UUID idUser2) {
        this.idUser2 = idUser2;
    }

    public LocalDateTime getFriendsForm() {
        return friendsForm;
    }

    public void setFriendsForm(LocalDateTime friendsForm) {
        this.friendsForm = friendsForm;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "idUser1=" + idUser1.toString() +
                ", idUser2=" + idUser2.toString() +
                ", friendsForm=" + friendsForm +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Friendship f))
            return false;

        return (this.getIdUser1() == f.getIdUser1() && this.getIdUser2() == f.getIdUser2()) || (this.getIdUser1() == f.getIdUser2() &&
                this.getIdUser2() == f.getIdUser1()) && getFriendsForm().equals(f.getFriendsForm());

    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser1, idUser2, friendsForm);
    }
}
