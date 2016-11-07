package commands;


import com.mongodb.MongoClient;
import objects.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;


public class ProfileManagement {

    public static Response updateBioAndProfilePicture(Command command) {
        //TODO Zheng

        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Logins");
        datastore.ensureIndexes();
        /////////////////////////////////

        UpdateOperations<User> ops1;
        UpdateOperations<User> ops2;
        User user = command.getMainUser();
        String id = user.getId();
        String Bio = command.getMainUser().getBio();
        String profilePicURL = command.getMainUser().getProfilePictureUrl();
        Query<User> updateQuery1 = datastore.createQuery(User.class).filter("username =", user.getUsername());
        Query<User> updateQuery2 = datastore.createQuery(User.class).filter("username =", user.getUsername());

        System.out.println(updateQuery1.get().getUsername());

        ops1 = datastore.createUpdateOperations(User.class).set("bio", Bio);
        datastore.update(updateQuery1, ops1);
        ops2 = datastore.createUpdateOperations(User.class).set("profilePictureUrl", user.getProfilePictureUrl());
        datastore.update(updateQuery2, ops2);
        //datastore.update(Bio);
        return new Response(true, 0, null, null, null, null, null);
       // return null;
    }
}
