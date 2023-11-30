package algonquin.cst2335.androidfinalproject.sun;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.androidfinalproject.recipe.Recipe;

/**
 * ViewModel class for managing Sun-related data using LiveData.
 *
 * This class holds MutableLiveData objects for lists of Sun records, selected Sun record, and favorite Sun records.
 * It is responsible for providing data to the UI and surviving configuration changes.
 *
 * @author Yu Song
 */
public class SunViewModel extends ViewModel{
    /**
     * MutableLiveData for the list of Sun records.
     */
    public MutableLiveData<ArrayList<Sun>> suns = new MutableLiveData<>();

    /**
     * MutableLiveData for the selected Sun record.
     */
    public MutableLiveData<Sun> selectedSun = new MutableLiveData<>();

    /**
     * MutableLiveData for the list of favorite Sun records.
     */
    public MutableLiveData<ArrayList<Sun>> favSuns = new MutableLiveData<>();

}
