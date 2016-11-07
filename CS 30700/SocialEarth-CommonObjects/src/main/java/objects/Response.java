package objects;

import objects.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private static final long serialVersionUID = 7516482293622776147L;

    private boolean bool;
    private int integer;
    private User user;
    private Story story;
    private ArrayList<BasicStoryInfo> listOfStories;
    private ArrayList<User> listOfUsers;
    private ArrayList<Comment> listOfComments;

    public Response(boolean bool, int integer, User user, Story story, ArrayList<BasicStoryInfo> listOfStories, ArrayList<User> listOfUsers, ArrayList<Comment> listOfComments) {
        this.bool = bool;
        this.integer = integer;
        this.user = user;
        this.story = story;
        this.listOfStories = listOfStories;
        this.listOfUsers = listOfUsers;
        this.listOfComments = listOfComments;
    }

    public boolean isBool() {
        return bool;
    }

    public int getInteger() {
        return integer;
    }

    public User getUser() {
        return user;
    }

    public Story getStory() {
        return story;
    }

    public ArrayList<BasicStoryInfo> getListOfStories() {
        return listOfStories;
    }

    public ArrayList<User> getListOfUsers() {
        return listOfUsers;
    }

    public ArrayList<Comment> getListOfComments() {
        return listOfComments;
    }
}