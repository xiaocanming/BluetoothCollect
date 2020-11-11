package com.example.ming.bluetoothcollect.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.ming.bluetoothcollect.MainActivity;
import com.example.ming.bluetoothcollect.R;
import com.example.ming.bluetoothcollect.base.MainApplication;
import com.example.ming.bluetoothcollect.controller.ClientManager;
import com.example.ming.bluetoothcollect.controller.DbManager;
import com.example.ming.bluetoothcollect.model.DeviceDetailInfo;
import com.example.ming.bluetoothcollect.model.DeviceInfo;
import com.example.ming.bluetoothcollect.util.BluetoothTool;
import com.example.ming.bluetoothcollect.util.TipsTool;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

public class FileController  extends HomeController {
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    public FileController(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fragment_home3, this);
        ButterKnife.bind(this);
        initGroupListView();
    }

    @OnClick(R.id.h_head)
    public void onViewClicked1() {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setTitle("导出日志")
                .setMessage("确定导出当前保存的数据吗？")
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
                        TipsTool.showtipDialog( getContext(),"保存成功",QMUITipDialog.Builder.ICON_TYPE_SUCCESS);
                    }
                })
                .create().show();
    }

    private void initGroupListView() {
        int height = QMUIResHelper.getAttrDimen(getContext(), com.qmuiteam.qmui.R.attr.qmui_list_item_height);
        //日志信息
        QMUICommonListItemView itemWithDetailBelowWithChevronWithIcon1 = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_listitem_file),
                "20200827.log",
                "/mun/bluetooth/20200827.log",
                QMUICommonListItemView.VERTICAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        //日志信息
        QMUICommonListItemView itemWithDetailBelowWithChevronWithIcon2 = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_listitem_file),
                "20200827.log",
                "/mun/bluetooth/20200827.log",
                QMUICommonListItemView.VERTICAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        //日志信息
        QMUICommonListItemView itemWithDetailBelowWithChevronWithIcon3 = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_listitem_file),
                "20200827.log",
                "/mun/bluetooth/20200827.log",
                QMUICommonListItemView.VERTICAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        //日志信息
        QMUICommonListItemView itemWithDetailBelowWithChevronWithIcon4 = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_listitem_file),
                "20200827.log",
                "/mun/bluetooth/20200827.log",
                QMUICommonListItemView.VERTICAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivity(intent);
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        QMUIGroupListView.newSection(getContext())
                .setTitle("日志列表")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemWithDetailBelowWithChevronWithIcon1, onClickListener)
                .addItemView(itemWithDetailBelowWithChevronWithIcon2, onClickListener)
                .addItemView(itemWithDetailBelowWithChevronWithIcon3, onClickListener)
                .addItemView(itemWithDetailBelowWithChevronWithIcon4, onClickListener)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                .addTo(mGroupListView);
    }
}
