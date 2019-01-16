package com.xpleemoon.plugin.sample.copyfiles2assets

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStreamReader
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contentBuilder = StringBuilder()
        val reader = InputStreamReader(assets.open("data.txt"))
        reader.readLines().forEach {
            contentBuilder.append("$it\n")
        }
        reader.close()
        content_tv.text = contentBuilder.toString()
    }
}
