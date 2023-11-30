package algonquin.cst2335.androidfinalproject.sun;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import algonquin.cst2335.androidfinalproject.databinding.SunDetailsLayoutBinding;

/**
 * Fragment class to display detailed information about a Sun record.
 *
 * This class includes views to show various details such as sunrise, solar noon, golden hour,
 * sunset, timezone, latitude, longitude, city name, current date, and current time.
 *
 * @author Yu Song
 */
public class SunDetailsFragment extends Fragment {
    /** Represents the Sun record to be displayed in the fragment.*/
    Sun selected;
    /** Represents the TAG that will be used in the Log.*/
    public static final String TAG = "SunDetailsFragment";

    /** no-argument constructor to prevent app crash */
    public SunDetailsFragment(){}

    /**
     * Constructor for the class, which takes a Sun object as a data source for the TextViews.
     *
     * @param toDisplay The Sun object to be displayed in the fragment.
     */
    public SunDetailsFragment(Sun toDisplay){
        selected = toDisplay;
    }

    /**
     * Called to create the content view for this Fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setReenterTransition(true);
        //inflate an XML layout for this Fragment
        SunDetailsLayoutBinding binding = SunDetailsLayoutBinding.inflate(getLayoutInflater());

        // set the latitude and longitude views:
        binding.sunLatDetail.setText(selected.sunLatitude);
        binding.sunLngDetail.setText(selected.sunLongitude);

        //set the text views:
        binding.sunSunriseDetail.setText(selected.sunrise);
        binding.sunNoonDetail.setText(selected.solar_noon);
        binding.sunGoldenDetail.setText(selected.golder_hour);
        binding.sunSunsetDetail.setText(selected.sunset);
        binding.sunTimezoneDetail.setText(selected.timezone);

        // set the cityName view:
        binding.sunCityName.setText(selected.cityName.toUpperCase());

        // Get the current date and time
        Date currentDate = Calendar.getInstance().getTime();

        // Format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        // Format the time
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        String formattedTime = timeFormat.format(currentDate);

        //set the text views:
        binding.sunDateDetailTitle.setText(formattedDate);
        binding.sunTimeDetailTitle.setText(formattedTime);

        return binding.getRoot();
    }

}
