package com.xpleemoon.plugin.click

object LogUtils {
    var enableLog = true

    fun d(msg: String) {
        if (enableLog) {
            println(msg)
        }
    }

    fun i(msg: String) {
        println(msg)
    }
}