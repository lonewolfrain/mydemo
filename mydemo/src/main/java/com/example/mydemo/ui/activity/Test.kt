package com.example.mydemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mydemo.R
import com.example.mydemo.databinding.ActivityTestBinding
import java.io.InputStream

class Test : AppCompatActivity() {
    private lateinit var mBinding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var `is` : InputStream? = null
        try{
            `is` = assets.open("demo.png")
            mBinding.bigImageView.setImageBitmap(`is`)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}