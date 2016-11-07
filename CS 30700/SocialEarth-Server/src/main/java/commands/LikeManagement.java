package commands;

import com.mongodb.MongoClient;
import objects.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

public class LikeManagement {
    public static Response likeStory(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Likes");
        datastore.ensureIndexes();
        /////////////////////////////////

        User u = UserManagement.getUserHelper(command.getMainUser().getUsername());
        Story s = command.getStory();
        Response userExists = LoginsManagement.accountExists(command);
        Response alreadyLikes = userLikesStory(command);

        if (userExists.isBool() && !alreadyLikes.isBool()) {
            datastore.save(new Like(null,u.getId(),s.getId()));
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }

        return new Response(true, 0, u, null, null, null, null);
    }

    public static Response unlikeStory(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Likes");
        datastore.ensureIndexes();
        /////////////////////////////////

        User u = UserManagement.getUserHelper(command.getMainUser().getUsername());
        Story s = command.getStory();
        Response userExists = LoginsManagement.accountExists(command);
        Response alreadyLikes = userLikesStory(command);

        System.out.println(userExists.isBool());
        System.out.println(alreadyLikes.isBool());
        if (userExists.isBool() && alreadyLikes.isBool()) {
            datastore.delete(datastore.createQuery(Like.class).filter("userId =",u.getId()).filter("storyId =",s.getStoryId()));
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }

        return new Response(true, 0, u, null, null, null, null);
    }

    public static Response userLikesStory(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Likes");
        datastore.ensureIndexes();
        /////////////////////////////////

        User u = UserManagement.getUserHelper(command.getMainUser().getUsername());
        Story s = command.getStory();

        Query<Like> q = datastore.createQuery(Like.class).filter("userId =",u.getId()).filter("storyId =",s.getStoryId());
        Like l = q.get();

        if (l != null) {
            return new Response(true, 0, null, null, null, null, null);
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }
    }

    public static Response getLikeCount(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Likes");
        datastore.ensureIndexes();
        /////////////////////////////////

        Story s = command.getStory();

        Query<Like> q = datastore.createQuery(Like.class).filter("storyId =",s.getStoryId());
        int count = (int)q.countAll();

        return new Response(true, count, null, null, null, null, null);
    }
}
