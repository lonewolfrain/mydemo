package com.example.plugincore;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//代理Activity,不需要拥有自己的想法
public class ProxyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取到真实目的地的ActivityName
        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");
        try {
            Class<?> aClass = PluginManager.getInstance().getDexClassLoader().loadClass(activity);
            Intent intentClass = new Intent(this, aClass);
            startActivity(intentClass);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
