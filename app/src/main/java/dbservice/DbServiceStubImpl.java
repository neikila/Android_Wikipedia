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

    List <String> savedArticles;
    Map<String, Article> savedArticleMap;

    public DbServiceStubImpl() {
        requestedArticles = new ArrayList<>();
        articleMap = new HashMap<>();

        savedArticles = new ArrayList<>();
        savedArticleMap = new HashMap<>();

        saveArticleInHistory(new Article("Test article", "Test article/1"));
        saveArticle(new Article("Saved test article", "Saved test article/1"));
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
    public List<String> getSavedArticlesNames(int length) {
        return savedArticles.subList(0, length);
    }

    @Override
    public List<String> getSavedArticlesName() {
        return getSavedArticlesNames(savedArticles.size());
    }

    @Override
    public void saveArticleInHistory(Article article) {
        requestedArticles.add(article.getTitle());
        articleMap.put(article.getTitle(), article);
    }

    @Override
    public void saveArticle(Article article) {
        savedArticles.add(article.getTitle());
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
