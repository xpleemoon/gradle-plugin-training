package com.xpleemoon.plugin.sample.preventfastrepeatclick

import android.util.Log
import android.view.View
import com.xpleemoon.plugin.click.annotation.PreventFastRepeatClick

class MyFirstOnClickListeners : View.OnClickListener {
    override fun onClick(v: View?) {
        Log.d("info_out", "123456")
    }
}

class MySecondOnClickListeners : View.OnClickListener {
    @PreventFastRepeatClick(isExclude = true)
    override fun onClick(v: View?) {
        Log.d("info_out", "123456")
    }
}

class MyThirdOnClickListeners : View.OnClickListener {
    @PreventFastRepeatClick(intervalTimeMs = 0L)
    override fun onClick(v: View?) {
        Log.d("info_out", "123456")
    }
}

class MyFourthOnClickListeners : View.OnClickListener {
    @PreventFastRepeatClick(isExclude = true, intervalTimeMs = 250L)
    override fun onClick(v: View?) {
        Log.d("info_out", "123456")
    }
}