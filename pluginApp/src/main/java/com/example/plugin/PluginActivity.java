package com.example.plugin;

import android.os.Bundle;
import android.util.Log;

import com.example.plugincore.BasePluginActivity;

public class PluginActivity extends BasePluginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
    }
}