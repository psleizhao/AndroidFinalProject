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
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import algonquin.cst2335.androidfinalproject.databinding.SunDetailsLayoutBinding;

public class SunDetailsFragment extends Fragment {
    Sun selected;

    //constructor for your class which takes a Sun object that it will use as a data source for the TextViews:
    public SunDetailsFragment(Sun toDisplay){
        selected = toDisplay;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //inflate an XML layout for this Fragment
        SunDetailsLayoutBinding binding = SunDetailsLayoutBinding.inflate(getLayoutInflater());

        //set the text views:
        binding.sunSunriseDetail.setText(selected.sunrise);
        binding.sunNoonDetail.setText(selected.solar_noon);
        binding.sunGoldenDetail.setText(selected.golder_hour);
        binding.sunSunsetDetail.setText(selected.sunset);
        binding.sunTimezoneDetail.setText(selected.timezone);
//        binding.sunTimezoneDetail.setText("-600");

        // Get the current time in the selected timezone
//        TimeZone timeZone = TimeZone.getTimeZone("GMT");  // Set your default timezone
//        int utcOffset = Integer.parseInt(selected.timezone);  // Replace with selected.timezone or use a default value
//        timeZone.setRawOffset(utcOffset * 60 * 1000);  // Set the UTC offset in milliseconds
//
//        // Create a SimpleDateFormat instance for date
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd-MMM-yyyy", Locale.getDefault());
//        dateFormat.setTimeZone(timeZone);
//
//        // Create a SimpleDateFormat instance for time
//        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
//        timeFormat.setTimeZone(timeZone);
//
//        // Format the date and time
//        String currentDate = dateFormat.format(new Date());
//        String currentTime = timeFormat.format(new Date());
//
//        //set the text views:
//        binding.sunDateDetailTitle.setText(currentDate);
//        binding.sunTimeDetailTitle.setText(currentTime);

        return binding.getRoot();
    }
}
