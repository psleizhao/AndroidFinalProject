package algonquin.cst2335.androidfinalproject.recipe;
/**
 * Name: Lei ZHao 041086365
 * Course Section: CST2335 022
 * Description: This is the final project for the course CST2335 Mobile Graphical Interface Programming.
 *              This class represents the DAO pattern which can manipulate the database.
 * */
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) interface for interacting with the Room database
 * to perform CRUD operations on the Recipe entity.
 */
@Dao
public interface RecipeDAO {

    /**
     * Inserts a new recipe into the database.
     *
     * @param r The Recipe object to be inserted.
     * @return The unique identifier (ID) assigned to the inserted recipe.
     */
    @Insert
    long insertRecipe(Recipe r);

    /**
     * Retrieves all recipes from the database.
     *
     * @return A list of all Recipe objects stored in the database.
     */
    @Query("Select * from Recipe")
    List<Recipe> getAllRecipes();

    /**
     * Updates an existing recipe in the database.
     *
     * @param r The Recipe object with updated information.
     */
    @Update
    void updateRecipe(Recipe r);

    /**
     * Deletes a recipe from the database.
     *
     * @param r The Recipe object to be deleted.
     */
    @Delete
    void deleteRecipe(Recipe r);
}
