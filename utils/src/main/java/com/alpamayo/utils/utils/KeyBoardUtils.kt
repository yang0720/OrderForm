package com.alpamayo.utils.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * �򿪻�ر������

 * @author ����ľ
 */
object KeyBoardUtils {
    /**
     * �������

     * @param mEditText
     * *            �����
     * *
     * @param mContext
     * *            ������
     */
    fun openKeybord(mEditText: EditText, mContext: Context) {
        val imm = mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * �ر������

     * @param mEditText
     * *            �����
     * *
     * @param mContext
     * *            ������
     */
    fun closeKeybord(mEditText: EditText, mContext: Context) {
        val imm = mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }
}  