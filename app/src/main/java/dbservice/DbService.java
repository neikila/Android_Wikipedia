package dbservice;

import java.util.List;

import wikipedia.Article;

/**
 * Created by neikila on 16.11.15.
 */
public interface DbService {

    void clean();

    List<Article> getArticlesFromHistory();
    List<Article> getArticlesFromHistory(Integer length);

    List<Article> getSavedArticles();
    List<Article> getSavedArticles(Integer length);

    void saveArticleInHistory(Article article);

    void saveArticle(Article article);

    Article getArticleByTitle(String title);

    Article getRandomArticle();

    Article getArticleLikeByTitle(String title);
}
