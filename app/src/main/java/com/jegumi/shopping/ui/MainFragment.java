package com.jegumi.shopping.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jegumi.shopping.R;
import com.jegumi.shopping.adapters.ProductsAdapter;
import com.jegumi.shopping.model.Category;
import com.jegumi.shopping.model.Product;
import com.jegumi.shopping.model.Products;
import com.jegumi.shopping.network.Api;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

// This class contains the recycler view that load the products of the category

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private RecyclerView mProductRecyclerView;
    private Category mCategory;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProductRecyclerView = (RecyclerView) view.findViewById(R.id.products_recycler_view);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.num_columns));
        mProductRecyclerView.setLayoutManager(llm);
        mProductRecyclerView.setAdapter(new ProductsAdapter(new ArrayList<Product>()));
    }

    public void updateCategory(Category category) {
        mCategory = category;
        refreshCategory();
    }

    private void refreshCategory() {
        Api.loadProducts(mCategory.CategoryId, new Response.Listener<Products>() {
            @Override
            public void onResponse(Products products) {
                mProductRecyclerView.setAdapter(new ProductsAdapter(Arrays.asList(products.Listings)));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "Error loading category", volleyError);
                int resIdMessage = volleyError.getCause() instanceof UnknownHostException ?
                        R.string.error_no_connectivity : R.string.error_loading_category;

                Toast.makeText(getActivity(), getString(resIdMessage), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
