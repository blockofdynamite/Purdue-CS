package mongoSimplification;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class Mongo {
    public static Datastore getDatastore(String database, Class... classes) {
        final Morphia morphia = new Morphia();
        morphia.map(classes);
        final Datastore datastore = morphia.createDatastore(new MongoClient(), database);
        datastore.ensureIndexes();
        return datastore;
    }
}
