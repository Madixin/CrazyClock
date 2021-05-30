package com.madixin.clock.clock.util;

/**
 * PlayerStateListener
 */
public interface PlayerStateListener {
    void onPlaySuccess(int totalTime);

    void onPauseSuccess();

    void onPositionChange(int currentTime);

    void onMusicFinished();

    void onUriSet(String name);
}
