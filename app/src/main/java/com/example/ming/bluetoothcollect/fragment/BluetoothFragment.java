package com.example.ming.bluetoothcollect.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ming.bluetoothcollect.R;
import com.example.ming.bluetoothcollect.base.BaseFragment;
import com.example.ming.bluetoothcollect.base.BaseRecyclerAdapter;
import com.example.ming.bluetoothcollect.base.RecyclerViewHolder;
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
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.SwipeBackLayout;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;


public class BluetoothFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout mPullRefreshLayout;
    @BindView(R.id.listview)
    RecyclerView mListView;
    private BaseRecyclerAdapter<SearchResult> mAdapter;
    private List<SearchResult> mDevices;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bluetooth, null);
        ButterKnife.bind(this, root);
        initTopBar();
        initList();
        searchDevice();
        return root;
    }

    private void initTopBar() {
        mTopBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.app_color_theme_6));
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mTopBar.setTitle(R.string.string_settingtitle);
    }

    private void initList() {
        //初始化蓝牙设备
        mDevices = new ArrayList<SearchResult>();

        mListView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mAdapter = new BaseRecyclerAdapter<SearchResult>(getActivity(), null) {
            @Override
            public int getItemLayoutId(int viewType) {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, SearchResult item) {
                holder.setText(android.R.id.text1, item);
            }

        };

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, final int pos) {
                final SearchResult item = (SearchResult) mAdapter.getItem(pos);
                new QMUIDialog.MessageDialogBuilder(getContext())
                        .setTitle("连接蓝牙")
                        .setMessage("确定连接该设备["+item.getName()+"]吗？")
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
                                final QMUITipDialog tipDialog;
                                tipDialog = new QMUITipDialog.Builder(getContext())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                        .setTipWord("连接中" )
                                        .create();
                                tipDialog.show();
                                ClientManager.getClient().connect(item.getAddress(), BluetoothTool.getBleConnectOptions(), new BleConnectResponse() {
                                    @Override
                                    public void onResponse(int code, BleGattProfile profile) {
                                        tipDialog.dismiss();
                                        if (code == REQUEST_SUCCESS) {
                                            //获取service列表
                                            List<DeviceDetailInfo> items = new ArrayList<DeviceDetailInfo>();
                                            List<BleGattService> services = profile.getServices();
                                            for (BleGattService service : services) {
                                                items.add(new DeviceDetailInfo(null, item.getAddress(),0,service.getUUID().toString(), ""));
                                                List<BleGattCharacter> characters = service.getCharacters();
                                                for (BleGattCharacter character : characters) {
                                                    items.add(new DeviceDetailInfo(null, item.getAddress(),1,service.getUUID().toString(), character.getUuid().toString()));
                                                }
                                            }
                                            //添加
                                            DeviceInfo deviceInfo = new DeviceInfo(null,item.getAddress(),item.getName(),true);
                                            DbManager.getClient().insertDeviceInfo(deviceInfo,items);
                                            popBackStack();
                                        }
                                        else {
                                            TipsTool.showtipDialog(getContext(), "连接失败",QMUITipDialog.Builder.ICON_TYPE_FAIL);
                                        }
                                    }
                                });
                            }
                        })
                        .create().show();


            }
        });

        mListView.setAdapter(mAdapter);

        mPullRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                searchDevice();
            }
        });
    }

    private void searchDevice() {
        ClientManager.getClient().search(BluetoothTool.getSearchRequest(), mSearchResponse);
    }

    /**
     * 查找设备
     */
    private final SearchResponse mSearchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            mPullRefreshLayout.finishRefresh();
            mTopBar.setTitle(R.string.string_settingrefreshing);
            mDevices.clear();
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            if (!mDevices.contains(device)) {
                mDevices.add(device);
                mAdapter.setData(mDevices);
            }
        }

        @Override
        public void onSearchStopped() {
            mTopBar.setTitle(R.string.string_settingtitle);
            mPullRefreshLayout.finishRefresh();
        }

        @Override
        public void onSearchCanceled() {
            mTopBar.setTitle(R.string.string_settingtitle);
            mPullRefreshLayout.finishRefresh();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        ClientManager.getClient().stopSearch();
    }

    @Override
    public QMUIFragment.TransitionConfig onFetchTransitionConfig() {
        return SCALE_TRANSITION_CONFIG;
    }

    @Override
    protected SwipeBackLayout.ViewMoveAction dragViewMoveAction() {
        return SwipeBackLayout.MOVE_VIEW_TOP_TO_BOTTOM;
    }
}
