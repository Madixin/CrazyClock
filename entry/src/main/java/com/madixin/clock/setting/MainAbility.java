package com.madixin.clock.setting;

import com.madixin.clock.setting.slice.AddClockAbilitySlice;
import com.madixin.clock.setting.slice.DeleteClockAbilitySlice;
import com.madixin.clock.setting.slice.EditClockAbilitySlice;
import com.madixin.clock.setting.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

        addActionRoute("action.clock.addclock", AddClockAbilitySlice.class.getName());
        addActionRoute("action.clock.editclock", EditClockAbilitySlice.class.getName());
        addActionRoute("action.clock.deleteclock", DeleteClockAbilitySlice.class.getName());
    }
}
