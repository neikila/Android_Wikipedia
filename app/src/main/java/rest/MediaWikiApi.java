package rest;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;

import java.util.Map;

/**
 * Created by vasiliy on 12.12.15.
 */
public interface MediaWikiApi {

    @GET("/")
    Response getArticleByTitle(@QueryMap Map<String, String> parameters);


    @GET("/")
    Response getListOfArticle(@QueryMap Map<String, String> parameters);

}
