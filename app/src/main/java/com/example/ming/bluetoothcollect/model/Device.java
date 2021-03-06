package com.example.ming.bluetoothcollect.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Device {
    private String address;//设备地址

    private String name;//设备名称

    private DeviceService datedeviceservice;//0xFFF6

    private DeviceService collectdeviceservice;//0xFFE1

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceService getDatedeviceservice() {
        return datedeviceservice;
    }

    public void setDatedeviceservice(DeviceService datedeviceservice) {
        this.datedeviceservice = datedeviceservice;
    }

    public DeviceService getCollectdeviceservice() {
        return collectdeviceservice;
    }

    public void setCollectdeviceservice(DeviceService collectdeviceservice) {
        this.collectdeviceservice = collectdeviceservice;
    }

}



