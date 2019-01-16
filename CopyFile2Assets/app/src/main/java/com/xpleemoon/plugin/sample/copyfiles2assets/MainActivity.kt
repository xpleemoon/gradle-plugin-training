package com.xpleemoon.plugin.sample.copyfiles2assets

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStreamReader
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataTask =
            @SuppressLint("StaticFieldLeak")
            object : AsyncTask<Void, Int, String>() {
                override fun doInBackground(vararg params: Void?): String? {
                    val contentBuilder = StringBuilder()
                    val reader = InputStreamReader(assets.open("data.txt"))
                    reader.readLines().forEach {
                        contentBuilder.append("$it\n")
                    }
                    reader.close()
                    return contentBuilder.toString()
                }

                override fun onPostExecute(result: String) {
                    content_tv.text = result
                }
            }
        dataTask.execute()

        val imgTask =
            @SuppressLint("StaticFieldLeak")
            object : AsyncTask<Void, Int, Bitmap?>() {
                override fun doInBackground(vararg params: Void?): Bitmap? {
                    val imgIS = assets.open("img/普普通通杀猪的.jpg")
                    val imgBitmap = BitmapFactory.decodeStream(imgIS)
                    imgIS.close()
                    return imgBitmap
                }

                override fun onPostExecute(result: Bitmap?) {
                    result ?: return
                    content_img.setImageBitmap(result)
                }
            }
        imgTask.execute()
    }
}
