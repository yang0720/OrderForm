package com.alpamayo.utils.utils

/***
 * �ļ�������
 * @author ����ľ
 */
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.text.TextUtils

/**
 * File Utils
 *
 * Read or write file
 *  * [.readFile] read file
 *  * [.readFileToList] read file to string list
 *  * [.writeFile] write file from String
 *  * [.writeFile] write file from String
 *  * [.writeFile] write file from String List
 *  * [.writeFile] write file from String List
 *  * [.writeFile] write file
 *  * [.writeFile] write file
 *  * [.writeFile] write file
 *  * [.writeFile] write file
 *
 *
 * Operate file
 *  * [.moveFile] or [.moveFile]
 *  * [.copyFile]
 *  * [.getFileExtension]
 *  * [.getFileName]
 *  * [.getFileNameWithoutExtension]
 *  * [.getFileSize]
 *  * [.deleteFile]
 *  * [.isFileExist]
 *  * [.isFolderExist]
 *  * [.makeFolders]
 *  * [.makeDirs]
 *

 * @author [Trinea](http://www.trinea.cn) 2012-5-12
 */
class FileUtils private constructor() {

    init {
        throw AssertionError()
    }

    companion object {
        fun openFile(mContext: Context, path: String): Boolean {
            if (StringUtils.isBlank(path)) {
                return false
            }
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(Uri.fromFile(File(path)), getMIMEType(path))
            mContext.startActivity(intent)
            return true
        }

        fun getMIMEType(filePath: String): String {
            val end = getFileExtension(filePath)
            var type = ""
            if (end == "mp3" || end == "aac" || end == "aac"
                    || end == "amr" || end == "mpeg"
                    || end == "mp4") {
                type = "audio"
            } else if (end == "jpg" || end == "gif"
                    || end == "png" || end == "jpeg") {
                type = "image"
            } else {
                type = "*"
            }
            type += "/*"
            return type
        }

        val FILE_EXTENSION_SEPARATOR = "."

        /**
         * read file

         * @param filePath
         * *
         * @param charsetName The name of a supported [&lt;/code&gt;charset&lt;code&gt;][java.nio.charset.Charset]
         * *
         * @return if file not exist, return null, else return content of file
         * *
         * @throws RuntimeException if an error occurs while operator BufferedReader
         */
        fun readFile(filePath: String): String? {
            var fileContent = ""
            val file = File(filePath)
            if (file == null || !file.isFile) {
                return null
            }

            var reader: BufferedReader? = null
            try {
                val `is` = InputStreamReader(FileInputStream(file))
                reader = BufferedReader(`is`)
                var line: String? = null
                val i = 0
                line = reader.readLine()
                while (line != null) {
                    fileContent += "$line "
                    line = reader.readLine()
                }
                reader.close()
                return fileContent
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            return fileContent
        }

        /**
         * write file

         * @param filePath
         * *
         * @param content
         * *
         * @param append is append, if true, write to the end of file, else clear content of file and write into it
         * *
         * @return return false if content is empty, true otherwise
         * *
         * @throws RuntimeException if an error occurs while operator FileWriter
         */
        @JvmOverloads fun writeFile(filePath: String, content: String, append: Boolean = false): Boolean {
            if (StringUtils.isEmpty(content)) {
                return false
            }

            var fileWriter: FileWriter? = null
            try {
                makeDirs(filePath)
                fileWriter = FileWriter(filePath, append)
                fileWriter.write(content)
                fileWriter.close()
                return true
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close()
                    } catch (e: IOException) {
                        throw RuntimeException("IOException occurred. ", e)
                    }

                }
            }
        }

