package security;

import objects.Response;
import objects.User;
import org.mindrot.jbcrypt.BCrypt;

public class Security {
    private static String salt = "$2a$10$Sd3HO2YAt5R7MWzr5Zua0.";

    public static void removePasswords(Response response) {
        if (response != null && response.getUser() != null)
            response.getUser().removePassword();
        else if (response != null && response.getListOfUsers() != null)
            response.getListOfUsers().forEach(User::removePassword);
    }

    public static String salt(String password) {
        return BCrypt.hashpw(password, salt);
    }
}
