package utils;

import java.util.ArrayList;
import java.util.List;

import wikipedia.Article;

/**
 * Created by neikila on 15.12.15.
 */
public class ResultArticle extends OttoMessage {
    private List<Article> articles;

    public ResultArticle(Article article) {
        messageType = MessageType.ResultArticle;
        List<Article> list = new ArrayList<>();
        list.add(article);
        this.articles = list;
    }

    public ResultArticle(List<Article> articles) {
        this.articles = articles;
        messageType = MessageType.ResultArticle;
    }

    public Article getArticle() {
        return articles.get(0);
    }

    public List<Article> getArticles() {
        return articles;
    }
}
