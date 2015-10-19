package com.jegumi.shopping.helpers;

import java.util.HashMap;

// Simple Map containing the products added to the bag.

public class BagHelper {

    public HashMap<Integer, Integer> mBagMap = new HashMap<>();

    public void addToBag(int productId) {
        mBagMap.put(productId, isInBag(productId) ? getQuantityAdded(productId) + 1 : 1);
    }

    public boolean isInBag(int productId) {
        return mBagMap.containsKey(productId);
    }

    public int getQuantityAdded(int productId) {
        return mBagMap.get(productId);
    }
}
