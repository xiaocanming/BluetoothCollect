package com.example.ming.bluetoothcollect.controller;

import com.example.ming.bluetoothcollect.base.MainApplication;
import com.inuker.bluetooth.library.BluetoothClient;

public class ClientManager {
    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(MainApplication.getInstance());
                }
            }
        }
        return mClient;
    }
}
