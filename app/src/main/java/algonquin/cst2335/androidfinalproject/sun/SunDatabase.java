package algonquin.cst2335.androidfinalproject.sun;
/**
 * Name: Yu Song 040873597
 * Course Section: CST2335 022
 * Description: This is the final project for the course CST2335 Mobile Graphical Interface Programming.
 *              This class represents the database, which initialize the DAO class.
 * */

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
