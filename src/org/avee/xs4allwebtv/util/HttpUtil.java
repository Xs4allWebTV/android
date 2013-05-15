package org.avee.xs4allwebtv.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpUtil {
	public static JSONObject fetchJSONObject(String url) throws JSONException {
		String json = getUrlContents(url);
		JSONObject obj = new JSONObject(json);
		return obj;
	}
	
    public static String getUrlContents(String theUrl)
    {
      StringBuilder content = new StringBuilder();

      // many of these calls can throw exceptions, so i've just
      // wrapped them all in one try/catch statement.
      try
      {
        // create a url object
        URL url = new URL(theUrl);

        // create a urlconnection object
        URLConnection urlConnection = url.openConnection();

        // wrap the urlconnection in a bufferedreader
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        String line;

        // read from the urlconnection via the bufferedreader
        while ((line = bufferedReader.readLine()) != null)
        {
          content.append(line + "\n");
        }
        bufferedReader.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      return content.toString();
    }
}
