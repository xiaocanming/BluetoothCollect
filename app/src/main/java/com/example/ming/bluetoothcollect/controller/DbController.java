package com.example.ming.bluetoothcollect.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.ming.bluetoothcollect.model.DaoMaster;
import com.example.ming.bluetoothcollect.model.DaoSession;
import com.example.ming.bluetoothcollect.model.Device;
import com.example.ming.bluetoothcollect.model.DeviceDetailInfo;
import com.example.ming.bluetoothcollect.model.DeviceDetailInfoDao;
import com.example.ming.bluetoothcollect.model.DeviceInfo;
import com.example.ming.bluetoothcollect.model.DeviceInfoDao;
import com.example.ming.bluetoothcollect.model.DeviceService;
import com.example.ming.bluetoothcollect.model.NotifyInfo;
import com.example.ming.bluetoothcollect.model.NotifyInfoDao;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattService;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class DbController {
    /**
     * Helper
     */
    private DaoMaster.DevOpenHelper mHelper;//获取Helper对象
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * DaoMaster
     */
    private DaoMaster mDaoMaster;
    /**
     * DaoSession
     */
    private DaoSession mDaoSession;
    /**
     * 上下文
     */
    private Context context;
    /**
     * dao
     */
    private DeviceDetailInfoDao deviceDetailInfoDao;
    private DeviceInfoDao deviceInfoDao;
    private NotifyInfoDao notifyInfoDao;

    /**
     * 初始化
     * @param context
     */
    public DbController(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context,"bluetooth.db", null);
        mDaoMaster =new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        deviceDetailInfoDao = mDaoSession.getDeviceDetailInfoDao();
        deviceInfoDao = mDaoSession.getDeviceInfoDao();
        notifyInfoDao = mDaoSession.getNotifyInfoDao();
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase(){
        if(mHelper == null){
            mHelper = new DaoMaster.DevOpenHelper(context,"bluetooth.db",null);
        }
        SQLiteDatabase db =mHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     * @return
     */
    private SQLiteDatabase getWritableDatabase(){
        if(mHelper == null){
            mHelper =new DaoMaster.DevOpenHelper(context,"bluetooth.db",null);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db;
    }


    /**
     * 蓝牙连接设备
     * @param deviceInfo
     * @param deviceDetailInfoList
     */
    public void insertDeviceInfo(DeviceInfo deviceInfo,List<DeviceDetailInfo> deviceDetailInfoList){
        //更改其他使用设备
        List<DeviceInfo>personInfors = (List<DeviceInfo>) deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.Isuse.eq(true)).list();
        if(personInfors!=null){
            for (DeviceInfo device:personInfors) {
                device.setIsuse(false);
                deviceInfoDao.update(device);
            }
        }
        //新增设备
        //判断设备是否存在
        DeviceInfo olddeviceInfo = deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.Address.eq(deviceInfo.getAddress())).build().unique();
        if(olddeviceInfo!=null){
            olddeviceInfo.setIsuse(true);
            deviceInfoDao.update(olddeviceInfo);
        }else {
            deviceInfoDao.insert(deviceInfo);
            deviceDetailInfoDao.insertInTx(deviceDetailInfoList);
        }
    }

    /**
     * 获取正在使用的蓝牙设备
     */
    public Device searchIsUseDevice(){
        Device device=new Device();
        List<DeviceInfo>deviceInfos = (List<DeviceInfo>) deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.Isuse.eq(true)).list();
        if(deviceInfos.size()>0){
            device.setAddress(deviceInfos.get(0).getAddress());
            device.setName(deviceInfos.get(0).getName());
            //获取服务信息
            List<DeviceDetailInfo>deviceDetailInfos = (List<DeviceDetailInfo>) deviceDetailInfoDao.queryBuilder().where(DeviceDetailInfoDao.Properties.Address.eq(device.getAddress())).list();
            for (DeviceDetailInfo deviceDetailInfo : deviceDetailInfos) {
                if(deviceDetailInfo.getType()==1){
                    if(deviceDetailInfo.getCharacter().toString().contains("ffe1")){
                        DeviceService item=new DeviceService();
                        item.setType(1);
                        item.setService(deviceDetailInfo.getService().equals("") ?null:UUID.fromString(deviceDetailInfo.getService()));
                        item.setCharacter( deviceDetailInfo.getCharacter().equals("") ?null: UUID.fromString(deviceDetailInfo.getCharacter()));
                        device.setCollectdeviceservice(item);
                    }
                    if(deviceDetailInfo.getCharacter().toString().contains("fff6")){
                        DeviceService item=new DeviceService();
                        item.setType(2);
                        item.setService(deviceDetailInfo.getService().equals("") ?null:UUID.fromString(deviceDetailInfo.getService()));
                        item.setCharacter( deviceDetailInfo.getCharacter().equals("") ?null: UUID.fromString(deviceDetailInfo.getCharacter()));
                        device.setDatedeviceservice(item);
                    }
                }
            }
        }
        else {
            return null;
        }
        return device;
    }


    /**
     * 新增通知消息
     * @param notifyInfo
     */
    public void insertNotifyInfo(NotifyInfo notifyInfo){
        notifyInfoDao.insert(notifyInfo);
    }

    /**
     * 按条件查询通知消息
     */
    public List<NotifyInfo> searchNotifyInfoByWhere(String address){
        List<NotifyInfo>notifyInfos = (List<NotifyInfo>) notifyInfoDao.queryBuilder().where(NotifyInfoDao.Properties.Address.eq(address)).list();
        return notifyInfos;
    }


}