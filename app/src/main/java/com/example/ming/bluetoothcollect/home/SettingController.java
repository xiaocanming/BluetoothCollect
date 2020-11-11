package com.example.ming.bluetoothcollect.home;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.ming.bluetoothcollect.R;
import com.example.ming.bluetoothcollect.base.MainApplication;
import com.example.ming.bluetoothcollect.controller.ClientManager;
import com.example.ming.bluetoothcollect.controller.DbManager;
import com.example.ming.bluetoothcollect.fragment.BluetoothFragment;
import com.example.ming.bluetoothcollect.model.Device;
import com.example.ming.bluetoothcollect.model.DeviceDetailInfo;
import com.example.ming.bluetoothcollect.model.DeviceInfo;
import com.example.ming.bluetoothcollect.model.DeviceService;
import com.example.ming.bluetoothcollect.model.NotifyInfo;
import com.example.ming.bluetoothcollect.model.ProvinceBean;
import com.example.ming.bluetoothcollect.util.BluetoothTool;
import com.example.ming.bluetoothcollect.util.StringTool;
import com.example.ming.bluetoothcollect.util.TipsTool;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

public class SettingController extends HomeController {
    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    //正在使用设备信息
    private Device isUseDevice;
    private boolean mConnected;
    //采集周期选择器
    private OptionsPickerView pvCollectOptions;
    private ArrayList<ProvinceBean> options1ItemsCollect = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> options2ItemsCollect = new ArrayList<>();
    //日志周期选择器
    private OptionsPickerView pvLogOptions;
    private ArrayList<ProvinceBean> options1ItemsLog = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> options2ItemsLog = new ArrayList<>();
    //设备信息
    //使用设备
    private QMUICommonListItemView itemWithUseDevice;
    View.OnClickListener itemWithUseDeviceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof QMUICommonListItemView) {
                if(!mConnected){
                }
            }
        }
    };
    //设备时间
    private QMUICommonListItemView itemWithDeviceTime;
    View.OnClickListener itemWithDeviceTimeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof QMUICommonListItemView) {
                new QMUIDialog.MessageDialogBuilder(getContext())
                        .setTitle("设备时间")
                        .setMessage("读取和设定设备时间！")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "读取时间", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                if(isUseDevice!=null){
                                    if(isUseDevice.getDatedeviceservice()!=null){
                                        ClientManager.getClient().read(isUseDevice.getAddress(), isUseDevice.getDatedeviceservice().getService(), isUseDevice.getDatedeviceservice().getCharacter(), mReadRsp);
                                    }
                                }
                            }
                        })
                        .addAction(0, "同步时间", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                if(isUseDevice!=null){
                                    if(isUseDevice.getDatedeviceservice()!=null){
                                        ClientManager.getClient().write(isUseDevice.getAddress(), isUseDevice.getDatedeviceservice().getService(), isUseDevice.getDatedeviceservice().getCharacter(),ByteUtils.stringToBytes(StringTool.getStringByNowDate()), mWriteRsp);
                                    }
                                }
                            }
                        })
                        .create().show();



            }
        }
    };
    private final BleReadResponse mReadRsp = new BleReadResponse() {
        @Override
        public void onResponse(int code, byte[] data) {
            if (code == REQUEST_SUCCESS) {
                itemWithDeviceTime.setDetailText(StringTool.getDateStringByString(ByteUtils.byteToString(data)));
            } else {
                itemWithDeviceTime.setDetailText("未读取到数据！");
            }
        }
    };
    private final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                TipsTool.showtipDialog(getContext(),"写入时间成功",QMUITipDialog.Builder.ICON_TYPE_SUCCESS);
            } else {
                TipsTool.showtipDialog(getContext(),"写入时间失败",QMUITipDialog.Builder.ICON_TYPE_FAIL);
            }
        }
    };
    //设备设置
    //开启采集
    private QMUICommonListItemView itemWithCollectSwitch;
    View.OnClickListener itemWithCollectSwitcheOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof QMUICommonListItemView) {
                ((QMUICommonListItemView) v).getSwitch().toggle();
            }
        }
    };
    //采集周期
    private QMUICommonListItemView itemWithCollectCycle;
    View.OnClickListener itemWithCollectCycleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof QMUICommonListItemView) {
                pvCollectOptions.show();
            }
        }
    };

    //日志设置
    //数据保存周期
    private QMUICommonListItemView itemWithLogCycle;
    View.OnClickListener itemWithLogCycleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof QMUICommonListItemView) {
                pvLogOptions.show();
            }
        }
    };
    //查看监听数据
    private QMUICommonListItemView itemWithDetail;
    View.OnClickListener itemWithDetailOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof QMUICommonListItemView) {
                if(isUseDevice!=null){
                    List<NotifyInfo> newUseDevice= DbManager.getClient().searchNotifyInfoByWhere(isUseDevice.getAddress());
                    String message="";
                    for(NotifyInfo notifyInfo :newUseDevice){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        message+=format.format(notifyInfo.getCreatetime())+notifyInfo.getMessage()+"\n\n";
                    }
                    new QMUIDialog.MessageDialogBuilder(getContext())
                            .setTitle("监听数据")
                            .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                            .setMessage(message)
                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
                }

            }
        }
    };

    public SettingController(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fragment_home1, this);
        ButterKnife.bind(this);
        initTopBar();
        initGroupListView();
        connectedBuletoothDevice();
        getOptionData();
        initOptionPicker();
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }
    //重新加载页面
    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        connectedBuletoothDevice();
        super.dispatchRestoreInstanceState(container);
    }

    private void initTopBar() {
        mTopBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.app_color_theme_6));
        mTopBar.setTitle("设备管理");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_bluetooth, QMUIViewHelper.generateViewId()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( BluetoothTool.ChikcBlue(getContext())){
                    BluetoothFragment fragment = new BluetoothFragment();
                    startFragment(fragment);
                }
            }
        });
    }
    private void connectedBuletoothDevice() {
        //获取正在使用蓝牙
        Device newUseDevice= DbManager.getClient().searchIsUseDevice();
        if(newUseDevice==null){
            mConnected=false;
            isUseDevice=null;
        }else {
            //判断是否更换蓝牙
            if(isUseDevice==null){
                ClientManager.getClient().registerConnectStatusListener(newUseDevice.getAddress(), mConnectStatusListener);
                isUseDevice=newUseDevice;
                connectDeviceIfNeeded();
            }
            else if (!isUseDevice.getAddress().equals(newUseDevice.getAddress())){
                ClientManager.getClient().unregisterConnectStatusListener(isUseDevice.getAddress(), mConnectStatusListener);
                ClientManager.getClient().registerConnectStatusListener(newUseDevice.getAddress(), mConnectStatusListener);
                isUseDevice=newUseDevice;
                mConnected=false;
                connectDeviceIfNeeded();
            }
        }
    }
    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            mConnected = (status == STATUS_CONNECTED);
            connectDeviceIfNeeded();
        }
    };
    private void connectDeviceIfNeeded() {
        if (!mConnected) {
            itemWithUseDevice.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
            QMUILoadingView loadingView1 = new QMUILoadingView(getContext());
            itemWithUseDevice.addAccessoryCustomView(loadingView1);
            itemWithUseDevice.setDetailText("连接中");
            itemWithUseDevice.setText(isUseDevice.getName());
            ClientManager.getClient().connect(isUseDevice.getAddress(), BluetoothTool.getBleConnectOptions(), new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile profile) {
                    itemWithUseDevice.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);
                    itemWithUseDevice.setDetailText(code == REQUEST_SUCCESS?"已连接":"未连接");
                }
            });
        }
    }
    private void initGroupListView() {
        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        //设备信息

        //使用设备
        itemWithUseDevice = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_setting_selected),
                isUseDevice==null?"未连接到设备":isUseDevice.getName(),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithUseDevice.setDetailText(mConnected?"已连接":"未连接");
        //设备时间
        itemWithDeviceTime = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_listitem_time),
                "设备时间",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDeviceTime.setDetailText("---------");

        QMUIGroupListView.newSection(getContext())
                .setTitle("设备信息")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemWithUseDevice, itemWithUseDeviceOnClickListener)
                .addItemView(itemWithDeviceTime, itemWithDeviceTimeOnClickListener)
                .addTo(mGroupListView);

        //开启采集
        itemWithCollectSwitch = mGroupListView.createItemView("血糖仪数据获取开关");
        itemWithCollectSwitch.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithCollectSwitch.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isUseDevice!=null){
                    if(isUseDevice.getCollectdeviceservice()!=null){
                        if(isChecked){
                            ClientManager.getClient().notify(isUseDevice.getAddress(), isUseDevice.getCollectdeviceservice().getService(), isUseDevice.getCollectdeviceservice().getCharacter(), mNotifyRsp);
                        }else {
                            ClientManager.getClient().unnotify(isUseDevice.getAddress(), isUseDevice.getCollectdeviceservice().getService(), isUseDevice.getCollectdeviceservice().getCharacter(), mUnnotifyRsp);
                        }
                    }
                }
            }
        });
        //采集周期
        itemWithCollectCycle = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_listitem_cycle),
                "采集周期",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithCollectCycle.setDetailText("15 分钟");

        QMUIGroupListView.newSection(getContext())
                .setTitle("监听设置")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemWithCollectSwitch, itemWithCollectSwitcheOnClickListener)
                .addItemView(itemWithCollectCycle, itemWithCollectCycleOnClickListener)
                .addTo(mGroupListView);

        //日志设置
        //数据保留周期
        itemWithLogCycle = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_listitem_cycle),
                "日志保留周期",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithLogCycle.setDetailText("14天");
        int height = QMUIResHelper.getAttrDimen(getContext(), com.qmuiteam.qmui.R.attr.qmui_list_item_height);
        itemWithDetail = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_listitem_file),
                "监听数据",
                "最新读取到的数据：---------",
                QMUICommonListItemView.VERTICAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        QMUIGroupListView.newSection(getContext())
                .setTitle("日志设置")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemWithLogCycle, itemWithLogCycleOnClickListener)
                .addItemView(itemWithDetail, itemWithDetailOnClickListener)
                .addTo(mGroupListView);
    }

    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(isUseDevice.getCollectdeviceservice().getService()) && character.equals(isUseDevice.getCollectdeviceservice().getCharacter())) {
                Toast.makeText(getContext(), String.format("%s", ByteUtils.byteToString(value)), Toast.LENGTH_SHORT).show();
                itemWithDetail.setDetailText("最新读取到的数据："+String.format("%s", ByteUtils.byteToString(value)));
                Calendar calendar = Calendar.getInstance();
                NotifyInfo notifyInfo = new NotifyInfo(null,isUseDevice.getAddress(),isUseDevice.getCollectdeviceservice().getService().toString(),isUseDevice.getCollectdeviceservice().getCharacter().toString(), calendar.getTime(),String.format("%s", ByteUtils.byteToString(value)));
                DbManager.getClient().insertNotifyInfo(notifyInfo);
            }
        }

        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                TipsTool.showtipDialog(getContext(),"打开监听成功",QMUITipDialog.Builder.ICON_TYPE_SUCCESS);
            } else {
                TipsTool.showtipDialog(getContext(),"打开监听失败",QMUITipDialog.Builder.ICON_TYPE_SUCCESS);
            }
        }
    };

    private final BleUnnotifyResponse mUnnotifyRsp = new BleUnnotifyResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                TipsTool.showtipDialog(getContext(),"关闭监听成功",QMUITipDialog.Builder.ICON_TYPE_SUCCESS);
            } else {
                TipsTool.showtipDialog(getContext(),"关闭监听失败",QMUITipDialog.Builder.ICON_TYPE_SUCCESS);
            }
        }
    };

    private void initOptionPicker() {//周期选择器

        /**
         * 采集周期选择器
         */
        pvCollectOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                int tx = options1ItemsCollect.get(options1).getUnitnum()
                        * options2ItemsCollect.get(options1).get(options2);
                itemWithCollectCycle.setDetailText(String.valueOf(tx) +" 分钟");
            }
        })
                .setTitleText("周期选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.DKGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        pvCollectOptions.setPicker(options1ItemsCollect, options2ItemsCollect);//二级选择器
        /**
         * 日志周期选择器
         */
        pvLogOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                int tx = options1ItemsLog.get(options1).getUnitnum()
                        * options2ItemsLog.get(options1).get(options2);
                itemWithLogCycle.setDetailText(String.valueOf(tx) +" 天");
            }
        })
                .setTitleText("周期选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.DKGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        pvLogOptions.setPicker(options1ItemsLog, options2ItemsLog);//二级选择器
    }

    private void getOptionData() {

        //采集周期
        options1ItemsCollect.add(new ProvinceBean(0, "分钟", 1));
        options1ItemsCollect.add(new ProvinceBean(1, "小时", 60));
        ArrayList<Integer> options2ItemsCollect_01 = new ArrayList<>();
        for(int i=1;i<=60;i++){
            options2ItemsCollect_01.add(i);
        }
        ArrayList<Integer> options2ItemsCollect_02 = new ArrayList<>();
        for(int i=1;i<=2;i++){
            options2ItemsCollect_02.add(i);
        }
        options2ItemsCollect.add(options2ItemsCollect_01);
        options2ItemsCollect.add(options2ItemsCollect_02);

        //日志周期
        options1ItemsLog.add(new ProvinceBean(0, "天", 1));
        options1ItemsLog.add(new ProvinceBean(1, "周", 7));
        //采集周期选择
        ArrayList<Integer> options2ItemsLog_01 = new ArrayList<>();
        for(int i=1;i<=30;i++){
            options2ItemsLog_01.add(i);
        }
        ArrayList<Integer> options2ItemsLog_02 = new ArrayList<>();
        for(int i=1;i<=4;i++){
            options2ItemsLog_02.add(i);
        }
        options2ItemsLog.add(options2ItemsLog_01);
        options2ItemsLog.add(options2ItemsLog_02);
    }


}