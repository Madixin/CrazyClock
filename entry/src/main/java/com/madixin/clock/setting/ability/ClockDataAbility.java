package com.madixin.clock.setting.ability;

import com.madixin.clock.common.util.LogUtil;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.dataability.DataAbilityUtils;
import ohos.data.rdb.*;
import ohos.data.resultset.ResultSet;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;
import ohos.utils.PacMap;

import java.io.FileDescriptor;

public class ClockDataAbility extends Ability {
    private static final String TAG = ClockDataAbility.class.getName();

    public static final String DB_NAME = "crazyclock.db";

    public static final String DB_TAB_NAME = "crazyclock";

    public static final String DB_COLUMN_ID = "id";

    public static final String DB_COLUMN_NAME = "name";

    public static final String DB_COLUMN_BELL = "bell";

    public static final String DB_COLUMN_HOUR = "hour";

    public static final String DB_COLUMN_MINUTE = "minute";

    public static final String DB_COLUMN_DURATION = "duration";

    public static final String DB_COLUMN_ENABLE = "enable";

    private static final int DB_VERSION = 1;

    private StoreConfig config = StoreConfig.newDefaultConfig(DB_NAME);

    private RdbStore rdbStore;

    private RdbOpenCallback rdbOpenCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            store.executeSql("create table if not exists "
                    + DB_TAB_NAME + " ("
                    + DB_COLUMN_ID + " integer primary key, "
                    + DB_COLUMN_NAME + " text not null, "
                    + DB_COLUMN_BELL + " integer not null, "
                    + DB_COLUMN_HOUR + " integer not null, "
                    + DB_COLUMN_MINUTE + " integer not null, "
                    + DB_COLUMN_DURATION + " integer not null, "
                    + DB_COLUMN_ENABLE + " boolean)");
        }

        private int duration;

        private int strategy;

        private boolean isEnable;

        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        LogUtil.info(TAG, "ClockDataAbility onStart");

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        rdbStore = databaseHelper.getRdbStore(config, DB_VERSION, rdbOpenCallback, null);
    }

    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        ResultSet resultSet = rdbStore.query(rdbPredicates, columns);
        if (resultSet == null) {
            LogUtil.info(TAG, "resultSet is null");
        }
        return resultSet;
    }

    @Override
    public int insert(Uri uri, ValuesBucket value) {
        LogUtil.info(TAG, "ClockDataAbility insert");
        String path = uri.getLastPath();
        if (!DB_TAB_NAME.equals(path)) {
            LogUtil.info(TAG, "DataAbility insert path is not matched");
            return -1;
        }
        ValuesBucket values = new ValuesBucket();
        values.putString(DB_COLUMN_NAME, value.getString(DB_COLUMN_NAME));
        values.putInteger(DB_COLUMN_BELL, value.getInteger(DB_COLUMN_BELL));
        values.putInteger(DB_COLUMN_HOUR, value.getInteger(DB_COLUMN_HOUR));
        values.putInteger(DB_COLUMN_MINUTE, value.getInteger(DB_COLUMN_MINUTE));
        values.putInteger(DB_COLUMN_DURATION, value.getInteger(DB_COLUMN_DURATION));
        values.putBoolean(DB_COLUMN_ENABLE, value.getBoolean(DB_COLUMN_ENABLE));
        int index = (int) rdbStore.insert(DB_TAB_NAME, values);
        DataAbilityHelper.creator(this, uri).notifyChange(uri);//todo ?
        return index;
    }

    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        int index = rdbStore.delete(rdbPredicates);
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return index;
    }

    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        int index = rdbStore.update(value, rdbPredicates);
        LogUtil.info(TAG, "update: " + index);
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return index;
    }

    @Override
    public FileDescriptor openFile(Uri uri, String mode) {
        return null;
    }

    @Override
    public String[] getFileTypes(Uri uri, String mimeTypeFilter) {
        return new String[0];
    }

    @Override
    public PacMap call(String method, String arg, PacMap extras) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}