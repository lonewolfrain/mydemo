package com.example.plugincore;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public interface PluginInterface {
    /**
     * 注入上下文
     */
    void attach(Activity activity);
    /**
     * 模拟生命周期
     */
    void onCreate(Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();

    void onSaveInstanceState(Bundle outState);
    boolean onTouchEvent(MotionEvent event);
    void onBackPressed();
}
