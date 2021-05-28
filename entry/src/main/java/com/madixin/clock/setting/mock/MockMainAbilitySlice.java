package com.madixin.clock.setting.mock;

import com.huawei.asm.core.PreviewerMock;
import com.huawei.asm.core.annotation.PreviewerMockMethod;
import com.madixin.clock.setting.ResourceTable;
import com.madixin.clock.setting.model.Clock;
import com.madixin.clock.setting.slice.MainAbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.TabList;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;

import java.util.LinkedList;
import java.util.List;

public class MockMainAbilitySlice extends PreviewerMock {
    private static final String TAG = MockMainAbilitySlice.class.getName();

    @PreviewerMockMethod
    public void initTablist(MainAbilitySlice mainAbilitySlice) {
        Element icon = ElementScatter.getInstance(mainAbilitySlice.getContext()).parse(ResourceTable.Graphic_clock);

        TabList tabList = (TabList) mainAbilitySlice.findComponentById(ResourceTable.Id_tab_bottom_list);
        TabList.Tab tab = tabList.new Tab(mainAbilitySlice.getContext());
        tab.setText(ResourceTable.String_clock);
        tab.setIconElement(icon);
        tab.setPadding(130, 0, 0, 0);
        tabList.addTab(tab);

        TabList.Tab tab2 = tabList.new Tab(mainAbilitySlice.getContext());
        tab2.setText(ResourceTable.String_setting);

        Element icon2 = ElementScatter.getInstance(mainAbilitySlice.getContext()).parse(ResourceTable.Graphic_setting);

        tab2.setIconElement(icon2);
        tab2.setPadding(130, 0, 0, 0);
        tabList.addTab(tab2);
        tabList.setFixedMode(true);
        tabList.selectTab(tab);

        tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                Component clockComponent = mainAbilitySlice.findComponentById(ResourceTable.Id_layout_clock);
                Component settingComponent = mainAbilitySlice.findComponentById(ResourceTable.Id_layout_setting);

                switch (tab.getPosition()) {
                    case 0:
                        clockComponent.setVisibility(Component.VISIBLE);
                        settingComponent.setVisibility(Component.HIDE);

                        break;
                    case 1:
                        clockComponent.setVisibility(Component.HIDE);
                        settingComponent.setVisibility(Component.VISIBLE);
                        break;
                }
            }

            @Override
            public void onUnselected(TabList.Tab tab) {

            }

            @Override
            public void onReselected(TabList.Tab tab) {

            }
        });
    }

    @PreviewerMockMethod
    public List<Clock> getAllClocks(MainAbilitySlice mainAbilitySlice) {
        List<Clock> clockList = new LinkedList<>();
        Clock clock1 = new Clock();
        clock1.setName("早起闹钟");
        clock1.setHour(7);
        clock1.setMinute(55);
        clock1.setEnable(true);

        Clock clock2 = new Clock();
        clock2.setName("上班闹钟");
        clock2.setHour(8);
        clock2.setMinute(0);
        clock2.setEnable(false);

        Clock clock3 = new Clock();
        clock3.setName("下班闹钟");
        clock3.setHour(9);
        clock3.setMinute(7);
        //clock3.setTime("09:00");


        clockList.add(clock1);
        clockList.add(clock2);
        clockList.add(clock3);
        return clockList;
    }

    @PreviewerMockMethod
    public void createNewClock(MainAbilitySlice mainAbilitySlice) {

    }
}
