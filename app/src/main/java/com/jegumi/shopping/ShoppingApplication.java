package com.jegumi.shopping;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.jegumi.shopping.helpers.BagHelper;
import com.jegumi.shopping.helpers.FavouritesHelper;
import com.jegumi.shopping.network.ImageCacheManager;

public class ShoppingApplication extends Application {

    private static final int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
    private static final int DISK_IMAGECACHE_QUALITY = 100;
    private static final Bitmap.CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    private static BagHelper mBagHelper;
    private static FavouritesHelper mFavouritesHelper;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mBagHelper = new BagHelper();
        mFavouritesHelper = new FavouritesHelper();
        mContext = getApplicationContext();
        createImageCache();
    }

    private void createImageCache() {
        ImageCacheManager.getInstance().init(this, this.getPackageCodePath(), DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT, DISK_IMAGECACHE_QUALITY, ImageCacheManager.CacheType.MEMORY);
    }

    public static BagHelper getBagHelper() {
        return mBagHelper;
    }

    public static FavouritesHelper getFavouritesHelper() {
        return mFavouritesHelper;
    }

    public static Context getContext() {
        return mContext;
    }
}
