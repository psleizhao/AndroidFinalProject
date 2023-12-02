package algonquin.cst2335.androidfinalproject.sun;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Entity class representing a Sun record with various attributes such as latitude, longitude,
 * sunrise, sunset, solar noon, golden hour, timezone, and city name.
 *
 * @author Yu Song
 */
@Entity
public class Sun {

    /**
     * Primary key representing the unique identifier for a Sun record.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="sunId")
    public long sunId;

    /**
     * Latitude of the Sun record.
     */
    @ColumnInfo(name="sunLatitude")
    protected String sunLatitude;

    /**
     * Longitude of the Sun record.
     */
    @ColumnInfo(name="sunLongitude")
    protected String sunLongitude;

    /**
     * Sunrise time of the Sun record.
     */
    @ColumnInfo(name="sunrise")
    protected String sunrise;

    /**
     * Sunset time of the Sun record.
     */
    @ColumnInfo(name="sunset")
    protected String sunset;

    /**
     * Solar noon time of the Sun record.
     */
    @ColumnInfo(name="solar_noon")
    protected String solar_noon;

    /**
     * Golden hour time of the Sun record.
     */
    @ColumnInfo(name="golder_hour")
    protected String golder_hour;

    /**
     * Timezone of the Sun record.
     */
    @ColumnInfo(name="timezone")
    protected String timezone;

    /**
     * City name associated with the Sun record.
     */
    @ColumnInfo(name="cityName")
    protected String cityName;

    /**
     * Default no-argument constructor for the Sun class.
     */
    public Sun(){}

    /**
     * Constructor for the Sun class, initializing its attributes with the provided values.
     *
     * @param sunLatitude Latitude of the Sun record.
     * @param sunLongitude Longitude of the Sun record.
     * @param sunrise Sunrise time of the Sun record.
     * @param sunset Sunset time of the Sun record.
     * @param solar_noon Solar noon time of the Sun record.
     * @param golder_hour Golden hour time of the Sun record.
     * @param timezone Timezone of the Sun record.
     * @param cityName City name associated with the Sun record.
     */
    public Sun(String sunLatitude, String sunLongitude, String sunrise, String sunset, String solar_noon, String golder_hour, String timezone, String cityName) {
        this.sunLatitude = sunLatitude;
        this.sunLongitude = sunLongitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.solar_noon = solar_noon;
        this.golder_hour = golder_hour;
        this.timezone = timezone;
        this.cityName = cityName;
    }

    /**
     * Gets the latitude of the Sun record.
     *
     * @return The latitude value.
     */
    public String getSunLatitude() {
        return sunLatitude;
    }

    /**
     * Gets the longitude of the Sun record.
     *
     * @return The longitude value.
     */
    public String getSunLongitude() {
        return sunLongitude;
    }

    /**
     * Gets the sunrise time of the Sun record.
     *
     * @return The sunrise time.
     */
    public String getSunrise() {
        return sunrise;
    }

    /**
     * Gets the sunset time of the Sun record.
     *
     * @return The sunset time.
     */
    public String getSunset() {
        return sunset;
    }

    /**
     * Gets the solar noon time of the Sun record.
     *
     * @return The solar noon time.
     */
    public String getSolar_noon() {
        return solar_noon;
    }

    /**
     * Gets the golden hour time of the Sun record.
     *
     * @return The golden hour time.
     */
    public String getGolder_hour() {
        return golder_hour;
    }

    /**
     * Gets the timezone of the Sun record.
     *
     * @return The timezone value.
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Gets the city name associated with the Sun record.
     *
     * @return The city name.
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Sets the sunrise time of the Sun record.
     *
     * @param sunrise The new sunrise time to set.
     */
    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    /**
     * Sets the sunset time of the Sun record.
     *
     * @param sunset The new sunset time to set.
     */
    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    /**
     * Sets the solar noon time of the Sun record.
     *
     * @param solar_noon The new solar noon time to set.
     */
    public void setSolar_noon(String solar_noon) {
        this.solar_noon = solar_noon;
    }

    /**
     * Sets the golden hour time of the Sun record.
     *
     * @param golder_hour The new golden hour time to set.
     */
    public void setGolder_hour(String golder_hour) {
        this.golder_hour = golder_hour;
    }

    /**
     * Sets the city name of the Sun record.
     *
     * @param cityName The new city name to set.
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
