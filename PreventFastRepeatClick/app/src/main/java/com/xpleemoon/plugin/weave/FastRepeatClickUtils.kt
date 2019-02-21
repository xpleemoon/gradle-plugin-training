package com.xpleemoon.plugin.weave

import android.os.SystemClock
import android.support.v4.util.LruCache
import android.view.View

private val lastClickLru = LruCache<String, Long>(8)

@Suppress("LocalVariableName")
fun isPreventFastRepeatClick(v: View, clickIntervalTimeMs: Long): Boolean {
    val lastClick_K = v.toString()
    val lastCLick_V = lastClickLru.get(lastClick_K)
    val elapsedRealtime = SystemClock.elapsedRealtime()
    if (lastCLick_V != null && elapsedRealtime - lastCLick_V < clickIntervalTimeMs) {
        return true
    }
    lastClickLru.put(lastClick_K, elapsedRealtime)
    return false
}
