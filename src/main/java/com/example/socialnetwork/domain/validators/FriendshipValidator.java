package com.example.socialnetwork.domain.validators;

import com.example.socialnetwork.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errors = "";
        if(entity.getId() == null)
            errors += "Id null!";

        if(!errors.equals(""))
            throw new ValidationException(errors);
    }
}
