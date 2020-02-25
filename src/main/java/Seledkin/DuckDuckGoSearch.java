package Seledkin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;


class DuckDuckGoSearch {
    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";

    private static String getSearchResults(String query){
        Document doc ;
        Map<String, String> res = null;
        try {

            doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).timeout(5000).get();

            Elements results = doc.getElementById("links").getElementsByClass("results_links");
            res = results.stream().map(s -> s.getElementsByClass("links_main").first().getElementsByTag("a").first()).collect(Collectors.toMap(p -> p.attr("href"), t -> t.text()));
        }
        catch (IOException e){e.printStackTrace();}
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(res);

    }

    private static void createFile(String jsonString) {
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

    public static void main(String[] args) {
        String query = args[ 0 ];

        createFile(getSearchResults(query));


    }
}