import javax.swing.*;

/**
 * CS 180 - Lab 08 - CollegeFeeCalculator
 * <p/>
 * A GUI application that calculates cost of college
 *
 * @author hughe127
 * @lab 802
 * @date 07/11/14
 */

public class SimpleGUIs {

    public static void getCost() {
        int totalCost = 0;
        JOptionPane.showMessageDialog(null, "Welcome to CollegeFeeCalculator!",
                "CollegeFeeCalculator", JOptionPane.INFORMATION_MESSAGE);
        String name = JOptionPane.showInputDialog(null, "Please enter your name, then press OK", "Name",
                JOptionPane.QUESTION_MESSAGE);
        while (name.equals("")) {
            name = JOptionPane.showInputDialog(null, "Please enter your name, then press OK", "Name",
                    JOptionPane.QUESTION_MESSAGE);
        }
        String[] enrollment = {"Full Time", "Part Time"};
        int typeOfEnrollment;
        boolean fullTime = false;
        int reply = JOptionPane.showOptionDialog(null, "Which type of student are you?",
                "Choose a type", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, enrollment, null);
        if (reply == 0) {
            typeOfEnrollment = 1;
            fullTime = true;
        } else  if (reply == 1) {
            typeOfEnrollment = 2;
        } else {
            throw new NullPointerException();
        }

        String enrollmentType;
        if (fullTime) {
            enrollmentType = "Full-time";
        } else {
            enrollmentType = "Part-time";
        }
        String hours = JOptionPane.showInputDialog(null,
                "Please enter the no. of credit hours, then press OK", "Credit Hours", JOptionPane.QUESTION_MESSAGE);
        if (hours == null) {
            throw new NullPointerException();
        }
        boolean matches = false;
        int numHours = 0;
        while (!matches) {
            try {
                numHours = Integer.parseInt(hours);
                if (numHours > 0) {
                    matches = true;
                    break;
                }
            } catch (NumberFormatException e) {
                matches = false;
            }
            JOptionPane.showMessageDialog(null, "Please enter a correct number of credit hours", "Invalid no. of credits",
                    JOptionPane.ERROR_MESSAGE);
            hours = JOptionPane.showInputDialog(null, "Please enter credit hours", "Credit Hours",
                    JOptionPane.QUESTION_MESSAGE);
            if (hours == null) {
                throw new NullPointerException();
            }
        }
        String[] residency = {"In-State", "Out-of-State", "International"};
        String residencyType = (String) JOptionPane.showInputDialog(null, "Enter residency type: ", "Residency",
                JOptionPane.QUESTION_MESSAGE, null, residency, null);

        if (residencyType == null) {
            throw new NullPointerException();
        }

        int residencyTypeInt;
        if (residencyType.equals(residency[0])) {
            residencyTypeInt = 0;
        } else if (residencyType.equals(residency[1])) {
            residencyTypeInt = 1;
        } else {
            residencyTypeInt = 2;
        }

        if (typeOfEnrollment == 1 && residencyTypeInt == 0) {
            totalCost += 4996;
        } else if (typeOfEnrollment == 1 && residencyTypeInt == 1) {
            totalCost += 9401 + 4996;
        } else if (typeOfEnrollment == 1 && residencyTypeInt == 2) {
            totalCost += 9401 + 4996 + 1000;
        } else if (typeOfEnrollment == 2 && residencyTypeInt == 0) {
            totalCost += 350 * numHours;
        } else if (typeOfEnrollment == 2 && residencyTypeInt == 1) {
            totalCost += (600 + 350) * numHours;
        } else if (typeOfEnrollment == 2 && residencyTypeInt == 2) {
            totalCost += (70 + 600 + 350) * numHours;
        }


        String[] housingTypes = {"On-campus", "Off-campus"};
        int housing = JOptionPane.showOptionDialog(null, "Which type of housing?",
                "Choose a type", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, housingTypes, null);
        String dorm = null;
        int housingCost = 0;
        if (housing == 0) {
            String[] dorms = {"Hillenbrand", "Owen", "Earhart", "Windsor"};
            dorm = (String) JOptionPane.showInputDialog(null, "Which dorm?",
                    "Choose a dorm", JOptionPane.QUESTION_MESSAGE, null, dorms, null);
            if (dorm.equals(dorms[0])) {
                totalCost += 5307;
                housingCost = 5307;
            } else if (dorm.equals(dorms[1])) {
                totalCost += 4130;
                housingCost = 4130;
            } else if (dorm.equals(dorms[2])) {
                totalCost += 4745;
                housingCost = 4745;
            } else  if (dorms.equals(dorms[3])) {
                totalCost += 4150;
                housingCost = 4150;
            } else {
                throw new NullPointerException();
            }
        } else if (housing == 1) {

        } else {
            throw new NullPointerException();
        }
        if (dorm == null) {
            JOptionPane.showMessageDialog(null, "Name: " + name + "\n" + "Credit hours: " + hours + "\n" +
                    "Enrollment: " + enrollmentType + "\n" + "Residency Type: "
                    + residencyType + "\n" + "Housing: Off-campus\n" + "Total cost: $" + totalCost
                    + "\n", "Cost Calculator", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Name: " + name + "\n" + "Credit hours: " + hours + "\n" +
                            "Enrollment: " + enrollmentType + "\n" + "Residency Type: " + residencyType + "\n"
                            + "Housing: $" + housingCost + "\n" + "Total cost: $" + totalCost + "\n", "Cost Calculator",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        int yes = 0;
        while (yes == 0) {
            try {
                getCost();
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Thank you for using the Cost Calculator!",
                        "Thank you!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            yes = JOptionPane.showConfirmDialog(null, "Would you like to run the cost calculator again?",
                    "Cost Calculator", JOptionPane.YES_NO_OPTION);
        }
    }
}
