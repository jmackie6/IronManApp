package cs246.ironmanapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by John on 6/22/15.
 */

public class URLReader {

    private static final String TAG_URL_READER = "Url Reader";
    private final String USER_AGENT = "Mozilla/5.0";
    private String url;
    private String params;

    /**
     * Constructor for a GET request takes only a url
     * @param url url ex. "http://robbise.no-ip.info/ironman/getContestants.php?semester=FALL2015"
     */
    public URLReader(String url) {
        this.url = url;
        this.params = "";
    }

    /**
     * Constructor for a POST request takes a url string and parameters
     * @param url ex. url = "http://robbise.no-ip.info/ironman/newUser.php"
     * @param params params = username=batman
     */
    public URLReader(String url, String params) {
        this.url = url;
        this.params = params;
    }

    /**
     * Executes an HTTP POST request
     *
     * @param url The url is a string that holds all of the information used to generate race
     *            percentages and contestant names.
     * @return returns a response of the given string, otherwise sends a blank '{}'
     * @throws Exception
     */
    public static String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        Log.i(TAG_URL_READER, "\nSending 'GET' request to URL : " + url);
        Log.i(TAG_URL_READER, "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //return result
        String jsonResponse = response.toString();
        if (isJSONValid(jsonResponse))
            return response.toString();
        else
            Log.e(TAG_URL_READER, jsonResponse + " is not valid JSON");
        return "{}";
    }

    /**
     * Executes an HTTP post request
     *
     * @param url
     * @param urlParameters
     * @return
     * @throws Exception
     */
    public static String sendPost(String url, String urlParameters) throws Exception {

        Log.i(TAG_URL_READER, "Sending a post in url reader");

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        Log.i(TAG_URL_READER, "headers set OK");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        Log.i(TAG_URL_READER, "REquest actually sent!");

        int responseCode = con.getResponseCode();
        Log.i(TAG_URL_READER, "\nSending 'POST' request to URL : " + url);
        Log.i(TAG_URL_READER, "Post parameters : " + urlParameters);
        Log.i(TAG_URL_READER, "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //return result
        String jsonResponse = response.toString();
        if (isJSONValid(jsonResponse))
            return response.toString();
        else
            Log.e(TAG_URL_READER, jsonResponse + " is not valid JSON");
        return "{}";
    }

    /**
     * This function determines if a string is valid json or not
     *
     * @param test - A string to validate
     * @return - true(Is valid json) false(is not valid json)
     */
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                Log.e(TAG_URL_READER, test + "is not json");
                return false;
            }
        }
        return true;
    }
}
