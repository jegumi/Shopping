package com.jegumi.shopping.ui;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jegumi.shopping.R;
import com.jegumi.shopping.ShoppingApplication;
import com.jegumi.shopping.adapters.ProductsImagesAdapter;
import com.jegumi.shopping.helpers.DisplayHelper;
import com.jegumi.shopping.model.ProductDetails;
import com.jegumi.shopping.network.Api;

import java.net.UnknownHostException;
import java.util.Arrays;

/*
   Nothing interesting to comment here. This fragment doesn't change with rotation, as the
   layout is quite simple, so in the manifest it's indicated not to reload the activity parent on
   rotation
*/
public class ProductFragment extends Fragment {

    private RecyclerView mProductImagesRecyclerView;
    private Button mAddToBag;
    private View mLoadingView;
    private ProgressBar mProgressBar;
    private TextView mDetails;
    private TextView mBrandName;
    private TextView mErrorMessage;
    private int mProductId;

    public static ProductFragment newInstance(int productId) {
        ProductFragment productFragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.EXTRA_PRODUCT, productId);
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
        mLoadingView = view.findViewById(R.id.loading_view);

        /*
            This is needed to fix an Android bug: http://developer.android.com/intl/es/reference/android/widget/RelativeLayout.html
            Note: In platform version 17 and lower, RelativeLayout was affected by a measurement bug
            that could cause child views to be measured with incorrect MeasureSpec values.
            (See MeasureSpec.makeMeasureSpec for more details.) This was triggered when a RelativeLayout
            container was placed in a scrolling container, such as a ScrollView or HorizontalScrollView.
            If a custom view not equipped to properly measure with the MeasureSpec mode UNSPECIFIED was placed
            in a RelativeLayout, this would silently work anyway as RelativeLayout would pass a very large
            AT_MOST MeasureSpec instead.
         */
        mLoadingView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mLoadingView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mLoadingView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                Point point = DisplayHelper.getScreenSize();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mLoadingView.getLayoutParams();
                lp.height = point.y - DisplayHelper.getActionBarHeight();
                lp.width = point.x;
            }
        });
        setRetry();

        Bundle arguments = getArguments();
        if (arguments != null) {
            mProductId = arguments.getInt(MainActivity.EXTRA_PRODUCT, 0);
            initProduct();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Api.cancelAllRequests(getActivity());
    }

    private void initProduct() {
        Api.loadProductDetails(mProductId, new Response.Listener<ProductDetails>() {
            @Override
            public void onResponse(final ProductDetails productDetails) {
                setProduct(productDetails);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgressBar.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
                mErrorMessage.setText(getErrorCode(volleyError));
            }
        });
    }

    private void setRetry() {
        mErrorMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mErrorMessage.setVisibility(View.GONE);
                initProduct();
            }
        });
    }

    private int getErrorCode(VolleyError volleyError) {
        return volleyError.getCause() instanceof UnknownHostException ? R.string.error_no_connectivity : R.string.error_loading_data;
    }

    private void setProduct(ProductDetails productDetails) {
        mBrandName.setText(productDetails.Brand);
        mDetails.setText(productDetails.Description);
        setAddToBag(productDetails);

        if (productDetails.ProductImageUrls != null) {
            mProductImagesRecyclerView.setAdapter(new ProductsImagesAdapter(Arrays.asList(productDetails.ProductImageUrls)));
        }
        mLoadingView.setVisibility(View.GONE);
    }

    private void setAddToBag(final ProductDetails productDetails) {
        if (ShoppingApplication.getBagHelper().isInBag(mProductId)) {
            updateButtonText(productDetails, mProductId);
        } else {
            mAddToBag.setText(getString(R.string.add_to_bag, productDetails.CurrentPrice));
        }

        mAddToBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingApplication.getBagHelper().addToBag(mProductId);
                updateButtonText(productDetails, mProductId);

                Toast.makeText(v.getContext(), getString(R.string.added_to_bag_feedback), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // We are using productId instead of productDetails because the data we are receiving are inconsistent
    // and we always received the same product details, no matter the productId
    private void updateButtonText(ProductDetails productDetails, int productId) {
        int quantity = ShoppingApplication.getBagHelper().getQuantityAdded(productId);
        mAddToBag.setText(getString(R.string.added_to_bag, quantity, productDetails.CurrentPrice));
    }
}
