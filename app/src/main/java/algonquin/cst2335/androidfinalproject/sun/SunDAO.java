package algonquin.cst2335.androidfinalproject.sun;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
/**
 * Data Access Object (DAO) interface for the Sun entity.
 * Defines methods to interact with the underlying database for Sun-related operations.
 *
 * @author Yu Song
 */
@Dao
public interface SunDAO {
    /**
     * Inserts a new Sun record into the database.
     *
     * @param s The Sun object to be inserted.
     * @return The row ID of the inserted Sun record.
     */
    @Insert
    long insertSun(Sun s);

    /**
     * Retrieves all Sun records from the database.
     *
     * @return A list containing all Sun records in the database.
     */
    @Query("Select * from Sun")
    List<Sun> getAllSuns();

    /**
     * Updates an existing Sun record in the database.
     *
     * @param s The Sun object with updated information.
     */
    @Update
    void updateSun(Sun s);

    /**
     * Deletes a Sun record from the database.
     *
     * @param s The Sun object to be deleted.
     */
    @Delete
    void deleteSun(Sun s);

}
