package com.esmc.mcnp.client.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpUtil {

    public static final int MAX_CONNECTION = 1000;
    public static final int TIMEOUT = 5000;
    private static PoolingHttpClientConnectionManager cm = null;
    private static RequestConfig requestConfig = null;
    public static CloseableHttpClient httpclient = null;
    private static CredentialsProvider provider = null;
    private static UsernamePasswordCredentials credentials = null;

    private static final ExecutorService pool = Executors.newFixedThreadPool(MAX_CONNECTION);

    static {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(MAX_CONNECTION);
        cm.setDefaultMaxPerRoute(MAX_CONNECTION);

        provider = new BasicCredentialsProvider();
        credentials
                = new UsernamePasswordCredentials("mawuli", "mawuli");
        provider.setCredentials(AuthScope.ANY, credentials);

        requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();

        httpclient = HttpClients.custom().setDefaultCredentialsProvider(provider).setConnectionManager(cm).setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler()).build();
    }

    /**
     *
     * @param req
     * @param handler
     * @param <T>
     * @return
     */
    public static <T> Future<T> run(final HttpUriRequest req, final ResponseHandler<T> handler) throws RejectedExecutionException, NullPointerException {
        return pool.submit(() -> {
            return httpclient.execute(req, handler);
        });
    }

    public static void shutdown() throws IOException {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                //Logger.error("[HttpUtil.shutdown] pool_termination_fail");
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }
        httpclient.close();
    }
}
