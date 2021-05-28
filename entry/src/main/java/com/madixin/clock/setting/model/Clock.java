package com.madixin.clock.setting.model;

public class Clock {
    private int id;//主键

    private String name;//名称

    private int hour;//小时

    private int minute;//分钟

    private int bell;//铃声

    private int duration;//持续时间

    private boolean isEnable;//是否开启

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getBell() {
        return bell;
    }

    public void setBell(int bell) {
        this.bell = bell;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
