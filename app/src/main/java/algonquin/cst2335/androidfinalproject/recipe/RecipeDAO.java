package algonquin.cst2335.androidfinalproject.recipe;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface RecipeDAO {

    @Insert
    long insertRecipe(Recipe r);

    @Query("Select * from Recipe")
    List<Recipe> getAllRecipes();

    @Update
    void updateRecipe(Recipe r);

    @Delete
    void deleteRecipe(Recipe r);
}
