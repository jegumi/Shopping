package com.jegumi.shopping.network;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

public class GSonRequest<T> extends Request<T> {

    private final Gson gson;
    private final Class<T> clazz;
    private final Listener<T> listener;

    public GSonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
        this.clazz = clazz;
        this.listener = listener;
    }


    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}