package com.xpleemoon.plugin.sample.preventfastrepeatclick

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        val currentTimes = "当前时间：${SystemClock.elapsedRealtime()}"
        show_time_tv.text = currentTimes
        Log.i("info_out", currentTimes)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fast_click_btn.setOnClickListener(this)
    }
}
