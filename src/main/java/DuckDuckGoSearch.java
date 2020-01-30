import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


class DuckDuckGoSearch {
    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";
    private static void getSearchResults(String query){
        Document doc = null;
        for (int i=0; i<2;i++) {
            try {
                if (i==0) {
                    doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).timeout(5000).get();
                }
                else {
                    doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query+"&s=30&nextParams=&v=l&o=json&dc=30&api=%2Fd.js&kl=us-en").timeout(5000).get();
                }
                Elements results = doc.getElementById("links").getElementsByClass("results_links");

                for (Element result : results) {

                    Element title = result.getElementsByClass("links_main").first().getElementsByTag("a").first();
                    System.out.println("\nURL:" + title.attr("href"));
                    System.out.println("Title:" + title.text());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        System.out.print("Search phrase:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String query = reader.readLine();

        getSearchResults(query);
    }
}