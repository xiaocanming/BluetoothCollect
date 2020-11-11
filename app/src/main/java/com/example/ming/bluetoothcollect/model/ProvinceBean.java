package com.example.ming.bluetoothcollect.model;

import com.bigkoo.pickerview.model.IPickerViewData;

public class ProvinceBean implements IPickerViewData {
    private long id;
    private String name;
    private int unitnum;

    public ProvinceBean(long id,String name,int unitnum){
        this.id = id;
        this.name = name;
        this.unitnum = unitnum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitnum() {
        return unitnum;
    }

    public void setUnitnum(int unitnum) {
        this.unitnum = unitnum;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过getPickerViewText方法获取字符串显示出来。
    @Override
    public String getPickerViewText() {
        return name;
    }
}

