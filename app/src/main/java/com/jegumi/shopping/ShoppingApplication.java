package com.jegumi.shopping;

import android.app.Application;
import android.graphics.Bitmap;

import com.jegumi.shopping.helpers.BagHelper;
import com.jegumi.shopping.network.ImageCacheManager;
import com.squareup.otto.Bus;

public class ShoppingApplication extends Application {

    private static final int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
    private static final int DISK_IMAGECACHE_QUALITY = 100;
    private static final Bitmap.CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    private static BagHelper mBagHelper;
    private static Bus mBus;

    @Override
    public void onCreate() {
        super.onCreate();
        mBagHelper = new BagHelper();
        mBus = new Bus();
        createImageCache();
    }

    private void createImageCache() {
        ImageCacheManager.getInstance().init(this,
                this.getPackageCodePath()
                , DISK_IMAGECACHE_SIZE
                , DISK_IMAGECACHE_COMPRESS_FORMAT
                , DISK_IMAGECACHE_QUALITY
                , ImageCacheManager.CacheType.MEMORY);
    }

    public static BagHelper getBagHelper() {
        return mBagHelper;
    }

    public static Bus getBus() {
        return mBus;
    }
}
