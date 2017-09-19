package com.httpstest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class HttpDouble {
	
	 /**
     * ���KeyStore.
     * @param keyStorePath
     *            ��Կ��·��
     * @param password
     *            ����
     * @return ��Կ��
     * @throws Exception
     */
    public static KeyStore getKeyStore(String password, String keyStorePath)
            throws Exception {
        // ʵ������Կ��
        KeyStore ks = KeyStore.getInstance("JKS");
        // �����Կ���ļ���
        FileInputStream is = new FileInputStream(keyStorePath);
        // ������Կ��
        ks.load(is, password.toCharArray());
        // �ر���Կ���ļ���
        is.close();
        return ks;
    }
    
    public static KeyStore getKeyStore(String password, InputStream keyStoreInputStream)
            throws Exception {
        // ʵ������Կ��
        KeyStore ks = KeyStore.getInstance("BKS");
        // �����Կ���ļ���
        // ������Կ��
        ks.load(keyStoreInputStream, password.toCharArray());
        // �ر���Կ���ļ���
        keyStoreInputStream.close();
        return ks;
    }
    
    public static KeyStore getTrustStore(String password, InputStream trustStoreInputStream)
            throws Exception {
        // ʵ������Կ��
        KeyStore ks = KeyStore.getInstance("BKS");
        // �����Կ���ļ���
        // ������Կ��
        ks.load(trustStoreInputStream, password.toCharArray());
        // �ر���Կ���ļ���
        trustStoreInputStream.close();
        return ks;
    }

    /**
     * ���SSLSocketFactory.
     * @param password
     *            ����
     * @param keyStorePath
     *            ��Կ��·��
     * @param trustStorePath
     *            ���ο�·��
     * @return SSLSocketFactory
     * @throws Exception
     */
    public static SSLContext getSSLContext(String password,
    		InputStream keyStorePath, InputStream trustStorePath) throws Exception {
        // ʵ������Կ��
        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        // �����Կ��
        KeyStore keyStore = getKeyStore(password, keyStorePath);
        // ��ʼ����Կ����
        keyManagerFactory.init(keyStore, password.toCharArray());

        // ʵ�������ο�
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // ������ο�
        KeyStore trustStore = getKeyStore(password, trustStorePath);
        // ��ʼ�����ο�
        trustManagerFactory.init(trustStore);
        // ʵ����SSL������
        SSLContext ctx = SSLContext.getInstance("TLS");
        // ��ʼ��SSL������
        ctx.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(), null);
        // ���SSLSocketFactory
        return ctx;
    }

    /**
     * ��ʼ��HttpsURLConnection.
     * @param password
     *            ����
     * @param keyStorePath
     *            ��Կ��·��
     * @param trustStorePath
     *            ���ο�·��
     * @throws Exception
     */
    public static void initHttpsURLConnection(String password,
    		InputStream keyStorePath, InputStream trustStorePath) throws Exception {
        // ����SSL������
        SSLContext sslContext = null;
        // ʵ������������֤�ӿ�
        HostnameVerifier hnv = new MyHostnameVerifier();
        try {
            sslContext = getSSLContext(password, keyStorePath, trustStorePath);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
                    .getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
        System.out.println("��ʼ��˫����֤���");
    }
    
    static class MyHostnameVerifier implements HostnameVerifier{

		@Override
		public boolean verify(String hostname, SSLSession session) {
			// TODO Auto-generated method stub
			return true;
		}
    	
    }

}
