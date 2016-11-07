package commands;

import com.mongodb.MongoClient;
import objects.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.query.Query;
import security.Security;

import java.util.Collection;
import java.util.List;

public class LoginsManagement {
    public static Response handleLogin(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Logins");
        datastore.ensureIndexes();
        /////////////////////////////////

        User oldUser = command.getMainUser();
        User user = new User(null, oldUser.getUsername(), oldUser.getRealname(), oldUser.getEmail(), Security.salt(oldUser.getPassword()), oldUser.getProfilePictureUrl(), oldUser.getBio());
        Response response = accountExists(command);

        // System.out.println(response.getUser().getUsername() + "awkjbfqjhgbwe");

        if (response.isBool()) {
            if (user.getPassword().equals(response.getUser().getPassword())) {
                return new Response(true, 0, response.getUser(), null, null, null, null);
            } else {
                return new Response(false, 0, null, null, null, null, null);
            }
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }
    }

    public static Response accountExists(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Logins");
        datastore.ensureIndexes();
        /////////////////////////////////

        User user = command.getMainUser();
        Query<User> q = datastore.createQuery(User.class).filter("username =", user.getUsername());
        user = q.get();

        if (user != null) {
            return new Response(true, 0, user, null, null, null, null);
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }
    }


    public static Response createAccount(Command command) {
        //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Logins");
        datastore.ensureIndexes();
        /////////////////////////////////

        User oldUser = command.getMainUser();
        User user = new User(null, oldUser.getUsername(), oldUser.getRealname(), oldUser.getEmail(), Security.salt(oldUser.getPassword()), oldUser.getProfilePictureUrl(), oldUser.getBio());
        Response response = accountExists(command);

        if (!response.isBool() && usernameIsValid(user.getUsername())) {
            datastore.save(user);
        } else {
            return new Response(false, 0, null, null, null, null, null);
        }

        return new Response(true, 0, user, null, null, null, null);
    }

    public static Response loginWithTwitter(Command command) {
        //TODO Zheng
        handleLogin(command);

       /* //morphia and data store setups//
        final Morphia morphia = new Morphia();
        morphia.map(User.class, Story.class, BasicStoryInfo.class);

        final Datastore datastore = morphia.createDatastore(new MongoClient(), "Logins");
        datastore.ensureIndexes();
        /////////////////////////////////
        //ifoauth succesfull
        if(true)
        handleLogin(command);
        //else return false
        else
        return new Response(false, 0, null, null, null, null);*/
        return new Response(true, 0, null, null, null, null, null);
    }

    public static Response loginWithGoogle(Command command) {

        handleLogin(command);
        //
        return null; //shouldmnt get in here!
    }

    //from matt, used for extra security
    private static boolean usernameIsValid(String username) {
        // return true iff username contains only alphanumeric, hyphens, and underscores.
        if (username.length() > 24 || username.length() < 4)
            return false;
        username = username.toLowerCase();
        for (int i = 0; i < username.length(); i++) {
            if (!((username.charAt(i) >= 'a' && username.charAt(i) <= 'z') ||
                    (username.charAt(i) >= '0' && username.charAt(i) <= '9') ||
                    username.charAt(i) == '_' || username.charAt(i) == '-')) {
                return false;
            }
        }
        return true;
    }

}
