package com.example.ming.bluetoothcollect.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

public class BaseFragment extends QMUIFragment {
    public BaseFragment() {
    }

    @Override
    protected View onCreateView() {
        return null;
    }

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(requireContext(), 100);
    }
}
