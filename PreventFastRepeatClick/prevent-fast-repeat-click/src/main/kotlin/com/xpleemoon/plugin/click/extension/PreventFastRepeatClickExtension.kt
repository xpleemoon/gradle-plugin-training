package com.xpleemoon.plugin.click.extension

import org.gradle.api.tasks.Input

private const val DEFAULT_PREVENT_FAST_REPEAT_CLICK_INTERVAL_TIME_MS = 500L

/**
 * 点击拦截extension配置类
 * - 使用open修饰类的原因：gradle创建extension实例是通过代理形式（[org.gradle.api.internal.AbstractClassGenerator.generate]）来进行的，因此类修饰不能为final
 * - 构造方法使用默认实现的原因：gradle实例化真正的extension是通过反射，而这个反射调用的就是无参构造方法
 * @author xpleemoon
 */
open class PreventFastRepeatClickExtension {
    /**
     * 是否开启调试日志
     */
    @Input
    var enableDebugLog: Boolean = true

    /**
     * 拦截快速点击的默认间隔时间
     */
    @Input
    var defaultIntervalTimeMs: Long = DEFAULT_PREVENT_FAST_REPEAT_CLICK_INTERVAL_TIME_MS
}