package algonquin.cst2335.androidfinalproject.sun;
/**
 * Name: Yu Song 040873597
 * Course Section: CST2335 022
 * Description: This is the final project for the course CST2335 Mobile Graphical Interface Programming.
 *              This class represents view model for holding data.
 * */
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


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
