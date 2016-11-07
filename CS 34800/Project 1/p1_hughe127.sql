rem CS348 SQL Project 11 
rem Jeffrey Hughes 
rem hughe127@purdue.edu

-- PART 1 (70 points)

-- Queries

-- Write SQL queries for the following questions and run them on the Oracle system. If you are making any assumptions, state them clearly and document your queries.

-- 1. Find the names of musicians who cannot play 'Piano'.
SELECT DISTINCT Name FROM Musician WHERE
  Musician.MNO NOT IN (SELECT DISTINCT MNO FROM Play WHERE Instrument = 'Piano');

-- 2. Find the names of musicians who only use 'Piano' or 'Guitar' to perform.
SELECT DISTINCT Name FROM Musician WHERE
  Musician.MNO NOT IN (SELECT MNO FROM Perform WHERE Instrument != 'Piano' AND Instrument != 'Guitar')
  AND Musician.MNO IN (SELECT DISTINCT MNO FROM Perform);

-- 3. Find the phone numbers of all musicians who had used 'Guitar' to perform.
-- Since some Musicians have the same phone number, I have chosen to include name as well to distinguish them. 
SELECT DISTINCT Name, Phone FROM Musician WHERE
  Musician.MNO IN (SELECT DISTINCT MNO from Perform WHERE Instrument = 'Guitar');

-- 4. Find the ANOs of the albums which have a song performed by 'Guitar'.
SELECT DISTINCT ANO FROM SONG WHERE
  SNO IN (SELECT DISTINCT SNO FROM PERFORM WHERE Instrument = 'Guitar');

-- 5. Find the ANOs of the albums which only have songs performed by 'Guitar'.
SELECT ANO FROM (SELECT DISTINCT ANO, ATITLE, INSTRUMENT, count(DISTINCT INSTRUMENT) OVER (PARTITION BY ANO) as
  instrument_count FROM ALBUM FULL JOIN PERFORM ON ALBUM.MNO = PERFORM.MNO WHERE ALBUM.MNO IS NOT NULL) WHERE
  instrument_count = 1 AND INSTRUMENT = 'Guitar';

-- 6. Find the MNO of the musician who produces the largest number of albums.
WITH Album_Count_Table as (SELECT DISTINCT MNO, count(DISTINCT ATITLE) OVER (PARTITION BY MNO)
  AS album_count FROM ALBUM ORDER BY album_count ASC) SELECT MUSICIAN.MNO FROM MUSICIAN FULL JOIN
  Album_Count_Table on MUSICIAN.MNO = Album_Count_Table.MNO WHERE
  album_count = (SELECT max(album_count) FROM Album_Count_Table);

-- 7. Find the names of the musicians who can play all instruments.
WITH Instrument_Count_Table as (SELECT DISTINCT MNO, count(DISTINCT INSTRUMENT) OVER (PARTITION BY MNO)
  AS instrument_count FROM PLAY ORDER BY instrument_count ASC) SELECT DISTINCT NAME FROM MUSICIAN FULL JOIN
  Instrument_Count_Table on MUSICIAN.MNO = Instrument_Count_Table.MNO WHERE
  instrument_count = (SELECT max(instrument_count) FROM Instrument_Count_Table);

-- 8. Find the names of the musicians who can play only one instrument.
WITH Instrument_Count_Table as (SELECT DISTINCT MNO, count(DISTINCT INSTRUMENT) OVER (PARTITION BY MNO) 
  AS instrument_count FROM PLAY ORDER BY instrument_count ASC) SELECT NAME FROM MUSICIAN FULL JOIN 
  Instrument_Count_Table on MUSICIAN.MNO = Instrument_Count_Table.MNO WHERE instrument_count = 1;

-- 9. Find the names of the musicians who have never performed a song produced by other musicians.
 SELECT DISTINCT NAME FROM MUSICIAN WHERE MNO NOT IN (SELECT DISTINCT PERFORM.MNO FROM SONG FULL JOIN ALBUM ON SONG.ANO = ALBUM.ANO FULL JOIN
  PERFORM ON SONG.SNO = PERFORM.SNO WHERE ALBUM.MNO != PERFORM.MNO);

-- 10. Find the ANOs of the albums that have a song performed by other musicians (not the producer).
-- This is assuming that the producer is the album producer, aka the original artist and we're finding 
-- if other artists have performed songs off that album other than the producer. 
SELECT DISTINCT SONG.ANO FROM SONG FULL JOIN ALBUM ON SONG.ANO = ALBUM.ANO FULL JOIN
  PERFORM ON SONG.SNO = PERFORM.SNO WHERE ALBUM.MNO != PERFORM.MNO;
  
-- 11. Find the names of the musicians who have performed for albums that have a copyright before '03-Sep-1999'.
SELECT DISTINCT NAME FROM MUSICIAN WHERE MNO IN (SELECT MNO FROM ALBUM WHERE COPYRIGHT_DATE < '03-Sep-1999');

-- 12. Find the name of the musician who has the album with the maximum number of sold copies.
SELECT * FROM (SELECT MUSICIAN.NAME from Musician full join ALBUM on Musician.MNO = ALBUM.MNO WHERE
  COPY_SOLD IS NOT NULL ORDER BY COPY_SOLD DESC) WHERE ROWNUM = 1;

-- 13. Find the names of (distinct) musicians who can play 2 or more instruments.
WITH Instrument_Count_Table as (SELECT DISTINCT MNO, count(DISTINCT INSTRUMENT) OVER (PARTITION BY MNO)
  AS instrument_count FROM PLAY ORDER BY instrument_count ASC) SELECT DISTINCT NAME FROM MUSICIAN FULL JOIN
  Instrument_Count_Table on MUSICIAN.MNO = Instrument_Count_Table.MNO WHERE instrument_count >= 2;

-- 14. For each musician except 'Sid Vicious', print the musician's name, phone and average number of sold copies of albums produced by the musician. If the musician has not produced any album, print 0 for the average number of sold copies.
SELECT DISTINCT NAME, PHONE, AVG(COALESCE(COPY_SOLD, 0)) OVER (PARTITION BY ALBUM.MNO) as average FROM
  MUSICIAN LEFT JOIN ALBUM ON MUSICIAN.MNO = ALBUM.MNO WHERE NAME != 'Sid Vicious' ORDER BY average;
