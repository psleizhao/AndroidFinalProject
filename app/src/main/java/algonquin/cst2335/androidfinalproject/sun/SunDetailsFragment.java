package algonquin.cst2335.androidfinalproject.sun;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import algonquin.cst2335.androidfinalproject.databinding.SunDetailsLayoutBinding;

public class SunDetailsFragment extends Fragment {
    Sun selected;

    //constructor for your class which takes a Sun object that it will use as a data source for the TextViews:
    public SunDetailsFragment(Sun toDisplay){
        selected = toDisplay;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //inflate an XML layout for this Fragment
        SunDetailsLayoutBinding binding = SunDetailsLayoutBinding.inflate(inflater);

        //set the text views:
        binding.sunSunriseDetail.setText(selected.sunrise);
        binding.sunNoonDetail.setText(selected.solar_noon);
        binding.sunGoldenDetail.setText(selected.golder_hour);
        binding.sunSunsetDetail.setText(selected.sunset);
        binding.sunTimezoneDetail.setText(selected.timezone);

        /*
        The below date time function requires Api > 26
         */
        // Get the current time in the selected timezone
        TimeZone timeZone = TimeZone.getTimeZone("GMT");  // Set your default timezone
        int utcOffset = selected.timezone;  // Replace with selected.timezone or use a default value
        timeZone.setRawOffset(utcOffset * 60 * 1000);  // Set the UTC offset in milliseconds

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime localDateTime = currentDateTime.plusMinutes(timeZone.getRawOffset() / (60 * 1000));

        // Format the date and time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.getDefault());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());

        //set the text views:
        binding.sunDateDetailTitle.setText(currentDateTime.format(dateFormatter));
        binding.sunTimeDetailTitle.setText(localDateTime.format(timeFormatter));

        return binding.getRoot();
    }
}
