package dbservice;

import java.util.List;

import wikipedia.Article;

/**
 * Created by neikila on 16.11.15.
 */
public interface DbService {

    void clean();

    List<String> getArticlesNameFromHistory();
    List<String> getArticlesNameFromHistory(int length);

    void saveArticle(Article article);

    Article getArticleByTitle(String title);

    Article getRandomArticle();
}
