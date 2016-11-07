public class Runner {
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                (new Crawler()).crawl();
            }
            (new Searcher(8080)).search();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
