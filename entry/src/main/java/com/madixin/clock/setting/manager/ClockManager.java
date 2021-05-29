package com.madixin.clock.setting.manager;

import com.madixin.clock.common.util.LogUtil;
import com.madixin.clock.setting.ability.ClockDataAbility;
import com.madixin.clock.setting.model.Clock;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.app.Context;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ClockManager {
    private static final String TAG = ClockManager.class.getName();

    private static ClockManager instance;

    private static final String BASE_URI = "dataability:///com.madixin.clock.setting.ability.ClockDataAbility";

    private Context context;

    private DataAbilityHelper databaseHelper;

    private static final String DATA_PATH = "/crazyclock";

    private ClockManager(Context context) {
        this.context = context;
        this.databaseHelper = DataAbilityHelper.creator(context);
    }

    private List<Clock> clockList = new LinkedList<>();

    public static ClockManager getInstance(Context context) {
        if (instance == null) {
            instance = new ClockManager(context);
        }
        return instance;
    }

    /**
     * 获取所有闹钟
     * @return 闹钟列表
     */
    public List<Clock> getAllClocks() {
        this.clockList = new LinkedList<>();

        String[] columns = new String[]{ClockDataAbility.DB_COLUMN_ID,
                ClockDataAbility.DB_COLUMN_NAME, ClockDataAbility.DB_COLUMN_BELL, ClockDataAbility.DB_COLUMN_HOUR,
                ClockDataAbility.DB_COLUMN_MINUTE, ClockDataAbility.DB_COLUMN_DURATION, ClockDataAbility.DB_COLUMN_ENABLE};
        // 构造查询条件
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        //predicates.between(DB_COLUMN_AGE, 15, 40);
        try {
            ResultSet resultSet = databaseHelper.query(Uri.parse(BASE_URI + DATA_PATH),
                    columns, predicates);
            if (resultSet == null || resultSet.getRowCount() == 0) {
                LogUtil.info(TAG, "query: resultSet is null or no result found");
                return this.clockList;
            }
            resultSet.goToFirstRow();
            do {
                Clock clock = new Clock();
                clock.setId(resultSet.getInt(resultSet.getColumnIndexForName(ClockDataAbility.DB_COLUMN_ID)));
                clock.setName(resultSet.getString(resultSet.getColumnIndexForName(ClockDataAbility.DB_COLUMN_NAME)));
                clock.setBell(resultSet.getInt(resultSet.getColumnIndexForName(ClockDataAbility.DB_COLUMN_BELL)));
                clock.setHour(resultSet.getInt(resultSet.getColumnIndexForName(ClockDataAbility.DB_COLUMN_HOUR)));
                clock.setMinute(resultSet.getInt(resultSet.getColumnIndexForName(ClockDataAbility.DB_COLUMN_MINUTE)));
                clock.setDuration(resultSet.getInt(resultSet.getColumnIndexForName(ClockDataAbility.DB_COLUMN_DURATION)));
                clock.setEnable(resultSet.getInt(resultSet.getColumnIndexForName(ClockDataAbility.DB_COLUMN_ENABLE)) == 1);
                this.clockList.add(clock);
                LogUtil.info(TAG, "query: clock :" + clock);
            } while (resultSet.goToNextRow());
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            LogUtil.error(TAG, "query: dataRemote exception | illegalStateException" + exception.getMessage());
        }

        LogUtil.info(TAG, "getAllClocks : size= " + this.clockList.size());
        return this.clockList;
    }

    /**
     * 创建新闹钟
     * @param clock clock
     */
    public void createNewClock(Clock clock) {
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(ClockDataAbility.DB_COLUMN_NAME, clock.getName());
        valuesBucket.putInteger(ClockDataAbility.DB_COLUMN_BELL, clock.getBell());
        valuesBucket.putInteger(ClockDataAbility.DB_COLUMN_HOUR, clock.getHour());
        valuesBucket.putInteger(ClockDataAbility.DB_COLUMN_MINUTE, clock.getMinute());
        valuesBucket.putInteger(ClockDataAbility.DB_COLUMN_DURATION, clock.getDuration());
        valuesBucket.putBoolean(ClockDataAbility.DB_COLUMN_ENABLE, clock.isEnable());

        try {
            int id = databaseHelper.insert(Uri.parse(BASE_URI + DATA_PATH), valuesBucket);
            if (id == -1) {
                LogUtil.error(TAG, "fail to insert");
            } else {
                LogUtil.info(TAG, "success to insert id=" + id);
            }
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            LogUtil.error(TAG, "insert: dataRemote exception | illegalStateException" + exception.getMessage());
        }
    }

    /**
     * 删除闹钟
     * @param clock clock
     * @return
     */
    public int deleteClock(Clock clock) {
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.contains(ClockDataAbility.DB_COLUMN_ID, String.valueOf(clock.getId()));
        try {
            return databaseHelper.delete(Uri.parse(BASE_URI + DATA_PATH), predicates);
        } catch (DataAbilityRemoteException e) {
            LogUtil.error(TAG, "delete: dataRemote exception | illegalStateException" + e.getMessage());
        }
        return -1;
    }

    /**
     * 返回列表第几个的闹钟数据
     *
     * @param itemId 列表项
     * @return Optional<Clock>
     */
    public Optional<Clock> getClockByItemId(int itemId) {
        if (itemId > this.clockList.size() || this.clockList.size() == 0) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.clockList.get(itemId));
    }

    /**
     * 更新闹钟
     * @param clock clock
     * @return
     */
    public int updateClock(Clock clock) {
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.contains(ClockDataAbility.DB_COLUMN_ID, String.valueOf(clock.getId()));

        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(ClockDataAbility.DB_COLUMN_NAME, clock.getName());
        valuesBucket.putInteger(ClockDataAbility.DB_COLUMN_BELL, clock.getBell());
        valuesBucket.putInteger(ClockDataAbility.DB_COLUMN_HOUR, clock.getHour());
        valuesBucket.putInteger(ClockDataAbility.DB_COLUMN_MINUTE, clock.getMinute());
        valuesBucket.putInteger(ClockDataAbility.DB_COLUMN_DURATION, clock.getDuration());
        valuesBucket.putBoolean(ClockDataAbility.DB_COLUMN_ENABLE, clock.isEnable());

        try {
            int id = databaseHelper.update(Uri.parse(BASE_URI + DATA_PATH), valuesBucket, predicates);
            if (id == -1) {
                LogUtil.error(TAG, "fail to insert");
            } else {
                LogUtil.info(TAG, "success to insert id=" + id);
                return id;
            }
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            LogUtil.error(TAG, "insert: dataRemote exception | illegalStateException" + exception.getMessage());
        }
        return -1;
    }
}
