CREATE VIEW TACOURSE AS
  SELECT
    First,
    Last,
    Salary.Course,
    Enrollment
  FROM Name
    FULL OUTER JOIN Salary ON NAME.EMAIL = Salary.Email
    FULL OUTER JOIN Enrollment ON Salary.Course = ENROLLMENT.COURSE;

CREATE VIEW TA AS
  SELECT
    Name.Email,
    First,
    Last,
    Salary.Course,
    Salary,
    Standing,
    Enrollment
  FROM Name
    FULL OUTER JOIN Salary ON NAME.EMAIL = Salary.Email
    FULL OUTER JOIN Enrollment ON Salary.Course = ENROLLMENT.COURSE;
