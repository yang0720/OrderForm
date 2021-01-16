package com.alpamayo.utils.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Administrator on 2016/10/17 0017.
 */

class SBUtils private constructor(private val mSnackbar: Snackbar) {

    private fun getSnackBarLayout(snackbar: Snackbar?): View? {
        if (snackbar != null) {
            return snackbar.view
        }
        return null

    }


    private fun setSnackBarBackColor(colorId: Int): Snackbar {
        val snackBarView = getSnackBarLayout(mSnackbar)
        snackBarView?.setBackgroundColor(colorId)
        return mSnackbar
    }

    fun info() {
        setSnackBarBackColor(color_info)
        show()
    }

    fun info(actionText: String, listener: View.OnClickListener) {
        setSnackBarBackColor(color_info)
        show(actionText, listener)
    }

    fun warning() {
        setSnackBarBackColor(color_warning)
        show()
    }

    fun warning(actionText: String, listener: View.OnClickListener) {
        setSnackBarBackColor(color_warning)
        show(actionText, listener)
    }

    fun danger() {
        setSnackBarBackColor(color_danger)
        show()
    }

    fun danger(actionText: String, listener:View.OnClickListener) {
        setSnackBarBackColor(color_danger)
        show(actionText, listener)
    }

    fun confirm() {
        setSnackBarBackColor(color_success)
        show()
    }

    fun confirm(actionText: String, listener: View.OnClickListener) {
        setSnackBarBackColor(color_success)
        show(actionText, listener)
    }

    fun show() {
        mSnackbar.show()
    }

    fun show(actionText: String, listener: View.OnClickListener) {
        //mSnackbar.setActionTextColor(action_color);
        mSnackbar.setActionTextColor(Color.WHITE)
        mSnackbar.setAction(actionText, listener).show()
    }

    companion object {
        private val color_danger = 0xfff93535.toInt()
        private val color_success = 0xff3c763d.toInt()
        private val color_info = 0xff31708f.toInt()
        private val color_warning = 0xff8a6d3b.toInt()

        private val action_color = 0xffCDC5BF.toInt()

        fun makeShort(view: View, text: String): SBUtils {
            val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
            return SBUtils(snackbar)
        }

        fun makeLong(view: View, text: String): SBUtils {
            val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            return SBUtils(snackbar)
        }
    }
}
