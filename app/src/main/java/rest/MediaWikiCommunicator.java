package rest;


import java.io.IOException;

/**
 * Created by vasiliy on 12.12.15.
 */
public interface MediaWikiCommunicator {

    String getArticleByTitle(String title) throws IOException;
}
