package com.jegumi.shopping.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jegumi.shopping.R;
import com.jegumi.shopping.model.Product;
import com.jegumi.shopping.network.ImageCacheManager;
import com.jegumi.shopping.ui.MainActivity;
import com.jegumi.shopping.ui.ProductActivity;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>{

    private List<Product> mProducts;
    private ImageLoader mImageLoader;

    public ProductsAdapter(List<Product> products){
        mProducts = products;
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
        holder.previousPrice.setText(product.PreviousPrice);
        holder.currentPrice.setText(product.CurrentPrice);
        if (mProducts.get(position).ProductImageUrl.length > 0) {
            holder.productPhoto.setImageUrl(product.ProductImageUrl[0], mImageLoader);
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cv;
        public TextView title;
        public TextView previousPrice;
        public TextView currentPrice;
        public NetworkImageView productPhoto;
        public int productId;

        ProductViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cv = (CardView)itemView.findViewById(R.id.product_card_view);
            title = (TextView)itemView.findViewById(R.id.title_text_view);
            previousPrice = (TextView)itemView.findViewById(R.id.previous_price_text_view);
            currentPrice = (TextView)itemView.findViewById(R.id.current_price_text_view);
            productPhoto = (NetworkImageView)itemView.findViewById(R.id.product_image);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ProductActivity.class);
            intent.putExtra(MainActivity.EXTRA_PRODUCT, productId);
            v.getContext().startActivity(intent);
        }
    }
}
