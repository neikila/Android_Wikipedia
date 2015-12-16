package wikipedia;

import android.graphics.Bitmap;

/**
 * Created by neikila on 16.11.15.
 */
public class Article {
    private String title;
    private String logo;
    private String body = "";
    private String link;
    private Bitmap logoBitmap;

    public Article(String title, String logo, String link) {
        this.title = title;
        this.logo = logo;
        this.link = link;
        this.logoBitmap = null;
    }

    public Article(String title, String logo, String link, String body) {
        this.title = title;
        this.logo = logo;
        this.link = link;
        this.body = body;
        this.logoBitmap = null;
    }

    public void setLogoBitmap(Bitmap bitmap) {
        logoBitmap = bitmap;
    }

    public Bitmap getLogoBitmap() {
        return logoBitmap;
    }

    public void recycle() {
        logoBitmap.recycle();
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getLogo() {
        return logo;
    }

    public String getLink() {
        return link;
    }
}
