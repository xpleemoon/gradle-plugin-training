package com.xpleemoon.plugin

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.tasks.PackageApplication
import org.gradle.api.Plugin
import org.gradle.api.Project

class CopyFiles2AssetsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.afterEvaluate {
            project.plugins.withId('com.android.application') {
                project.android.applicationVariants.all { ApplicationVariant variant ->
                    def variantName = variant.name
                    variant.outputs.each { ApkVariantOutput variantOutput ->
                        variantOutput.packageApplication.doFirst { PackageApplication task ->
                            println "${variantName}：插件开始资源拷贝"
                            def startTimeMillis = System.currentTimeMillis()
                            project.copy {
                                from "${project.rootDir.absolutePath}/myAssets/data.txt"
                                into "${task.assets.files[0].absolutePath}"
                                exclude '**/.git/**', '**/.gitignore', '**/.svn/**'
                            }
                            println "${variantName}：插件完成资源拷贝，耗时为${System.currentTimeMillis() - startTimeMillis}ms"
                        }
                    }
                }
            }
        }
    }
}