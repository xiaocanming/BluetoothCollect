package com.example.ming.bluetoothcollect.util;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

import com.example.ming.bluetoothcollect.base.MainApplication;
import com.example.ming.bluetoothcollect.controller.ClientManager;
import com.example.ming.bluetoothcollect.controller.DbManager;
import com.example.ming.bluetoothcollect.model.DeviceDetailInfo;
import com.example.ming.bluetoothcollect.model.DeviceInfo;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * 蓝牙工具类
 */
public class BluetoothTool {
    /**
     * 检查是否开启蓝牙
     */
    public static boolean ChikcBlue(Context context){
        BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
        //支持蓝牙模块
        if (blueadapter!=null){
            if (blueadapter.isEnabled()){
                return  true;
            }
            else {
                new QMUIDialog.MessageDialogBuilder(context)
                        .setTitle("打开蓝牙")
                        .setMessage("蓝牙功能尚未打开，是否打开蓝牙？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                if (turnOnBluetooth()){
                                    Toast.makeText(context, "打开蓝牙成功", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "打开蓝牙失败，请手动打开！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .create().show();
                return  false;
            }
        }else{//不支持蓝牙模块
            Toast.makeText(MainApplication.getContext(), "该设备不支持蓝牙或没有蓝牙模块", Toast.LENGTH_SHORT).show();
            return  false;
        }
    }
    /**
     * 强制开启当前 Android 设备的 Bluetooth
     * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
     */
    public static boolean turnOnBluetooth()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();

        if (bluetoothAdapter != null)
        {
            return bluetoothAdapter.enable();
        }
        return false;
    }

    /**
     * 蓝牙搜索设置
     */
    public  static SearchRequest getSearchRequest(){
        return new SearchRequest.Builder()
                .searchBluetoothLeDevice(5000, 2).build();
    }
    /**
     * 蓝牙连接设置
     */
    public  static BleConnectOptions getBleConnectOptions(){
        return  new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(20000)   // 连接超时20s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                .build();
    }
}
