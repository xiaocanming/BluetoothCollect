package com.example.ming.bluetoothcollect.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.ming.bluetoothcollect.controller.AnalysisManager;
import com.example.ming.bluetoothcollect.controller.DbController;
import com.example.ming.bluetoothcollect.controller.DbManager;
import com.example.ming.bluetoothcollect.model.Device;
import com.example.ming.bluetoothcollect.model.MessageEvent;
import com.example.ming.bluetoothcollect.model.NotifyInfo;
import com.example.ming.bluetoothcollect.util.AnalysisTool;
import com.example.ming.bluetoothcollect.util.StringTool;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class SettingService {

    private static AnalysisTool analysisClient = AnalysisManager.getClient();

    public static void SaveBleNotifyData(ThreadPoolExecutor threadPoolExecutor, Handler mHandler, String address, byte[] value) {
        MessageEvent event = analysisClient.TryParse(value);
        switch (event.getType()) {
            case TimeBack:
                TimeBackThread time = new TimeBackThread(event.getMessage(), mHandler);
                Thread timeThread = new Thread(time);
                threadPoolExecutor.execute(timeThread);
                break;
            case BatteryBack:
                BatteryBackThread battery = new BatteryBackThread(event.getMessage(), mHandler);
                Thread batteryThread = new Thread(battery);
                threadPoolExecutor.execute(batteryThread);
                break;
            case ReceiveBack:
                ReceiveBackThread receive = new ReceiveBackThread(event.getMessage());
                Thread receiveThread = new Thread(receive);
                threadPoolExecutor.execute(receiveThread);
                break;
            case RealData:
                RealDataThread real = new RealDataThread(event.getMessage(), address);
                Thread realThread = new Thread(real);
                threadPoolExecutor.execute(realThread);
                break;
            case HistoryData:
                HistoryDataThread history = new HistoryDataThread(event.getMessage(), address);
                Thread historyThread = new Thread(history);
                threadPoolExecutor.execute(historyThread);
                break;
            case ErrorData:
                ErrorDataThread error = new ErrorDataThread(event.getMessage());
                Thread errorThread = new Thread(error);
                threadPoolExecutor.execute(errorThread);
                break;
        }
    }
}

//修改时间
class TimeBackThread implements Runnable { // 实现Runnable接口，作为线程的实现类
    private byte[] value;       // 表示线程的名称

    private Handler mHandler;

    public TimeBackThread(byte[] value, Handler mHandler) {
        this.value = value;      // 通过构造方法配置name属性
        this.mHandler = mHandler;
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体
        DbController mClient = DbManager.getClient();
        Date data = StringTool.getDataByBytes(value, 1);
        //需要数据传递，用下面方法；
        Message msg = new Message();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(data);
        msg.what=0;
        msg.obj = dateString;//可以是基本类型，可以是对象，可以是List、map等；
        mHandler.sendMessage(msg);
    }
};

//修改电量
class BatteryBackThread implements Runnable { // 实现Runnable接口，作为线程的实现类
    private byte[] value;       // 表示线程的名称
    private Handler mHandler;

    public BatteryBackThread(byte[] value, Handler mHandler) {
        this.value = value;      // 通过构造方法配置name属性
        this.mHandler = mHandler;
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体
        Double d1 = StringTool.getBatteryByBytes(value);
        mHandler.sendEmptyMessage(1);
        //需要数据传递，用下面方法；
        Message msg = new Message();
        msg.what=1;
        msg.obj = String.valueOf(d1);//可以是基本类型，可以是对象，可以是List、map等；
        mHandler.sendMessage(msg);
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

    public RealDataThread(byte[] value, String address) {
        this.value = value;      // 通过构造方法配置name属性
        this.address = address;
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体
        DbController mClient = DbManager.getClient();
        Calendar calendar = Calendar.getInstance();
        //获取数据开始时间
        Date startDate = StringTool.getDataByBytes(value, 0);
        List<NotifyInfo> NotifyInfoList = new ArrayList<>();
        for (int i = 8; i < value.length - 8; i = i + 2) {
            //判断时间为白天还是晚上
            NotifyInfo info = new NotifyInfo();
            info.setType(1);
            info.setCreatetime(calendar.getTime());
            info.setTime(startDate);
            info.setAddress(address);
            info.setMessage(Double.valueOf(StringTool.byte2short(value, i)));
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            startDate = new Date(startDate.getTime() + 100 * 1);
            NotifyInfoList.add(info);
        }
        mClient.insertNotifyInfo(NotifyInfoList);
    }
};

//历史数据
class HistoryDataThread implements Runnable { // 实现Runnable接口，作为线程的实现类
    private byte[] value;       // 表示线程的名称
    private String address;       // 表示线程的名称

    public HistoryDataThread(byte[] value, String address) {
        this.value = value;      // 通过构造方法配置name属性
        this.address = address;
    }

    public void run() {  // 覆写run()方法，作为线程 的操作主体
        DbController mClient = DbManager.getClient();
        //获取设备信息
        Device newUseDevice = DbManager.getClient().searchDeviceInfo();
        Calendar calendar = Calendar.getInstance();
        //获取数据开始时间
        Date startDate = StringTool.getDataByBytes(value, 8);
        //获取早间隔
        int dayinterval = StringTool.ByteInt_Single(value[14]);
        //获取晚间隔
        int negihtinterval = StringTool.ByteInt_Single(value[15]);
        List<NotifyInfo> NotifyInfoList = new ArrayList<>();
        for (int i = 16; i < value.length - 8; i = i + 2) {
            //判断时间为白天还是晚上
            NotifyInfo info = new NotifyInfo();
            info.setType(2);
            info.setCreatetime(calendar.getTime());
            info.setTime(startDate);
            info.setAddress(address);
            info.setMessage(Double.valueOf(StringTool.byte2short(value, i)));
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            if (6 < cal.HOUR_OF_DAY && cal.HOUR_OF_DAY < 18) {
                startDate = new Date(startDate.getTime() + 60000 * dayinterval);
            } else {
                startDate = new Date(startDate.getTime() + 60000 * negihtinterval);
            }
            NotifyInfoList.add(info);
        }
        mClient.insertNotifyInfo(NotifyInfoList);
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

