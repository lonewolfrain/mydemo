package com.example.plugincore;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static PluginManager pluginManager = new PluginManager();
    //插件的类加载器
    private DexClassLoader dexClassLoader;
    //宿主APP的上下文
    private Context context;
    //插件的资源对象
    private Resources resources;

    private PluginManager() {
    }

    public static PluginManager getInstance() {
        return pluginManager;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    //SD卡中加载 通过一个路径去加载
    public void loadPluginApk(String path) {
        Log.w("loadPluginApk", "loadPluginApk:" + path);
        //获取私有路径
        File plugin = context.getDir("plugin", Context.MODE_PRIVATE);
        //创建了一个类加载器 并且这个类加载器能加载到插件中的类
        dexClassLoader = new DexClassLoader(path, plugin.getAbsolutePath(), null, context.getClassLoader());
        try {
            //先创建一个AssetManager
            AssetManager assetManager = AssetManager.class.newInstance();
            //获取到里面一个用来设置管理资源路径的方法
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            //执行这个方法
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager, path);
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public Resources getResources() {
        return resources;
    }
}
