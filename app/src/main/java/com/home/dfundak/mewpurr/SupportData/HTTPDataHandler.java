package com.home.dfundak.mewpurr.SupportData;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


//https://www.c-sharpcorner.com/article/create-a/
public class HTTPDataHandler {
    static String stream = null;

    public HTTPDataHandler() {
    }

    public String GetHTTPData(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Check the connection status
            if (urlConnection.getResponseCode() == 200) {
                //if response code = 200 ~ HTTP.OK
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                //Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null)
                    sb.append(line);
                stream = sb.toString();
                urlConnection.disconnect();
            } else {
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("timestamp", stream);
        return stream;
    }

    public void PostHTTPData(String urlString, String json) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            byte[] out = json.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            urlConnection.setFixedLengthStreamingMode(length);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.connect();

            try (OutputStream os = urlConnection.getOutputStream()) {
                os.write(out);
            }
            InputStream response = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DeleteHTTPData(String urlString, String json) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoOutput(true);

            byte[] out = json.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            urlConnection.setFixedLengthStreamingMode(length);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.connect();
            try (OutputStream os = urlConnection.getOutputStream()) {
                os.write(out);
            }
            InputStream response = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}