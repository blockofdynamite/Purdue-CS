import java.util.ArrayList;
import java.util.List;

public class SongCollection {
    // Organize all the songs here

    //RedBlack tree sorted using names of songs
    private RedBlackBST<String, Song> redBlackTreeName = new RedBlackBST<>();

    //RedBlack tree sorted using popularity and names of songs
    private RedBlackBST<String, Song> redBlackTreePop = new RedBlackBST<>();

    //Keeping track of how many songs
    private int sizeOfTree;

    //Keep track of whether we've hit our initial three songs
    private boolean overThreeInitial = false;

    //Min popularity song
    private Song minSong;

    //Max popularity song
    private Song maxSong;

    //Top three
    private ArrayList<Song> maxSongs = new ArrayList<>();

    // add a song at date, with the name S{date}
    // and default L and N
    public boolean addSong(int date) {
        //Create new song with default properties
        Song s = new Song(60, 20, 20, date);

        //Checking to see if tree is over 500 songs
        if (++sizeOfTree > 500) {
            //Let caller know it's over <s>9000!!</s> 500
            return false;
        }

        //Checking if we've hit our initial 3 songs
        if (sizeOfTree > 3) {

            overThreeInitial = true;

            //Set min and max global variables for songs
            setMinAndMaxSongs();
        }

        //Add to name-based tree
        redBlackTreeName.put(s.getName(), s);

        //Add to pop-based tree
        redBlackTreePop.put(String.format("%05d", s.getPopularity()) + "-" + s.getName(), s);

        //Print out
        System.out.printf("%s: N=%d, L=%d, pop=%d\n", s.getName(), s.getListens(), s.getLikes(), s.getPopularity());

        //Let the caller know it was successful.
        return true;
    }

    // delete the n songs of lowest priorities
    public boolean deleteSong(int n) {
        //Checking to make sure we don't go under three songs
        if (sizeOfTree - n < 3 && overThreeInitial) {
            //Let caller know we failed
            return false;
        }

        //for all the songs we need to delete
        for (int i = 0; i < n; i++) {
            //get name of minimum popularity from the pop tree
            String songName = redBlackTreePop.min();

            //get it from the name tree
            Song s = redBlackTreeName.get(songName.substring(songName.lastIndexOf("-") + 1));

            //print out the song
            System.out.printf("%s deleted, pop %d\n", s.getName(), s.getPopularity());

            //Delete it from the name tree
            redBlackTreeName.delete(s.getName());

            //Delete it from the pop tree
            redBlackTreePop.delete(String.format("%05d", s.getPopularity()) + "-" + s.getName());
        }

        //Set min and max global variables for songs
        setMinAndMaxSongs();

        //Let caller know we're finished and successful.
        return true;
    }

    // update song named by songName
    public void updateSong(String songName, int deltaN, int deltaL) {
        //Get the song from the name tree
        Song s = redBlackTreeName.get(songName);

        //Delete it from the pop tree
        redBlackTreePop.delete(String.format("%05d", s.getPopularity()) + "-" + s.getName());

        //Set new params for the song
        s.setLikes(s.getLikes() + deltaL);
        s.setListens(s.getListens() + deltaN);
        s.setPopularity(s.getLikes() * 2 + s.getListens());

        //Print out the details
        System.out.printf("%s: N=%d, L=%d, pop=%d\n", s.getName(), s.getListens(), s.getLikes(), s.getPopularity());

        //Re-add the song to the pop tree with the new name
        redBlackTreePop.put(String.format("%05d", s.getPopularity()) + "-" + s.getName(), s);

        //Set the global min and max songs
        setMinAndMaxSongs();
    }

    // return the 3 most popular songs in the return value
    // with the most popular at index 0
    //      the second popular at index 1
    //      the third popular at index 2
    public List<Song> popular() {
        return maxSongs;
    }

    // return the popularity
    // of the most and least popular songs
    // the 0th integer in the returned object
    // should be the popularity of the most popular song
    // and the 1st integer should be the popularity of
    // the least popular song
    public List<Integer> minMax() {
        ArrayList<Integer> toReturn = new ArrayList<>();
        toReturn.add(minSong.getPopularity());
        toReturn.add(maxSong.getPopularity());
        return toReturn;
    }

    //Set the global variables for max, top 3, and min songs by pop
    private void setMinAndMaxSongs() {
        //get name of minimum popularity from the pop tree
        String songName = redBlackTreePop.min();

        //get it from the name tree and set it to the global variable for minimum
        minSong = redBlackTreeName.get(songName.substring(songName.lastIndexOf("-") + 1));

        //get name of max pop from the pop tree;
        String maxSongName = redBlackTreePop.max();

        //Get it from the name tree and set it to the global variable for maximum
        maxSong = redBlackTreeName.get(maxSongName.substring(maxSongName.lastIndexOf("-") + 1));

        maxSongs = redBlackTreePop.top3();
    }
}