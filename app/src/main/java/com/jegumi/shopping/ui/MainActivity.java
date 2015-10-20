package com.jegumi.shopping.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jegumi.shopping.R;
import com.jegumi.shopping.ShoppingApplication;
import com.jegumi.shopping.events.UpdateCategoryEvent;
import com.jegumi.shopping.model.Category;
import com.jegumi.shopping.model.Menu;
import com.jegumi.shopping.network.Api;

import java.net.UnknownHostException;

// Normally I'd have created a WelcomeActivity to contain the initial spinner and make some calls
// to download the menu, but in this case, I'm using this class as initial screen to reuse the background
// image as progress feedback to be shown everytime the info is reloaded (change of orientation, etc)
// and give the feedback that the info is being reloaded

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    private static final String CURRENT_CATEGORY = "currentCategory";
    private static final String CURRENT_POSITION = "currentPosition";

    public static final String EXTRA_PRODUCT = "com.jegumi.shopping.product_item";
    public static final String EXTRA_CATEGORY = "com.jegumi.shopping.category";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private SlidingAdapter mMenuAdapter;
    private View mLoadingView;
    private TextView mErrorMessage;
    private ProgressBar mLoadingProgressBar;
    private Button mMenButton;
    private Button mWomenButton;
    private MenuCategories mCurrentCategory;
    private int mCurrentPosition;

    public enum MenuCategories {
        MEN(0), WOMEN(1);

        private int value;

        MenuCategories(int value) {
            this.value = value;
        }

        public static MenuCategories getMenuCategories(int value) {
            return value == 0 ? MEN : WOMEN;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);
        mLoadingView = findViewById(R.id.loading_view);
        mErrorMessage = (TextView) findViewById(R.id.error_message_text_view);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);

        if (savedInstanceState != null) {
            mCurrentCategory = MenuCategories.getMenuCategories(savedInstanceState.getInt(CURRENT_CATEGORY));
            mCurrentPosition = savedInstanceState.getInt(CURRENT_POSITION);
        }

        setActionBar(true);
        loadMainFragment();
        setDrawer();
        setMenuAdapter();
    }

    private void loadMainFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MainFragment mainFragment = MainFragment.newInstance();
        ft.replace(R.id.main_container, mainFragment, TAG);
        ft.commit();
    }

    private void setDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setDrawerHeader();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setDrawerHeader() {
        View header = getLayoutInflater().inflate(R.layout.drawer_header, null);
        mMenButton = (Button) header.findViewById(R.id.category_men);
        mWomenButton = (Button) header.findViewById(R.id.category_women);
        mMenButton.setOnClickListener(this);
        mWomenButton.setOnClickListener(this);

        if (mCurrentCategory == MenuCategories.MEN) {
            mMenButton.setActivated(true);
        } else {
            mWomenButton.setActivated(true);
        }

        mDrawerList.addHeaderView(header, null, false);
    }

    private void setMenuAdapter() {
        Api.loadCategories(mCurrentCategory, new Response.Listener<Menu>() {
                    @Override
                    public void onResponse(Menu menu) {
                        Category[] categories = menu.Listing;
                        mMenuAdapter = new SlidingAdapter(MainActivity.this);
                        for (Category category : categories) {
                            mMenuAdapter.add(new SlidingItem(category));
                        }
                        mDrawerList.setAdapter(mMenuAdapter);
                        selectItem(mCurrentPosition);
                        mLoadingView.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "Error loading menu", volleyError);

                        mLoadingProgressBar.setVisibility(View.GONE);
                        mErrorMessage.setVisibility(View.VISIBLE);
                        int resIdMessage = volleyError.getCause() instanceof UnknownHostException ?
                                R.string.error_no_connectivity : R.string.error_loading_data;
                        mErrorMessage.setText(resIdMessage);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_men:
                mMenButton.setActivated(true);
                mWomenButton.setActivated(false);
                mCurrentCategory = MenuCategories.MEN;
                setMenuAdapter();
                break;
            case R.id.category_women:
                mMenButton.setActivated(false);
                mWomenButton.setActivated(true);
                mCurrentCategory = MenuCategories.WOMEN;
                setMenuAdapter();
                break;
        }
    }

    private class SlidingItem {
        private Category category;

        public SlidingItem(Category category) {
            this.category = category;
        }
    }

    public class SlidingAdapter extends ArrayAdapter<SlidingItem> {

        public SlidingAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, null);
            }
            TextView title = (TextView) convertView.findViewById(R.id.category_name_text_view);
            title.setText(getItem(position).category.Name);
            title.setTag(position);
            convertView.setTag(position);

            return convertView;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position - 1);
            mDrawerLayout.closeDrawers();
        }
    }

    private void selectItem(int position) {
        mCurrentPosition = position;
        Category category = mMenuAdapter.getItem(position).category;
        ShoppingApplication.getBus().post(new UpdateCategoryEvent(category));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(CURRENT_CATEGORY, mCurrentCategory.value);
        outState.putInt(CURRENT_POSITION, mCurrentPosition);
    }
}