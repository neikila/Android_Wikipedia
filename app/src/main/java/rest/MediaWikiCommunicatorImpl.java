package rest;

import retrofit.RestAdapter;
import retrofit.client.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasiliy on 12.12.15.
 */

//https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&titles=Far_Eastern_Party&pithumbsize=500&format=xml -возвращет ссылку на картинку соответсвуещего размера к статье
//размер можно задать

//https://en.wikipedia.org/w/api.php?action=query&prop=images&titles=Far_Eastern_Party&format=xml сылки на все картинки картинки

//https://en.wikipedia.org/w/api.php?action=query&prop=extlinks&titles=Far_Eastern_Party возвращает все внешние ссылки

//https://en.wikipedia.org/w/api.php?action=query&titles=Far_Eastern_Party&prop=revisions&rvprop=content&format=json
//https://en.wikipedia.org/w/api.php?action=query&titles=Far_Eastern_Party&prop=revisions&rvprop=content&format=xml
//https://ru.wikipedia.org/w/api.php?action=opensearch&search=%EC%E0%F1%F2%E5%F0%20%E8%20%EC%E0%F0%E3%E0%F0%E8%F2%E0&prop=info&format=xml&inprop=url
public class MediaWikiCommunicatorImpl implements MediaWikiCommunicator {
    private MediaWikiApi service;
    private RestAdapter retrofit;

    public MediaWikiCommunicatorImpl(){
        retrofit = new RestAdapter.Builder().setEndpoint("https://en.wikipedia.org/w/api.php").build();
        //retrofit = new RestAdapter.Builder().setEndpoint("https://upload.wikimedia.org").build();
        service = retrofit.create(MediaWikiApi.class);
    }

    //метод возвращает xml в котором есть тело вики-текста сайта
    @Override
    public String getArticleByTitle(String title) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("action", "query");
        parameters.put("titles", title);
        parameters.put("prop", "revisions");
        parameters.put("rvprop", "content");
        parameters.put("format", "json");
        Response tmp = service.getArticleByTitle(parameters);

        String result = stringFromResponse(tmp);
        return result;
    }

    //https://en.wikipedia.org/w/api.php?action=query&list=search&srwhat=text&srsearch=zorge&format=jsonfm&srlimit=50
    // метод возвращает заголовки страниц с кратким оисанием удволетворяющих поисковому запросу, 10 результатов,
    // также там можно найти заголовки по которым можно получить страницу используя метод  getArticleByTitle
    @Override
    public String getListOfArticle(String searchWords, Integer limit) {
        Map<String, String> parameters = new HashMap<String, String>();
        if ((limit <= 0) || (limit >= 51))
            limit = 10;
        parameters.put("action", "query");
        parameters.put("list", "search");
        parameters.put("srwhat", "text");
        parameters.put("format", "json");
        parameters.put("srsearch", searchWords);
        parameters.put("srlimit", limit.toString());

        Response tmp = service.getListOfArticle(parameters);
        String result = stringFromResponse(tmp);
        return result;
    }

    //https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&titles=Far_Eastern_Party&pithumbsize=500&format=xml
    @Override
    public String getRawLinkImageTitle(String title, Integer size) throws IOException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("action", "query");
        parameters.put("prop","pageimages");
        parameters.put("titles", title);
        parameters.put("pithumbsize", size.toString());
        parameters.put("format", "json");
        Response tmp = service.getRawLinkImageTitle(parameters);
        String result = stringFromResponse(tmp);
        return result;
    }





    protected String stringFromResponse(Response response){
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
