package com.runningcode.noadapter.hmi

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/*
 * Copyright 2021 ccy.All Rights Reserved
 */

class NoAdapterPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println("========自定义NoAdapterTest插件=========")
        def android = target.extensions.getByType(AppExtension)
        android.registerTransform(new NoAdapterTransform(target))
    }
}