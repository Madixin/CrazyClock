package com.madixin.clock.common.util;

import ohos.app.Context;
import ohos.global.resource.Element;

public class ResourceUtil {
    private static final String TAG = ResourceUtil.class.getName();

    public static String getString(Context context, int id) {

        try {
            Element element = context.getResourceManager().getElement(id);
            return element.getString();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(TAG, e.getMessage());
        }
        return "";
    }
}
