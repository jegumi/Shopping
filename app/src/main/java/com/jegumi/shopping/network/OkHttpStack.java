package com.jegumi.shopping.network;

import android.util.Log;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.UUID;

public class OkHttpStack extends HurlStack {

    private final OkHttpClient client;

    public OkHttpStack() {
        this(new OkHttpClient());
    }

    public OkHttpStack(OkHttpClient client) {
        if (client == null) {
            throw new NullPointerException("Client must not be null.");
        }

        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credential = Credentials.basic("Authorization", "");
                return response.request().newBuilder().header("Authorization", credential).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });

        try {
            File cacheDir = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
            Cache cache = new Cache(cacheDir, 1024);
            client.setCache(cache);
        } catch (IOException e) {
            Log.i("HttpCache", "HTTP response cache installation failed:" + e);
        }
        this.client = client;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection conn = new OkUrlFactory(client).open(url);
        conn.setRequestProperty("User-Agent", "jegumi");

        return conn;
    }
}