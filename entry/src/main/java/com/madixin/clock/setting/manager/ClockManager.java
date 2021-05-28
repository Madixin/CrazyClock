package com.madixin.clock.setting.manager;

import com.madixin.clock.setting.model.Clock;
import ohos.app.Context;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ClockManager {
    private static final String TAG = ClockManager.class.getName();

    private static ClockManager instance;

    private Context context;

    private ClockManager(Context context) {
        this.context = context;
    }

    private List<Clock> clockList = new LinkedList<>();

    public static ClockManager getInstance(Context context) {
        if (instance == null) {
            instance = new ClockManager(context);
        }
        return instance;
    }

    public void createNewClock(Clock clock) {
        clockList.add(clock);
    }

    public List<Clock> getAllClocks() {
        return clockList;
    }

    /**
     * 返回列表第几个的闹钟数据
     *
     * @param itemId 列表项
     * @return Optional<Clock>
     */
    public Optional<Clock> getClockByItemId(int itemId) {
        if (itemId > clockList.size() || clockList.size() == 0) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.clockList.get(itemId));
    }

    public int deleteClock(Clock clockInstance) {
        return -1;
    }

    public int updateClock(Clock clockInstance) {
        return -1;
    }
}
