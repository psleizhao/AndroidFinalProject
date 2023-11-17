package algonquin.cst2335.androidfinalproject.sun;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sun {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="SunId")
    public long SunId;

    @ColumnInfo(name="sunLatitude")
    protected String sunLatitude;

    @ColumnInfo(name="sunLongitude")
    protected String sunLongitude;

    @ColumnInfo(name="sunrise")
    protected String sunrise;

    @ColumnInfo(name="sunset")
    protected String sunset;

    @ColumnInfo(name="solar_noon")
    protected String solar_noon;

    @ColumnInfo(name="golder_hour")
    protected String golder_hour;

    @ColumnInfo(name="timezone")
    protected int timezone;


    // constructor
    public Sun(String sunLatitude, String sunLongitude, String sunrise, String sunset, String solar_noon, String golder_hour, int timezone) {
        this.sunLatitude = sunLatitude;
        this.sunLongitude = sunLongitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.solar_noon = solar_noon;
        this.golder_hour = golder_hour;
        this.timezone = timezone;
    }

    public String getSunLatitude() {
        return sunLatitude;
    }

    public String getSunLongitude() {
        return sunLongitude;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getSolar_noon() {
        return solar_noon;
    }

    public String getGolder_hour() {
        return golder_hour;
    }

    public int getTimezone() {
        return timezone;
    }


}
