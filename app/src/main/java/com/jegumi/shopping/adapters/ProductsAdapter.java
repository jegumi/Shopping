package com.jegumi.shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jegumi.shopping.R;
import com.jegumi.shopping.ShoppingApplication;
import com.jegumi.shopping.helpers.FavouritesHelper;
import com.jegumi.shopping.model.Product;
import com.jegumi.shopping.network.ImageCacheManager;
import com.jegumi.shopping.ui.MainActivity;
import com.jegumi.shopping.ui.ProductActivity;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private List<Product> mProducts;
    private ImageLoader mImageLoader;
    private int mDefaultColor;
    private int mFavouriteColor;
    private Resources mResources;

    public ProductsAdapter(List<Product> products) {
        mProducts = products;
        mResources = ShoppingApplication.getContext().getResources();
        mDefaultColor = mResources.getColor(R.color.card_view_default_color);
        mFavouriteColor = mResources.getColor(R.color.card_view_favourite_color);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        mImageLoader = ImageCacheManager.getInstance().getImageLoader();
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = mProducts.get(position);
        holder.productId = product.ProductId;
        holder.title.setText(product.Title);
        holder.price.setText(mResources.getString(R.string.price, product.PreviousPrice, product.CurrentPrice));
        if (mProducts.get(position).ProductImageUrl.length > 0) {
            holder.productPhoto.setImageUrl(product.ProductImageUrl[0], mImageLoader);
        }
        if (ShoppingApplication.getFavouritesHelper().isInFavourites(holder.productId)) {
            holder.cv.setCardBackgroundColor(mFavouriteColor);
        } else {
            holder.cv.setCardBackgroundColor(mDefaultColor);
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public CardView cv;
        public TextView title;
        public TextView price;
        public NetworkImageView productPhoto;
        public int productId;

        ProductViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            cv = (CardView) itemView.findViewById(R.id.product_card_view);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            price = (TextView) itemView.findViewById(R.id.price_text_view);
            productPhoto = (NetworkImageView) itemView.findViewById(R.id.product_image);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ProductActivity.class);
            intent.putExtra(MainActivity.EXTRA_PRODUCT, productId);
            v.getContext().startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            FavouritesHelper favouritesHelper = ShoppingApplication.getFavouritesHelper();
            if (favouritesHelper.isInFavourites(productId)) {
                favouritesHelper.removeFromFavourites(productId);
                showCardVieFeedback(v.getContext(), R.color.card_view_default_color, R.string.removed_favourites);
            } else {
                favouritesHelper.addToFavourites(productId);
                showCardVieFeedback(v.getContext(), R.color.card_view_favourite_color, R.string.added_favourites);
            }
            return true;
        }

        private void showCardVieFeedback(Context context, int color, int resIdMessage) {
            Toast.makeText(context, context.getString(resIdMessage), Toast.LENGTH_SHORT).show();
            cv.setCardBackgroundColor(context.getResources().getColor(color));
        }
    }
}
