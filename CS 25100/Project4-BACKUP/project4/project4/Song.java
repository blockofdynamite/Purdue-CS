/**
 * This is the song class.
 * We will be using this
 */

public class Song implements Comparable {

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
     * @param o - The song to compare
     * @return - 0 if same, -1 if o is less popular, 1 if o is more popular
     */
    @Override
    public int compareTo(Object o) {

        if (o.getClass() != Song.class) {
            throw new IllegalArgumentException();
        }

        return name.compareTo(((Song) o).getName());
    }
}
