package com.jegumi.shopping.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jegumi.shopping.R;
import com.jegumi.shopping.ShoppingApplication;

public class BaseActivity extends ActionBarActivity {

    protected Toolbar mToolbar;

    public void setActionBar(boolean isHome) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(!isHome);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
            case R.id.favourites:
                int favItems = ShoppingApplication.getFavouritesHelper().getNumberOfElements();
                Toast.makeText(this, getResources().getQuantityString(R.plurals.fav_items, favItems, favItems), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.basket:
                int basketItems = ShoppingApplication.getBagHelper().getNumberOfElements();
                Toast.makeText(this, getResources().getQuantityString(R.plurals.basket_items, basketItems, basketItems), Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}