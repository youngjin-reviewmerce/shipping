package com.reviewmerce.shipping;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.reviewmerce.shipping.PublicLaboratory.ShoppingDataLab;

/**
 * Created by onebuy on 2015-07-30.
 */
public class Thread_Load extends AsyncTask {

    // http://api.reviewmerce.com/v1/ui/main
    // http://api.reviewmerce.com/v1/ui/top?current_ui_ver=
    // http://api.reviewmerce.com/v1/ui/mid?current_ui_ver=
    // http://api.reviewmerce.com/v1/ui/bot?current_ui_ver=
    // http://api.reviewmerce.com/v1/search/query?keywords=
    // http://api.reviewmerce.com/v1/search/suggest?keywords=



    public Thread_Load( Handler handler, int nProcType) {


    }
    public void run() {
        long t = System.currentTimeMillis();
        {
            ShoppingDataLab dataLab = new ShoppingDataLab(null);
            dataLab.loadData_all();
        }
        long t2 = System.currentTimeMillis();
        String s = String.format("%d ms", t2 - t);
        Log.i("Network : ", s);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        run();
        return null;
    }
}

