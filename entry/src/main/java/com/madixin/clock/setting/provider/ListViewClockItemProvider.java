package com.madixin.clock.setting.provider;

import com.madixin.clock.setting.ResourceTable;
import com.madixin.clock.setting.model.Clock;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ListViewClockItemProvider extends BaseItemProvider {

    private AbilitySlice slice;

    private List<Clock> dataList = new LinkedList<>();

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public interface SwitchStateChangedListener {
        public void onSwitchStateChanged(Button button, boolean isEnable, int position);
    }

    private SwitchStateChangedListener switchStateChangedListener;

    public ListViewClockItemProvider(AbilitySlice abilitySlice) {
        this.slice = abilitySlice;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List<Clock> getDataList() {
        return dataList;
    }

    public void setDataList(List<Clock> dataList) {
        this.dataList = dataList;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        final Component cpt;
        ViewHolder viewHolder;

        if (component == null) {
            cpt = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_clock, null, false);
            viewHolder = new ViewHolder();
            viewHolder.textName = (Text) cpt.findComponentById(ResourceTable.Id_item_text_name);
            viewHolder.textTime = (Text) cpt.findComponentById(ResourceTable.Id_item_text_time);
            viewHolder.switchState = (Switch) cpt.findComponentById(ResourceTable.Id_item_switch_state);
            cpt.setTag(viewHolder);
        } else {
            cpt = component;
            viewHolder = (ViewHolder) component.getTag();
        }

        Clock clock = dataList.get(position);
        viewHolder.textName.setText(clock.getName());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, clock.getHour());
        calendar.set(Calendar.MINUTE, clock.getMinute());
        viewHolder.textTime.setText(sdf.format(calendar.getTime()));
        viewHolder.switchState.setChecked(clock.isEnable());
        viewHolder.switchState.setCheckedStateChangedListener((button, isEnable) -> {
            switchStateChangedListener.onSwitchStateChanged(button, isEnable, position);
        });
        return cpt;
    }

    public SwitchStateChangedListener getSwitchStateChangedListener() {
        return switchStateChangedListener;
    }

    public void setSwitchStateChangedListener(SwitchStateChangedListener switchStateChangedListener) {
        this.switchStateChangedListener = switchStateChangedListener;
    }

    class ViewHolder {
        Text textName;
        Text textTime;
        Switch switchState;
    }
}
