/**
 * CS 180 - Lab 02 - StringManipulator
 *
 * Generates an email
 *
 * @author Jeffrey Hughes
 *
 * @lab 802
 *
 * @date 9/12/14
 *
 */

import java.util.Scanner;

public class StringManipulator {

    public String makeUserName(String fullName) {

        String lowerCaseName = fullName.toLowerCase();
        int spaceLocation = lowerCaseName.indexOf(" ");
        lowerCaseName = lowerCaseName.replaceAll("\\s","");
        String userName = lowerCaseName.charAt(0) + lowerCaseName.substring(spaceLocation);

        return userName;
    }

    public String makeEmail(String userName, String domain) {
        userName = userName.toLowerCase();
        domain = domain.toLowerCase();

        return userName + "@" + domain;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter the name of the person. Please put in first name then last name: ");
        String name = in.nextLine();

        System.out.println("Please enter the domain: ");
        String domain = in.nextLine();

        StringManipulator banana = new StringManipulator();

        String userName = banana.makeUserName(name);
        String email = banana.makeEmail(userName, domain);

        System.out.println("Your username is: " + userName);
        System.out.println("Your email is: " + email);
    }

}
