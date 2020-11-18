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

    @Convert(converter = StringDateConverter.class, columnType = String.class)
    private Date createtime;

    @Convert(converter = StringDateConverter.class, columnType = String.class)
    private Date time;

    private Double  message;

    private int  type;//1实时数据 2历史数据

    @Generated(hash = 14744991)
    public NotifyInfo(Long id, String address, Date createtime, Date time,
            Double message, int type) {
        this.id = id;
        this.address = address;
        this.createtime = createtime;
        this.time = time;
        this.message = message;
        this.type = type;
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

    public Date getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getMessage() {
        return this.message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
