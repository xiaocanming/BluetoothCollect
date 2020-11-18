package com.example.ming.bluetoothcollect.service;

import com.example.ming.bluetoothcollect.controller.AnalysisManager;
import com.example.ming.bluetoothcollect.controller.DbController;
import com.example.ming.bluetoothcollect.controller.DbManager;
import com.example.ming.bluetoothcollect.model.Device;
import com.example.ming.bluetoothcollect.model.MessageEvent;
import com.example.ming.bluetoothcollect.model.NotifyInfo;
import com.example.ming.bluetoothcollect.util.AnalysisTool;
import com.example.ming.bluetoothcollect.util.StringTool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SettingService {

    private static AnalysisTool analysisClient = AnalysisManager.getClient();

    public static void SaveBleNotifyData(String address, byte[] value) {
        MessageEvent event = analysisClient.TryParse(value);
        switch (event.getType()) {
            case TimeBack:
                TimeBackThread time = new TimeBackThread(event.getMessage());
                Thread timeThread = new Thread(time);
                timeThread.start();    // 启动多线程
                break;
            case BatteryBack:
                BatteryBackThread battery = new BatteryBackThread(event.getMessage());
                Thread batteryThread = new Thread(battery);
                batteryThread.start();    // 启动多线程
                break;
            case ReceiveBack:
                ReceiveBackThread receive = new ReceiveBackThread(event.getMessage());
                Thread receiveThread = new Thread(receive);
                receiveThread.start();    // 启动多线程
                break;
            case RealData:
                RealDataThread real = new RealDataThread(event.getMessage(),address);
                Thread realThread = new Thread(real);
                realThread.start();    // 启动多线程
                break;
            case HistoryData:
                HistoryDataThread history = new HistoryDataThread(event.getMessage(),address);
                Thread historyThread = new Thread(history);
                historyThread.start();    // 启动多线程
                break;
            case ErrorData:
                ErrorDataThread error = new ErrorDataThread(event.getMessage());
                Thread errorThread = new Thread(error);
                errorThread.start();    // 启动多线程
                break;
        }
    }
}

//修改时间
class TimeBackThread implements Runnable { // 实现Runnable接口，作为线程的实现类
    private byte[] value;       // 表示线程的名称

    public TimeBackThread(byte[] value) {
        this.value = value;      // 通过构造方法配置name属性
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体
        DbController mClient = DbManager.getClient();
        Date d1 = StringTool.getDataByBytes(value, 1);
        mClient.updateDeviceInfo(d1, null);
    }
};

//修改电量
class BatteryBackThread implements Runnable { // 实现Runnable接口，作为线程的实现类
    private byte[] value;       // 表示线程的名称

    public BatteryBackThread(byte[] value) {
        this.value = value;      // 通过构造方法配置name属性
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体
        DbController mClient = DbManager.getClient();
        Double d1 = StringTool.getBatteryByBytes(value);
        mClient.updateDeviceInfo(null, d1);
    }
};

//数据接收中
class ReceiveBackThread implements Runnable { // 实现Runnable接口，作为线程的实现类
    private byte[] value;       // 表示线程的名称

    public ReceiveBackThread(byte[] value) {
        this.value = value;      // 通过构造方法配置name属性
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体

    }
};

//实时数据
class RealDataThread implements Runnable { // 实现Runnable接口，作为线程的实现类
    private byte[] value;       // 表示线程的名称
    private String address;       // 表示线程的名称

    public RealDataThread(byte[] value,String address) {
        this.value = value;      // 通过构造方法配置name属性
        this.address=address;
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体
        DbController mClient = DbManager.getClient();
        //获取设备信息
        Device newUseDevice= DbManager.getClient().searchDeviceInfo();
        Calendar calendar = Calendar.getInstance();
        //获取数据开始时间
        Date startDate = StringTool.getDataByBytes(value, 8);
        //获取早间隔
        int dayinterval=StringTool.ByteInt_Single(value[14]);
        //获取晚间隔
        int negihtinterval=StringTool.ByteInt_Single(value[15]);
        List<NotifyInfo> NotifyInfoList=new ArrayList<>();
        for(int i=0;i<111;i++){
            //判断时间为白天还是晚上
            NotifyInfo info=new NotifyInfo();
            info.setCreatetime(calendar.getTime());
            info.setTime(startDate);
            info.setAddress(address);
            Calendar cal=Calendar.getInstance();
            cal.setTime(startDate);
            if(6<cal.HOUR_OF_DAY&&cal.HOUR_OF_DAY<18){
                startDate = new Date(startDate .getTime() + 60000*dayinterval);
            }else {
                startDate = new Date(startDate .getTime() + 60000*negihtinterval);
            }
        }
    }
};

//历史数据
class HistoryDataThread implements Runnable { // 实现Runnable接口，作为线程的实现类
    private byte[] value;       // 表示线程的名称
    private String address;       // 表示线程的名称

    public HistoryDataThread(byte[] value,String address) {
        this.value = value;      // 通过构造方法配置name属性
        this.address=address;
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体
        DbController mClient = DbManager.getClient();
        //获取设备信息
        Device newUseDevice= DbManager.getClient().searchDeviceInfo();
        Calendar calendar = Calendar.getInstance();
        //获取数据开始时间
        Date startDate = StringTool.getDataByBytes(value, 8);
        //获取早间隔
        int dayinterval=StringTool.ByteInt_Single(value[14]);
        //获取晚间隔
        int negihtinterval=StringTool.ByteInt_Single(value[15]);
        List<NotifyInfo> NotifyInfoList=new ArrayList<>();
        for(int i=0;i<111;i++){
            //判断时间为白天还是晚上
            NotifyInfo info=new NotifyInfo();
            info.setCreatetime(calendar.getTime());
            info.setTime(startDate);
            info.setAddress(address);
            Calendar cal=Calendar.getInstance();
            cal.setTime(startDate);
            if(6<cal.HOUR_OF_DAY&&cal.HOUR_OF_DAY<18){
                startDate = new Date(startDate .getTime() + 60000*dayinterval);
            }else {
                startDate = new Date(startDate .getTime() + 60000*negihtinterval);
            }
        }
    }
};

//错误数据
class ErrorDataThread implements Runnable { // 实现Runnable接口，作为线程的实现类
    private byte[] value;       // 表示线程的名称

    public ErrorDataThread(byte[] value) {
        this.value = value;      // 通过构造方法配置name属性
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体

    }
};

