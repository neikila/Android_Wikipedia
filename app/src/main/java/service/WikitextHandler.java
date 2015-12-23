package service;

import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasiliy on 16.12.15.
 */
public class WikitextHandler {
    public static final String TITLE = "title";
    public static final String QUERY = "query";
    public static final String SEARCH = "search";
    public static final String SNIPPET = "snippet";
    public static final String PAGES = "pages";
    public static final String REVISIONS = "revisions";
    public static final String THUMBNAIL = "thumbnail";
    public static final String SOURCE = "source";

    //краткая информация, каждый элемент JSONObject из которого можно достать snippet,title, F.e. listOfArticles.get(0).get("title")
    public static List<JSONObject> getListOfArticleInGSON(String ListArticles){
        JSONParser parser = new JSONParser();
        Object obj = null;
        ArrayList<JSONObject> listOfArticles = new ArrayList<JSONObject>();
        try {
            obj = parser.parse(ListArticles);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        JSONObject jsonQueryKey = (JSONObject) jsonObj.get(QUERY);
        JSONArray jsonSearchKey = (JSONArray) jsonQueryKey.get(SEARCH);
        for (int k = 0; k < jsonSearchKey.size(); k++) {
            JSONObject jsonSearchElement = (JSONObject) jsonSearchKey.get(k);
            JSONObject tmp = new JSONObject();
            tmp.put(TITLE, jsonSearchElement.get(TITLE));

            WikiModel wikiModel = new WikiModel("https://ru.wikipedia.org/wiki/${image}", "https://ru.wikipedia.org/wiki/${title}");

            String plainStr = wikiModel.render(new PlainTextConverter(), (String) jsonSearchElement.get(SNIPPET));
            tmp.put(SNIPPET, plainStr);

            listOfArticles.add(tmp);
        }
        //System.out.print(listOfArticles.get(0).get("snippet"));
        return listOfArticles;
    }

    private static String getWikiText(String rawWikiText) throws ParseException{
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(rawWikiText);
        JSONObject jsonObj = (JSONObject) obj;
        JSONObject jsonQueryKey = (JSONObject) jsonObj.get(QUERY);
        JSONObject jsonPagesKey = (JSONObject) jsonQueryKey.get(PAGES);
        Object[] idPage = jsonPagesKey.keySet().toArray();
        String idP = idPage[0].toString();
        JSONObject jsonIdPageKey = (JSONObject) jsonPagesKey.get(idP);
        JSONArray jsonRevisionsKey = (JSONArray) jsonIdPageKey.get(REVISIONS);
        JSONObject jsonWikiInformation = (JSONObject) jsonRevisionsKey.get(0);
        String WikiText = (String) jsonWikiInformation.get("*");
        return WikiText;
    }

    public static String getHTMLfromWikiText(String rawWikiText) throws ParseException {
        String WikiText = getWikiText(rawWikiText);
        WikiModel wikiModel = new WikiModel("https://ru.wikipedia.org/wiki/${image}", "https://ru.wikipedia.org/wiki/${title}");
        String htmlText = wikiModel.toHtml(WikiText);
        return htmlText;
    }

    public static String getPlainTextFromWikiText(String rawWikiText) throws ParseException {
        String WikiText = getWikiText(rawWikiText);

        WikiModel wikiModel = new WikiModel("https://ru.wikipedia.org/wiki/${image}", "https://ru.wikipedia.org/wiki/${title}");
        String plainStr = wikiModel.render(new PlainTextConverter(), WikiText);

        return plainStr;

    }

    public static String getLinkImageTitle(String rawWikiText) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(rawWikiText);
        JSONObject jsonObj = (JSONObject) obj;
        JSONObject jsonQueryKey = (JSONObject) jsonObj.get(QUERY);
        JSONObject jsonPagesKey = (JSONObject) jsonQueryKey.get(PAGES);
        Object[] idPage = jsonPagesKey.keySet().toArray();
        String idP = idPage[0].toString();
        JSONObject jsonIdPageKey = (JSONObject) jsonPagesKey.get(idP);
        JSONObject jsonThumbnailKey = (JSONObject) jsonIdPageKey.get(THUMBNAIL);
        String link = jsonThumbnailKey.get(SOURCE).toString();
        return link;
    }
}
