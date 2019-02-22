package com.xpleemoon.plugin.click.annotation

/**
 * 点击拦截注解
 * @param isExclude 对所注解的[android.view.View.OnClickListener.onClick]方法是否排除快速拦截的标记，简单的说若取值为true：不做快速点击拦截处理；反之，做快速点击拦截处理
 * @param intervalTimeMs 快速点击拦截的间隔时间，优先级高于gradle配置，注意若取值小于等于0，实际也相当于不拦截
 * @author xpleemoon
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
annotation class PreventFastRepeatClick(val isExclude: Boolean = false, val intervalTimeMs: Long = 0L)