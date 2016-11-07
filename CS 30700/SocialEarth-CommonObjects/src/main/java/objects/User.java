package objects;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 7516482293622776147L;

    @Id
    private String id;

    @Property("username")
    private String username;

    @Property("realname")
    private String realname;

    @Property("email")
    private String email;

    @Property("password")
    private String password;

    private String profilePictureUrl;
    private String bio;

    public User() {
        id = null;
        username = null;
        realname = null;
        email = null;
        password = null;
    }

    public User(String id, String username, String realname, String email, String password, String profilePictureUrl, String bio) {
        this.id = id;
        this.username = username;
        this.realname = realname;
        this.email = email;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;

    }

    public String getUsername() {
        return username;
    }

    public String getRealname() {
        return realname;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void removePassword() {
        this.password = null;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
