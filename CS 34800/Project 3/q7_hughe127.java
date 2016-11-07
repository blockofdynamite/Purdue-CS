/*

JVM Version: 1.8.0_92

Compile with:

javac q7_hughe127.java
java -cp .:/p/oracle12c/ojdbc7.jar q7_hughe127 yourusername yourpassword

If not running under CS Unix, get ojdbc7.jar from:
http://www.cs.purdue.edu/~clifton/cs348/protected/ojdbc7.jar (on campus), or
http://www.oracle.com/technetwork/database/features/jdbc/jdbc-drivers-12c-download-1958347.html

*/

import java.sql.*;

class q7_hughe127 {
  public static void main(String[] args) {
      try {
          Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@claros.cs.purdue.edu:1524:strep", args[0], args[1]);

          PreparedStatement ps = conn.prepareStatement("SELECT First, Last FROM TA WHERE COURSE = ?");
          ps.setString(1, args[2]);
          ResultSet rs = ps.executeQuery();

          if (!rs.next()) {
              System.out.println("Course not found in DB.");
              return;
          }
          do {
              System.out.println(rs.getString(1) + " " + rs.getString(2));
          } while (rs.next());

          PreparedStatement salary = conn.prepareStatement("SELECT SUM(SALARY) FROM TA WHERE COURSE = ?");
          salary.setString(1, args[2]);
          ResultSet salaryRS = salary.executeQuery();

          if (salaryRS.next()) {
              System.out.println("Total salary: " + salaryRS.getInt(1));
          }

          PreparedStatement prepareToUp = conn.prepareStatement("SELECT Enrollment, count(Email) FROM TA WHERE COURSE = ? GROUP BY Enrollment");
          prepareToUp.setString(1, args[2]);
          ResultSet prepareToUpRS = prepareToUp.executeQuery();
          if (prepareToUpRS.next()) {
              int numberTAs = prepareToUpRS.getInt(2);
              int enrollment = prepareToUpRS.getInt(1);
              if (enrollment / numberTAs > 50) {
                  PreparedStatement salaryUp = conn.prepareStatement("SELECT EMAIL, STANDING FROM TA WHERE COURSE = ?");
                  salaryUp.setString(1, args[2]);
                  ResultSet salaryUpRS = salaryUp.executeQuery();
                  while (salaryUpRS.next()) {
                      if (salaryUpRS.getString(2).toLowerCase().equals("excellent") || salaryUpRS.getString(2).toLowerCase().equals("good")) {
                          PreparedStatement updateSalary = conn.prepareStatement("UPDATE EARNINGS SET SALARY = SALARY * 1.05 WHERE ? = EMAIL");
                          updateSalary.setString(1, salaryUpRS.getString(1));
                          updateSalary.execute();
                      }
                  }
              }
          }

      } catch (SQLException s) {
          s.printStackTrace();
      }
  }
}
