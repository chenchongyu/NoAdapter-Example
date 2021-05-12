package com.baidu.hmi

import org.gradle.api.Plugin
import org.gradle.api.Project

/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */

class NoAdapterPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println("========自定义NoAdapter插件=========")
    }
}