public class Song implements Comparable<Song> {

    private int popularity;
    private int listens;
    private int likes;
    private int date;
    private String name;

    /**
     * Constructor.
     *
     * @param popularity
     * @param listens
     * @param likes
     * @param date
     */
    public Song(int popularity, int listens, int likes, int date) {
        this.popularity = popularity;
        this.listens = listens;
        this.likes = likes;
        this.date = date;
        this.name = "S" + date;
    }

    /**
     * This constructor is for when we're only given the name;
     *
     * @param name
     */
    public Song(String name) {
        this.popularity = 60;
        this.listens = 20;
        this.likes = 20;
        this.date = Integer.parseInt(name.substring(1, name.length()));
        this.name = name;
    }

    //GETTERS AND SETTERS

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getListens() {
        return listens;
    }

    public void setListens(int listens) {
        this.listens = listens;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is designed to determine where to put it in the RedBlackBST
     *
     * @param song - The song to compare
     * @return - difference in name
     */
    @Override
    public int compareTo(Song song) {
        return name.compareTo(song.getName());
    }
}
