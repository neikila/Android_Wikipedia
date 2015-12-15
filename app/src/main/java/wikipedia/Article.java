package wikipedia;

/**
 * Created by neikila on 16.11.15.
 */
public class Article {
    private String title;
    private String logo = "";

    public Article(String title, String logo) {
        this.title = title;
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public String getLogo() {
        return logo;
    }
}
