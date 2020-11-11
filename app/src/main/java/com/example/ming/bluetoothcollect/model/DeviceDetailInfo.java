package com.example.ming.bluetoothcollect.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DeviceDetailInfo {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    private String address;//人员编号

    private int type;//SERVICE = 0 CHARACTER = 1

    private String service;//Service

    private String character;//Character

    @Generated(hash = 1731627940)
    public DeviceDetailInfo(Long id, String address, int type, String service,
            String character) {
        this.id = id;
        this.address = address;
        this.type = type;
        this.service = service;
        this.character = character;
    }

    @Generated(hash = 1634143685)
    public DeviceDetailInfo() {
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

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCharacter() {
        return this.character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

}
