package com.xpleemoon.plugin.sample.preventfastrepeatclick;

import android.view.View;
import android.widget.Toast;
import com.xpleemoon.plugin.click.annotation.PreventFastRepeatClick;

/**
 * 测试在java文件中，字节码注入是否正常
 *
 * @author xpleemoon
 */
public class MyOnClickListener implements View.OnClickListener {
//    @PreventFastRepeatClick(intervalTimeMs = 250L)
    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "123", Toast.LENGTH_SHORT).show();
    }
}
