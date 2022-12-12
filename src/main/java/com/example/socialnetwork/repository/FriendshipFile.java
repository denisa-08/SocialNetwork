package com.example.socialnetwork.repository;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FriendshipFile extends AbstractFileRepo<Long, Friendship>
{
    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship(UUID.fromString(attributes.get(1)), UUID.fromString(attributes.get(2)), LocalDateTime.parse(attributes.get(3)));
        friendship.setId(Long.parseLong(attributes.get(0)));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().toString() + ";" + entity.getIdUser1().toString() + ";" + entity.getIdUser2().toString() + ";" + entity.getFriendsForm();
    }
}
