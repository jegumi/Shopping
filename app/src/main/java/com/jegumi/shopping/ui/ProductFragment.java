package com.jegumi.shopping.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jegumi.shopping.R;
import com.jegumi.shopping.ShoppingApplication;
import com.jegumi.shopping.adapters.ProductsImagesAdapter;
import com.jegumi.shopping.model.ProductDetails;
import com.jegumi.shopping.network.Api;

import java.net.UnknownHostException;
import java.util.Arrays;

// Nothing interesting to comment here. This fragment doesn't change with rotation, as the
// layout is quite simple, so in the manifest it's indicated not to reload it on config changes


public class ProductFragment extends Fragment {

    private RecyclerView mProductImagesRecyclerView;
    private Button mAddToBag;
    private ProgressBar mProgressBar;
    private TextView mDetails;
    private TextView mBrandName;
    private TextView mErrorMessage;

    public static ProductFragment newInstance(int productId) {
        ProductFragment productFragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.EXTRA_CATEGORY, productId);
        productFragment.setArguments(bundle);
        return productFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.product_details, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((BaseActivity) getActivity()).setActionBar(false);

        mBrandName = (TextView) view.findViewById(R.id.brand_text_view);
        mDetails = (TextView) view.findViewById(R.id.details_text_view);
        mProductImagesRecyclerView = (RecyclerView) view.findViewById(R.id.products_images_recycler_view);
        mProductImagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mAddToBag = (Button) view.findViewById(R.id.add_bag_button);
        mErrorMessage = (TextView) view.findViewById(R.id.error_message_text_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_progress_bar);

        Bundle arguments = getArguments();
        if (arguments != null) {
            initProduct(arguments.getInt(MainActivity.EXTRA_PRODUCT, 0));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Api.cancelAllRequests(getActivity());
    }

    private void initProduct(final int productId) {
        Api.loadProductDetails(getActivity(), productId, new Response.Listener<ProductDetails>() {
            @Override
            public void onResponse(final ProductDetails productDetails) {
                mBrandName.setText(productDetails.Brand);
                mDetails.setText(productDetails.Description);

                if (ShoppingApplication.getBagHelper().isInBag(productDetails.ProductId)) {
                    updateButtonText(productDetails);
                } else {
                    mAddToBag.setText(getString(R.string.add_to_bag, productDetails.CurrentPrice));
                }

                mAddToBag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShoppingApplication.getBagHelper().addToBag(productDetails.ProductId);
                        updateButtonText(productDetails);

                        Toast.makeText(v.getContext(), getString(R.string.added_to_bag_feedback), Toast.LENGTH_SHORT).show();
                    }
                });
                if (productDetails.ProductImageUrls != null && productDetails.ProductImageUrls.length > 0) {
                    mProductImagesRecyclerView.setAdapter(new ProductsImagesAdapter(Arrays.asList(productDetails.ProductImageUrls)));
                }
                mProgressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgressBar.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
                int resIdMessage = volleyError.getCause() instanceof UnknownHostException ? R.string.error_no_connectivity : R.string.error_loading_data;
                mErrorMessage.setText(resIdMessage);
            }
        });
    }

    private void updateButtonText(ProductDetails productDetails) {
        int quantity = ShoppingApplication.getBagHelper().getQuantityAdded(productDetails.ProductId);
        mAddToBag.setText(getString(R.string.added_to_bag, quantity, productDetails.CurrentPrice));
    }
}
