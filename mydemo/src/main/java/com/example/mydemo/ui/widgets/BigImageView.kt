package com.example.mydemo.ui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import com.example.mydemo.R
import java.io.InputStream

/**
 * @author: zzx
 * @date: 2024/4/18
 * desc:分段显示大图,内存复用,避免OOM
 */
class BigImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), GestureDetector.OnGestureListener, View.OnTouchListener{
    private var mScale: Float? = 0f
    private var mBitmap: Bitmap? = null
    private var mViewHeight: Int = 0
    private var mViewWidth: Int = 0
    private var mDecoder: BitmapRegionDecoder? = null
    private var mImageHeight: Int = 0
    private var mImageWidth: Int = 0
    private var mScroller: Scroller
    private var mGestureDetector: GestureDetector
    private var mOptions: BitmapFactory.Options
    private var mRect: Rect? = null

    init {
        // 第一步 设置bugView需要的矩形区域成员变量
        mRect = Rect()
        mOptions = BitmapFactory.Options()
        // 手势支持
        mGestureDetector = GestureDetector(context, this)
        // 滚动类
        mScroller = Scroller(context)
        setOnTouchListener(this)
    }

    // 第二步 设置图片
    fun setImageBitmap(`is` : InputStream) {
        // 获取图片宽高
        mOptions.inJustDecodeBounds = true
//        BitmapFactory.decodeResource(resources, R.mipmap.demo, mOptions)
        BitmapFactory.decodeStream(`is`, null, mOptions)
        mImageWidth = mOptions.outWidth
        mImageHeight = mOptions.outHeight

        // 开启内存复用
        mOptions.inMutable = true
        // 设置图片格式 rgb565
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565
        
        mOptions.inJustDecodeBounds = false

        // 区域解码器
        try {
            mDecoder = BitmapRegionDecoder.newInstance(`is`, false)
        }catch (e: Exception){
            e.printStackTrace()
        }
        requestLayout()
    }

    // 第三步 测量图片
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewWidth = measuredWidth
        mViewHeight = measuredHeight

        // 确定图片加载区域
        mRect?.top = 0
        mRect?.left = 0
        mRect?.right = mImageWidth
        mRect?.bottom = mViewHeight
        Log.w("onMeasure","onMeasurewidthMeasureSpec:${widthMeasureSpec},heightMeasureSpec:$heightMeasureSpec")
        Log.w("onMeasure","onMeasuremViewWidth:${mViewWidth},mViewHeight:$mViewHeight")
        Log.w("onMeasure","onMeasuremmImageWidth:${mImageWidth},mViewHeight:$mImageHeight")
    }
    // 第四步 画图
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mDecoder?.let {
            // 内存复用
            mOptions.inBitmap = mBitmap
            mBitmap = mDecoder!!.decodeRegion(mRect!!, mOptions)

            // 计算缩放因子
            mScale = mViewWidth.toFloat() / mImageWidth.toFloat()
            // 得到矩阵进行缩放
            val matrix = Matrix()
            matrix.setScale(mScale!!, mScale!!)
            canvas?.drawBitmap(mBitmap!!, matrix, null)
        }
    }
    // 第五步 处理点击事件
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?.let {
            return mGestureDetector.onTouchEvent(event)
        }
        return false
    }
    // 第六步 处理手势按下事件
    override fun onDown(e: MotionEvent): Boolean {
        // 如果滑动没有停止,强制停止滑动
        if (!mScroller.isFinished) {
            mScroller.forceFinished(true)
        }
        //接收后续事件
        return true
    }
    // 第七步 处理手势滑动事件
    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        // 上下滚动时,直接改变Rect的显示区域
        mRect?.offset(0, distanceY.toInt())
        // 判断到顶的情况
        if (mRect?.top!! < 0) {
            mRect?.top = 0
            mRect?.bottom = mViewHeight
        }
        // 判断到底的情况
        if (mRect?.bottom!! > mImageHeight) {
            mRect?.top = mImageHeight - mViewHeight
            mRect?.bottom = mImageHeight
        }
        invalidate()
        return false
    }

    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

}