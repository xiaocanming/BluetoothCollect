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

    @Convert(converter = StringDateConverter.class, columnType = String.class)
    private Date time; //设备时间

    private Double battery;//电量

    @Generated(hash = 1940866757)
    public DeviceInfo(Long id, String address, String name, Date time,
            Double battery) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.time = time;
        this.battery = battery;
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

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getBattery() {
        return this.battery;
    }

    public void setBattery(Double battery) {
        this.battery = battery;
    }

}
