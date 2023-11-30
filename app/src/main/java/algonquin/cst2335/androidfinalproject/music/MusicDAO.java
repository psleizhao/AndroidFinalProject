package algonquin.cst2335.androidfinalproject.music;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


/**
 * Data Access Object (DAO) for the Music entity.
 * Provides methods for performing database operations on Music objects.
 */
@Dao
public interface MusicDAO {

    /**
     * Inserts a new Music object into the database.
     *
     * @param m The Music object to be inserted.
     * @return The row ID of the newly inserted music record.
     */
    @Insert
    long insertMusic(Music m);

    /**
     * Retrieves all music records from the database.
     *
     * @return A list of all Music objects.
     */
    @Query("Select * from Music")
    List<Music> getAllMusics();

    /**
     * Updates an existing Music object in the database.
     *
     * @param m The Music object to be updated.
     */
    @Update
    void updateMusic(Music m);

    /**
     * Deletes a Music object from the database.
     *
     * @param m The Music object to be deleted.
     */
    @Delete
    void deleteMusic(Music m);
}