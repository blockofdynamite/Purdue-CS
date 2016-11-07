/**
 * Created by jeff on 10/30/16.
 */
public class Result {
    private String link;
    private String image;
    private String desc;
    private String title;

    public Result(String link, String title, String image, String desc) {
        this.link = link;
        this.title = title;
        this.image = image;
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }
}
