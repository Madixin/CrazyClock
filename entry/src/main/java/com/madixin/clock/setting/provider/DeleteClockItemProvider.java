package com.madixin.clock.setting.provider;

import com.madixin.clock.setting.ResourceTable;
import com.madixin.clock.setting.model.Clock;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class DeleteClockItemProvider extends BaseItemProvider {
    private static final String TAG = DeleteClockItemProvider.class.getName();

    private AbilitySlice slice;

    private List<Clock> dataList = new LinkedList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public DeleteClockItemProvider(AbilitySlice abilitySlice) {
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

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        final Component cpt;
        ViewHolder viewHolder;
        Clock clock = dataList.get(position);

        if (component == null) {
            cpt = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_delete_clock, null, false);
            viewHolder = new ViewHolder();
            viewHolder.textName = (Text) cpt.findComponentById(ResourceTable.Id_item_text_name);
            viewHolder.textTime = (Text) cpt.findComponentById(ResourceTable.Id_item_text_time);
            viewHolder.checkboxDelete = (Checkbox) cpt.findComponentById(ResourceTable.Id_item_checkbox);

            viewHolder.checkboxDelete.setCheckedStateChangedListener((absButton, isCheck) -> {
                clock.setEnable(isCheck);
            });

            cpt.setTag(viewHolder);
        } else {
            cpt = component;
            viewHolder = (ViewHolder) component.getTag();
        }

        viewHolder.textName.setText(clock.getName());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, clock.getHour());
        calendar.set(Calendar.MINUTE, clock.getMinute());
        viewHolder.textTime.setText(sdf.format(calendar.getTime()));

        viewHolder.checkboxDelete.setChecked(clock.isEnable());

        return cpt;
    }

    public List<Clock> getDataList() {
        return dataList;
    }

    public void setDataList(List<Clock> dataList) {
        this.dataList = dataList;
    }
    class ViewHolder {
        Text textName;
        Text textTime;
        Checkbox checkboxDelete;
    }
}
