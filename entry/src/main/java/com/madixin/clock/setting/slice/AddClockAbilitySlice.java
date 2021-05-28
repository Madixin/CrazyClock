package com.madixin.clock.setting.slice;

import com.madixin.clock.common.util.LogUtil;
import com.madixin.clock.common.util.ResourceUtil;
import com.madixin.clock.setting.ResourceTable;
import com.madixin.clock.setting.manager.ClockManager;
import ohos.aafwk.content.Intent;
import java.util.Calendar;

public class AddClockAbilitySlice extends BaseClockAbilitySlice {
    private static final String TAG = AddClockAbilitySlice.class.getName();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_add_clock);

        initClock();
        initView();
    }

    private void initClock() {
        Calendar calendar = Calendar.getInstance();
        clockInstance.setName(ResourceUtil.getString(this, ResourceTable.String_defaultName));
        clockInstance.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        clockInstance.setMinute(calendar.get(Calendar.MINUTE));
        clockInstance.setDuration(0);
        clockInstance.setBell(0);
        clockInstance.setEnable(true);
    }

    @Override
    protected void saveClock() {
        clockInstance.setHour(timePicker.getHour());
        clockInstance.setMinute(timePicker.getMinute());
        ClockManager.getInstance(this.getApplicationContext()).createNewClock(clockInstance);
        LogUtil.info(TAG, "createNewClock:" + clockInstance.toString());
    }
}
