package com.xpleemoon.plugin.click

import com.android.build.gradle.AppExtension
import com.xpleemoon.plugin.click.extension.PreventFastRepeatClickExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.xpleemoon.plugin.click.transform.PreventFastRepeatClickTransform

const val PLUGIN_NAME = "xpleemoon-prevent-fast-repeat-click"

/**
 * @author xpleemoon
 */
class PreventFastRepeatClickPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val preventFastRepeatClickExtension =
            project.extensions.create(
                "preventFastRepeatClick",
                PreventFastRepeatClickExtension::class.java
            )
        project.afterEvaluate {
            LogUtils.enableLog = preventFastRepeatClickExtension.enableDebugLog
            LogUtils.i("${PLUGIN_NAME}配置的默认间隔时间为${preventFastRepeatClickExtension.defaultIntervalTimeMs}")
        }
        val preventFastRepeatClickTransform =
            PreventFastRepeatClickTransform(preventFastRepeatClickExtension)
        val android = project.extensions.getByType(AppExtension::class.java)
        android.registerTransform(preventFastRepeatClickTransform)
    }
}