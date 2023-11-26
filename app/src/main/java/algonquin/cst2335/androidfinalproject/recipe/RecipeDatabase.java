package algonquin.cst2335.androidfinalproject.recipe;

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
