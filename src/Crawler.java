import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Crawler {
    static int maxSearches = 5;
    static String website = "https://www.mozilla.org";
    static private Set<String> pagesVisited = new HashSet<>();
    public static JSONArray jsonarray = new JSONArray();
    static JSONObject item = new JSONObject();
    static int mainCounter = 0;


    public static void begin() throws IOException {
        System.out.println("Scraping main website");
        Elements linksOnPage = Jsoup.connect(website).get().select("a[href]");
        //System.out.println(linksOnPage);
        //System.out.println("Scraping main website ends");
        visitUrl(linksOnPage, 0);
    }
    public static Elements connect(Element link) throws IOException{
        String hrefAttr = link.attr("href");
        if(!hrefAttr.contains("http"))
            hrefAttr = website + hrefAttr;
        while(item.keys().hasNext())
            item.remove((String)item.keys().next());
        Connection.Response resp = null;
        try {
            resp = Jsoup.connect(hrefAttr).execute();
        }
        catch (Exception e){
        }

        Document htmlDocument;
        if(resp != null && resp.statusCode() == 200) //connection.response().statusCode() == 200
            if(resp.contentType().contains("text/html")) {
                item.append("page_url",hrefAttr);
                htmlDocument = resp.parse();
                Elements listOfLinks = htmlDocument.select("a[href]");
                item.append("links",listOfLinks.toString());
                Elements media = htmlDocument.select("[img]");
                List<String> imgList = new ArrayList<>();
                for (Element src : media) {
//                    if (src.tagName().equals("img"))
                    imgList.add(src.attr("src"));
                }
                item.append("images",imgList);
                jsonarray.put(item);
                return listOfLinks;
            }
            item.append("images",null);
            jsonarray.put(item);
            return null;
    }

    static int visitUrl(Elements links, int count) throws IOException {

        mainCounter ++ ;
        if(mainCounter == 1000){
            PrintWriter out = new PrintWriter("output.txt");
            out.println(Crawler.jsonarray.toString());
            System.out.println(Crawler.jsonarray.toString());
            System.exit(0);
        }

        if(links == null){
            System.out.println("Links null");
            return count -1;
        }
        if(count > maxSearches){
            System.out.println("Max reached");
            return count -1;
        }
        for(Element link : links){
            String hrefAttr = link.attr("href");
            System.out.println(count);
            System.out.println(hrefAttr);

            if(pagesVisited.contains(hrefAttr) || pagesVisited.contains(website + hrefAttr)){
                System.out.println("Page visited");
                continue;
            }
            pagesVisited.add(hrefAttr);
            count = visitUrl(connect(link), count + 1);
        }
        return count-1;

    }
}
