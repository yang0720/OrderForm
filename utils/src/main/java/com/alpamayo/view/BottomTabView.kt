package com.alpamayo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.alpamayo.utils.R

/**
 * Created by 陈序员 on 2017/4/28.
 * Email: Matthew_Chen_1994@163.com
 * Blog: https://blog.ifmvo.cn
 */

class BottomTabView : LinearLayout {

    /**
     * 记录最新的选择位置
     */
    private var lastPosition = -1

    /**
     * 所有 TabItem 的集合
     */
    private var tabItemViews: List<TabItemView>? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     * 连接 Viewpager
     * @param viewPager　
     */
    fun setUpWithViewPager(viewPager: ViewPager) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                updatePosition(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        setOnTabItemSelectListener(object : OnTabItemSelectListener {
            override fun onTabItemSelect(position: Int) {
                //viewPager.currentItem = position  //滑动动画
                viewPager.setCurrentItem(position,false) //取消滑动动画
            }
        })
    }

    /**
     * 设置 Tab Item View
     */
    @JvmOverloads fun setTabItemViews(tabItemViews: List<TabItemView>?, centerView: View? = null) {

        if (this.tabItemViews != null) {
            throw RuntimeException("不能重复设置！")
        }

        if (tabItemViews == null || tabItemViews.size < 2) {
            throw RuntimeException("TabItemView 的数量必须大于2！")
        }

        this.tabItemViews = tabItemViews
        for (i in tabItemViews.indices) {

            if (centerView != null && i == tabItemViews.size / 2) {
                this.addView(centerView)
            }

            val tabItemView = tabItemViews[i]

            this.addView(tabItemView)

            val finalI = i

            tabItemView.setOnClickListener(View.OnClickListener {
                if (finalI == lastPosition) {
                    // 第二次点击
                    if (onSecondSelectListener != null) {
                        onSecondSelectListener!!.onSecondSelect(finalI)
                    }
                    return@OnClickListener
                }

                updatePosition(finalI)

                if (onTabItemSelectListener != null) {
                    onTabItemSelectListener!!.onTabItemSelect(finalI)
                }
            })
        }

        /**
         * 将所有的 TabItem 设置为 初始化状态
         */
        for (tab in tabItemViews) {
            tab.setStatus(TabItemView.DEFAULT)
        }

        /**
         * 默认状态选择第一个
         */
        updatePosition(0)
    }

    /**
     * 更新被选中 Tab Item 的状态
     * 恢复上一个 Tab Item 的状态
     */
    fun updatePosition(position: Int) {
        if (lastPosition != position) {
            if (tabItemViews != null && tabItemViews!!.size != 0) {
                tabItemViews!![position].setStatus(TabItemView.PRESS)
                if (lastPosition != -1) {
                    tabItemViews!![lastPosition].setStatus(TabItemView.DEFAULT)
                }
                lastPosition = position
            } else {
                throw RuntimeException("please setTabItemViews !")
            }
        }
    }

    internal var onTabItemSelectListener: OnTabItemSelectListener? = null
    internal var onSecondSelectListener: OnSecondSelectListener? = null

    fun setOnTabItemSelectListener(onTabItemSelectListener: OnTabItemSelectListener) {
        this.onTabItemSelectListener = onTabItemSelectListener
    }

    fun setOnSecondSelectListener(onSecondSelectListener: OnSecondSelectListener) {
        this.onSecondSelectListener = onSecondSelectListener
    }

    /**
     * 第二次被选择的监听器
     */
    interface OnSecondSelectListener {
        fun onSecondSelect(position: Int)
    }

    /**
     * 第一次被选择的监听器
     */
    interface OnTabItemSelectListener {
        fun onTabItemSelect(position: Int)
    }

    /**
     * Item
     */
    class TabItemView(context: Context,
                      /**
                       * Item 的标题
                       */
                      var title: String,
                      /**
                       * 标题的两个状态的颜色 选中、未选中
                       */
                      var colorDef: Int, var colorPress: Int,
                      /**
                       * 两个图标的 资源 id ，选中、未选中
                       */
                      var iconResDef: Int, var iconResPress: Int) : LinearLayout(context) {

        lateinit var tvTitle: TextView
        lateinit var ivIcon: ImageView

        init {
            init()
        }

        /**
         * 初始化
         */
        fun init() {
            val view = LayoutInflater.from(super.getContext()).inflate(R.layout.utiles_view_bottomtabview_view_tab_item, this)
            tvTitle = view.findViewById(R.id.tvTitle) as TextView
            ivIcon = view.findViewById(R.id.ivIcon) as ImageView

            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            layoutParams.weight = 1f
            view.setLayoutParams(layoutParams)

            tvTitle.text = title

        }

        /**
         * 设置状态
         */
        fun setStatus(status: Int) {
            tvTitle.setTextColor(ContextCompat.getColor(super.getContext(), if (status == PRESS) colorPress else colorDef))
            ivIcon.setImageResource(if (status == PRESS) iconResPress else iconResDef)
        }

        companion object {

            /**
             * 两个状态 选中、未选中
             */
            val PRESS = 1
            val DEFAULT = 2
        }
    }
}
/**
 * 设置 Tab Item View
 */
