package com.reviewmerce.shipping.PublicLaboratory;

import java.util.HashMap;

/**
 * Created by onebuy on 2016-05-09.
 */
public interface BasicImplementDataLab {
    void alloc() throws Exception;
    void release() throws Exception;
    // type(int) : 종류

    void loadData(HashMap<String, String> mapArg) throws Exception;
    void saveData(HashMap<String, String> mapArg) throws Exception;
    void initData() throws Exception;
    void addJsonArray(String sArray) throws Exception;
    void removeData(int nIndex) throws Exception;
    void clearData() throws Exception;
}
