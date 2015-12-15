package dbservice;

import java.util.List;

import wikipedia.Article;

/**
 * Created by neikila on 16.11.15.
 */
public interface DbService {

    void clean();

    List<Article> getArticlesFromHistory();
    List<Article> getArticlesFromHistory(int length);

    List<Article> getSavedArticles();
    List<Article> getSavedArticles(int length);

    void saveArticleInHistory(Article article);

    void saveArticle(Article article);

    Article getArticleByTitle(String title);

    Article getRandomArticle();
}
