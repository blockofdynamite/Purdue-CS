package objects;

import com.sun.javafx.beans.IDProperty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Comment implements Serializable {
    private static final long serialVersionUID = 7516482293622776147L;

    @Id
    private String id;

    @Property("userId")
    private String userId;

    @Property("storyId")
    private String storyId;

    @Property("commentText")
    private String commentText;

    @Property("date")
    private Date date;

    @Property("order")
    private int order; // the order in which the comments should exist (incase Date screws up)

    public Comment() {
        this.id = null;
        this.userId = null;
        this.storyId = null;
        this.commentText = null;
        this.date = null;
        this.order = 0;
    }

    public Comment(String id, String userId, String storyId, String commentText, Date date, int order) {
        this.id = id;
        this.userId = userId;
        this.storyId = storyId;
        this.commentText = commentText;
        this.date = date;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getStoryId() {
        return storyId;
    }

    public String getCommentText() {
        return commentText;
    }

    public Date getDate() {
        return date;
    }

    public int getOrder() {
        return order;
    }
}
