package com.example.socialnetwork.domain.validators;
import com.example.socialnetwork.domain.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that implements Validator interface to validate an Entity of User type.
 */
public class UserValidator implements Validator<User> {
    /**
     * Validates a User
     * The name of the user should not be null or empty, should not have more than 30 characters and contain something
     * other than letters. (also the name should begin with uppercase)
     * The email should have a defined email structure (with regex)
     * @param entity the Entity that has to be validated
     * @throws ValidationException if the User does not meet its defined requirements
     */
    @Override
    public void validate(User entity) throws ValidationException {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$");
        Matcher mat = pattern.matcher(entity.getEmail());

        String errors = "";
        if(entity.getId() == null)
            errors += "ID is null!\n";
        if(entity.getFirstName() == null || entity.getFirstName().equals(""))
            errors += "First name is null!\n";
        if(!entity.getFirstName().matches("^[A-Z][a-zA-Z]+"))
            errors += "First name should contain only letters and should begin with an uppercase letter.\n";
        if(!entity.getLastName().matches("^[A-Z][a-zA-Z]+"))
            errors += "Last name should contain only letters and should begin with an uppercase letter.\n";
        if(entity.getLastName() == null || entity.getLastName().equals(""))
            errors += "Last name is null!\n";
        if(!mat.matches())
            errors += "Email is not valid!\n";
        if (entity.getFirstName().length() > 30)
            errors += "First name is too long. Provide a first name smaller than 30.\n";
        if (entity.getLastName().length() > 30)
            errors += "Last name is too long. Provide a first name smaller than 30.\n";

        if(!errors.equals(""))
            throw new ValidationException(errors);
    }
}