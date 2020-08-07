package com.xpleemoon.plugin.click.utils

import android.os.SystemClock
import android.view.View

object FastRepeatClickUtils {
    private val lastClickLru = LruCache<String, Long>(16)

    @JvmStatic
    fun resizeCache(maxSize: Int) {
        lastClickLru.resize(maxSize)
    }

    @JvmStatic
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
}
