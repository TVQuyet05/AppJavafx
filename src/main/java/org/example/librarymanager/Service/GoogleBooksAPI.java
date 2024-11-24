package org.example.librarymanager.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.librarymanager.Model.Book;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.client.utils.DateUtils.parseDate;

public class GoogleBooksAPI {
    private static final String API_KEY = "AIzaSyDleoSeEPLFNorWDMcTn9WO4AluKM71Rv8";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";


    public List<Book> searchBooks(String query) {
        List<Book> books = new ArrayList<>();
        try {
            // Encode the query string
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String url = BASE_URL + encodedQuery + "&key=" + API_KEY;

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    String json = EntityUtils.toString(response.getEntity());
                    parseBookInfo(json, books);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    private void parseBookInfo(String json, List<Book> books) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray items = jsonObject.has("items") ? jsonObject.getAsJsonArray("items") : null;

        if (items != null) {
            for (JsonElement item : items) {
                JsonObject book = item.getAsJsonObject();
                JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");

                // Parse book details
                String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "No title available";
                String authors = volumeInfo.has("authors") ? volumeInfo.get("authors").toString() : "No authors available";
                String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available";
                String genre = volumeInfo.has("categories") ? volumeInfo.get("categories").toString() : "No genre available";
                String publishedDate = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "No published date available";
                String imageUrl = volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")
                        ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString()
                        : "No image available";

                // Parse ISBN if available
                String isbn = "No ISBN available";
                if (volumeInfo.has("industryIdentifiers")) {
                    JsonArray identifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
                    for (JsonElement identifier : identifiers) {
                        JsonObject idObj = identifier.getAsJsonObject();
                        if (idObj.has("type") && idObj.get("type").getAsString().equals("ISBN_13")) {
                            isbn = idObj.get("identifier").getAsString();
                            break;
                        }
                    }
                }

                //if(isbn.equals("No ISBN available")) isbn = "0";

                isbn = "123";

//                publishedDate = "2024-11-24";

                // Create a Book object and add it to the list
                books.add(new Book( Integer.parseInt(isbn), title, authors, genre, (Date) parseDate(publishedDate), description, 0, imageUrl));
            }
        }
    }










//    public String searchBook(String query) {
//        try {
//            // Encode the query string
//            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
//            String url = BASE_URL + encodedQuery + "&key=" + API_KEY;
//
//            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//                HttpGet request = new HttpGet(url);
//                try (CloseableHttpResponse response = httpClient.execute(request)) {
//                    String json = EntityUtils.toString(response.getEntity());
//                    return parseBookInfo(json);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private String parseBookInfo(String json) {
//        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
//        JsonElement items = jsonObject.get("items");
//        if (items != null && items.isJsonArray()) {
//            JsonObject book = items.getAsJsonArray().get(0).getAsJsonObject();
//            JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");
//
//            // Check and retrieve the title
//            String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "No title available";
//
//            // Check and retrieve the authors
//            String authors = volumeInfo.has("authors") ? volumeInfo.get("authors").toString() : "No authors available";
//
//            // Check and retrieve the description
//            String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available";
//
//            return "Title: " + title + "\nAuthors: " + authors + "\nDescription: " + description;
//        }
//        return "No books found";
//    }

}
