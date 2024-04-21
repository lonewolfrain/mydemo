package com.example.mydemo.util

import android.content.Context
import android.util.Log
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.File
import java.lang.reflect.Array

class FixManagerUtil {
    companion object {
        // 装载所有补丁的容器
        private var dexs = HashSet<File>()

        init {
            dexs.clear()
        }

        fun loadDex(context: Context) {
            // 获取到所有补丁的路径
            val odex = context.getDir("codex", Context.MODE_PRIVATE)
            // 获取到这个目录下面所有的文件
            val files = odex.listFiles()
            // 校验是否是补丁
            // 判断这个文件是不是以.dex结尾
            for (file in files!!) if (file.name.endsWith(".dex")) {
                // 加入到集合中
                dexs.add(file)
            }

            // 创建放dex优化后的文件的路径
            val optFileStr = "${odex.absolutePath}${File.separator}opt_dex"
            val optFile = File(optFileStr)
            if (!optFile.exists()) {
                optFile.mkdirs()
            }
            for (dex in dexs) {
                // 第一步 获取到当前应用的类加载器中的dexElements
                try {
                    // 获取到当前应用的类加载器
                    val pathClassLoader = context.classLoader as PathClassLoader
                    // BaseDexClassLoader的class
                    val aClass = Class.forName("dalvik.system.BaseDexClassLoader")
                    // 获取到pathList
                    val pathListField = aClass.getDeclaredField("pathList")
                    pathListField.isAccessible = true
                    // 执行pathListField对象的get方法
                    val pathList = pathListField.get(pathClassLoader)
                    // 获取到dexElements的反射对象
                    val dexElementsField = pathList!!.javaClass.getDeclaredField("dexElements")
                    dexElementsField.isAccessible = true
                    // 获取到应用的类加载器中的dexElements变量的值
                    val dexElements = dexElementsField.get(pathList)

                    // 第二步 获取到补丁包的类加载器中的dexElements
                    val dexClassLoader = DexClassLoader(
                        dex.absolutePath,
                        optFile.absolutePath,
                        null,
                        context.classLoader
                    )
                    // 获取到补丁的类加载器中的pathList
                    val dexPathList= pathListField.get(dexClassLoader)
                    // 获取到补丁的类加载器中的dexElements
                    val dexDexElements = dexElementsField.get(dexPathList)
                    // 创建新数组 进行数组合并
                    val length =Array.getLength(dexElements)
                    Log.w("newLength","length:${length}")
                    val dexLength = Array.getLength(dexDexElements)
                    Log.w("newLength","dexLength:${dexLength}")
                    var newLength = length + dexLength
                    Log.w("newLength","newLength:${newLength}")
                    val componentType = dexDexElements.javaClass.componentType
                    val newArray = Array.newInstance(componentType, newLength)
                    // 数据填充
                    for (i in 0 until newLength) {
                        if (i < dexLength) {
                            // 填充补丁的dexDexElements
                            Array.set(newArray, i, Array.get(dexDexElements, i))
                        } else {
                            // 填充当前应用的dexElements
                            Array.set(newArray, i, Array.get(dexElements, i - dexLength))
                        }
                    }
                    // 将新数组赋值给pathList中的dexElements
                    dexElementsField.set(pathList, newArray)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}