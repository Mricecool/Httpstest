package com.httpstest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HTTPSPost {

	/**
	 * ��������.
	 * 
	 * @param httpsUrl
	 *            ����ĵ�ַ
	 * @param xmlStr
	 *            ���������
	 */
	public static String post(String httpsUrl, Map<String, String> params) {
		String result = null;
		StringBuilder sBuilder = new StringBuilder();
		HttpsURLConnection urlCon = null;
		byte[] content = {};
		try {
			if (params != null) {
				StringBuffer stringBuffer = new StringBuffer();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					try {
						stringBuffer.append(entry.getKey()).append("=")
								.append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				// ɾ�����һ�� & �ַ�
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
				content = stringBuffer.toString().getBytes("UTF-8");
			}
			urlCon = (HttpsURLConnection) (new URL(httpsUrl)).openConnection();
			urlCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlCon.setRequestMethod("POST");
			urlCon.setConnectTimeout(3000);
			urlCon.setReadTimeout(3000);
			urlCon.setDoInput(true);
			urlCon.setDoOutput(true);
			urlCon.getOutputStream().write(content);
			urlCon.getOutputStream().flush();
			urlCon.getOutputStream().close();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				sBuilder.append(line);
			}
			result = sBuilder.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
