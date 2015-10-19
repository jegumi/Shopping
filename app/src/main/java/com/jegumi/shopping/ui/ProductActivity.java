package com.jegumi.shopping.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.jegumi.shopping.model.Category;
import com.jegumi.shopping.model.Product;

// This class is just a container. We could have just the activity and all the logic here instead
// of using a fragment inside, but I think this is a good way to make the code portable, in the case
// you want to have a different layout for tablets, with more content in the screen, or different
// fragments depending of some rules, etc

public class ProductActivity extends BaseActivity {
    private static final String TAG = ProductActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFragment(getIntent().getIntExtra(MainActivity.EXTRA_PRODUCT, 0));
    }

    private void loadFragment(int productId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ProductFragment startFragment = ProductFragment.newInstance(productId);
        ft.replace(android.R.id.content, startFragment, TAG);
        ft.commit();
    }
}