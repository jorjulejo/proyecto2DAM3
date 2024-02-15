package com.example.app_android.manager;

import com.example.app_android.models.DataItem;

import java.util.List;

public interface DataLoadListener {
    void onDataLoaded(List<DataItem> items);
    void onError(Exception e);
}
