package Seledkin;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


class DuckDuckGoSearch {
    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";

    private static String getSearchResults(String query) {
        Document doc = null;
        HashMap<String, String> res = new HashMap<String, String>();
        for (int i = 0; i < 2; i++) {
            try {
                if (i == 0) {
                    doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).timeout(5000).get();
                } else {
                    doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query + "&s=30&nextParams=&v=l&o=json&dc=30&api=%2Fd.js&kl=us-en").timeout(5000).get();
                }
                Elements results = doc.getElementById("links").getElementsByClass("results_links");
                for (Element result : results) {

                    Element title = result.getElementsByClass("links_main").first().getElementsByTag("a").first();
                    res.put(title.attr("href"), title.text());


                    //           System.out.println("\nURL:" + title.attr("href"));
                    //          System.out.println("Title:" + title.text());
                    //         System.out.println(res);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(res);
        return jsonString;

    }

    public static void createFile(String query) {
        String jsonString = getSearchResults(query);
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formattedTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss-SSS");
        File dir = new File("logs");
        if (!(dir.exists())) {
            dir.mkdir();
        }
        String filepath = "logs\\result_" + time.format(formattedTime) + ".txt";
        File file = new File(filepath);
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(filepath);
            writer.write(jsonString);
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        String query = args[ 0 ];
        createFile(query);
    }
}