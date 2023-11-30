package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * The {@code DictViewModel} class serves as the ViewModel for the dictionary feature
 * in the Android final project. It extends {@link ViewModel} and provides LiveData
 * objects to observe and manage the list of dictionaries and the selected dictionary
 * entry.
 *
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * DictViewModel dictViewModel = new ViewModelProvider(this).get(DictViewModel.class);
 * MutableLiveData<ArrayList<Dict>> dictsLiveData = dictViewModel.Dicts;
 * MutableLiveData<Dict> selectedDictLiveData = dictViewModel.selectedDicts;
 * }
 * </pre>
 *
 * @author Yuling Guo
 * @version 1.0
 * @since 2023-11-29
 */
public class DictViewModel extends ViewModel {
    /**
     * LiveData object holding the list of dictionaries.
     */
    public MutableLiveData<ArrayList<Dict>> Dicts = new MutableLiveData<>();

    /**
     * LiveData object holding the selected dictionary entry.
     */
    public MutableLiveData<Dict> selectedDicts = new MutableLiveData<>();
}