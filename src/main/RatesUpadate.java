package main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class RatesUpadate {
    public String RatesUpd(String nameCountry) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        String url_Conversion = "https://v6.exchangerate-api.com/v6/"+System.getenv("api_key")+"/latest/"+nameCountry;
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url_Conversion)).build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        //System.out.println(response.body());
        String json = response.body();
        return json;
    }
}
