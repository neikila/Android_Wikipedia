package service;

import info.bliki.wiki.model.WikiModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by vasiliy on 16.12.15.
 */
public class WikitextHandler {

    public static JSONObject getListOfArticleInGSON(String ListArticles){
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(ListArticles);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        return (JSONObject) jsonObj.get("query");
    }

    public static String getHTMLfromWikiText(String rawWikiText) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(rawWikiText);
        JSONObject jsonObj = (JSONObject) obj;
        JSONObject jsonQueryKey = (JSONObject) jsonObj.get("query");
        JSONObject jsonPagesKey = (JSONObject) jsonQueryKey.get("pages");
        Object[] idPage = jsonPagesKey.keySet().toArray();
        String idP = idPage[0].toString();
        JSONObject jsonIdPageKey = (JSONObject) jsonPagesKey.get(idP);
        JSONArray jsonRevisionsKey = (JSONArray) jsonIdPageKey.get("revisions");
        JSONObject jsonWikiInformation = (JSONObject) jsonRevisionsKey.get(0);
        String WikiText = (String ) jsonWikiInformation.get("*");

        //System.out.print(jsonWikiText);

        String htmlText = WikiModel.toHtml(WikiText);
        return htmlText;
    }


}