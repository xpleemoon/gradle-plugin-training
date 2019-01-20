package com.xpleemoon.plugin

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.tasks.PackageApplication
import com.xpleemoon.plugin.extensions.Files2AssetsExtension
import com.xpleemoon.plugin.extensions.FilesFrom
import org.gradle.api.Plugin
import org.gradle.api.Project

class CopyFiles2AssetsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 添加files2Assets extension
        Files2AssetsExtension files2AssetsExtension = Files2AssetsExtension.Factory.create(project, Files2AssetsExtension.DEFAULT_NAME)

        project.afterEvaluate {
            project.plugins.withId('com.android.application') {
                project.android.applicationVariants.all { ApplicationVariant variant ->
                    def variantName = variant.name
                    variant.outputs.each { ApkVariantOutput variantOutput ->
                        // apk打包前，拷贝文件到assets
                        variantOutput.packageApplication.doFirst { PackageApplication task ->
                            def filesSrc = ""
                            def startTimeMillis = System.currentTimeMillis()
                            project.copy {
                                into "${task.assets.files[0].absolutePath}"

                                def excludeFiles = files2AssetsExtension.excludeFiles
                                if (excludeFiles != null) {
                                    exclude excludeFiles
                                }

                                def separator = ", "
                                def path = null
                                files2AssetsExtension.from.all { FilesFrom filesFrom ->
                                    if (filesFrom != null) {
                                        path = filesFrom.path
                                        if (path != null && !path.isEmpty()) {
                                            filesSrc = "$filesSrc$path$separator"
                                            from path
                                        }
                                    }
                                }
                                filesSrc = "[${filesSrc.substring(0, filesSrc.length() - separator.length())}]"
                            }
                            println "${variantName}：${CopyFiles2AssetsPlugin.class.simpleName}拷贝${filesSrc}，耗时${System.currentTimeMillis() - startTimeMillis}ms"
                        }
                    }
                }
            }
        }
    }
}