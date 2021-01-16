package com.alpamayo.utils.utils

import java.io.File

import android.os.Environment
import android.os.StatFs

/**
 * SD����صĸ�����

 * @author ����ľ
 */
class SDCardUtils private constructor() {
    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        /**
         * �ж�SDCard�Ƿ����

         * @return
         */
        val isSDCardEnable: Boolean
            get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

        /**
         * ��ȡSD��·��

         * @return
         */
        val sdCardPath: String
            get() = Environment.getExternalStorageDirectory().absolutePath + File.separator

        /**
         * ��ȡSD����ʣ������ ��λbyte

         * @return
         */
        // ��ȡ���е����ݿ������
        // ��ȡ�������ݿ�Ĵ�С��byte��
        val sdCardAllSize: Long
            get() {
                if (isSDCardEnable) {
                    val stat = StatFs(sdCardPath)
                    val availableBlocks = stat.availableBlocks.toLong() - 4
                    val freeBlocks = stat.availableBlocks.toLong()
                    return freeBlocks * availableBlocks
                }
                return 0
            }

        /**
         * ��ȡָ��·�����ڿռ��ʣ����������ֽ�������λbyte

         * @param filePath
         * *
         * @return �����ֽ� SDCard���ÿռ䣬�ڲ��洢���ÿռ�
         */
        fun getFreeBytes(filePath: String): Long {
            var filePath = filePath
            // �����sd�����µ�·�������ȡsd����������
            if (filePath.startsWith(sdCardPath)) {
                filePath = sdCardPath
            } else {// ������ڲ��洢��·�������ȡ�ڴ�洢�Ŀ�������
                filePath = Environment.getDataDirectory().absolutePath
            }
            val stat = StatFs(filePath)
            val availableBlocks = stat.availableBlocks.toLong() - 4
            return stat.blockSize * availableBlocks
        }

        /**
         * ��ȡϵͳ�洢·��

         * @return
         */
        val rootDirectoryPath: String
            get() = Environment.getRootDirectory().absolutePath
    }


} 