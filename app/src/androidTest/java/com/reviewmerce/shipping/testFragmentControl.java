package com.reviewmerce.shipping;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by onebuy on 2016-05-09.
 */
@RunWith(AndroidJUnit4.class)
public class testFragmentControl {
    MainActivity mTestActivity;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mTestActivity = mActivityRule.getActivity();
    }

    @Test
    public void testSaveLoad()
    {
        try {
            String str = mTestActivity.connectTest();
            assertThat("success",is(str));
            Log.d("Test-BankDataLab","testSaveLoad() was succeed");
        }catch (Exception e)
        {
            Log.d("Test-BankDataLab","testSave() fail");
//            fail();
        }
    }



}
