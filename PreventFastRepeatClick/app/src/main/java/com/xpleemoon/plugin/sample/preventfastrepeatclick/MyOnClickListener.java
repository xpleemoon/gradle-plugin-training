package com.xpleemoon.plugin.sample.preventfastrepeatclick;

import android.view.View;
import android.widget.Toast;

/**
 * @author xpleemoon
 */
public class MyOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "123", Toast.LENGTH_SHORT).show();
    }
}
