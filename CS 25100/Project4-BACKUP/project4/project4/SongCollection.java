import java.util.List;

public class SongCollection {
	// TODO Task 2
	// Organize all the songs here

	RedBlackTree redBlackTree = new RedBlackTree();
	int sizeOfTree;

	// add a song at date, with the name S{date}
	// and default L and N
	public boolean addSong(int date) {
		Song s = new Song(60, 20, 20, date);
		if (++sizeOfTree > 500) {
			return false;
		}
		redBlackTree.insert(s);
		return true;
	}

	// delete the n songs of lowest priorities
	public boolean deleteSong(int n) {
		return true;
	}

	// update song named by songName
	public void updateSong(String songName, int deltaN, int deltaL) {
		//redBlackTree.find();
	}

	// return the 3 most popular songs in the return value
	// with the most popular at index 0
	//      the second popular at index 1
	//      the third popular at index 2
	public List<Song> popular() {
		// TODO Task 3
		return null;
	}

	// return the popularity
	// of the most and least popular songs
	// the 0th integer in the returned object
	// should be the popularity of the most popular song
	// and the 1st integer should be the popularity of
	// the least popular song 
	public List<Integer> minMax() {
		// TODO Task 3
		return null;
	}
}