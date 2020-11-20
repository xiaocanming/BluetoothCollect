package com.example.ming.bluetoothcollect.model;

import com.example.ming.bluetoothcollect.util.StringDateConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

@Entity
public class DeviceInfo {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    @Index(unique = true)//设置唯一性
    private String address;//设备地址

    private String name;//设备名称

    private int dayinterval;//早间隔

    private int negihtinterval;//晚间隔

    @Generated(hash = 1960340631)
    public DeviceInfo(Long id, String address, String name, int dayinterval,
            int negihtinterval) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.dayinterval = dayinterval;
        this.negihtinterval = negihtinterval;
    }

    @Generated(hash = 2125166935)
    public DeviceInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDayinterval() {
        return this.dayinterval;
    }

    public void setDayinterval(int dayinterval) {
        this.dayinterval = dayinterval;
    }

    public int getNegihtinterval() {
        return this.negihtinterval;
    }

    public void setNegihtinterval(int negihtinterval) {
        this.negihtinterval = negihtinterval;
    }

}
