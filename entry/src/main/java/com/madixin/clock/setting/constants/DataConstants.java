package com.madixin.clock.setting.constants;

import java.util.HashMap;
import java.util.Map;

public class DataConstants {
    private static Map<Integer, Integer> durationMap;

    private static Map<Integer, String> bellMap;

    static {
        durationMap = new HashMap<>();
        durationMap.put(0, 1);
        durationMap.put(1, 5);
        durationMap.put(2, 10);

        bellMap = new HashMap<>();
        bellMap.put(0, "Cannon");
        bellMap.put(1, "Classic");
        bellMap.put(2, "Army");
    }

    public static int getDutration(int id) {
        return durationMap.getOrDefault(id, 0);
    }

    public static String getBell(int id) {
        return bellMap.getOrDefault(id, "Cannon");
    }
}
