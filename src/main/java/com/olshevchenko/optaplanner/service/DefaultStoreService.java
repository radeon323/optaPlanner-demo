package com.olshevchenko.optaplanner.service;

import com.olshevchenko.optaplanner.entity.MapPoint;
import com.olshevchenko.optaplanner.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultStoreService {
    private static final String STORE_NAME = "Склад №1";

    @Transactional(readOnly = true)
    public List<Store> getMainStore() {
        List<Store> storeList = new ArrayList<>();
        Store mainStore = new Store(1, STORE_NAME, "-", new MapPoint(1, 50.08, 29.89));
        storeList.add(mainStore);
        return storeList;
    }
}
