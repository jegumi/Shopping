package com.jegumi.shopping.tests;

import android.view.View;

import com.jegumi.RobolectricGradleTestRunner;
import com.jegumi.footballdata.R;
import com.jegumi.footballdata.ui.ListCompetitionsActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
public class AndroidListCompetitionLayoutTest {

    @Test
    public void testTabletOrMobile() throws Exception {
        ListCompetitionsActivity activity = new ListCompetitionsActivity();

        ActivityController.of(activity).attach().create();

        boolean isTablet = activity.isTablet();
        View left = activity.findViewById(R.id.left_layout);
        assertTrue(isTablet ? left != null : left == null);
    }
}
