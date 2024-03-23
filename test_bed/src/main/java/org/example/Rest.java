package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {
    public String sayHello(String world) {
        return "Hello " + world;
    }

    /*
     * public String getData(String url) throws IOException {
     * URL obj = new URL(url);
     * HttpURLConnection con = (HttpURLConnection) obj.openConnection();
     * con.setRequestMethod("GET");
     * int responseCode = con.getResponseCode();
     * System.out.println("GET Response Code :: " + responseCode);
     * if (responseCode == HttpURLConnection.HTTP_OK) { // success
     * BufferedReader in = new BufferedReader(new InputStreamReader(
     * con.getInputStream()));
     * String inputLine;
     * StringBuffer response = new StringBuffer();
     * 
     * while ((inputLine = in.readLine()) != null) {
     * response.append(inputLine);
     * }
     * in.close();
     * 
     * // print result
     * return response.toString();
     * }
     * return "RESPONSE CODE: " + responseCode + " " + con.getResponseMessage();
     * }
     */
}
