package objects;

import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

@Entity
public class BasicStoryInfo implements Serializable {
    private static final long serialVersionUID = 7516482293622776147L;

    @Id
    private String id;

    @Property("lat")
    private double latitude;

    @Property("long")
    private double longitude;

    @Property("storyName")
    private String storyName;

    public BasicStoryInfo() {
        
    }

    public BasicStoryInfo(double latitude, double longitude, String storyName, String id) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storyName = storyName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}