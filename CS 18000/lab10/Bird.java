/**
 * Created by jeff on 11/21/14.
 */
public class Bird extends Animal {
    public Bird(String status) {
        super(status);
    }

    @Override
    public void makeMove(SiteGrid sg) {
        try {
            if (site.getType().equals("WINTERING") && this.getStatus().equals("ALIVE")) {
                die();
            }
        } catch (NullPointerException e) {
            System.out.println("Status is defaulting to NOT WINTERING! NullPointerException was thrown.");
        }
        if (!this.getStatus().equals("DEAD")) {
            super.makeMove(sg);
        }
    }
}
