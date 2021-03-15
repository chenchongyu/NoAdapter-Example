/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.hmi.adpater;

import java.util.ArrayList;
import java.util.List;

import com.baidu.hmi.annotation.IVHRegistry;

public class ViewHolderRegistry {
    private static List<IVHRegistry> list = new ArrayList<>();

    public static void add(IVHRegistry ivhRegistry) {
        list.add(ivhRegistry);
    }

    public static int getItemViewType(Object data) {
        for (IVHRegistry ivhRegistry : list) {
            int itemViewType = ivhRegistry.getItemViewType(data);
            if (itemViewType > 0) {
                return itemViewType;
            }
        }

        System.err.println("ViewHolderRegistry find type value by " + data.getClass() + " is "
                + "error.");
        return 0;
    }

    public static Class getVHClass(int type) {
        for (IVHRegistry ivhRegistry : list) {
            Class vhClass = ivhRegistry.getVHClass(type);
            if (vhClass != null) {
                return vhClass;
            }
        }

        return null;
    }
}
