package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.domain.validators.ValidationException;
import com.example.socialnetwork.domain.validators.Validator;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for managing entities from a file
 * @param <ID> the type of Entity's ID
 * @param <E> the type of Entity
 */
public abstract class AbstractFileRepo<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {

    String fileName; //the file to read from

    public AbstractFileRepo(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();
    }

    /**
     * Loads data of E type from the file "fileName"
     */
     private void loadData() {
         Path path = Paths.get(fileName);
         try {
             List<String> lines = Files.readAllLines(path);
             lines.forEach(linie -> {
                 E entity = extractEntity(Arrays.asList(linie.split(";")));
                 super.save(entity);
             });
         }
         catch(IOException exception)
         {
             System.err.println("Error while reading the file " + this.fileName);
             exception.printStackTrace();
         }
         catch(IllegalArgumentException | ValidationException | IndexOutOfBoundsException exception)
         {
             System.err.println("File corrupted!");
             exception.printStackTrace();
         }
     }

    /*private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                List<String> attr = Arrays.asList(linie.split(";"));
                E e = extractEntity(attr);
                super.save(e);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error while reading file!");
            e.printStackTrace();
        } catch(IllegalArgumentException | ValidationException | IndexOutOfBoundsException exception)
        {
            System.err.println("File corrupted!");
            exception.printStackTrace();
        }

    }*/

    //sau cu lambda - curs 4, sem 4 si 5
//        Path path = Paths.get(fileName);
//        try {
//            List<String> lines = Files.readAllLines(path);
//            lines.forEach(linie -> {
//                E entity=extractEntity(Arrays.asList(linie.split(";")));
//                super.save(entity);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    /**
     *  Abstract method that creates an entity of type E having a specified list of @code attributes
     *  extract entity  - template method design pattern
     * @param attributes Entity's specific attributes as a list of strings
     * @return an entity of type E
     */

    public abstract E extractEntity(List<String> attributes);

    /**
     * Abstract method that creates a string based on an entity
     * @param entity - the entity which will be seen as a string
     * @return the entity as a string
     */
    protected abstract String createEntityAsString(E entity);


    /**
     * Method that saves a new entity and writes it in the file
     * @param entity the entity that has to be added/saved
     *         entity must be not null
     * @return the entity that was added/saved
     */
    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;

    }

    /**
     * Method that updates an entity in file
     * @param entity the entity to be updated
     *          entity must not be null
     * @return
     */
    @Override
    public E update(E entity) {
        E e = super.update(entity);
        if(e == null)
            this.updateFile(super.findAll());
        return e;
    }

    /**
     * Method that removes an entity by id from the file
     * @param id id of the entity that has to be removed
     *      id must be not null
     * @return the deleted entity
     */
    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        if(e != null)
            this.updateFile(super.findAll());
        return e;
    }

    /**
     * Method that writes the entity seen as a String to file
     * @param entity the entity to be written
     */
    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that brings the file to the newest version
     * @param entities the list of entities to be found in the file
     */
    protected void updateFile(Iterable<E> entities) {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(this.fileName, false)))
        {
            for(E entity: entities)
            {
                bW.write(createEntityAsString(entity));
                bW.newLine();
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}

