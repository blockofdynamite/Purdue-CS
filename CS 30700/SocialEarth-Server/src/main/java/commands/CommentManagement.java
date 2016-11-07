package commands;

import com.mongodb.MongoClient;
import objects.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;
import java.util.ArrayList;
import java.util.List;

public class CommentManagement {

    // TODO Zheng
    public static Response getCommentsForStory(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class, Follow.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Comments");
        datastore.ensureIndexes();

        System.out.println("StoryId = " + command.getStory().getId());
        Query<Comment> q = datastore.createQuery(Comment.class).filter("storyId", command.getStory().getId());
        for (Comment comment : q.asList())
            System.out.println("Recovered id = " + comment.getId());
        return new Response(true,0,command.getMainUser(),command.getStory(),null,null,(ArrayList<Comment>) q.asList());

    }

    // TODO Nick
    public static Response postCommentInStory(Command command)
    {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class, Follow.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Comments");
        datastore.ensureIndexes();
        /////////////////////////////////
       // Query<Comment> q = datastore.createQuery(Comment.class).filter("storyId =",command.getStory().getStoryId());
       // int count = (int)q.countAll();
       // Comment newcomment =  new Comment(null,comment.getUserId(),comment.getStoryId(),comment.getCommentText(),comment.getDate(),count - 1);
       // datastore.save(newcomment);
        System.out.println("PostedId=" + command.getComment().getStoryId());
        datastore.save(command.getComment());
        return new Response(true,0,command.getMainUser(),command.getStory(),null,null,null);
    }

    // TODO Nick
    public static Response deleteComment(Command command)
    {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Comments");
        datastore.ensureIndexes();
        /////////////////////////////////

        datastore.delete(datastore.createQuery(Comment.class).filter("_id", new ObjectId(command.getComment().getId())));
        return new Response(true, 0, null, null, null, null, null);
    }
}
