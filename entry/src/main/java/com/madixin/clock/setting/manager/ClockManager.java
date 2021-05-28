package com.madixin.clock.setting.manager;

import com.madixin.clock.setting.model.Clock;
import ohos.app.Context;

public class ClockManager {
    private static final String TAG = ClockManager.class.getName();

    private static ClockManager instance;

    private Context context;

    private ClockManager(Context context) {
        this.context = context;
    }

    public static ClockManager getInstance(Context context) {
        if (instance == null) {
            instance = new ClockManager(context);
        }
        return instance;
    }

    public void createNewClock(Clock clock2) {
    }
}
