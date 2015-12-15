package utils;

import wikipedia.Article;

/**
 * Created by neikila on 15.12.15.
 */
public class ResultArticle extends OttoMessage {
    private Article article;

    public ResultArticle(Article article) {
        messageType = MessageType.ResultArticle;
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }
}
