package algonquin.cst2335.androidfinalproject.sun;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.androidfinalproject.recipe.Recipe;

public class SunViewModel extends ViewModel{
    public MutableLiveData<ArrayList<Sun>> suns = new MutableLiveData<>();
    public MutableLiveData<Sun> selectedSun = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Sun>> favSuns = new MutableLiveData<>();

}
