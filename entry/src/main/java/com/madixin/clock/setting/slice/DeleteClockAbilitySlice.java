package com.madixin.clock.setting.slice;

import com.madixin.clock.common.util.LogUtil;
import com.madixin.clock.setting.ResourceTable;
import com.madixin.clock.setting.manager.ClockManager;
import com.madixin.clock.setting.provider.DeleteClockItemProvider;
import ohos.aafwk.ability.AbilitySlice;
import com.madixin.clock.setting.model.Clock;

import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;


import java.util.List;

public class DeleteClockAbilitySlice extends AbilitySlice {
    private static final String TAG = DeleteClockAbilitySlice.class.getName();

    private ListContainer listContainer;

    private Checkbox checkboxSelectAll;

    private DeleteClockItemProvider listViewItemProvider;

    private boolean isModify = false;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_delete_clock);
        initView();
        initListContainer();
    }

    private void initListContainer() {
        listContainer = (ListContainer) this.findComponentById(ResourceTable.Id_list_clock_view);
        listViewItemProvider = new DeleteClockItemProvider(this);

        List<Clock> clockList = getAllClocks();
        // 默认所有闹钟取消选中
        clockList.stream().forEach(clock -> {
            clock.setEnable(false);
        });

        listViewItemProvider.setDataList(clockList);
        listContainer.setItemProvider(listViewItemProvider);
    }

    private List<Clock> getAllClocks() {
        return ClockManager.getInstance(this.getApplicationContext()).getAllClocks();
    }

    private void initView() {
        Image imageCancel = (Image) findComponentById(ResourceTable.Id_image_cancel);
        imageCancel.setClickedListener(component -> {
            backToMainSlice(isModify);
        });

        checkboxSelectAll = (Checkbox) findComponentById(ResourceTable.Id_checkbox_selectall);
        checkboxSelectAll.setCheckedStateChangedListener((absButton, isCheck) -> {
            LogUtil.info(TAG,"setCheckedStateChangedListener: " + isCheck);
            List<Clock> clockList = listViewItemProvider.getDataList();
            for (Clock clock : clockList) {
                clock.setEnable(isCheck);
            }
            listViewItemProvider.notifyDataChanged();
        });

        Button btnDelete = (Button) findComponentById(ResourceTable.Id_button_delete);
        btnDelete.setClickedListener((button) -> {
            List<Clock> clockList = listViewItemProvider.getDataList();
            int selectClock = 0;
            for (Clock clock : clockList) {
                if (clock.isEnable()) {
                    selectClock++;
                }
            }
            if (selectClock > 0) {
                CommonDialog alertDialog = new CommonDialog(this);
                TextField textFieldName = new TextField(this);
                textFieldName.setText(String.format("是否删%d个闹钟?", selectClock));
                textFieldName.setTextSize(20, Text.TextSizeType.FP);
                DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig();
                layoutConfig.alignment = LayoutAlignment.CENTER;

                textFieldName.setLayoutConfig(layoutConfig);

                alertDialog.setContentCustomComponent(textFieldName);
                alertDialog.setButton(IDialog.BUTTON1, "取消", new IDialog.ClickedListener() {
                    @Override
                    public void onClick(IDialog iDialog, int i) {
                        iDialog.destroy();
                    }
                });
                alertDialog.setButton(IDialog.BUTTON3, "删除", new IDialog.ClickedListener() {
                    @Override
                    public void onClick(IDialog iDialog, int i) {
                        iDialog.destroy();
                        deleteClocks();
                        //backToMainSlice(true);

                        // 重新加载闹钟数据
                        List<Clock> clockList = getAllClocks();
                        // 默认所有闹钟取消选中
                        clockList.stream().forEach(clock -> {
                            clock.setEnable(false);
                        });
                        listViewItemProvider.setDataList(clockList);
                        listViewItemProvider.notifyDataChanged();

                        checkboxSelectAll.setChecked(false);
                    }
                });
                alertDialog.show();
            }
        });

    }

    /**
     * 删除选中闹钟
     */
    private void deleteClocks() {
        this.isModify = true;
        List<Clock> clockList = listViewItemProvider.getDataList();
        for (Clock clock : clockList) {
            if (clock.isEnable()) {
                int res = ClockManager.getInstance(this.getApplicationContext()).deleteClock(clock);
                LogUtil.info(TAG,"success delete clock:" + res);
            }
        }
    }

    /**
     * 返回闹钟首页
     *
     * @param isRefresh 是否刷新界面
     */
    private void backToMainSlice(boolean isRefresh) {
        Intent resultIntent = new Intent();
        resultIntent.setParam("isRefresh", isRefresh);
        setResult(resultIntent);
        terminate();
    }
}
