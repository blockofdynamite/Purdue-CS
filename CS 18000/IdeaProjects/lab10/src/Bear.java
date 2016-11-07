/**
 * Created by jeff on 11/21/14.
 */
public class Bear extends Animal {
    public Bear(String status) {
        super(status);
    }

    @Override
    public void makeMove(SiteGrid sg) {
        if (site.getType().equals("WINTERING") && this.getStatus().equals("ALIVE")) {
            if (Math.random() <= .3) {
                die();
            }
            if (!this.getStatus().equals("DEAD")) {
                super.makeMove(sg);
            }
        }
    }
}
