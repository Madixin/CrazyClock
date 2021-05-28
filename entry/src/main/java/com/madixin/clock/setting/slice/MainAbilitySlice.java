package com.madixin.clock.setting.slice;

import com.madixin.clock.common.util.LogUtil;
import com.madixin.clock.setting.ResourceTable;
import com.madixin.clock.setting.manager.ClockManager;
import com.madixin.clock.setting.model.Clock;
import com.madixin.clock.setting.provider.ListViewClockItemProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.components.TabList;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;

import java.util.LinkedList;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice {

    private static final String TAG = MainAbilitySlice.class.getName();

    private Image imageAddClock;

    private ListContainer listClockContainer;

    private ListViewClockItemProvider listViewClockItemProvider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        initTablist();
        initListContainer();
    }

    private void initListContainer() {
        listClockContainer = (ListContainer) this.findComponentById(ResourceTable.Id_list_clock_view);
        listViewClockItemProvider = new ListViewClockItemProvider(this);

        List<Clock> clockList = getAllClocks();
        if (clockList.size() == 0) {
            Clock clock1 = new Clock();
            clock1.setName("zzz闹钟");
            clock1.setBell(0);
            clock1.setHour(6);
            clock1.setDuration(10);
            clock1.setMinute(66);
            clock1.setEnable(true);

            ClockManager.getInstance(this.getApplicationContext()).createNewClock(clock1);

            Clock clock2 = new Clock();
            clock2.setName("madixin闹钟");
            clock2.setBell(0);
            clock2.setHour(8);
            clock2.setDuration(10);
            ClockManager.getInstance(this.getApplicationContext()).createNewClock(clock2);

            clockList.add(clock1);
            clockList.add(clock2);
        }

        listViewClockItemProvider.setDataList(clockList);
        listClockContainer.setItemProvider(listViewClockItemProvider);
        listClockContainer.setReboundEffect(true);
    }

    private List<Clock> getAllClocks() {
        return new LinkedList<>();
    }

    private void initTablist() {
        this.getApplicationContext();
        Element icon = ElementScatter.getInstance(this.getContext()).parse(ResourceTable.Graphic_clock);

        TabList tabList = (TabList) this.findComponentById(ResourceTable.Id_tab_bottom_list);
        TabList.Tab tab = tabList.new Tab(this.getContext());
        tab.setText(ResourceTable.String_clock);
        tab.setIconElement(icon);
        tab.setPadding(130, 0, 0, 0);
        tabList.addTab(tab);

        TabList.Tab tab2 = tabList.new Tab(this.getContext());
        tab2.setText(ResourceTable.String_setting);

        //tab2.setPadding(12, 0, 12, 0);
        Element icon2 = ElementScatter.getInstance(this.getContext()).parse(ResourceTable.Graphic_setting);

        tab2.setIconElement(icon2);
        tab2.setPadding(130, 0, 0, 0);
        tabList.addTab(tab2);

        tabList.selectTab(tab);
        tabList.setFixedMode(true);

        AbilitySlice curSlice = this;

        tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            Component clockComponent = curSlice.findComponentById(ResourceTable.Id_layout_clock);
            Component settingComponent = curSlice.findComponentById(ResourceTable.Id_layout_setting);
            @Override
            public void onSelected(TabList.Tab tab) {
                LogUtil.info(TAG, "onSelected: " + tab.getPosition());
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

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
