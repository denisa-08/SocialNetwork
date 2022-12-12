package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.domain.validators.ValidationException;
import com.example.socialnetwork.domain.validators.Validator;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class used for managing entities in memory
 * @param <ID> the type of Entity's ID
 * @param <E> the type of Entity that has to be managed
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private final Validator<E> validator;
    private final Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id) throws IllegalArgumentException{
        if (id==null)
            throw new IllegalArgumentException("Id must be not null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) throws IllegalArgumentException, ValidationException {
        if (entity==null)
            throw new IllegalArgumentException("Entity must be not null");

        validator.validate(entity);
        /*
        if(entities.get(entity.getId()) != null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);

        return null;
         */

        for (Map.Entry<ID, E> entry : this.entities.entrySet())
        {
            if (entry.getValue().equals(entity))
            {
                return entry.getValue();
            }
        }

        if (this.entities.containsKey(entity.getId()))
        {
            return entity;
        }

        this.entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        return this.entities.remove(id);
    }

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");

        validator.validate(entity);

        /*entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }*/
        if(this.entities.containsKey(entity.getId()))
        {
            this.entities.put(entity.getId(), entity);
            return null;
        }
        return entity;

    }

    @Override
    public int size() {
        return this.entities.size();
    }
}
