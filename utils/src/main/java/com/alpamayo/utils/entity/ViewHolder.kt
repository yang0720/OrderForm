package com.alpamayo.utils.entity


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alpamayo.utils.utils.Alpa

/**
 * Created by Administrator on 2017/6/6.
 */
class ViewHolder(itemView: View, resID: IntArray): RecyclerView.ViewHolder(itemView){
    lateinit var v: View
    lateinit var tag1: View
    lateinit var tag2: View
    lateinit var tag3: View
    lateinit var tag4: View
    lateinit var tag5: View
    lateinit var tag6: View
    lateinit var tag7: View
    lateinit var tag8: View
    lateinit var tag9: View
    lateinit var tag10: View
    lateinit var tag11: View
    lateinit var tag12: View
    lateinit var tag13: View
    lateinit var tag14: View
    init {
        v = itemView
        for (i in resID.indices) {
            when (i) {
                0 -> tag1 = v.findViewById(resID[i])
                1 -> tag2 = v.findViewById(resID[i])
                2 -> tag3 = v.findViewById(resID[i])
                3 -> tag4 = v.findViewById(resID[i])
                4 -> tag5 = v.findViewById(resID[i])
                5 -> tag6 = v.findViewById(resID[i])
                6 -> tag7 = v.findViewById(resID[i])
                7 -> tag8 = v.findViewById(resID[i])
                8 -> tag9 = v.findViewById(resID[i])
                9 -> tag10 = v.findViewById(resID[i])
                10 -> tag11 = v.findViewById(resID[i])
                11 -> tag12 = v.findViewById(resID[i])
                12 -> tag13 = v.findViewById(resID[i])
                13 -> tag14 = v.findViewById(resID[i])
                else -> {
                }
            }
        }
    }
    fun text1() = tag1 as TextView
    fun text2() = tag2 as TextView
    fun text3() = tag3 as TextView
    fun text4() = tag4 as TextView
    fun text5() = tag5 as TextView
    fun text6() = tag6 as TextView
    fun text7() = tag7 as TextView
    fun text8() = tag8 as TextView
    fun text9() = tag9 as TextView
    fun text10() = tag10 as TextView
    fun text11() = tag11 as TextView
    fun text12() = tag12 as TextView
    fun text13() = tag13 as TextView
    fun text14() = tag14 as TextView

    fun img1() = tag1 as ImageView
    fun img2() = tag1 as ImageView
    fun img3() = tag1 as ImageView
}