package com.madixin.clock.clock.slice;

import com.madixin.clock.clock.ResourceTable;
import com.madixin.clock.clock.util.PlayerManager;
import com.madixin.clock.clock.util.PlayerStateListener;
import com.madixin.clock.common.util.Constants;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.bundle.ElementName;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class ClockAlarmAbilitySlice extends AbilitySlice {

    private PlayerManager playerManager;

    private IRemoteObject scheduleServiceAbility;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_clock_alarm);

        Button btnClose = (Button) findComponentById(ResourceTable.Id_btn_close);
        btnClose.setClickedListener((button) -> {
            if (scheduleServiceAbility != null) {
                MessageParcel data = MessageParcel.obtain();
                MessageParcel reply = MessageParcel.obtain();
                MessageOption option = new MessageOption(MessageOption.TF_SYNC);
                try {
                    scheduleServiceAbility.sendRequest(Constants.CLOSE_CLOCK, data, reply, option);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            if (playerManager.isPlaying()) {
                playerManager.releasePlayer();
            }
            this.terminate();//暂时直接关闭
        });

        String bell = "Cannon";
        if (intent != null) {
            Object obj = intent.getStringParam("bell");
            if (obj instanceof String) {
                bell = (String) obj;
            }
            obj = intent.getStringParam("deviceLocal");
            if (obj instanceof String) {
                Intent connectPaIntent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withBundleName(getBundleName())
                        .withAbilityName("com.madixin.clock.setting.ability.ScheduleServiceAbility")
                        .build();
                connectPaIntent.setOperation(operation);
                IAbilityConnection conn = new IAbilityConnection() {
                    @Override
                    public void onAbilityConnectDone(ElementName elementName, IRemoteObject remote, int resultCode) {
                        scheduleServiceAbility = remote;
                    }

                    @Override
                    public void onAbilityDisconnectDone(ElementName elementName, int i) {
                        scheduleServiceAbility = null;
                        disconnectAbility(this);
                    }
                };
                this.connectAbility(connectPaIntent, conn);
            }
        }
        initMedia(bell);
    }

    @Override
    public void onActive() {
        super.onActive();
        playerManager.play();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void initMedia(String bell) {
        playerManager = new PlayerManager(this, "resources/rawfile/" + bell + ".mp3");
        playerManager.setPlayerStateListener(new PlayerStateListener() {
            @Override
            public void onPlaySuccess(int totalTime) {
                playerManager.play();
            }

            @Override
            public void onPauseSuccess() {

            }

            @Override
            public void onPositionChange(int currentTime) {

            }

            @Override
            public void onMusicFinished() {

            }

            @Override
            public void onUriSet(String name) {

            }
        });
        playerManager.init();
        playerManager.play();

    }

}
