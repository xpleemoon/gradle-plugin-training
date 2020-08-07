package com.xpleemoon.plugin.sample.preventfastrepeatclick

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import com.xpleemoon.plugin.click.annotation.PreventFastRepeatClick
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var lastClickTimes = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        fast_click_btn.setOnClickListener(this)
//        fast_click_btn.setOnClickListener(OnClickListener2())
        fast_click_btn.setOnClickListener { clickLog("HOHOHO") }
    }

    @PreventFastRepeatClick(intervalTimeMs = 600L)
    override fun onClick(v: View?) {
        clickLog("哈哈哈")
    }

    private fun clickLog(desc: String) {
        // 由于FastRepeatClickUtils内部也有时间的获取，所以这里获取的时间只是近似，跟实际会有误差，但是放心实际的拦截时间是相对精确的
        val currentClickTimes = SystemClock.elapsedRealtime()
        val intervalTimes = currentClickTimes - lastClickTimes
        last_time_tv.text = "上次点击时间：$lastClickTimes"
        current_time_tv.text = "当前点击时间：$currentClickTimes"
        interval_time_tv.text = "两次点击间隔时间：$intervalTimes"
        lastClickTimes = currentClickTimes
        Log.i(
            "info_out",
            "$desc: ${last_time_tv.text}，${current_time_tv.text}，${interval_time_tv.text}"
        )
    }
}
