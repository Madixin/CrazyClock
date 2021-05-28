package com.madixin.clock.setting.slice;

import com.madixin.clock.common.util.LogUtil;
import com.madixin.clock.common.util.ResourceUtil;
import com.madixin.clock.setting.ResourceTable;
import com.madixin.clock.setting.manager.ClockManager;
import com.madixin.clock.setting.model.Clock;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;

import java.util.Optional;

public class EditClockAbilitySlice extends BaseClockAbilitySlice {
    private static final String TAG = EditClockAbilitySlice.class.getName();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_edit_clock);

        //clockInstance by id
        int item = intent.getIntParam("itemClock", 0);
        LogUtil.info(TAG, "itemClock=" + item);

        Optional<Clock> clockOptional = ClockManager.getInstance(this.getApplicationContext()).getClockByItemId(item);

        if (!clockOptional.isPresent()) {
            backToMainSlice(false);
        }
        clockInstance = clockOptional.get();

        initView();

        Button btnDelete = (Button) findComponentById(ResourceTable.Id_btn_delete);
        btnDelete.setClickedListener((btn) -> {
            CommonDialog alertDialog = new CommonDialog(this);
            TextField textFieldName = new TextField(this);
            textFieldName.setText(ResourceTable.String_suretodelete);
            textFieldName.setTextSize(20, Text.TextSizeType.FP);
            DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig();
            layoutConfig.alignment = LayoutAlignment.CENTER;
            textFieldName.setLayoutConfig(layoutConfig);

            alertDialog.setContentCustomComponent(textFieldName);
            alertDialog.setButton(IDialog.BUTTON1, ResourceUtil.getString(this, ResourceTable.String_cancel), new IDialog.ClickedListener() {
                @Override
                public void onClick(IDialog iDialog, int i) {
                    iDialog.destroy();
                }
            });
            alertDialog.setButton(IDialog.BUTTON3, ResourceUtil.getString(this, ResourceTable.String_delete), new IDialog.ClickedListener() {
                @Override
                public void onClick(IDialog iDialog, int i) {
                    iDialog.destroy();
                    ClockManager.getInstance(getApplicationContext()).deleteClock(clockInstance);
                    backToMainSlice(true);
                }
            });
            alertDialog.show();
        });
    }

    @Override
    protected void saveClock() {
        clockInstance.setHour(timePicker.getHour());
        clockInstance.setMinute(timePicker.getMinute());
        clockInstance.setEnable(true);
        ClockManager.getInstance(this.getApplicationContext()).updateClock(clockInstance);
        LogUtil.info(TAG, "updateClock:" + clockInstance.toString());
    }
}
