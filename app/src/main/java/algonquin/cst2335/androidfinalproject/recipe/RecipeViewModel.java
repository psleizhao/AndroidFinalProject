package algonquin.cst2335.androidfinalproject.recipe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel class for managing the data related to recipes.
 *
 * This class extends the ViewModel and includes MutableLiveData instances
 * to hold lists of recipes, favorite recipes, and the currently selected recipe.
 */
public class RecipeViewModel extends ViewModel {
    /**
     * MutableLiveData holding the list of recipes.
     */
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

    /**
     * MutableLiveData holding the list of favorite recipes.
     */
    public MutableLiveData<ArrayList<Recipe>> favRecipes = new MutableLiveData<>();

    /**
     * MutableLiveData holding the currently selected recipe.
     */
    public MutableLiveData<Recipe> selectedrecipe = new MutableLiveData< >();
    /**
     * MutableLiveData holding the page title when rotating.
     */
    public MutableLiveData<String> recipeTitleText = new MutableLiveData< >();
}
