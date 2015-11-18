package dbservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import wikipedia.Article;

/**
 * Created by neikila on 16.11.15.
 */
public class DbServiceStubImpl implements DbService {
    List <String> requestedArticles;
    Map<String, Article> articleMap;

    public DbServiceStubImpl() {
        requestedArticles = new ArrayList<>();
        articleMap = new HashMap<>();

        Article article = new Article("Test article");
        saveArticle(article);
    }

    @Override
    public void clean() {
        requestedArticles.clear();
    }

    @Override
    public List<String> getArticlesNameFromHistory(int length) {
        return requestedArticles.subList(0, length);
    }

    @Override
    public List<String> getArticlesNameFromHistory() {
        return getArticlesNameFromHistory(requestedArticles.size());
    }

    @Override
    public void saveArticle(Article article) {
        requestedArticles.add(article.getTitle());
        articleMap.put(article.getTitle(), article);
    }

    @Override
    public Article getArticleByTitle(String title) {
        return articleMap.get(title);
    }

    @Override
    public Article getRandomArticle() {
        Random random = new Random();
        return new ArrayList<>(articleMap.values()).get(random.nextInt(articleMap.size()));
    }
}
