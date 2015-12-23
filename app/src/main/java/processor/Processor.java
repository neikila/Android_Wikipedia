package processor;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dbservice.DbService;
import dbservice.DbServiceImpl;
import rest.MediaWikiCommunicator;
import rest.MediaWikiCommunicatorImpl;
import retrofit.RetrofitError;
import ru.mail.park.android_wikipedia.R;
import utils.CustomSettings;
import wikipedia.Article;
import java.io.IOException;


/**
 * Created by neikila on 16.12.15.
 */
public class Processor {
    private MediaWikiCommunicator wiki = new MediaWikiCommunicatorImpl();
    private DbService dbService;
    private Context context;
    final private static String IMAGE_DIR = "imageDir";
    final private static String LOGO_DEF = "main";
    final private CustomSettings settings;

    public Processor(Context context) {
        this.context = context;
        dbService = new DbServiceImpl(context);
        settings = new CustomSettings(context);
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
        Bitmap logo;
        logo = loadImageFromStorage(article.getTitle(), article.getLogo());
        if (logo == null) {
            try {
                logo = loadImageFromWeb(article.getTitle());
            } catch (ParseException | RetrofitError e) {
                e.printStackTrace();
            }
        }
        article.setLogoBitmap(logo);
    }

    public Bitmap loadImageFromStorage(String article, String filename) {
        Bitmap bitmap = null;
        try {
            File directory = context.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
            File articleDirectory = new File(directory, article.replace(' ', '_'));
            bitmap = Picasso
                    .with(context)
                    .load(new File(articleDirectory, filename))
                    .resize(200, 200)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap loadImageFromWeb(String tile) throws ParseException {
        Bitmap bitmap = null;
        String rawLink = null;
        try {
            rawLink = wiki.getRawLinkImageTitle(tile, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String linkOnImage = "";

        try {
            linkOnImage = WikitextHandler.getLinkImageTitle(rawLink);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            bitmap = Picasso
                    .with(context)
                    .load(linkOnImage)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public String saveToInternalStorage(Bitmap bitmapImage, String articleName, String filename) {
        File directory = context.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
        File articleDirectory = new File(directory, articleName.replace(' ', '_'));
        articleDirectory.mkdir();
        // Create imageDir
        File myPath = new File(articleDirectory, filename);

        try {
            FileOutputStream fos = new FileOutputStream(myPath);
            try {
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null)
                    fos.close();
            }
        } catch (IOException e) {

        }
        return myPath.getAbsolutePath();
    }

    public void clean() {
        dbService.clean();
    }

    public Bitmap getDefaultBitmap() {
        Bitmap logo = null;
        try {
            logo = Picasso.with(context)
                    .load(R.drawable.wiki_icon)
                    .error(R.drawable.debug)
                    .resize(200, 200)
                    .get();
        } catch (IOException e) {
            Log.d("getBitmap", e.toString());
        }
        return logo;
    }

    public boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child: fileOrDirectory.listFiles())
                deleteRecursive(child);
        return fileOrDirectory.delete();
    }

    public void prepareTestData() {
        File directory = context.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
        deleteRecursive(directory);
        dbService.clean();
        Bitmap logo = null;
        try {
            logo = Picasso.with(context)
                    .load(R.drawable.test)
                    .resize(200, 200)
                    .get();
        } catch (IOException e) {
            Log.d("getBitmap", e.toString());
        }
        Article temp;

        temp = new Article("Test article", "profile.jpg", "qwe.com/1");
        dbService.saveArticleInHistory(temp);
        saveToInternalStorage(logo, temp.getTitle(), temp.getLogo());

        temp = new Article("Saved test article", "profile.jpg", "qwe.com/2");
        dbService.saveArticle(temp);
        saveToInternalStorage(logo, temp.getTitle(), temp.getLogo());

        temp = new Article("Test article1", "profile.jpg", "qwe.com1/1");
        dbService.saveArticleInHistory(temp);
        saveToInternalStorage(logo, temp.getTitle(), temp.getLogo());

        temp = new Article("Saved test article1", "profile.jpg", "qwe.com2/2");
        dbService.saveArticle(temp);
        saveToInternalStorage(logo, temp.getTitle(), temp.getLogo());
    }

    public List<Article> searchArticleByTitle(String title) throws RetrofitError {
        List<Article> list = new ArrayList<>();
        if (settings.getOfflineSettings()) {
            List<Article> temp = dbService.getArticleLikeByTitle(title);
            for (Article article: temp) {
                list.add(article);
            }
        } else {
            try {
                List<JSONObject> temp = WikitextHandler.getListOfArticleInGSON(wiki.getListOfArticle(title, -1));
                for (JSONObject obj : temp) {
                    list.add(new Article((String) obj.get(WikitextHandler.TITLE)));
                }
            } catch (IOException e) {
                // TODO error
            }
        }
        return list;
    }

    public void showToast(String path) {
        //создаем и отображаем текстовое уведомление
        try {
            Toast toast = Toast.makeText(context.getApplicationContext(),
                    path,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (NullPointerException e) {
            Log.d("hardcodeArticles", "Не так быстро!Я не успел сохранить статью!");
        }
    }

    public void saveInHistory(String title) {
        dbService.saveArticleInHistory(new Article(title, LOGO_DEF));
    }
}
