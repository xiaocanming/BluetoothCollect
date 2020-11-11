package com.example.ming.bluetoothcollect.model;

import com.example.ming.bluetoothcollect.util.StringDateConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

@Entity
public class NotifyInfo {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    private String address;

    private String service;

    private String character;

    @Convert(converter = StringDateConverter.class, columnType = String.class)
    private Date createtime;

    private String  message;

    @Generated(hash = 443305637)
    public NotifyInfo(Long id, String address, String service, String character,
            Date createtime, String message) {
        this.id = id;
        this.address = address;
        this.service = service;
        this.character = character;
        this.createtime = createtime;
        this.message = message;
    }

    @Generated(hash = 859430374)
    public NotifyInfo() {
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

    public Date getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
