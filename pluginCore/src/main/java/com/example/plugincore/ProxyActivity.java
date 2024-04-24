package com.example.plugincore;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;

//代理Activity,不需要拥有自己的想法 壳
public class ProxyActivity extends AppCompatActivity {
    //实际上就是Activity的对象
    PluginInterface pluginInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取到真实目的地的ActivityName
        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");
        try {
            Class<?> aClass = PluginManager.getInstance().getDexClassLoader().loadClass(activity);
            //实例化插件Activity
            Object activityObject = aClass.newInstance();
            //判断这个类是否是PluginInterface的子类
            if(activityObject instanceof PluginInterface){
                pluginInterface = (PluginInterface) activityObject;
                pluginInterface.attach(this);
                pluginInterface.onCreate(savedInstanceState);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources(){
        return PluginManager.getInstance().getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public void onStart() {
        super.onStart();
        pluginInterface.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        pluginInterface.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        pluginInterface.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        pluginInterface.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pluginInterface.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        pluginInterface.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        pluginInterface.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        pluginInterface.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
