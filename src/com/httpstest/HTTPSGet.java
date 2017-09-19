package com.httpstest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class HTTPSGet {

	public static String doGet(String url) {
		String result = null;
		StringBuilder sBuilder = new StringBuilder();
		HttpsURLConnection urlConnection = null;
		try {
			URL requestedUrl = new URL(url);
			urlConnection = (HttpsURLConnection) requestedUrl.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setConnectTimeout(1500);
			urlConnection.setReadTimeout(1500);

			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String line;
			while ((line = in.readLine()) != null) {
				sBuilder.append(line);
			}
			result = sBuilder.toString();
		} catch (Exception ex) {
			result = ex.toString();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return result;
	}
}
