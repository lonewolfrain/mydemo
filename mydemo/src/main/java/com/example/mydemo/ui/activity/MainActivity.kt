package com.example.mydemo.ui.activity
import android.content.Intent
import android.os.Bundle
import com.example.mydemo.BaseActivity
import com.example.mydemo.R
import com.example.mydemo.databinding.ActivityMainBinding
import com.example.mydemo.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private lateinit var acBinding: ActivityMainBinding

    override fun layout(): Int = R.layout.activity_main

    override fun getViewModel(): MainViewModel {

        return MainViewModel()
    }


    override fun initView(binding: ActivityMainBinding,savedInstanceState: Bundle?) {
        acBinding = binding
        acBinding.text.setText("不是text了")
        startActivity(Intent(this,FixManagerActivity::class.java))
    }

}