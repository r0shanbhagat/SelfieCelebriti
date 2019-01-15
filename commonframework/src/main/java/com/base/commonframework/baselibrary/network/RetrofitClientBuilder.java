package com.base.commonframework.baselibrary.network;

import android.annotation.SuppressLint;

import com.base.commonframework.BuildConfig;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Builder for creating Square OkHttpClient with pinned certificate that can be used with Retrofit.
 */
public class RetrofitClientBuilder {

    private static final String BOUNCY_CASTLE = "BKS";
    private static final String TLS = "TLS";
    /**
     * The Ok http client.
     */
    private OkHttpClient okHttpClient = new OkHttpClient();
    private OkHttpClient.Builder builder = okHttpClient.newBuilder();

    /**
     * Sets connection timeout.
     *
     * @param connectionTimeout the connection timeout
     * @param timeUnit          the time unit
     * @return connection timeout
     */
    public RetrofitClientBuilder setConnectionTimeout(int connectionTimeout, TimeUnit timeUnit) {
        builder.connectTimeout(connectionTimeout, timeUnit);
        return this;
    }

    /**
     * Sets read timeout.
     *
     * @param connectionTimeout the connection timeout
     * @param timeUnit          the time unit
     * @return read timeout
     */
    public RetrofitClientBuilder setReadTimeout(int connectionTimeout, TimeUnit timeUnit) {
        builder.readTimeout(connectionTimeout, timeUnit);
        return this;
    }

    /**
     * Sets write timeout.
     *
     * @param connectionTimeout the connection timeout
     * @param timeUnit          the time unit
     * @return write timeout
     */
    public RetrofitClientBuilder setWriteTimeout(int connectionTimeout, TimeUnit timeUnit) {
        builder.writeTimeout(connectionTimeout, timeUnit);
        return this;
    }

    /**
     * Sets interceptors.
     *
     * @param interceptors the interceptors
     * @return interceptors
     */
    public RetrofitClientBuilder setInterceptors(Interceptor interceptors) {
        builder.interceptors().add(interceptors);
        return this;
    }

    /**
     * Sets network interceptors.
     *
     * @param interceptors the interceptors
     * @return network interceptors
     */
    public RetrofitClientBuilder setNetworkInterceptors(Interceptor interceptors) {
        builder.networkInterceptors().add(interceptors);
        return this;
    }

    /**
     * Sets retry on connection failure.
     *
     * @param isRetry the is retry
     * @return retry on connection failure
     */
    public RetrofitClientBuilder setRetryOnConnectionFailure(boolean isRetry) {
        builder.retryOnConnectionFailure(isRetry);
        return this;
    }

    /**
     * Sets cache.
     *
     * @param cache the cache
     * @return cache
     */
    public RetrofitClientBuilder setCache(Cache cache) {
        builder.cache(cache);
        return this;
    }

    /**
     * Pin certificates retrofit client builder.
     *
     * @param resourceStream the resource stream
     * @param password       the password
     * @return retrofit client builder
     * @throws KeyStoreException         the key store exception
     * @throws CertificateException      the certificate exception
     * @throws NoSuchAlgorithmException  the no such algorithm exception
     * @throws IOException               the io exception
     * @throws UnrecoverableKeyException the unrecoverable key exception
     * @throws KeyManagementException    the key management exception
     */
    public RetrofitClientBuilder pinCertificates(InputStream resourceStream, char[] password) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance(BOUNCY_CASTLE);
        keyStore.load(resourceStream, password);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        TrustManager[] trustManagers = {new CustomTrustManager(keyStore)};

        kmf.init(keyStore, password);

        SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.TLS);
        sslContext.init(kmf.getKeyManagers(), trustManagers, null);

        builder.hostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        builder.sslSocketFactory(sslContext.getSocketFactory());

        return this;
    }


    /**
     * Ignore certificates retrofit client builder.
     *
     * @return retrofit client builder
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws KeyManagementException   the key management exception
     */
    public RetrofitClientBuilder ignoreCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        X509TrustManager easyTrustManager = new X509TrustManager() {

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {

            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                easyTrustManager
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        builder.hostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        builder.sslSocketFactory(sc.getSocketFactory());

        return this;
    }

    /**
     * Build ok http client.
     *
     * @return ok http client
     */
    public OkHttpClient build() {
        if (!BuildConfig.FLAVOR.equalsIgnoreCase("isg")) {
            if (!BuildConfig.DEBUG) {
                System.setProperty("https.proxyPort", "443");
                System.setProperty("http.proxyPort", "80");
            }
        }
        return builder.build();
    }

}
