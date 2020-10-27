package com.group13.augmentedView.libs.places;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Maxime Beugoms
 * @author Florian Duprez
 * @author Baptiste Lapiere
 * @author Martin Meerts
 *
 * This class make a HTTP request to receive google data
 */
public class DownloadURL {

    /**
     * This method use myUrl to make a HTTP request
     * @param myUrl it's the url for the request
     * @return a string containing the building's data
     * @throws IOException
     */
    public String readUrl(String myUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(myUrl);
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while((line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null)
                inputStream.close();

            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return data;
    }
}
