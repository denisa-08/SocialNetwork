package com.example.socialnetwork.domain.validators;

/**
 * Interface for validating different types of entities
 * @param <T> the type of Entity that has to be validated
 */
public interface Validator<T> {
    /**
     * Validates the entity of T type
     * @param entity the Entity that has to be validated
     * @throws ValidationException if the Entity does not meet its defined requirements
     */
    void validate(T entity) throws ValidationException;
}




















//pentru mesaj-id user care trimite, id mesaj, id user care primeste
