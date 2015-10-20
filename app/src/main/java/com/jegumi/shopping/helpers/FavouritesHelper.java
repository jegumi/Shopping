package com.jegumi.shopping.helpers;

import java.util.HashMap;

// Simple Map containing the favourites.

public class FavouritesHelper {

    public HashMap<Integer, Integer> mFavouritesMap = new HashMap<>();

    public void addToFavourites(int productId) {
        mFavouritesMap.put(productId, productId);
    }

    public boolean isInFavourites(int productId) {
        return mFavouritesMap.containsKey(productId);
    }
}
