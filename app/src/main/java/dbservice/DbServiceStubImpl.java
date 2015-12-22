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
    List <Article> requestedArticles;
    Map<String, Article> articleMap;

    List <Article> savedArticles;
    Map<String, Article> savedArticleMap;

    public DbServiceStubImpl() {
        requestedArticles = new ArrayList<>();
        articleMap = new HashMap<>();

        savedArticles = new ArrayList<>();
        savedArticleMap = new HashMap<>();

        saveArticleInHistory(new Article("Test article", "Test article/1", "qwe.com/1"));
        saveArticle(new Article("Saved test article", "Saved test article/1", "qwe.com/2"));
    }

    @Override
    public void clean() {
        requestedArticles.clear();
    }

    @Override
    public List<Article> getArticlesFromHistory(Integer length) {
        return requestedArticles.subList(0, length);
    }

    @Override
    public List<Article> getArticlesFromHistory() {
        return getArticlesFromHistory(requestedArticles.size());
    }

    @Override
    public List<Article> getSavedArticles(Integer length) {
        return savedArticles.subList(0, length);
    }

    @Override
    public List<Article> getSavedArticles() {
        return getSavedArticles(savedArticles.size());
    }

    @Override
    public void saveArticleInHistory(Article article) {
        requestedArticles.add(article);
        articleMap.put(article.getTitle(), article);
    }

    @Override
    public void saveArticle(Article article) {
        savedArticles.add(article);
        savedArticleMap.put(article.getTitle(), article);
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
