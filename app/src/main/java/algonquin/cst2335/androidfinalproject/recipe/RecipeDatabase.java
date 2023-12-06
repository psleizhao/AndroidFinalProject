package algonquin.cst2335.androidfinalproject.recipe;
/**
 * Name: Lei ZHao 041086365
 * Course Section: CST2335 022
 * Description: This is the final project for the course CST2335 Mobile Graphical Interface Programming.
 *              This class represents the database, which initialize the DAO class.
 * */
import androidx.room.Database;
import androidx.room.RoomDatabase;
/**
 * Room database class representing the database for storing Recipe entities.
 *
 * This database class is annotated with Room annotations to specify the entities
 * and version. It provides an abstract method to access the associated DAO.
 */
@Database(entities = {Recipe.class}, version = 2)
public abstract class RecipeDatabase extends RoomDatabase {
    /**
     * Abstract method to retrieve the RecipeDAO for performing database operations.
     *
     * @return The RecipeDAO instance associated with this database.
     */
    public abstract RecipeDAO recipeDAO();
}
