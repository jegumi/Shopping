package com.jegumi.shopping.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jegumi.shopping.R;
import com.jegumi.shopping.model.Menu;
import com.jegumi.shopping.network.Api;

import java.net.UnknownHostException;

public class WelcomeActivity extends Activity {

    private static final String TAG = WelcomeActivity.class.getSimpleName();

    private TextView mErrorMessage;
    private ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        mErrorMessage = (TextView) findViewById(R.id.error_message_text_view);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        loadMenu();
    }

    private void loadMenu() {
        Api.loadCategories(MainActivity.MenuCategories.MEN, new Response.Listener<Menu>() {
                    @Override
                    public void onResponse(Menu menu) {
                        openMainActivity(menu);
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
        setRetry();
    }

    private void setRetry() {
        mErrorMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                mErrorMessage.setVisibility(View.GONE);
                loadMenu();
            }
        });
    }

    private void openMainActivity(Menu menu) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(BaseActivity.EXTRA_MENU, menu);
        startActivity(intent);
    }
}
