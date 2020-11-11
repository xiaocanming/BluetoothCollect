package com.example.ming.bluetoothcollect.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DeviceInfo {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    @Index(unique = true)//设置唯一性
    private String address;//人员编号

    private String name;//人员姓名

    private boolean isuse;//人员姓名

    @Generated(hash = 863249134)
    public DeviceInfo(Long id, String address, String name, boolean isuse) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.isuse = isuse;
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

    public boolean getIsuse() {
        return this.isuse;
    }

    public void setIsuse(boolean isuse) {
        this.isuse = isuse;
    }

}
