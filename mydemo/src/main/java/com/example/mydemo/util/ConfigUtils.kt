package com.example.mydemo.util

import android.content.Context

class ConfigUtils {
    private var mContext: Context? = null

    private object SingletonHolder {
        val instance = ConfigUtils()
    }
    companion object {
        fun getInstance(context: Context?): ConfigUtils {
            SingletonHolder.instance.mContext = context
            return SingletonHolder.instance
        }
    }

    /** 获取Color  */
    fun getColor(colorRes: Int): Int {
        return mContext!!.resources.getColor(colorRes)
    }

    fun getDimens(dimenRes: Int): Float {
        return mContext!!.resources.getColor(dimenRes).toFloat()
    }


    /** dip转换px  */
    fun dip2px(dip: Int): Int {
        val scale = mContext!!.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }

    /** dip转换px  */
    fun sp2px(sp: Int): Int {
        val scale = mContext!!.resources.displayMetrics.scaledDensity
        return (sp * scale + 0.5f).toInt()
    }


    /** pxz转换dip  */
    fun px2dip(px: Int): Int {
        val scale = mContext!!.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }


    /** dip转换px  */
    fun dip2px(context: Context, dip: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }

    /** pxz转换dip  */
    fun px2dip(context: Context, px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }
}