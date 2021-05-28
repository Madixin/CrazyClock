package com.madixin.clock.setting.slice;

import com.madixin.clock.common.util.LogUtil;
import com.madixin.clock.common.util.ResourceUtil;
import com.madixin.clock.setting.ResourceTable;
import com.madixin.clock.setting.constants.DataConstants;
import com.madixin.clock.setting.model.Clock;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;

public class BaseClockAbilitySlice extends AbilitySlice {
    private static final String TAG = BaseClockAbilitySlice.class.getName();

    private static final float DIALOG_BOX_CORNER_RADIUS = 36.0f;

    private Text textName;
    private Text textDuration;
    private Text textBell;
    protected TimePicker timePicker;
    private boolean isModify = false;
    protected Clock clockInstance = new Clock();

    protected void initView() {
        textName = (Text) findComponentById(ResourceTable.Id_text_name);
        textDuration = (Text) findComponentById(ResourceTable.Id_item_text_duration);
        textBell = (Text) findComponentById(ResourceTable.Id_item_text_bell);
        timePicker = (TimePicker) findComponentById(ResourceTable.Id_time_picker);

        textName.setText(clockInstance.getName());
        textDuration.setText(DataConstants.getDutration(clockInstance.getDuration()) + " " + ResourceUtil.getString(this, ResourceTable.String_minute));
        textBell.setText(DataConstants.getBell(clockInstance.getBell()));
        timePicker.setHour(clockInstance.getHour());
        timePicker.setMinute(clockInstance.getMinute());
        timePicker.showSecond(false);//不显示秒

        //返回主界面
        Image imageCancel = (Image) findComponentById(ResourceTable.Id_image_cancel);
        imageCancel.setClickedListener(component -> {
            if (isModify) {
                CommonDialog alertDialog = new CommonDialog(this);
                TextField textFieldName = new TextField(this);
                textFieldName.setText(ResourceTable.String_suretomodify);
                textFieldName.setTextSize(20, Text.TextSizeType.FP);
                DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig();
                layoutConfig.alignment = LayoutAlignment.CENTER;
                textFieldName.setLayoutConfig(layoutConfig);

                alertDialog.setContentCustomComponent(textFieldName);
                alertDialog.setButton(IDialog.BUTTON1, ResourceUtil.getString(this, ResourceTable.String_giveup), new IDialog.ClickedListener() {
                    @Override
                    public void onClick(IDialog iDialog, int i) {
                        iDialog.destroy();
                        backToMainSlice(false);
                    }
                });
                alertDialog.setButton(IDialog.BUTTON3, ResourceUtil.getString(this, ResourceTable.String_save), new IDialog.ClickedListener() {
                    @Override
                    public void onClick(IDialog iDialog, int i) {
                        iDialog.destroy();
                        saveClock();
                        backToMainSlice(true);
                    }
                });
                alertDialog.show();
            } else {
                backToMainSlice(false);
            }
        });

        //保存闹钟
        Image imageOk = (Image) findComponentById(ResourceTable.Id_image_ok);
        imageOk.setClickedListener(component -> {
            saveClock();
            backToMainSlice(true);
        });

        //编辑闹钟名
        Component layoutName = findComponentById(ResourceTable.Id_layout_name);
        if (layoutName != null) {
            layoutName.setClickedListener(component -> {
                editClockName();
            });
        }

        //编辑闹钟时长
        Component layoutDuration = findComponentById(ResourceTable.Id_layout_duration);
        if (layoutDuration != null) {
            layoutDuration.setClickedListener(component -> {
                editDuration();
            });
        }

        //编辑闹钟铃声
        Component bellDuration = findComponentById(ResourceTable.Id_layout_bell);
        if (bellDuration != null) {
            bellDuration.setClickedListener(component -> {
                editBell();
            });
        }
    }


    /**
     * 返回闹钟首页
     *
     * @param isRefresh 是否刷新界面
     */
    protected void backToMainSlice(boolean isRefresh) {
        Intent resultIntent = new Intent();
        resultIntent.setParam("isRefresh", isRefresh);
        setResult(resultIntent);
        terminate();
    }

    private void editBell() {
        isModify = true;
        CommonDialog bellDialog = new CommonDialog(this);
        Component dialogLayout = LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_dialog_edit_bell, null, false);
        RadioContainer containerBell = (RadioContainer) dialogLayout.findComponentById(ResourceTable.Id_radio_container_bell);
        containerBell.mark(clockInstance.getBell());
        bellDialog.setCornerRadius(DIALOG_BOX_CORNER_RADIUS);
        bellDialog.setContentCustomComponent(dialogLayout);
        bellDialog.setButton(IDialog.BUTTON1, ResourceUtil.getString(this, ResourceTable.String_cancel), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog iDialog, int i) {
                iDialog.destroy();
            }
        });
        bellDialog.setButton(IDialog.BUTTON3, ResourceUtil.getString(this, ResourceTable.String_ok), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog iDialog, int i) {
                clockInstance.setBell(containerBell.getMarkedButtonId());
                textBell.setText(DataConstants.getBell(clockInstance.getBell()));
                iDialog.destroy();
            }
        });
        bellDialog.show();
    }

    private void editClockName() {
        isModify = true;
        CommonDialog nameDialog = new CommonDialog(this);
        Component dialogLayout = LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_dialog_edit_name, null, false);
        TextField textFieldName = (TextField) dialogLayout.findComponentById(ResourceTable.Id_textField_name);
        nameDialog.setContentCustomComponent(dialogLayout);
        nameDialog.setCornerRadius(DIALOG_BOX_CORNER_RADIUS);
        textFieldName.setText(textName.getText());
        nameDialog.setButton(IDialog.BUTTON1, ResourceUtil.getString(this, ResourceTable.String_cancel), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog iDialog, int i) {
                iDialog.destroy();
            }
        });
        nameDialog.setButton(IDialog.BUTTON3, ResourceUtil.getString(this, ResourceTable.String_ok), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog iDialog, int i) {
                iDialog.destroy();
                textName.setText(textFieldName.getText());
                clockInstance.setName(textFieldName.getText());
            }
        });
        nameDialog.show();
    }


    private void editDuration() {
        isModify = true;
        CommonDialog durationDialog = new CommonDialog(this);
        Component dialogLayout = LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_dialog_edit_duration, null, false);
        RadioContainer containerDuration = (RadioContainer) dialogLayout.findComponentById(ResourceTable.Id_radio_container_duration);
        containerDuration.mark(clockInstance.getDuration());
        durationDialog.setCornerRadius(DIALOG_BOX_CORNER_RADIUS);
        durationDialog.setContentCustomComponent(dialogLayout);
        durationDialog.setButton(IDialog.BUTTON1, ResourceUtil.getString(this, ResourceTable.String_cancel), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog iDialog, int i) {
                iDialog.destroy();
            }
        });
        durationDialog.setButton(IDialog.BUTTON3, ResourceUtil.getString(this, ResourceTable.String_ok), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog iDialog, int i) {
                clockInstance.setDuration(containerDuration.getMarkedButtonId());
                LogUtil.info(TAG, "editDuration : " + clockInstance.getDuration());

                textDuration.setText(((RadioButton) containerDuration.getComponentAt(clockInstance.getDuration())).getText());
                iDialog.destroy();
            }
        });
        durationDialog.show();
    }

    protected void saveClock() {

    }
}
