package com.thinkspark.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import com.thinkspark.model.NewsArticle;

public class NewsDao {

    private static final String API_KEY = "fdfe79c1c769498aab274a89f5d076df";
    private static final String API_URL = "https://newsapi.org/v2/everything?q=general&apiKey=";

    public ObservableList<NewsArticle> getNews() {
        ObservableList<NewsArticle> newsList = FXCollections.observableArrayList();
        try {
            URL url = new URL(API_URL + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray articles = jsonResponse.getJSONArray("articles");

            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("title");
                String description = article.isNull("description") ? "" : article.getString("description");
                String articleUrl = article.getString("url");
                newsList.add(new NewsArticle(title, description, articleUrl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }
}