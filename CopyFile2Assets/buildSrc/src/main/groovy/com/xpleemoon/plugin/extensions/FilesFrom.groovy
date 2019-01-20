package com.xpleemoon.plugin.extensions

import org.gradle.api.tasks.Input

/**
 * <ul>
 *     <li>用于{@link org.gradle.api.NamedDomainObjectContainer}，必须要提供一个name属性和给name属性赋值的构造方法</li>
 * </ul>
 */
class FilesFrom {
    String name
    /**
     * 文件路径
     */
    @Input
    String path

    FilesFrom(String name) {
        this.name = name
    }
}