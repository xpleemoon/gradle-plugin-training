package com.xpleemoon.plugin.listener2

import android.util.Log
import android.view.View

class OnClickListener2 : View.OnClickListener {
    override fun onClick(v: View?) {
        Log.d("info_out", "module: listener2")
    }
}