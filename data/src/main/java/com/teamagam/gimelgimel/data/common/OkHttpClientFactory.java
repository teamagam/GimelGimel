package com.teamagam.gimelgimel.data.common;


import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpClientFactory {
    public static OkHttpClient create(Logger logger,
                                      HttpLoggingInterceptor.Level loggingLevel) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(logger::v);

        interceptor.setLevel(loggingLevel);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(Constants.CONNECTION_SERVER_TIME_OUT_SECONDS, TimeUnit.SECONDS);

        if (Constants.SHOULD_USE_PROXY) {
            builder = builder
                    .proxy(createProxy())
                    .hostnameVerifier(createHostnameVerifier())
                    .sslSocketFactory(createSslSocketFactory())
                    .readTimeout(Constants.CONNECTION_SERVER_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(Constants.CONNECTION_SERVER_TIME_OUT_SECONDS, TimeUnit.SECONDS);
        }

        return builder.build();
    }

    private static Proxy createProxy() {
        return new Proxy(
                Proxy.Type.HTTP,
                InetSocketAddress.createUnresolved(Constants.PROXY_HOST, Constants.PROXY_PORT)
        );
    }

    private static HostnameVerifier createHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                try {
                    if (session.getPeerCertificates()[0] instanceof X509Certificate) {
                        X509Certificate x509Cer = (X509Certificate)
                                session.getPeerCertificates()[0];
                        String compare = Constants.PROXY_HOST;
                        String subject = x509Cer.getSubjectDN().getName();
                        String[] splitParts = subject.trim().split(",");
                        String[] subPart;
                        for (int i = 0; i < splitParts.length; i++) {
                            subPart = splitParts[i].split("=");
                            if (subPart[0].equals("CN") && subPart[1].compareToIgnoreCase(
                                    compare) == 0) {
                                return true;
                            }
                        }

                        Collection<List<?>> alternativeNames;

                        try {
                            alternativeNames = x509Cer.getSubjectAlternativeNames();
                            for (List<?> list : alternativeNames) {
                                if (((Integer) list.get(0) == Constants.DNS_ID)
                                        && ((String) list.get(1)).compareToIgnoreCase(
                                        compare) == 0) {
                                    return true;
                                }
                            }
                        } catch (CertificateParsingException e) {
                            return false;
                        }
                        return false;
                    }
                } catch (SSLPeerUnverifiedException e) {
                    return false;
                }
                return true;
            }
        };
    }

    private static SSLSocketFactory createSslSocketFactory() {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);
            X509TrustManager[] trustManagers = new X509TrustManager[1];
            trustManagers[0] = (X509TrustManager) factory.getTrustManagers()[0];
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustManagers, new SecureRandom());

            return sc.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException("Couldn't create socket-factory");
        }
    }
}
