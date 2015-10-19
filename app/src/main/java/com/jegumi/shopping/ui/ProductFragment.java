package com.jegumi.shopping.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jegumi.shopping.R;
import com.jegumi.shopping.ShoppingApplication;
import com.jegumi.shopping.model.ProductDetails;
import com.jegumi.shopping.network.Api;
import com.jegumi.shopping.network.ImageCacheManager;

import java.net.UnknownHostException;

// Nothing interesting to comment here. This fragment doesn't change with rotation, as the
// layout is quite simple, so in the manifest it's indicated not to reload it on config changes
// As I didn't have too many time I didn't do the ScrollView for the images, but to do it
// should be enough to add an recyclerView on this layout with an adapter managing the images

public class ProductFragment extends Fragment {

    private NetworkImageView mProductPhoto;
    private Button mAddToBag;
    private ImageLoader mImageLoader;
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
        mImageLoader = ImageCacheManager.getInstance().getImageLoader();

        mBrandName = (TextView) view.findViewById(R.id.brand_text_view);
        mDetails = (TextView) view.findViewById(R.id.details_text_view);
        mProductPhoto = (NetworkImageView) view.findViewById(R.id.product_image_details);
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
                    mProductPhoto.setImageUrl(productDetails.ProductImageUrls[0], mImageLoader);
                }
                mProgressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgressBar.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
                int resIdMessage =  volleyError.getCause() instanceof UnknownHostException ?
                        R.string.error_no_connectivity : R.string.error_loading_data;
                mErrorMessage.setText(resIdMessage);
            }
        });
    }

    private void updateButtonText(ProductDetails productDetails) {
        int quantity = ShoppingApplication.getBagHelper().getQuantityAdded(productDetails.ProductId);
        mAddToBag.setText(getString(R.string.added_to_bag, quantity, productDetails.CurrentPrice));
    }
}
