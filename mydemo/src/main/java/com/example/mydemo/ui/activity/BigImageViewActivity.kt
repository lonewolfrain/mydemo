package com.example.mydemo.ui.activity

import android.os.Bundle
import com.example.mydemo.BaseActivity
import com.example.mydemo.R
import com.example.mydemo.databinding.ActivityBigImageViewBinding
import com.example.mydemo.viewmodel.MainViewModel
import java.io.InputStream

class BigImageViewActivity : BaseActivity<ActivityBigImageViewBinding, MainViewModel>() {
    private lateinit var bvBinding: ActivityBigImageViewBinding

    override fun layout(): Int = R.layout.activity_big_image_view

    override fun getViewModel(): MainViewModel {
        return MainViewModel()
    }

    override fun initView(binding: ActivityBigImageViewBinding, savedInstanceState: Bundle?) {
        bvBinding = binding
        var `is` : InputStream? = null
        try{
            `is` = assets.open("demo.png")
            bvBinding.bigImageView.setImageBitmap(`is`)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
}