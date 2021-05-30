package com.madixin.clock.clock.slice;

import com.madixin.clock.clock.ResourceTable;
import com.madixin.clock.clock.util.PlayerManager;
import com.madixin.clock.clock.util.PlayerStateListener;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;

public class ClockAlarmAbilitySlice extends AbilitySlice {

    private PlayerManager playerManager;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_clock_alarm);

        Button btnClose = (Button) findComponentById(ResourceTable.Id_btn_close);
        btnClose.setClickedListener((button) -> {
            if (playerManager.isPlaying()){
                playerManager.releasePlayer();
            }
            this.terminate();//暂时直接关闭
        });

        String bell = "canon";
        if (intent != null) {
            Object obj = intent.getStringParam("bell");
            if (obj instanceof String) {
                bell = (String) obj;
            }
        }
        initMedia(bell);
    }

    @Override
    public void onActive() {
        super.onActive();
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
