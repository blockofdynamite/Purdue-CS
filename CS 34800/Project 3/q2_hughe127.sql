CREATE TABLE Name
(
  Email    VARCHAR2(500) PRIMARY KEY NOT NULL,
  First    VARCHAR2(500)             NOT NULL,
  Last     VARCHAR2(500)             NOT NULL,
  Standing VARCHAR2(500)             NOT NULL
);

INSERT INTO Name VALUE (SELECT UNIQUE
                          Email,
                          FirstName,
                          LastName,
                          Standing
                        FROM TA);

CREATE TABLE Enrollment
(
  Course     VARCHAR2(500) PRIMARY KEY NOT NULL,
  Enrollment NUMBER DEFAULT 0          NOT NULL
);

INSERT INTO Enrollment VALUE (SELECT UNIQUE
                                Course,
                                Enrollment
                              FROM TA);

CREATE TABLE Salary
(
  Email  VARCHAR2(500)    NOT NULL,
  Course VARCHAR2(500)    NOT NULL,
  Salary NUMBER DEFAULT 0 NOT NULL,
  CONSTRAINT Salary_NAME_EMAIL_fk FOREIGN KEY (Email) REFERENCES NAME (EMAIL),
  CONSTRAINT Salary_ENROLLMENT_COURSE_fk FOREIGN KEY (Course) REFERENCES ENROLLMENT (COURSE)
);

INSERT INTO Salary VALUE (SELECT
                            Email,
                            Course,
                            Salary
                          FROM TA);
