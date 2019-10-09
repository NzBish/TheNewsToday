package com.example.theNewsToday;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Connection {

    static String startConnection(String destination) {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(destination);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder address = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                address.append(line);
                address.append('\r');
            }
            bufferedReader.close();
            return address.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
