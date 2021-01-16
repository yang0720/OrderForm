package com.alpamayo.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.alpamayo.utils.R

import java.util.ArrayList
/**
 * Created by Administrator on 2017/7/8.
 */
class WaveView: View {

    // 初始波纹半径
    private var mMiniRadius = 30f
    //最大波纹半径
    private var mMaxRadius: Float = 0.toFloat()
    //波纹持续时间
    private var mWaveDuration: Long = 5000
    //波纹创建时间间隔
    private var mSpeed: Long = 800
    //波纹画笔
    private val mWavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    //中间图标画笔
    private val mCenterBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    //中间图标区域
    private val mCenterBitmapArea = Rect()
    //波纹颜色
    private var mWaveColor = 0x2B5F86//
    //波纹动画效果
    private var mInterpolator: Interpolator = AccelerateInterpolator()
    //所有的水波纹
    private val mAnimatorList = ArrayList<ValueAnimator>()
    //是否开启水波纹
    private var mIsRuning: Boolean = false
    //是否点击了中间图标
    private val mIsCenterClick = false
    //中间的图标
    lateinit var mCenterBitmap: Bitmap
    //中间的圆形图标
    private var mCenterCircleBitmap: Bitmap? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet,defStyleAttr: Int): super(context,attrs,defStyleAttr){}
    init {
        mWavePaint.strokeWidth = 1f
        mWavePaint.color = mWaveColor
        mWavePaint.isDither = true
        mWavePaint.style = Paint.Style.FILL

        mCenterBitmap = BitmapFactory.decodeResource(resources, R.drawable.wave_view_image)
        mMiniRadius = (Math.min(mCenterBitmap.width, mCenterBitmap.height) / 2).toFloat()
    }

    var mWaveRunable: Runnable = Runnable {
        run {
            if (mIsRuning){
                newWaveAnimator()
                invalidate()
                postDelayed(mWaveRunable, mSpeed)
            }
        }
    }

    //开启水波纹
    fun start() {
        if (!mIsRuning) {
            mIsRuning = true
            mWaveRunable.run()
        }
    }

    //是否开启水波纹
    fun isStart(): Boolean {
        return mIsRuning
    }

    //关闭水波纹
    fun stop() {
        mIsRuning = false
    }

    //设置水波纹颜色
    fun setColor(color: Int) {
        mWaveColor = color
    }

    //设置水波纹效果
    fun setInterpolator(interpolator: Interpolator) {
        mInterpolator = interpolator
    }

    //设计水波纹持续时间
    fun setDuration(duration: Long) {
        mWaveDuration = duration
    }

    //设置水波纹间隔时间
    fun setSpeed(speed: Long) {
        mSpeed = speed
    }

    //初始波纹半径
    fun getMiniRadius(): Float {
        return mMiniRadius
    }

    private fun newWaveAnimator(): ValueAnimator {
        val mWaveAnimator = ValueAnimator()
        mWaveAnimator.setFloatValues(mMiniRadius, mMaxRadius)
        mWaveAnimator.duration = mWaveDuration
        mWaveAnimator.repeatCount = 0
        mWaveAnimator.interpolator = mInterpolator
        mWaveAnimator.addUpdateListener {
            //                (Float) animation.getAnimatedValue();
        }
        mAnimatorList.add(mWaveAnimator)
        mWaveAnimator.start()
        return mWaveAnimator
    }



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mMaxRadius = (Math.min(w, h) / 2).toFloat()
        //计算中间图标区域
        mCenterBitmapArea.set((w - mCenterBitmap.width) / 2, (h - mCenterBitmap.height) / 2, (w + mCenterBitmap.width) / 2, (h + mCenterBitmap.height) / 2)
    }

    override fun onDraw(canvas: Canvas) {
        val iterator = mAnimatorList.iterator()
        while (iterator.hasNext()) {
            val valueAnimator = iterator.next()
            //            Log.e("AnimatedValue",(float)valueAnimator.getAnimatedValue() + "mMaxRadius:" + mMaxRadius);
            if (valueAnimator.animatedValue != mMaxRadius) {
                //设置透明度
                mWavePaint.alpha = getAlpha(valueAnimator.animatedValue as Float)
                //画水波纹
                canvas.drawCircle((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), valueAnimator.animatedValue as Float, mWavePaint)
            } else {
                valueAnimator.cancel()
                iterator.remove()
            }
        }

        //绘制中间图标
//        drawCenterBitmap(canvas)
        if (mAnimatorList.size > 0) {
            postInvalidateDelayed(10)
        }
    }

    //绘制中间图标
//    private fun drawCenterBitmap(canvas: Canvas) {
//        if (mCenterCircleBitmap == null) {
//            mCenterCircleBitmap = createCircleImage(mCenterBitmap, mCenterBitmap.width)
//        }
//        canvas.drawBitmap(mCenterCircleBitmap, null, mCenterBitmapArea, mCenterBitmapPaint)
//    }

    //根据原图和边长绘制圆形图片
    private fun createCircleImage(source: Bitmap, min: Int): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        val target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888)
        //产生一个同样大小的画布
        val canvas = Canvas(target)
        //首先绘制圆形
        canvas.drawCircle((min / 2).toFloat(), (min / 2).toFloat(), (min / 2).toFloat(), paint)
        //使用SRC_IN
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        //绘制图片
        canvas.drawBitmap(source, 0f, 0f, paint)
        return target
    }

    //获取水波纹透明度
    private fun getAlpha(mRadius: Float): Int {
        var alpha = 1
        if (mMaxRadius > 0) {
            alpha = ((1 - (mRadius - mMiniRadius) / (mMaxRadius - mMiniRadius)) * 255).toInt()
        }
        //        Log.e("alpha",alpha + "");
        return alpha
    }

}
