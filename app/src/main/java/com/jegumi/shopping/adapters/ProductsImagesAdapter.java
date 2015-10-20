package com.jegumi.shopping.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jegumi.shopping.R;
import com.jegumi.shopping.network.ImageCacheManager;

import java.util.List;

public class ProductsImagesAdapter extends RecyclerView.Adapter<ProductsImagesAdapter.ProductImageViewHolder> {

    private List<String> mUrls;
    private ImageLoader mImageLoader;

    public ProductsImagesAdapter(List<String> urls) {
        mUrls = urls;
    }

    @Override
    public ProductImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        mImageLoader = ImageCacheManager.getInstance().getImageLoader();
        return new ProductImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductImageViewHolder holder, int position) {
        String url = mUrls.get(position);
        holder.productPhoto.setImageUrl(url, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }

    public static class ProductImageViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView productPhoto;

        ProductImageViewHolder(View itemView) {
            super(itemView);
            productPhoto = (NetworkImageView) itemView.findViewById(R.id.product_image);
        }
    }
}
