package dbservice;

import java.util.List;

import wikipedia.Article;

/**
 * Created by neikila on 16.11.15.
 */
public interface DbService {

    void clean();

    List<Article> getArticlesNameFromHistory();
    List<Article> getArticlesNameFromHistory(int length);

    List<Article> getSavedArticlesName();
    List<Article> getSavedArticlesNames(int length);

    void saveArticleInHistory(Article article);

    void saveArticle(Article article);

    Article getArticleByTitle(String title);

    Article getRandomArticle();
}
