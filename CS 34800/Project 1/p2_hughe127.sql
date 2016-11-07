rem CS348 SQL Project 12 
rem Jeffrey Hughes 
rem hughe127@purdue.edu

 -- PART 2 (30 points)

 -- Writing PL/SQL programs to process data. 
 -- You are going to use PL/SQL (Oracle's procedural extension to SQL) to write a few programs to process data. The result of this part of the project should be a file named p2_your_career_login.sql that will contain all the PL/SQL statements you develop to handle the data processing tasks described below. Your grade depends on how you use the PL/SQL statements and the result of running the .sql file. You will use the same database used in Part 1 of this project. It is required that you perform the following tasks: 

 -- Write a program that generates information about songs aggregated by their albums, print the output of your program in the following format: 
 -- This program returns a list of the albums and information about songs in the album. NumOfSongs indicates the number of songs in a certain album. NumOfMusicians indicates the number of musicians that performed in an album (if a musician performed for more than one song, count him/her once). NumOfLyricists indicates the number of distinct lyricists for an album who wrote the lyrics for at least one song of the album.

DECLARE
	message varchar2(75) := 'Album   NumOfSongs   NumOfMusicians   NumOfLyricists';
	message_line varchar2(75) := '----------------------------------------------------';
	CURSOR artist_count IS 
		WITH Song_Count_Table as (SELECT DISTINCT ANO, count(ANO) OVER (PARTITION BY ANO)
		  AS song_count FROM SONG ORDER BY ANO), Lyric_Count_Table as (SELECT DISTINCT ANO, count(DISTINCT LYRICIST) OVER
		  (PARTITION BY ANO) AS lyric_count FROM SONG ORDER BY ANO), Artist_Count_Table as
		  (SELECT DISTINCT ANO, count(DISTINCT MNO) OVER (PARTITION BY ANO) AS artist_count
		  FROM PERFORM FULL JOIN SONG ON PERFORM.SNO = SONG.SNO) SELECT DISTINCT ATITLE,
		  coalesce(song_count, 0) as song_count, coalesce(artist_count, 0) as artist_count, coalesce(lyric_count, 0) as lyric_count
		  FROM ALBUM FULL JOIN Song_Count_Table ON Song_Count_Table.ANO = ALBUM.ANO FULL JOIN Lyric_Count_Table
		  on Lyric_Count_Table.ANO = ALBUM.ANO FULL JOIN Artist_Count_Table on Artist_Count_Table.ANO = ALBUM.ANO ORDER BY ATITLE;

BEGIN
	dbms_output.Put_line(message);
	dbms_output.Put_line(message_line);

	FOR n IN artist_count LOOP
      dbms_output.put_line(n.ATITLE || ' ' || n.song_count || ' ' || n.artist_count || ' ' || n.lyric_count);
 	END LOOP;
END;
/

 -- Write a program that generates for each musician, say M, the top two musicians that performed with M in most of their albums. Print the output of your program in the following format: 
 -- This program returns the top two musicians who performed with a certain musician in most of their albums. MusicianName indicates the name of the musician and NumOfAlbums indicates the number of albums in which this musician performed with musician M.

 -- This gets the information for the artist chosen. You must put the MNO in the mno_selected area. 

DECLARE
	message varchar2(75) := 'MNO   MusicianName   NumOfAlbums ';
	message_line varchar2(75) := '----------------------------------------------------';

	mno_selected int := 1;
	
	CURSOR artists IS SELECT DISTINCT NAME, MNO FROM MUSICIAN ORDER BY MNO;

	CURSOR top IS 
		SELECT DISTINCT NAME, MUSICIAN.MNO as m_mno, PERFORM.MNO as p_mno, count(DISTINCT SONG.ANO) OVER (PARTITION BY MUSICIAN.NAME) as
  			count FROM MUSICIAN FULL JOIN ALBUM ON MUSICIAN.MNO = ALBUM.MNO FULL JOIN SONG ON ALBUM.ANO = SONG.ANO FULL JOIN
  			PERFORM ON SONG.SNO = PERFORM.SNO WHERE MUSICIAN.MNO != PERFORM.MNO AND MUSICIAN.MNO = mno_selected ORDER BY COUNT DESC;

	counter integer := 1;
	mainName varchar2(200);
BEGIN
	FOR n IN artists LOOP
		counter := counter + 1;
		IF (n.MNO = mno_selected) THEN
			dbms_output.Put_line('Musician MNO: ' || mno_selected);
			dbms_output.Put_line('Musician Name: ' || n.NAME || chr(10));
		END IF;
	END LOOP;

	dbms_output.Put_line(message);
	dbms_output.Put_line(message_line);

	counter := 1;

	FOR n IN top LOOP
		counter := counter + 1;

		FOR x IN artists LOOP
			IF (x.MNO = n.p_mno) THEN
				dbms_output.Put_line(n.p_mno || ' ' || x.NAME || ' ' || n.count || ' ');
			END IF;
		END LOOP;

		IF (counter = 3) THEN
			EXIT;
		END IF;
	END LOOP;

END;
/
