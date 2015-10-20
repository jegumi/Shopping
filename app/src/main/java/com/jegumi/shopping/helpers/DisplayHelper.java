package com.jegumi.shopping.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.jegumi.shopping.ShoppingApplication;

public class DisplayHelper {

    public static Point getScreenSize() {
        WindowManager wm = (WindowManager) ShoppingApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    public static int getActionBarHeight() {
        int actionBarHeight = 0;
        Context context = ShoppingApplication.getContext();
        Resources res = context.getResources();
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, res.getDisplayMetrics());
        }
        return actionBarHeight;
    }

}
