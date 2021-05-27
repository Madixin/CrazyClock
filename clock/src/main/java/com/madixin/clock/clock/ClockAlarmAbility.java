package com.madixin.clock.clock;

import com.madixin.clock.clock.slice.ClockAlarmAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ClockAlarmAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ClockAlarmAbilitySlice.class.getName());
    }
}
