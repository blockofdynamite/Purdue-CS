package objects;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.io.Serializable;

@Entity
public class Like implements Serializable{
    private static final long serialVersionUID = 7516482293622776147L;

    @Id
    private String id;

    @Property("userId")
    private String userId;

    @Property("storyId")
    private String storyId;

    public Like() {

    }

    public Like(String id, String userId, String storyId) {
        this.id = id;
        this.userId = userId;
        this.storyId = storyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStoryId() {
        return storyId;
    }
}
