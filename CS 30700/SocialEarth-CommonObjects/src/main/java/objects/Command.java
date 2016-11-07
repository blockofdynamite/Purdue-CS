package objects;

import java.io.Serializable;

public class Command implements Serializable {
    private static final long serialVersionUID = 7516482293622776147L;

    public enum CommandType {
        LOGIN, // A user logs in
        ACCOUNT_EXISTS, // Returns true if the username is already taken, false otherwise
        CREATE_ACCOUNT, // A user creates a new account
        POST_STORY, // Create a new story
        GET_STORY, // Get the details for a specific story
        GET_ALL_USER_STORIES, // Get basic details for all stories by a user
        LOGIN_WITH_FACEBOOK,
        LOGIN_WITH_GOOGLE,
        LIKE_STORY,
        UNLIKE_STORY,
        USER_LIKES_STORY,
        GET_LIKE_COUNT,
        FOLLOW_USER,
        GET_USERS_THAT_BEGIN_WITH_SUBSTRING,
        UNFOLLOW_USER,
        USER_FOLLOWS_USER,
        GET_FOLLOWING_USERS_STORIES,
        GET_SPECIFIC_USERS_STORIES,
        UPDATE_BIO_AND_PROFILE_PICTURE,
        GET_USER,
        GET_USERS_THAT_USER_FOLLOWS,
        GET_USERS_THAT_FOLLOW_USER,
        GET_COMMENTS_FOR_STORY, // TODO
        POST_COMMENT_IN_STORY, // TODO
        GET_NUMEBR_OF_COMMENTS_BY_USER, // TODO
        GET_NUMEBR_OF_STORIES_BY_USER, // TODO
        GET_NUMEBR_OF_LIKES_BY_USER, // TODO
        DELETE_STORY,
        DELETE_COMMENT
    }

    private CommandType commandType;
    private Story story;
    private User mainUser; // When applicable, the leader
    private User secondaryUser; // When applicable, the follower
    private Comment comment;

    public Command(CommandType commandType, Story story, User mainUser, User secondaryUser, Comment comment) {
        this.commandType = commandType;
        this.mainUser = mainUser;
        this.story = story;
        this.secondaryUser = secondaryUser;
        this.comment = comment;
    }

    public Story getStory() {
        return story;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public User getMainUser() {
        return mainUser;
    }

    public User getSecondaryUser() {
        return secondaryUser;
    }

    public Comment getComment() {
        return comment;
    }
}