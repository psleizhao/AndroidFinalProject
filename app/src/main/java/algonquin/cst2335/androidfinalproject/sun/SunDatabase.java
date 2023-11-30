package algonquin.cst2335.androidfinalproject.sun;

import androidx.room.Database;
import androidx.room.RoomDatabase;
/**
 * Represents the Room Database for managing Sun records using the SunDAO.
 *
 * @Database annotation specifies the entities to be included and the version of the database.
 * @author Yu Song
 */
@Database(entities = {Sun.class}, version = 1)
public abstract class SunDatabase extends RoomDatabase {
    /**
     * Provides an abstract method to obtain the SunDAO interface for database operations.
     *
     * @return SunDAO object for accessing database operations.
     */
    public abstract SunDAO sunDAO();
}
