package commands;

import com.mongodb.MongoClient;
import objects.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;

public class UserManagement {
    public static Response getUsersThatBeginWithSubstring(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Logins");
        datastore.ensureIndexes();
        /////////////////////////////////

        User user = command.getMainUser();
        Query<User> q = datastore.createQuery(User.class);
        q.field("username").startsWith(command.getMainUser().getUsername());
        ArrayList<User> userList = new ArrayList<User>(q.asList());
        if (user != null) {
            return new Response(true, 0, null, null, null, userList, null);
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }
    }

    public static User getUserHelper(String username) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Logins");
        datastore.ensureIndexes();
        /////////////////////////////////

        Query<User> q = datastore.createQuery(User.class).filter("username =", username);
        return q.get();
    }

    public static Response getUser(Command command) {
        User user = getUserHelper(command.getMainUser().getUsername());
        if (user != null) {
            return new Response(true, 0, user, null, null, null, null);
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }
    }

    public static Response getUsersFollowed(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class, Follow.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Follows");
        datastore.ensureIndexes();

        final Morphia morphiaUsers = new Morphia();
        morphiaUsers.map(User.class, Story.class, BasicStoryInfo.class, Follow.class);
        final Datastore datastoreUsers = morphiaUsers.createDatastore(new MongoClient(), "Logins");
        datastoreUsers.ensureIndexes();
        /////////////////////////////////

        User follower = UserManagement.getUserHelper(command.getMainUser().getUsername());
        Response followerExists = LoginsManagement.accountExists(command);

        if (followerExists.isBool()) {
            Query<Follow> q = datastore.createQuery(Follow.class).filter("followerId =", follower.getId());
            ArrayList<Follow> followArray= new ArrayList<>(q.asList());
            ArrayList<User> userArray = new ArrayList<>();
            for(Follow follow : followArray) {
                ObjectId objectId = new ObjectId(follow.getLeaderId());
                User user = datastoreUsers.get(User.class, objectId);
                userArray.add(user);
            }
            return new Response(true, 0, follower, null, null, userArray, null);

        } else {
            return new Response(false, 0, null, null, null, null, null);
        }
    }

    public static Response getFollowers(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class, Follow.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Follows");
        datastore.ensureIndexes();

        final Morphia morphiaUsers = new Morphia();
        morphiaUsers.map(User.class, Story.class, BasicStoryInfo.class, Follow.class);
        final Datastore datastoreUsers = morphiaUsers.createDatastore(new MongoClient(), "Logins");
        datastoreUsers.ensureIndexes();
        /////////////////////////////////

        User leader = UserManagement.getUserHelper(command.getMainUser().getUsername());
        Response leaderExists = LoginsManagement.accountExists(command);

        if (leaderExists.isBool()) {
            Query<Follow> q = datastore.createQuery(Follow.class).filter("leaderId =", leader.getId());
            ArrayList<Follow> followArray= new ArrayList<>(q.asList());
            ArrayList<User> userArray = new ArrayList<>();
            for(Follow follow : followArray)
            {
                ObjectId objectId = new ObjectId(follow.getFollowerId());
                User user = datastoreUsers.get(User.class, objectId);
                userArray.add(user);
            }
            return new Response(true, 0, leader, null, null, userArray, null);
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }
    }
}
