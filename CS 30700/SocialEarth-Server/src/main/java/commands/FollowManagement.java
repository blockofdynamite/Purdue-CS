package commands;

import com.mongodb.MongoClient;
import objects.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

public class FollowManagement {

    public static Response followUser(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class, Follow.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Follows");
        datastore.ensureIndexes();
        /////////////////////////////////

        User follower = UserManagement.getUserHelper(command.getMainUser().getUsername());
        User leader = UserManagement.getUserHelper(command.getSecondaryUser().getUsername());
        Response followerExists = LoginsManagement.accountExists(new Command(command.getCommandType(), null, follower, null, null));
        Response leaderExists = LoginsManagement.accountExists(new Command(command.getCommandType(), null, leader, null, null));
        Response alreadyFollows = userFollowsUser(command);


        System.out.println("Saving:::\n" +
                "followerId=" + follower.getId() + "\n" +
                "leaderId=" + leader.getId());

        System.out.println("These should all be true...");
        System.out.println(followerExists.isBool());
        System.out.println(leaderExists.isBool());
        System.out.println(!alreadyFollows.isBool());

        if (followerExists.isBool() && leaderExists.isBool() && !alreadyFollows.isBool()) {
            datastore.save(new Follow(null, follower.getId(), leader.getId()));
            return new Response(true, 0, follower, null, null, null, null);
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }
    }

    public static Response unfollowUser(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class, Follow.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Follows");
        datastore.ensureIndexes();
        /////////////////////////////////

        User follower = UserManagement.getUserHelper(command.getMainUser().getUsername());
        User leader = UserManagement.getUserHelper(command.getSecondaryUser().getUsername());

        Query<Follow> q = datastore.createQuery(Follow.class).filter("followerId =", follower.getId()).filter("leaderId =", leader.getId());
        datastore.delete(q);
        System.out.println("Loading:::\n" +
                "followerId=" + follower.getId() + "\n" +
                "leaderId=" + leader.getId());

        if (q != null) {
            return new Response(true, 0, null, null, null, null, null);
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }

    }

    public static Response userFollowsUser(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class, Follow.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Follows");
        datastore.ensureIndexes();
        /////////////////////////////////

        User follower = UserManagement.getUserHelper(command.getMainUser().getUsername());
        User leader = UserManagement.getUserHelper(command.getSecondaryUser().getUsername());

        Query<Follow> q = datastore.createQuery(Follow.class).filter("followerId =", follower.getId()).filter("leaderId =", leader.getId());
        Follow f = q.get();

        if (f != null && f.getFollowerId() != null) {
            return new Response(true, 0, null, null, null, null, null);
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }


    }
}
