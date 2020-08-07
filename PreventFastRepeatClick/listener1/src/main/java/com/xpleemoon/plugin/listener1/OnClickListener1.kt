package com.xpleemoon.plugin.listener1

import android.util.Log
import android.view.View

class OnClickListener1 : View.OnClickListener {
    override fun onClick(v: View?) {
        Log.d("info_out", "module: listener1")
    }
}