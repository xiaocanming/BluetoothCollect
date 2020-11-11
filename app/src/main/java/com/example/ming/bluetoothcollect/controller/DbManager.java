package com.example.ming.bluetoothcollect.controller;

import com.example.ming.bluetoothcollect.base.MainApplication;

public class DbManager {
    private static DbController mClient;

    public static DbController getClient() {
        if (mClient == null) {
            synchronized (DbController.class) {
                if (mClient == null) {
                    mClient = new DbController(MainApplication.getContext());
                }
            }
        }
        return mClient;
    }

}