package com.xpleemoon.plugin.click

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.xpleemoon.plugin.click.transform.PreventFastRepeatClickTransform


/**
 * @author xpleemoon
 */
class PreventFastRepeatClickPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("注册PreventFastRepeatClickTransform")
        val android = project.extensions.getByType(AppExtension::class.java)
        android.registerTransform(PreventFastRepeatClickTransform(project))
    }
}