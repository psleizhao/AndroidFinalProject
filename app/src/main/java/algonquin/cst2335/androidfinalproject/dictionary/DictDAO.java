package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import algonquin.cst2335.androidfinalproject.dictionary.Dict;
/**
 * The {@code DictDAO} interface defines Data Access Object (DAO) methods
 * for interacting with the Room database to perform CRUD (Create, Read, Update, Delete)
 * operations on the {@link Dict} entities.
 *
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * DictDAO dictDAO = // Obtain an instance of DictDAO from the Room database.
 * Dict dict = new Dict("Word", "Definition");
 * long id = dictDAO.insertDict(dict);
 * }
 * </pre>
 *
 * @author Yuling Guo
 * @version 1.0
 * @since 2023-11-29
 */
@Dao
public interface DictDAO {
    /**
     * Inserts a dictionary entry into the Room database.
     *
     * @param d The dictionary entry to be inserted.
     * @return The unique identifier assigned to the inserted dictionary entry.
     */
    @Insert
    long insertDict(Dict d);

    /**
     * Retrieves all dictionary entries from the Room database.
     *
     * @return A list of all dictionary entries in the database.
     */
    @Query("Select * from Dict")
    List<Dict> getAllDicts();

    /**
     * Deletes a dictionary entry from the Room database.
     *
     * @param d The dictionary entry to be deleted.
     */
    @Delete
    void deleteDict(Dict d);

    /**
     * Updates a dictionary entry in the Room database.
     *
     * @param d The dictionary entry to be updated.
     */
    @Update
    void updateDict(Dict d);
}