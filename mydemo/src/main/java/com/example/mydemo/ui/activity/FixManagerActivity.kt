package com.example.mydemo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.mydemo.BaseActivity
import com.example.mydemo.R
import com.example.mydemo.databinding.ActivityFixManagerBinding
import com.example.mydemo.util.FixManagerUtil
import com.example.mydemo.util.TestUtils
import com.example.mydemo.viewmodel.MainViewModel
import com.example.plugincore.PluginManager
import com.example.plugincore.ProxyActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class FixManagerActivity : BaseActivity<ActivityFixManagerBinding, MainViewModel>() {
    private lateinit var fmBinding: ActivityFixManagerBinding
    override fun layout(): Int = R.layout.activity_fix_manager

    override fun getViewModel(): MainViewModel {
        return MainViewModel()
    }

    override fun initView(binding: ActivityFixManagerBinding, savedInstanceState: Bundle?) {
        fmBinding = binding
        setListener()
    }
    fun setListener(){
        fmBinding.btnToast.setOnClickListener {
            try {
//                val value = TestUtil().divisionCalculate(10, 0)
                val value = TestUtils().divisionCalculate(10, 2)

                Toast.makeText(this, "计算成功,结果为:$value", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, e.message ?: "异常信息为null", Toast.LENGTH_SHORT).show()
            }
        }
        fmBinding.btnFix.setOnClickListener {
            // 首先把这个补丁拷贝到私有目录的data/data/包名/codex下面
            val odex = this.getDir("codex", Context.MODE_PRIVATE)
            // 补丁名字
            val name = "output.dex"
            // 判断这个名字是否重名
            val file = File(odex, name)
            val absolutePath = file.absolutePath
            if (file.exists()) {
                // 先删除这个文件
                file.delete()
            }
            //创建文件输入输出流
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null
            try {
                // 起点
//                inputStream = this.assets.open(name)
                inputStream = FileInputStream(File(Environment.getExternalStorageDirectory(),name))
                // 终点
                outputStream = FileOutputStream(absolutePath)
                var len = 0
                var buffer = ByteArray(1024)
                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                var f = File(absolutePath)
                // 去加载dex文件
                FixManagerUtil.loadDex(application)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream?.close()
                    outputStream?.close()
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
        fmBinding.loadPlugin.setOnClickListener {
            PluginManager.getInstance().setContext(application)
            //初始化类加载器
            PluginManager.getInstance().loadPluginApk("${Environment.getExternalStorageDirectory()}/pluginApp-debug.apk")
            //加载类
            try {
//                val aClass = PluginManager.getInstance().dexClassLoader.loadClass("com.example.plugin.Test")
                val aClass = PluginManager.getInstance().dexClassLoader.loadClass("com.example.plugin.PluginActivity")
                Log.w("loadPlugin","loadPlugin${aClass.name}")
                val intent = Intent(this, ProxyActivity :: class.java)
                intent.putExtra("activity",aClass.name)
                startActivity(intent)
            }catch (e : ClassNotFoundException){
                e.printStackTrace()
            }
        }
    }

}