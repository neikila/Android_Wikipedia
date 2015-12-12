package rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import retrofit.RestAdapter;
import retrofit.client.Response;

/**
 * Created by vasiliy on 12.12.15.
 */
public class MediaWikiCommunicatorImpl implements MediaWikiCommunicator {
    private MediaWikiApi service;
    private RestAdapter retrofit;

    public MediaWikiCommunicatorImpl(){
        retrofit = new RestAdapter.Builder().setEndpoint("https://en.wikipedia.org/w/api.php").build();
        service = retrofit.create(MediaWikiApi.class);
    }

    @Override
    public String getArticleByTitle(String title) {
        Map<String, String> parameters = new HashMap<String, String>();
        //https://en.wikipedia.org/w/api.php?action=query&titles=Far_Eastern_Party&prop=revisions&rvprop=content&format=json
        parameters.put("action", "query");
        parameters.put("titles", title);
        parameters.put("prop", "revisions");
        parameters.put("rvprop", "content");
        parameters.put("format", "json");
        Response tmp = service.getArticleByTitle(parameters);

        String result = stringFromResponse(tmp);
        return result;
    }

    String stringFromResponse(Response response){
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(
                    response.getBody().in()));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = sb.toString();
        return result;
    }


}
