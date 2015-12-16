package processor;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import dbservice.DbService;
import dbservice.DbServiceImpl;
import ru.mail.park.android_wikipedia.R;
import wikipedia.Article;

/**
 * Created by neikila on 16.12.15.
 */
public class Processor {
    private DbService dbService;
    private Context context;

    public Processor(Context context) {
        this.context = context;
        dbService = new DbServiceImpl(context);
    }

    public Article getArticleByTitle(String title) {
        return dbService.getArticleByTitle(title);
    }

    public Article getRandomArticle() {
        return dbService.getRandomArticle();
    }

    public List<Article> getHistory(int amount) {
        if (amount > 0) {
            return dbService.getArticlesFromHistory(amount);
        } else {
            return dbService.getArticlesFromHistory();
        }
    }

    public List<Article> getSaved(int amount) {
        if (amount > 0) {
            return dbService.getSavedArticles(amount);
        } else {
            return dbService.getSavedArticles();
        }
    }

    public void setBitmap(List<Article> list) {
        for (Article article: list) {
            setBitmap(article);
        }
    }

    public void setBitmap(List<Article> list, Runnable afterAction) {
        for (Article article: list) {
            setBitmap(article);
            afterAction.run();
        }
    }

    private void setBitmap(Article article) {
        try {
            Bitmap logo = Picasso.with(context)
                    .load(R.drawable.test)
                    .placeholder(R.drawable.test1)
                    .error(R.drawable.test1)
                    .resize(200, 200)
                    .get();
            article.setLogoBitmap(logo);
        } catch (IOException e) {
            Log.d("getBitmap", e.toString());
        }
    }

    public void clean() {
        dbService.clean();
    }

    public Bitmap getDefaultBitmap() {
        Bitmap logo = null;
        try {
            logo = Picasso.with(context)
                    .load(R.drawable.test1)
                    .placeholder(R.drawable.test1)
                    .error(R.drawable.test1)
                    .resize(200, 200)
                    .get();
        } catch (IOException e) {
            Log.d("getBitmap", e.toString());
        }
        return logo;
    }
}
