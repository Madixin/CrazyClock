package com.madixin.clock.setting.ability;

import com.madixin.clock.common.util.Constants;
import com.madixin.clock.common.util.LogUtil;
import com.madixin.clock.setting.constants.DataConstants;
import com.madixin.clock.setting.manager.ClockManager;
import com.madixin.clock.setting.model.Clock;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.rpc.*;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleServiceAbility extends Ability {
    private static final String TAG = ScheduleServiceAbility.class.getName();

    private boolean isInAlarm = false;

    private ScheduleServiceAbility.MyRemote remote = new ScheduleServiceAbility.MyRemote();

    @Override
    public void onStart(Intent intent) {
        LogUtil.info(TAG, "ScheduleServiceAbility::onStart");
        super.onStart(intent);

        // 按分钟定时检查
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
                    doAlarm();
                    LogUtil.info(TAG, "ScheduleServiceAbility::onBackground");
                }
                , 0, 20, TimeUnit.SECONDS);

    }

    private void doAlarm() {
        LogUtil.info(TAG, "doAlarm isInAlarm=" + isInAlarm);
        if (isInAlarm) {
            return;
        }
//        Clock clock = new Clock();
//        clock.setBell(0);
//        startClockAlarmAbility(clock);
        Optional<Clock> optionClock = getAlarmClock();
        if (optionClock.isPresent()) {
            startClockAlarmAbility(optionClock.get());
        }
    }

    private void startClockAlarmAbility(Clock clock) {
        isInAlarm = true;
        startLocalClockAlarmAbility(clock);
        startRemoteClockAlarmAbility(clock);
    }

    private void startRemoteClockAlarmAbility(Clock clock) {
        Intent intent = new Intent();
        intent.setParam("bell", DataConstants.getBell(clock.getBell()));

        List<DeviceInfo> deviceInfos =
                DeviceManager.getDeviceList(ohos.distributedschedule.interwork.DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        for (DeviceInfo deviceInfo : deviceInfos) {
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId(deviceInfo.getDeviceId())
                    .withBundleName(getBundleName())
                    .withAbilityName("com.madixin.clock.clock.ClockAlarmAbility")
                    .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                    .build();
            intent.setOperation(operation);
            startAbility(intent);
        }
    }

    private void startLocalClockAlarmAbility(Clock clock) {
        Intent intent = new Intent();
        intent.setParam("bell", DataConstants.getBell(clock.getBell()));
        intent.setParam("deviceLocal", "local");
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(getBundleName())
                .withAbilityName("com.madixin.clock.clock.ClockAlarmAbility")
                .build();

        intent.setOperation(operation);
        startAbility(intent);
    }

    private Optional<Clock> getAlarmClock() {
//        return true;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        List<Clock> clockList = ClockManager.getInstance(this.getApplicationContext()).getAllClocks();
        for (Clock clock : clockList) {
            if (clock.isEnable() && clock.getHour() == hour && clock.getMinute() == minute) {
                return Optional.of(clock);
            }
        }
        return Optional.empty();
    }

    @Override
    public void onBackground() {
        super.onBackground();
        LogUtil.info(TAG, "ScheduleServiceAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.info(TAG, "ScheduleServiceAbility::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);
        return remote.asObject();
    }

    @Override
    public void onDisconnect(Intent intent) {
    }
    class MyRemote extends RemoteObject implements IRemoteBroker {

        MyRemote() {
            super("MyService_MyRemote");
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            LogUtil.info(TAG, "onRemoteRequest:" + code);
            switch (code) {
                case Constants.CLOSE_CLOCK:
                    isInAlarm = false;
                    return true;
                default:
                    return true;


            }
        }
        @Override
        public IRemoteObject asObject() {
            return this;
        }
    }
}