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

    @PreventFastRepeatClick(intervalTimeMs = 600L)
    override fun onClick(v: View?) {
        last_time_tv.text = "上次点击时间：$lastClickTimes"
        lastClickTimes = SystemClock.elapsedRealtime()
        current_time_tv.text = "当前点击时间：$lastClickTimes"
        Log.i("info_out", "${last_time_tv.text}，${current_time_tv.text}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fast_click_btn.setOnClickListener(this)
    }
}
