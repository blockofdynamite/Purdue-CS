package xyz.jhughes.socialmaps.helpers;

public class UsernameCheck {

    // return true iff username contains only alphanumeric, hyphens, and underscores.
    public static boolean usernameIsValid(String username) {
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
