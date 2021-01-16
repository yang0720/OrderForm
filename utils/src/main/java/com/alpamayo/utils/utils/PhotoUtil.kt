package com.alpamayo.utils.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Date


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment

object PhotoUtil {
    /**
     * 根据路径删除图片

     * @param path
     */
    fun deleteTempFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }

    /**
     * 压缩制定的图片后返回新的图片
     * @param path
     * *
     * @param tmpPath
     * *
     * @return
     */
    fun scal(path: String, tmpPath: String): String {
        var outputFile = File(path)
        val fileSize = outputFile.length()
        val fileMaxSize = (120 * 1024).toLong()
        if (fileSize >= fileMaxSize) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)
            val height = options.outHeight
            val width = options.outWidth

            val scale = Math.sqrt((fileSize.toFloat() / fileMaxSize).toDouble())
            options.outHeight = (height / scale).toInt()
            options.outWidth = (width / scale).toInt()
            options.inSampleSize = (scale + 0.5).toInt()
            options.inJustDecodeBounds = false

            val bitmap = BitmapFactory.decodeFile(path, options)
            outputFile = File(PhotoUtil.createImageFile(tmpPath).path)
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(outputFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos)
                fos.close()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            Log.e("压缩成功，图片大小：k " + outputFile.length())
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            } else {
                val tempFile = outputFile
                outputFile = File(PhotoUtil.createImageFile(tmpPath).path)
                PhotoUtil.copyFileUsingFileChannels(tempFile, outputFile)
            }

        }
        return outputFile.name

    }

    fun createImageFile(path: String): Uri {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(Date())
        val imageFileName = "JPEG" + timeStamp
        val storageDir = File(path)  //新文件地址
        var image: File? = null
        try {
            image = File.createTempFile(
                    imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    storageDir      /* directory */
            )
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image)
    }

    fun copyFileUsingFileChannels(source: File, dest: File) {
        var inputChannel: FileChannel? = null
        var outputChannel: FileChannel? = null
        try {
            try {
                inputChannel = FileInputStream(source).channel
                outputChannel = FileOutputStream(dest).channel
                outputChannel!!.transferFrom(inputChannel, 0, inputChannel!!.size())
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        } finally {
            try {
                inputChannel!!.close()
                outputChannel!!.close()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
    }

    fun getBitmapToFile(source: String): Bitmap {
        val options = BitmapFactory.Options()
        options.inSampleSize = 2
        val bm = BitmapFactory.decodeFile(source, options)
        return bm
    }
}
