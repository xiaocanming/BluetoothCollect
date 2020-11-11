package com.example.ming.bluetoothcollect.model;

import java.util.List;
import java.util.UUID;

public class Device {
    private String address;//人员编号

    private String name;//人员姓名

    private DeviceService datedeviceservice;//日期服务

    private DeviceService collectdeviceservice;//时间服务

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



