package com.madixin.clock.common.util;

import ohos.app.Context;
import ohos.global.resource.Element;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;

public class ResourceUtil {
    private static final String TAG = ResourceUtil.class.getName();
    private static final int GET_COLOR_STATE_FAILED = -1;

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

    /**
     * Get color method
     *
     * @param context context resourceID res id
     * @param resourceId res id
     * @return color
     * @since 2020-12-04
     */
    public static int getColor(Context context, int resourceId) {
        try {
            return context.getResourceManager().getElement(resourceId).getColor();
        } catch (IOException | NotExistException | WrongTypeException e) {
            LogUtil.error(TAG, "some exception happend");
        }
        return GET_COLOR_STATE_FAILED;
    }
}
