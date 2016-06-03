package com.reviewmerce.shipping;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by onebuy on 2016-05-19.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class OpeningActivityTest extends TestCase {

    @Rule
    public ActivityTestRule<OpeningActivity> testRule = new ActivityTestRule<OpeningActivity>(OpeningActivity.class);

    @Test
    public void checkHelloWorldText() {

        onView(withText("R.id."));


    }

    public void testOnCreate() throws Exception {

    }
}