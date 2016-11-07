package clientCommunication;

import commands.LoginsManagement;
import commands.*;
import objects.Command;
import objects.Response;

public class Execute {

    public Response executeCommand(Command command) {
        switch(command.getCommandType()) {
            case LOGIN:
                return LoginsManagement.handleLogin(command);
            case ACCOUNT_EXISTS:
                return LoginsManagement.accountExists(command);
            case CREATE_ACCOUNT:
                return LoginsManagement.createAccount(command);
            case POST_STORY:
                return StoryManagement.postStory(command);
            case GET_STORY:
                return StoryManagement.getStory(command);
            case GET_ALL_USER_STORIES:
                return StoryManagement.getAllUserStories(command);
            case LOGIN_WITH_FACEBOOK:
                return LoginsManagement.loginWithTwitter(command);
            case LOGIN_WITH_GOOGLE:
                return LoginsManagement.loginWithGoogle(command);
            case LIKE_STORY:
                return LikeManagement.likeStory(command);
            case UNLIKE_STORY:
                return LikeManagement.unlikeStory(command);
            case USER_LIKES_STORY:
                return LikeManagement.userLikesStory(command);
            case GET_LIKE_COUNT:
                return LikeManagement.getLikeCount(command);
            case FOLLOW_USER:
                return FollowManagement.followUser(command);
            case GET_USERS_THAT_BEGIN_WITH_SUBSTRING:
                return UserManagement.getUsersThatBeginWithSubstring(command);
            case UNFOLLOW_USER:
                return FollowManagement.unfollowUser(command);
            case USER_FOLLOWS_USER:
                return FollowManagement.userFollowsUser(command);
            case GET_FOLLOWING_USERS_STORIES:
                return StoryManagement.getFollowingUsersStories(command);
            case GET_SPECIFIC_USERS_STORIES:
                return StoryManagement.getSpecificUsersStories(command);
            case UPDATE_BIO_AND_PROFILE_PICTURE:
                return ProfileManagement.updateBioAndProfilePicture(command);
            case GET_USER:
                return UserManagement.getUser(command);
            case GET_USERS_THAT_USER_FOLLOWS:
                return UserManagement.getUsersFollowed(command);
            case GET_USERS_THAT_FOLLOW_USER:
                return UserManagement.getFollowers(command);
            case GET_COMMENTS_FOR_STORY:
                return CommentManagement.getCommentsForStory(command);
            case POST_COMMENT_IN_STORY:
                return CommentManagement.postCommentInStory(command);
            case GET_NUMEBR_OF_COMMENTS_BY_USER:
                return UserStats.getNumebrOfCommentsByUser(command);
            case GET_NUMEBR_OF_LIKES_BY_USER:
                return UserStats.getNumebrOfLikesByUser(command);
            case GET_NUMEBR_OF_STORIES_BY_USER:
                return UserStats.getNumebrOfStoriesByUser(command);
            case DELETE_COMMENT:
                return CommentManagement.deleteComment(command);
            case DELETE_STORY:
                return StoryManagement.deleteStory(command);
            default:
                throw new RuntimeException("Something went horribly, horribly wrong");
        }
    }
}
