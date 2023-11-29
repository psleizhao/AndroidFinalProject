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
    @Insert
    long insertDict(Dict d);
    @Query("Select * from Dict")
    List<Dict> getAllDicts();
    @Delete
    void deleteDict(Dict d);

    @Update
    void updateDict(Dict d);
}