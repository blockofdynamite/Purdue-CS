-- Actors Table

CREATE TABLE HUGHE127.Actor
(
  aid  NUMBER        NOT NULL,
  Name VARCHAR2(200) NOT NULL
);
CREATE UNIQUE INDEX "Actor_aid_uindex"
  ON Actor (aid);
ALTER TABLE Actor
  ADD (CONSTRAINT Actor_pk PRIMARY KEY (aid));
CREATE SEQUENCE Actor_seq START WITH 1;

INSERT INTO Actor VALUES (Actor_seq.nextval, 'Matt Damon');
INSERT INTO Actor VALUES (Actor_seq.nextval, 'Johnny Depp');
INSERT INTO Actor VALUES (Actor_seq.nextval, 'Robert Downey Jr');
INSERT INTO Actor VALUES (Actor_seq.nextval, 'Leonardo DiCaprio');

SELECT *
FROM Actor;

-- Auditions Table
-- 0 = no, 1 = ongoing, 2 = yes

CREATE TABLE HUGHE127.Audition
(
  rid    INT,
  aid    INT,
  status NUMBER DEFAULT 0,
  CONSTRAINT Audition_rid_aid_pk PRIMARY KEY (rid, aid),
  FOREIGN KEY (aid) REFERENCES Actor(aid)
);

INSERT INTO Audition VALUES (1, 2, 2);
INSERT INTO Audition VALUES (2, 4, 0);
INSERT INTO Audition VALUES (3, 3, 1);

SELECT *
FROM Audition;

-- ManagingDirectors Table

CREATE TABLE HUGHE127.ManagingDirector
(
  did  NUMBER NOT NULL,
  Name VARCHAR2(200)
);
CREATE UNIQUE INDEX "ManagingDirector_did_uindex"
  ON HUGHE127.ManagingDirector (did);
ALTER TABLE ManagingDirector
  ADD (CONSTRAINT MD_pk PRIMARY KEY (did));
CREATE SEQUENCE MD_seq START WITH 1;

INSERT INTO ManagingDirector VALUES (MD_seq.nextval, 'Martin Scorsese');
INSERT INTO ManagingDirector VALUES (MD_seq.nextval, 'Steven Spielberg');
INSERT INTO ManagingDirector VALUES (MD_seq.nextval, 'Quentin Tarantino');
INSERT INTO ManagingDirector VALUES (MD_seq.nextval, 'James Cameron');

SELECT *
FROM ManagingDirector;

-- Movies Table
-- Animated: yes = 1, no = 0

CREATE TABLE HUGHE127.Movie
(
  mid      NUMBER           NOT NULL,
  Name     VARCHAR2(200)    NOT NULL,
  Animated NUMBER DEFAULT 0 NOT NULL
);
CREATE UNIQUE INDEX "Movie_mid_uindex"
  ON HUGHE127.Movie (mid);
ALTER TABLE Movie
  ADD (CONSTRAINT Movie_pk PRIMARY KEY (mid));
CREATE SEQUENCE Movie_seq START WITH 1;

INSERT INTO Movie VALUES (Movie_seq.nextval, 'Inception', 0);
INSERT INTO Movie VALUES (Movie_seq.nextval, 'Alice in Wonderland', 1);
INSERT INTO Movie VALUES (Movie_seq.nextval, 'Iron Man 3', 0);
INSERT INTO Movie VALUES (Movie_seq.nextval, 'Jason Bourne', 0);
INSERT INTO Movie VALUES (Movie_seq.nextval, 'Princess Mononoke', 1);

SELECT *
FROM Movie;

-- Roles Table

CREATE TABLE HUGHE127.Role
(
  rid  NUMBER        NOT NULL,
  did  INT           NOT NULL,
  mid  INT           NOT NULL,
  Name VARCHAR2(200) NOT NULL,
  FOREIGN KEY (did) REFERENCES ManagingDirector(did),
  FOREIGN KEY (mid) REFERENCES Movie(mid)
);
CREATE UNIQUE INDEX "Roles_rid_uindex"
  ON HUGHE127.Role (rid);
ALTER TABLE Role
  ADD (CONSTRAINT Role_pk PRIMARY KEY (rid));
CREATE SEQUENCE Role_seq START WITH 1;

INSERT INTO Role VALUES (Role_seq.nextval, 1, 2, 'Alice');
INSERT INTO Role VALUES (Role_seq.nextval, 1, 2, 'Mr Hatter');

SELECT *
FROM Role;