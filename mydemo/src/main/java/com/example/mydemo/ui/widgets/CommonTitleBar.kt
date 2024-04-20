package com.example.mydemo.ui.widgets

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mydemo.R
import com.example.mydemo.util.ConfigUtils

class CommonTitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var tvTitle: TextView? = null
    private var tvLeft: TextView? = null
    private var ivLeft: ImageView? = null
    private var tvRight: TextView? = null
    private var ivRight: ImageView? = null
    private var titleText: String? = null
    private var leftText: String? = null
    private var rightText: String? = null
    private var showBack = false
    private var leftRes = 0
    private var rightRes = 0
    private var titleSize = 0f
    private var leftTextSize = 0f
    private var rightTextSize = 0f
    private var leftTextColor = 0
    private var titleColor = 0
    private var rightTextColor = 0

    private var utils: ConfigUtils? = null

    init {
        init(context, attrs)
    }

    /**
     * 初始化
     */
    fun init(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.title_bar, this)
        utils = ConfigUtils.getInstance(context)
        if (attrs != null) {
            val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar)
            titleText = ta.getString(R.styleable.CommonTitleBar_title)
            leftText = ta.getString(R.styleable.CommonTitleBar_textLeft)
            rightText = ta.getString(R.styleable.CommonTitleBar_textRight)
            leftRes = ta.getResourceId(R.styleable.CommonTitleBar_leftRes, R.mipmap.nav_back)
            rightRes = ta.getResourceId(R.styleable.CommonTitleBar_rightRes, -1)
            showBack = ta.getBoolean(R.styleable.CommonTitleBar_showBack, false)
            leftTextColor = ta.getColor(
                R.styleable.CommonTitleBar_leftTextColor,
                utils!!.getColor(R.color.white)
            )
            titleColor =
                ta.getColor(R.styleable.CommonTitleBar_titleColor, utils!!.getColor(R.color.white))
            rightTextColor = ta.getColor(
                R.styleable.CommonTitleBar_rightTextColor,
                utils!!.getColor(R.color.white)
            )
            titleSize = ta.getDimension(
                R.styleable.CommonTitleBar_titleSize,
                utils!!.sp2px(12).toFloat()
            )
            leftTextSize = ta.getDimension(
                R.styleable.CommonTitleBar_leftTextSize,
                utils!!.sp2px(12).toFloat()
            )
            rightTextSize = ta.getDimension(
                R.styleable.CommonTitleBar_rightTextSize,
                utils!!.sp2px(12).toFloat()
            )
            ta.recycle()
        }
        tvTitle = findViewById<TextView>(R.id.tv_title)
        tvLeft = findViewById<TextView>(R.id.tv_left)
        tvRight = findViewById<TextView>(R.id.tv_right)
        ivLeft = findViewById<ImageView>(R.id.iv_left)
        ivRight = findViewById<ImageView>(R.id.iv_right)
        if (!TextUtils.isEmpty(titleText)) {
            tvTitle!!.setText(titleText)
            tvTitle!!.setTextColor(titleColor)
            tvTitle!!.setTextSize(titleSize)
        }
        if (!TextUtils.isEmpty(leftText)) {
            tvLeft!!.setText(leftText)
            tvLeft!!.setVisibility(VISIBLE)
            tvLeft!!.setTextColor(leftTextColor)
            tvLeft!!.setTextSize(leftTextSize)
        } else {
            tvLeft!!.setVisibility(GONE)
        }
        if (!TextUtils.isEmpty(rightText)) {
            tvRight!!.setText(rightText)
            tvRight!!.setVisibility(VISIBLE)
            tvRight!!.setTextSize(rightTextSize)
            tvRight!!.setTextColor(rightTextColor)
        } else {
            tvRight!!.setVisibility(GONE)
        }
        if (rightRes == -1) {
            ivRight!!.setVisibility(GONE)
        } else {
            ivRight!!.setVisibility(VISIBLE)
            ivRight!!.setImageResource(rightRes)
        }
        if (leftRes == R.mipmap.nav_back && showBack) {
            ivLeft!!.setVisibility(VISIBLE)
            ivLeft!!.setImageResource(leftRes)
        } else {
            ivLeft!!.setVisibility(GONE)
        }
    }

    /**
     *
     * 右侧图片菜单
     *
     * @param rightRes 图片资源id
     * @param runnable 点击事件
     */
    fun setRightRes(rightRes: Int, runnable: Runnable?) {
        ivRight!!.visibility = VISIBLE
        ivRight!!.setImageResource(rightRes)
        ivRight!!.setOnClickListener { v: View? -> runnable?.run() }
    }
    /**
     * 设置返回按钮监听
     */
    fun setBackListener(runnable: Runnable) {
        if (showBack) {
            ivLeft!!.setOnClickListener { v: View? -> runnable.run() }
        }
    }

    fun setRightListener(runnable: Runnable) {
        tvRight!!.setOnClickListener { v: View? -> runnable.run() }
    }

    fun setTitle(title: String) {
        if (TextUtils.isEmpty(title)) {
            return
        }
        titleText = title
        tvTitle!!.text = titleText
    }

    fun setRightTitle(title: String) {
        if (TextUtils.isEmpty(title)) {
            return
        }
        rightText = title
        tvRight!!.text = rightText
    }

    fun setRightVisibility(visibility: Int) {
        tvRight!!.visibility = visibility
    }
}