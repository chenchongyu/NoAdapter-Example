package com.baidu.hmi

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */

class NoAdapterPluginTest implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println("========自定义NoAdapterTest插件=========")
        def android = target.extensions.getByType(AppExtension)
        android.registerTransform(new NoAdapterTransform(target))
    }
}