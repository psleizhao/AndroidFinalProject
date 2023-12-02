package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * The {@code DictDatabase} class represents the Room database for the dictionary feature
 * in the Android final project. It defines the database configuration, including the
 * entities and version information. This class extends {@link RoomDatabase}.
 *
 * @author Yuling Guo
 * @version 1.0
 * @since 2023-11-29
 */
@Database(entities = {Dict.class}, version = 1)
public abstract class DictDatabase extends RoomDatabase {
    /**
     * Retrieves the Data Access Object (DAO) for interacting with the Room database.
     *
     * @return The {@link DictDAO} instance.
     */
    public abstract DictDAO DictDAO();
}
