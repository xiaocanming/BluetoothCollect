package com.example.ming.bluetoothcollect.controller;

import com.example.ming.bluetoothcollect.base.MainApplication;
import com.example.ming.bluetoothcollect.util.AnalysisTool;

public class AnalysisManager {
    private static AnalysisTool mClient;

    public static AnalysisTool getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new AnalysisTool(MainApplication.getInstance());
                }
            }
        }
        return mClient;
    }
}