        /**
         * write file

         * @param filePath
         * *
         * @param contentList
         * *
         * @param append is append, if true, write to the end of file, else clear content of file and write into it
         * *
         * @return return false if contentList is empty, true otherwise
         * *
         * @throws RuntimeException if an error occurs while operator FileWriter
         */
        @JvmOverloads fun writeFile(filePath: String, contentList: List<String>?, append: Boolean = false): Boolean {
            if (contentList == null || contentList.size == 0) {
                return false
            }

            var fileWriter: FileWriter? = null
            try {
                makeDirs(filePath)
                fileWriter = FileWriter(filePath, append)
                var i = 0
                for (line in contentList) {
                    if (i++ > 0) {
                        fileWriter.write("\r\n")
                    }
                    fileWriter.write(line)
                }
                fileWriter.close()
                return true
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close()
                    } catch (e: IOException) {
                        throw RuntimeException("IOException occurred. ", e)
                    }

                }
            }
        }

        /**
         * write file

         * @param file the file to be opened for writing.
         * *
         * @param stream the input stream
         * *
         * @param append if `true`, then bytes will be written to the end of the file rather than the beginning
         * *
         * @return return true
         * *
         * @throws RuntimeException if an error occurs while operator FileOutputStream
         */
        @JvmOverloads fun writeFile(filePath: String?, stream: InputStream, append: Boolean = false): Boolean {
            return writeFile(if (filePath != null) File(filePath) else null, stream, append)
        }

        /**
         * write file

         * @param file the file to be opened for writing.
         * *
         * @param stream the input stream
         * *
         * @param append if `true`, then bytes will be written to the end of the file rather than the beginning
         * *
         * @return return true
         * *
         * @throws RuntimeException if an error occurs while operator FileOutputStream
         */
        @JvmOverloads fun writeFile(file: File?, stream: InputStream, append: Boolean = false): Boolean {
            var o: OutputStream? = null
            try {
                makeDirs(file!!.absolutePath)
                o = FileOutputStream(file, append)
                val data = ByteArray(1024)
                var length = stream.read(data)
                while (length != -1) {
                    o.write(data, 0, length)
                    length = stream.read(data)
                }
                o.flush()
                return true
            } catch (e: FileNotFoundException) {
                throw RuntimeException("FileNotFoundException occurred. ", e)
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            } finally {
                if (o != null) {
                    try {
                        o.close()
                        stream.close()
                    } catch (e: IOException) {
                        throw RuntimeException("IOException occurred. ", e)
                    }

                }
            }
        }

        /**
         * move file

         * @param sourceFilePath
         * *
         * @param destFilePath
         */
        fun moveFile(sourceFilePath: String, destFilePath: String) {
            if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
                throw RuntimeException("Both sourceFilePath and destFilePath cannot be null.")
            }
            moveFile(File(sourceFilePath), File(destFilePath))
        }

        /**
         * move file

         * @param srcFile
         * *
         * @param destFile
         */
        fun moveFile(srcFile: File, destFile: File) {
            val rename = srcFile.renameTo(destFile)
            if (!rename) {
                copyFile(srcFile.absolutePath, destFile.absolutePath)
                deleteFile(srcFile.absolutePath)
            }
        }

        /**
         * copy file

         * @param sourceFilePath
         * *
         * @param destFilePath
         * *
         * @return
         * *
         * @throws RuntimeException if an error occurs while operator FileOutputStream
         */
        fun copyFile(sourceFilePath: String, destFilePath: String): Boolean {
            var inputStream: InputStream? = null
            try {
                inputStream = FileInputStream(sourceFilePath)
            } catch (e: FileNotFoundException) {
                throw RuntimeException("FileNotFoundException occurred. ", e)
            }

            return writeFile(destFilePath, inputStream)
        }

        /**
         * read file to string list, a element of list is a line

         * @param filePath
         * *
         * @param charsetName The name of a supported [&lt;/code&gt;charset&lt;code&gt;][java.nio.charset.Charset]
         * *
         * @return if file not exist, return null, else return content of file
         * *
         * @throws RuntimeException if an error occurs while operator BufferedReader
         */
        fun readFileToList(filePath: String, charsetName: String): List<String>? {
            val file = File(filePath)
            val fileContent = ArrayList<String>()
            if (file == null || !file.isFile) {
                return null
            }

            var reader: BufferedReader? = null
            try {
                val `is` = InputStreamReader(FileInputStream(file), charsetName)
                reader = BufferedReader(`is`)
                var line: String? = reader.readLine()
                while (line != null) {
                    fileContent.add(line)
                    line= reader.readLine()
                }
                reader.close()
                return fileContent
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        throw RuntimeException("IOException occurred. ", e)
                    }

                }
            }
        }

        /**
         * get file name from path, not include suffix

         * <pre>
         * getFileNameWithoutExtension(null)               =   null
         * getFileNameWithoutExtension("")                 =   ""
         * getFileNameWithoutExtension("   ")              =   "   "
         * getFileNameWithoutExtension("abc")              =   "abc"
         * getFileNameWithoutExtension("a.mp3")            =   "a"
         * getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
         * getFileNameWithoutExtension("c:\\")              =   ""
         * getFileNameWithoutExtension("c:\\a")             =   "a"
         * getFileNameWithoutExtension("c:\\a.b")           =   "a"
         * getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
         * getFileNameWithoutExtension("/home/admin")      =   "admin"
         * getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
        </pre> *

         * @param filePath
         * *
         * @return file name from path, not include suffix
         * *
         * @see
         */
        fun getFileNameWithoutExtension(filePath: String): String {
            if (StringUtils.isEmpty(filePath)) {
                return filePath
            }

            val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
            val filePosi = filePath.lastIndexOf(File.separator)
            if (filePosi == -1) {
                return if (extenPosi == -1) filePath else filePath.substring(0, extenPosi)
            }
            if (extenPosi == -1) {
                return filePath.substring(filePosi + 1)
            }
            return if (filePosi < extenPosi) filePath.substring(filePosi + 1, extenPosi) else filePath.substring(filePosi + 1)
        }

        /**
         * get file name from path, include suffix

         * <pre>
         * getFileName(null)               =   null
         * getFileName("")                 =   ""
         * getFileName("   ")              =   "   "
         * getFileName("a.mp3")            =   "a.mp3"
         * getFileName("a.b.rmvb")         =   "a.b.rmvb"
         * getFileName("abc")              =   "abc"
         * getFileName("c:\\")              =   ""
         * getFileName("c:\\a")             =   "a"
         * getFileName("c:\\a.b")           =   "a.b"
         * getFileName("c:a.txt\\a")        =   "a"
         * getFileName("/home/admin")      =   "admin"
         * getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
        </pre> *

         * @param filePath
         * *
         * @return file name from path, include suffix
         */
        fun getFileName(filePath: String): String {
            if (StringUtils.isEmpty(filePath)) {
                return filePath
            }

            val filePosi = filePath.lastIndexOf(File.separator)
            return if (filePosi == -1) filePath else filePath.substring(filePosi + 1)
        }

        /**
         * get folder name from path

         * <pre>
         * getFolderName(null)               =   null
         * getFolderName("")                 =   ""
         * getFolderName("   ")              =   ""
         * getFolderName("a.mp3")            =   ""
         * getFolderName("a.b.rmvb")         =   ""
         * getFolderName("abc")              =   ""
         * getFolderName("c:\\")              =   "c:"
         * getFolderName("c:\\a")             =   "c:"
         * getFolderName("c:\\a.b")           =   "c:"
         * getFolderName("c:a.txt\\a")        =   "c:a.txt"
         * getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
         * getFolderName("/home/admin")      =   "/home"
         * getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
        </pre> *

         * @param filePath
         * *
         * @return
         */
        fun getFolderName(filePath: String): String {

            if (StringUtils.isEmpty(filePath)) {
                return filePath
            }

            val filePosi = filePath.lastIndexOf(File.separator)
            return if (filePosi == -1) "" else filePath.substring(0, filePosi)
        }

        /**
         * get suffix of file from path

         * <pre>
         * getFileExtension(null)               =   ""
         * getFileExtension("")                 =   ""
         * getFileExtension("   ")              =   "   "
         * getFileExtension("a.mp3")            =   "mp3"
         * getFileExtension("a.b.rmvb")         =   "rmvb"
         * getFileExtension("abc")              =   ""
         * getFileExtension("c:\\")              =   ""
         * getFileExtension("c:\\a")             =   ""
         * getFileExtension("c:\\a.b")           =   "b"
         * getFileExtension("c:a.txt\\a")        =   ""
         * getFileExtension("/home/admin")      =   ""
         * getFileExtension("/home/admin/a.txt/b")  =   ""
         * getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
        </pre> *

         * @param filePath
         * *
         * @return
         */
        fun getFileExtension(filePath: String): String {
            if (StringUtils.isBlank(filePath)) {
                return filePath
            }

            val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
            val filePosi = filePath.lastIndexOf(File.separator)
            if (extenPosi == -1) {
                return ""
            }
            return if (filePosi >= extenPosi) "" else filePath.substring(extenPosi + 1)
        }

        /**
         * Creates the directory named by the trailing filename of this file, including the complete directory path required
         * to create this directory. <br></br>
         * <br></br>
         *
         * **Attentions:**
         *  * makeDirs("C:\\Users\\Trinea") can only create users folder
         *  * makeFolder("C:\\Users\\Trinea\\") can create Trinea folder
         *

         * @param filePath
         * *
         * @return true if the necessary directories have been created or the target directory already exists, false one of
         * *         the directories can not be created.
         * *
         * *          * if [FileUtils.getFolderName] return null, return false
         * *          * if target directory already exists, return true
         * *          * return [File.makeFolder]
         * *
         */
        fun makeDirs(filePath: String): Boolean {
            val folderName = getFolderName(filePath)
            if (StringUtils.isEmpty(folderName)) {
                return false
            }

            val folder = File(folderName)
            return if (folder.exists() && folder.isDirectory) true else folder.mkdirs()
        }

        /**
         * @param filePath
         * *
         * @return
         * *
         * @see .makeDirs
         */
        fun makeFolders(filePath: String): Boolean {
            return makeDirs(filePath)
        }

        /**
         * Indicates if this file represents a file on the underlying file system.

         * @param filePath
         * *
         * @return
         */
        fun isFileExist(filePath: String): Boolean {
            if (StringUtils.isBlank(filePath)) {
                return false
            }

            val file = File(filePath)
            return file.exists() && file.isFile
        }

        /**
         * Indicates if this file represents a directory on the underlying file system.

         * @param directoryPath
         * *
         * @return
         */
        fun isFolderExist(directoryPath: String): Boolean {
            if (StringUtils.isBlank(directoryPath)) {
                return false
            }

            val dire = File(directoryPath)
            return dire.exists() && dire.isDirectory
        }

        /**
         * delete file or directory
         *
         *  * if path is null or empty, return true
         *  * if path not exist, return true
         *  * if path exist, delete recursion. return true
         *

         * @param path
         * *
         * @return
         */
        fun deleteFile(path: String): Boolean {
            if (StringUtils.isBlank(path)) {
                return true
            }

            val file = File(path)
            if (!file.exists()) {
                return true
            }
            if (file.isFile) {
                return file.delete()
            }
            if (!file.isDirectory) {
                return false
            }
            for (f in file.listFiles()) {
                if (f.isFile) {
                    f.delete()
                } else if (f.isDirectory) {
                    deleteFile(f.absolutePath)
                }
            }
            return file.delete()
        }

        /**
         * get file size
         *
         *  * if path is null or empty, return -1
         *  * if path exist and it is a file, return file size, else return -1
         *

         * @param path
         * *
         * @return returns the length of this file in bytes. returns -1 if the file does not exist.
         */
        fun getFileSize(path: String): Long {
            if (StringUtils.isBlank(path)) {
                return -1
            }

            val file = File(path)
            return if (file.exists() && file.isFile) file.length() else -1
        }
    }
}
/**
 * write file, the string will be written to the begin of the file

 * @param filePath
 * *
 * @param content
 * *
 * @return
 */
/**
 * write file, the string list will be written to the begin of the file

 * @param filePath
 * *
 * @param contentList
 * *
 * @return
 */
/**
 * write file, the bytes will be written to the begin of the file

 * @param filePath
 * *
 * @param stream
 * *
 * @return
 * *
 * @see {@link .writeFile
 */
/**
 * write file, the bytes will be written to the begin of the file

 * @param file
 * *
 * @param stream
 * *
 * @return
 * *
 * @see {@link .writeFile
 */