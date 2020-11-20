package com.example.ming.bluetoothcollect.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    private static String dbName="bluetooth1.db";
    /**
     * 初始化
     * @param context
     */
    public DbController(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context,dbName, null);
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
            mHelper = new DaoMaster.DevOpenHelper(context,dbName,null);
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
            mHelper =new DaoMaster.DevOpenHelper(context,dbName,null);
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
        //删除所有数据
        deviceInfoDao.deleteAll();
        deviceDetailInfoDao.deleteAll();
        //新增设备数据
        deviceInfoDao.insert(deviceInfo);
        deviceDetailInfoDao.insertInTx(deviceDetailInfoList);
    }


    /**
     * 获取正在使用的蓝牙设备
     */
    public Device searchDeviceInfo(){
        Device device=new Device();
        //获取设备信息
        List<DeviceInfo>deviceInfos = (List<DeviceInfo>) deviceInfoDao.queryBuilder().list();
        if(deviceInfos.size()>0){
            device.setAddress(deviceInfos.get(0).getAddress());
            device.setName(deviceInfos.get(0).getName());
            //获取服务信息
            List<DeviceDetailInfo>deviceDetailInfos = (List<DeviceDetailInfo>) deviceDetailInfoDao.queryBuilder().where(DeviceDetailInfoDao.Properties.Address.eq(device.getAddress())).list();
            for (DeviceDetailInfo deviceDetailInfo : deviceDetailInfos) {
                if(deviceDetailInfo.getType()==1){
                    if(deviceDetailInfo.getCharacter().toString().contains("ffe1")){
                        DeviceService item=new DeviceService();
                        item.setService(deviceDetailInfo.getService().equals("") ?null:UUID.fromString(deviceDetailInfo.getService()));
                        item.setCharacter( deviceDetailInfo.getCharacter().equals("") ?null: UUID.fromString(deviceDetailInfo.getCharacter()));
                        device.setCollectdeviceservice(item);
                    }
                    if(deviceDetailInfo.getCharacter().toString().contains("fff6")){
                        DeviceService item=new DeviceService();
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
     * @param notifyInfoList
     */
    public void insertNotifyInfo(List<NotifyInfo> notifyInfoList){
        notifyInfoDao.insertInTx(notifyInfoList);
    }

    /**
     * 按设备地址和日期查询通知消息
     */
    public List<NotifyInfo> searchNotifyInfoByWhere(String address,Date date){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String dateString=fmt.format(date);
        List<NotifyInfo>notifyInfos = (List<NotifyInfo>) notifyInfoDao.queryBuilder().where(NotifyInfoDao.Properties.Address.eq(address), NotifyInfoDao.Properties.Createdate.eq(dateString)).list();
        return notifyInfos;
    }

}