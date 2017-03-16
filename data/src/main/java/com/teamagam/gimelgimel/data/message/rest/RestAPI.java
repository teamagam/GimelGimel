package com.teamagam.gimelgimel.data.message.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.message.adapters.MessageJsonAdapter;
import com.teamagam.gimelgimel.data.message.adapters.MessageListJsonAdapter;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.rest.adapter.factory.RxErrorHandlingCallAdapterFactory;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;

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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class RestAPI {

    private static final Logger sLogger = LoggerFactory.create(RestAPI.class.getSimpleName());
    private static final String FAKE_VALID_URL = "http://lies";

    private GGMessagingAPI mMessagingAPI;

    private FilesDownloader.FilesDownloaderAPI mFilesDownloaderAPI;

    @Inject
    public RestAPI() {
        initializeAPIs();
    }

    public GGMessagingAPI getMessagingAPI() {
        return mMessagingAPI;
    }

    public FilesDownloader.FilesDownloaderAPI getFilesDownloaderAPI() {
        return mFilesDownloaderAPI;
    }

    private void initializeAPIs() {
        initializeMessagingAPI();
        initializeFilesDownloaderAPI();
    }

    private void initializeMessagingAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MESSAGING_SERVER_URL)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(getGsonConverterFactory())
                .client(getOkHttpClient(HttpLoggingInterceptor.Level.BODY))
                .build();
        mMessagingAPI = retrofit.create(GGMessagingAPI.class);
    }

    private void initializeFilesDownloaderAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FAKE_VALID_URL)    //Base url must be supplied, it won't be used by the API
                .client(getOkHttpClient(HttpLoggingInterceptor.Level.HEADERS))
                .build();
        mFilesDownloaderAPI = retrofit.create(FilesDownloader.FilesDownloaderAPI.class);
    }

    private GsonConverterFactory getGsonConverterFactory() {
        Gson gson = createMessagingGson();
        return GsonConverterFactory.create(gson);
    }

    private OkHttpClient getOkHttpClient(HttpLoggingInterceptor.Level loggingLevel) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(sLogger::v);

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

    private Proxy createProxy() {
        return new Proxy(
                Proxy.Type.HTTP,
                InetSocketAddress.createUnresolved(Constants.PROXY_HOST, Constants.PROXY_PORT)
        );
    }

    private HostnameVerifier createHostnameVerifier() {
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

    private SSLSocketFactory createSslSocketFactory() {
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

    private Gson createMessagingGson() {
        // The following code creates a new Gson instance that will convert all fields from lower
        // case with underscores to camel case and vice versa. It also registers a type adapter for
        // the Message class. This DateTypeAdapter will be used anytime Gson encounters a Date field.
        // The gson instance is passed as a parameter to GsonConverter, which is a wrapper
        // class for converting types.
        MessageJsonAdapter messageJsonAdapter = new MessageJsonAdapter();
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(MessageData.class, messageJsonAdapter)
                .registerTypeAdapter(List.class,
                        new MessageListJsonAdapter(messageJsonAdapter))
                .create();
    }
}
