package com.jegumi.shopping.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.jegumi.shopping.ShoppingApplication;
import com.jegumi.shopping.model.Menu;
import com.jegumi.shopping.model.ProductDetails;
import com.jegumi.shopping.model.Products;
import com.jegumi.shopping.ui.MainActivity;

public class Api {

    private static final String TAG = Api.class.getSimpleName();
    private static final String BASE_URL = "https://dl.dropboxusercontent.com/u/1559445/ASOS/SampleApi/";
    private static final String MEN_ENDPOINT = "cats_men.json";
    private static final String WOMEN_ENDPOINT = "cats_women.json";
    private static final String PRODUCT_CATEGORY_ENDPOINT = "anycat_products.json";
    private static final String PRODUCT_DETAILS_ENDPOINT = "anyproduct_details.json";
    private static final String CATEGORY_KEY = "catid";
    private static final Context mContext = ShoppingApplication.getContext();

    public static String getMenEndPoint() {
        return BASE_URL + MEN_ENDPOINT;
    }

    public static String getWomenEndPoint() {
        return BASE_URL + WOMEN_ENDPOINT;
    }

    public static String getProductEndPoint(String catId) {
        return BASE_URL + PRODUCT_CATEGORY_ENDPOINT + "?" + CATEGORY_KEY + "=" + catId;
    }

    public static String getProductDetailsEndpoint(int productId) {
        return BASE_URL + PRODUCT_DETAILS_ENDPOINT + "?" + CATEGORY_KEY + "=" + productId;
    }

    public static void cancelAllRequests(Context context) {
        VolleySingleton.getInstance(context).getRequestQueue().cancelAll(TAG);
    }

    public static void loadCategories(MainActivity.MenuCategories category,
                                      Response.Listener<Menu> listener, Response.ErrorListener errorListener) {
        RequestQueue queue = VolleySingleton.getInstance(mContext).getRequestQueue();

        String uri = category == MainActivity.MenuCategories.MEN ? Api.getMenEndPoint() : Api.getWomenEndPoint();

        GSonRequest<Menu> jsObjRequest = new GSonRequest<>(
                Request.Method.GET,
                uri,
                Menu.class,
                listener,
                errorListener);

        jsObjRequest.setTag(TAG);
        queue.add(jsObjRequest);
    }

    public static void loadProducts(String catId, Response.Listener<Products> listener,
                                    Response.ErrorListener errorListener) {
        RequestQueue queue = VolleySingleton.getInstance(mContext).getRequestQueue();

        String uri = Api.getProductEndPoint(catId);
        GSonRequest<Products> jsObjRequest = new GSonRequest<>(
                Request.Method.GET,
                uri,
                Products.class,
                listener,
                errorListener);

        jsObjRequest.setTag(TAG);
        queue.add(jsObjRequest);
    }

    public static void loadProductDetails(int productId, Response.Listener<ProductDetails> listener,
                                          Response.ErrorListener errorListener) {
        RequestQueue queue = VolleySingleton.getInstance(mContext).getRequestQueue();

        String uri = Api.getProductDetailsEndpoint(productId);
        GSonRequest<ProductDetails> jsObjRequest = new GSonRequest<>(
                Request.Method.GET,
                uri,
                ProductDetails.class,
                listener,
                errorListener);

        jsObjRequest.setTag(TAG);
        queue.add(jsObjRequest);
    }
}
