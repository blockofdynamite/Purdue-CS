package xyz.jhughes.socialmaps.helpers;

public class PasswordCheck {
    private static boolean containsNumber(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) >= '0' && password.charAt(i) <= '9') {
                return true;
            }
        }
        return false;
    }

    public static boolean passwordIsStrong(String password) {
        return password.length() >= 6 && password.length() <= 30 &&
                ! password.equals(password.toLowerCase()) &&
                ! password.equals(password.toUpperCase()) &&
                containsNumber(password);
    }
}
