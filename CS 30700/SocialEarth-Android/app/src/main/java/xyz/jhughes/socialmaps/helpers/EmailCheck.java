package xyz.jhughes.socialmaps.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailCheck {

    // The function below is very good and is taken from StackOverflow
    // http://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
    public static boolean emailIsValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]" +
                "{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = java.util.regex.Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
