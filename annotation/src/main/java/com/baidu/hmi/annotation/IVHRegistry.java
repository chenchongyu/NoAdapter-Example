/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.hmi.annotation;

public interface IVHRegistry {
    int getItemViewType(Object data);

    Class getVHClass(int type);
}
