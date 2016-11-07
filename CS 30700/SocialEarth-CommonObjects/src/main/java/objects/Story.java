package objects;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Entity
public class Story implements Serializable {
    private static final long serialVersionUID = 7516482293622776147L;

    public enum PrivacySetting {
        PUBILC, MUTUAL_FOLLOWERS, ONLY_ME
    }

    @Id
    private String id;

    @Property("storyId")
    private String storyId;

    @Property("lat")
    private double latitude;

    @Property("long")
    private double longitude;

    @Property("storyName")
    private String storyName;

    @Property("username")
    private String username;

    @Property("coverPhoto")
    private String coverPhotoUrl;

    @Property("photos")
    private ArrayList<String> photos;

    @Property("privacySetting")
    private PrivacySetting privacySeting;

    private String description;
    private Date date;

    public Story() {

    }

    public Story(String id, double latitude, double longitude, String storyName, String username, String description, Date date, String coverPhotoUrl, ArrayList<String> photos, PrivacySetting privacySeting) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storyName = storyName;
        this.username = username;
        this.description = description;
        this.date = date;
        this.storyId = id;
        this.photos = photos;
        this.coverPhotoUrl = coverPhotoUrl;
        this.privacySeting = privacySeting;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getStoryName() {
        return storyName;
    }

    public String getUser() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateRange() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoryId() {
        return storyId;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public PrivacySetting getPrivacySeting() {
        return privacySeting;
    }
}