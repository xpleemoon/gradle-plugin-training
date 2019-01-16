package com.xpleemoon.plugin

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.tasks.PackageApplication
import com.xpleemoon.plugin.extensions.Files2Assets
import org.gradle.api.Plugin
import org.gradle.api.Project

class CopyFiles2AssetsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 添加files2Assets extension
        project.extensions.add("files2Assets", Files2Assets)

        // 打包前执行拷贝
        project.afterEvaluate {
            project.plugins.withId('com.android.application') {
                Files2Assets files2Assets = project.files2Assets
                String filesSrc = files2Assets.filesSrc
                if (filesSrc != null && !filesSrc.isEmpty()) {
                    project.android.applicationVariants.all { ApplicationVariant variant ->
                        def variantName = variant.name
                        variant.outputs.each { ApkVariantOutput variantOutput ->
                            variantOutput.packageApplication.doFirst { PackageApplication task ->
                                def startTimeMillis = System.currentTimeMillis()
                                project.copy {
                                    from filesSrc
                                    into "${task.assets.files[0].absolutePath}"

                                    def excludeFiles = files2Assets.excludeFiles
                                    if (excludeFiles != null) {
                                        exclude excludeFiles
                                    }
                                }
                                println "${variantName}：${CopyFiles2AssetsPlugin.class.simpleName}拷贝${filesSrc}，耗时${System.currentTimeMillis() - startTimeMillis}ms"
                            }
                        }
                    }
                }
            }
        }
    }
}