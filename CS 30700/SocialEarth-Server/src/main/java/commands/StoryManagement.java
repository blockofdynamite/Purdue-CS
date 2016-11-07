package commands;

import com.mongodb.MongoClient;
import objects.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.List;
public class StoryManagement {

    public static Response postStory(Command command) {

        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Stories");
        datastore.ensureIndexes();
        /////////////////////////////////
        User user = command.getMainUser();
        String id = user.getId();
        Story story = command.getStory();
        Story newstory =  new Story(null, story.getLatitude(), story.getLongitude(), story.getStoryName(), id,
                story.getDescription(), story.getDateRange(), story.getCoverPhotoUrl(),story.getPhotos(), story.getPrivacySeting());
        datastore.save(newstory);
        return new Response(true, 0, user, newstory, null, null, null);
    }

    public static Response getStory(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Stories");
        datastore.ensureIndexes();
        /////////////////////////////////
        Story story = command.getStory();
        String storyId = story.getStoryId();
        ObjectId objectId = new ObjectId(storyId);
        Query<Story> q = datastore.createQuery(Story.class).filter("_id", objectId);
        Story nstory = q.get();

        if (nstory != null) {
            return new Response(true, 0, command.getMainUser(), nstory, null, null, null);
        } else {
            return new Response(false, 0, command.getMainUser(), null, null, null, null);
        }
    }

    // FIXME We should _really_ remove this, but right now doing so might break something.
    @Deprecated
    public static Response getAllUserStories(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Stories");
        datastore.ensureIndexes();
        /////////////////////////////////
        Query<Story> q = datastore.createQuery(Story.class).filter("username", command.getMainUser().getUsername());
        List<Story> storyList = q.asList();
        ArrayList<BasicStoryInfo> storyArray = new ArrayList<BasicStoryInfo>();
        for (Story story : storyList) {
            storyArray.add(new BasicStoryInfo(story.getLatitude(), story.getLongitude(), story.getStoryName(), story.getId()));
        }
        return new Response(true, 0, command.getMainUser(), null, storyArray, null, null);
    }

    //No changes made to this function, but if the other one works this should work too.
    public static Response getFollowingUsersStories(Command command) {
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Stories");
        datastore.ensureIndexes();
        /////////////////////////////////

        ArrayList<BasicStoryInfo> storyList = new ArrayList<>();
        ArrayList<User> users = UserManagement.getUsersFollowed(command).getListOfUsers();
        for (User user : users) {
            System.out.println(user.getUsername());
            Command comand = new Command(Command.CommandType.GET_SPECIFIC_USERS_STORIES, null, user, command.getMainUser(), null);
            ArrayList<BasicStoryInfo> stories = StoryManagement.getSpecificUsersStories(comand).getListOfStories();
            for (BasicStoryInfo story : stories) {
                System.out.println(story);
                storyList.add(story);
            }
        }
        return new Response(true, 0, command.getMainUser(), null, storyList, null, null);

    }

    // FIXME Nick (handle privacy setting)
    // Primary user is still the user who owns the story
    // Secondary user is now the logged in user
    public static Response getSpecificUsersStories(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Stories");
        datastore.ensureIndexes();
        ////////////////////////////////

        //this is a REALLY jank way to go about the query, but this is all I have
        //making a list of of strings that will default to being empty
        //if the users are MUTUALLY FOLLOWING, the USERNAME OF THE USER WHO MADE THE STORIES is added to the list
        //then, during the query, if we are looking a mutually following setting, we assert that the name of the
        //user making the story is in the list. This is more efficent (if it works) in both space and time
        // than checking for this kind of thing outside the query, the reason it seems so bad is because we cannot
        // match a USERNAME and USERID(STORIES store userNAMES, FOLLOWS store userIDs).
        // I hope it works ;-;

        List<String> mutualFollowingCheck = new ArrayList<String>();
        if (FollowManagement.userFollowsUser(new Command(null, null, command.getMainUser(), command.getSecondaryUser(), null)).isBool() && FollowManagement.userFollowsUser(new Command(null, null, command.getSecondaryUser(), command.getMainUser(), null)).isBool())
        {
            mutualFollowingCheck.add(command.getMainUser().getUsername());
        }
        //get ready for the query of the century
        Query<Story> q = datastore.createQuery(Story.class).filter("username", command.getMainUser().getUsername());

        //I have no idea on how to even organize this, you can only compare two things at once with the .or() for queries
        /*
        q.criteria("privacySetting").equal(Story.PrivacySetting.PUBILC);
        q.or(
                q.or(q.criteria("privacySetting").equal(Story.PrivacySetting.PUBILC),
                        q.and(q.criteria("privacySetting").equal(Story.PrivacySetting.ONLY_ME),q.criteria("username").equal(command.getSecondaryUser().getUsername()))),
                q.and(q.criteria("privacySetting").equal(Story.PrivacySetting.MUTUAL_FOLLOWERS), q.criteria("username").in(mutualFollowingCheck))
        ); */

        List<Story> storyList = q.asList();
        ArrayList<BasicStoryInfo> storyArray = new ArrayList<BasicStoryInfo>();
        for (Story story : storyList) {
            if (privacyMatches(story.getPrivacySeting(), command))
                storyArray.add(new BasicStoryInfo(story.getLatitude(), story.getLongitude(), story.getStoryName(), story.getId()));
        }
        return new Response(true, 0, command.getMainUser(), null, storyArray, null, null);
    }

    private static boolean privacyMatches(Story.PrivacySetting priv, Command command) {
        if (priv == null)
            return true;
        switch(priv) {
            case PUBILC:
                return true;
            case ONLY_ME:
                if (command.getMainUser().getUsername().equals(command.getSecondaryUser().getUsername()))
                    return true;
                else
                    return false;
            case MUTUAL_FOLLOWERS:
                if (usersFollowEachother(command) || command.getMainUser().getUsername().equals(command.getSecondaryUser().getUsername()))
                    return true;
                else
                    return false;
        }
        return true;
    }

    private static boolean usersFollowEachother(Command command) {
        Command otherCommand = new Command(Command.CommandType.USER_FOLLOWS_USER, null, command.getSecondaryUser(), command.getMainUser(), null);
        return FollowManagement.userFollowsUser(command).isBool() && FollowManagement.userFollowsUser(otherCommand).isBool();
    }

    // TODO Nick
    public static Response deleteStory(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Stories");
        datastore.ensureIndexes();
        /////////////////////////////////

        User u = UserManagement.getUserHelper(command.getMainUser().getUsername());
        Story s = command.getStory();
        Response userExists = LoginsManagement.accountExists(command);

        System.out.println(userExists.isBool());
        System.out.println("********************************** " + command.getStory().getId());
        if (userExists.isBool() && s.getId() != null) {
            System.out.println("Going to delete....");
            ObjectId objectId = new ObjectId(s.getId());
            System.out.println(datastore.createQuery(Story.class).filter("_id",objectId).get());
            datastore.delete(datastore.createQuery(Story.class).filter("_id",objectId));
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }

        return new Response(true, 0, u, null, null, null, null);
    }
}
