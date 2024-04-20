package com.example.mydemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<VB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {
    private lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout())

        // 可选：添加数据观察者以响应数据变化
        binding.lifecycleOwner = this
        initView(binding, savedInstanceState)
        initImmersionBar()
    }

    abstract fun layout(): Int

    abstract fun getViewModel() : VM

    abstract fun initView(binding : VB,savedInstanceState: Bundle?)

    open fun initImmersionBar() {
//        if (findViewById<View?>(R.id.title_bar) != null) {
//            ImmersionBar.with(this).titleBar(R.id.title_bar).init()
//        }
    }
}