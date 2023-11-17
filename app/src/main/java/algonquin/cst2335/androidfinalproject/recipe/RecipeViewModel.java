package algonquin.cst2335.androidfinalproject.recipe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class RecipeViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();
    public MutableLiveData<Recipe> selectedRecipes = new MutableLiveData< >();

}
