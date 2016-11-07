-- 1
SELECT Name
FROM Movie;

-- 2
SELECT DISTINCT
  Role.Name,
  Movie.Name
FROM Role
  INNER JOIN Movie ON Movie.mid = Role.mid
  INNER JOIN Audition ON Role.rid = Audition.rid;

-- 3
SELECT DISTINCT Name
FROM ManagingDirector;

-- 4
SELECT DISTINCT
  ManagingDirector.Name,
  Movie.Name
FROM ManagingDirector
  INNER JOIN Role ON ManagingDirector.did = Role.did
  INNER JOIN Movie ON Role.mid = Movie.mid;

-- 5
SELECT DISTINCT
  ManagingDirector.Name,
  Role.Name,
  Movie.Name
FROM ManagingDirector
  INNER JOIN Role ON ManagingDirector.did = Role.did
  INNER JOIN Movie ON Role.mid = Movie.mid;

-- 6
SELECT DISTINCT
  Movie.Name,
  Actor.Name
FROM Role
  INNER JOIN Movie ON Movie.mid = Role.mid
  INNER JOIN Audition ON Role.rid = Audition.rid
  INNER JOIN Actor ON Audition.aid = Actor.aid
WHERE Audition.status = 2;

-- 7
SELECT
  Movie.Name,
  Role.Name
FROM Role
  INNER JOIN Movie ON Movie.mid = Role.mid
  INNER JOIN Audition ON Role.rid = Audition.rid
WHERE Audition.status = 0 OR Audition.status = 1;

-- 8
SELECT DISTINCT
  Movie.Name,
  Actor.Name,
  Audition.status
FROM Role
  INNER JOIN Movie ON Movie.mid = Role.mid
  INNER JOIN Audition ON Role.rid = Audition.rid
  INNER JOIN Actor ON Audition.aid = Actor.aid;

-- 9
SELECT DISTINCT
  Actor.Name,
  Role.Name,
  Movie.Name
FROM Role
  INNER JOIN Movie ON Movie.mid = Role.mid
  INNER JOIN Audition ON Role.rid = Audition.rid
  INNER JOIN Actor ON Audition.aid = Actor.aid;

-- 10
SELECT DISTINCT
  Actor.Name,
  ManagingDirector.Name
FROM Audition
  INNER JOIN Role ON Role.rid = Audition.rid
  INNER JOIN Actor ON Audition.aid = Actor.aid
  INNER JOIN ManagingDirector ON Role.did = ManagingDirector.did;