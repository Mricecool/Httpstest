package com.httpstest;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Https单向验证
 * 
 * @author app
 *
 */
public class HttpSingle {
	public static KeyStore getHttpsKeyStore(InputStream ins) {
		try {
			// 读取证书
			CertificateFactory cerFactory = CertificateFactory.getInstance("X.509"); // 问1
			Certificate cer = cerFactory.generateCertificate(ins);
			// 创建一个证书库，并将证书导入证书库
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 问2
			keyStore.load(null, null);
			// alias(test)貌似写什么都行？
			keyStore.setCertificateEntry("test", cer);
			return keyStore;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void initSSLContext(InputStream ins) {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(getHttpsKeyStore(ins));
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession sslsession) {

					if ("192.168.1.137".equals(hostname)) {
						return true;
					} else {
						return false;
					}
				}
			});
			System.out.println("初始化单向验证完成");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
