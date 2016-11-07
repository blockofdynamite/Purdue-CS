package objects;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.io.Serializable;

@Entity
public class Follow implements Serializable {
    private static final long serialVersionUID = 7516482293622776147L;

    @Id
    private String id;

    @Property("followerId")
    private String followerId;

    @Property("leaderId")
    private String leaderId;

    public Follow() {

    }

    public Follow(String id, String followerId, String leaderId) {
        this.id = id;
        this.followerId = followerId;
        this.leaderId = leaderId;
    }

    public String getFollowerId() {
        return followerId;
    }

    public String getLeaderId() {
        return leaderId;
    }
}