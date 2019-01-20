package com.xpleemoon.plugin.extensions

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class Files2AssetsExtension {
    static def DEFAULT_NAME = "files2Assets"
    static class Factory {
        /**
         * 往{@code project}中添加名为{@code name}的extension
         * @param project
         * @param name
         */
        static void add(Project project, String name) {
            create(project, name)
        }

        /**
         * 创建名为{@code name}的extension，并添加到{@code project}
         * @param project
         * @param name
         * @return
         */
        static Files2AssetsExtension create(Project project, String name) {
            NamedDomainObjectContainer<FilesFrom> from = project.container(FilesFrom)
            return project.extensions.create(name, Files2AssetsExtension, from)
        }

        /**
         * 从{@code project}中获取{@code name}的extension
         * @param project
         * @param name
         * @return
         */
        static Files2AssetsExtension get(Project project, String name) {
            return project.extensions.getByName(name)
        }
    }

    /**
     * 需要排除的文件信息
     */
    String[] excludeFiles = ['**/.git/**', '**/.gitignore']
    NamedDomainObjectContainer<FilesFrom> from

    Files2AssetsExtension(NamedDomainObjectContainer<FilesFrom> from) {
        this.from = from
    }

    /**
     * 用于gradle dsl语法，需要与关联的from属性同名
     * @param action
     */
    void from(Action<NamedDomainObjectContainer<FilesFrom>> action) {
        action.execute(from)
    }
}