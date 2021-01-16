package com.alpamayo.utils.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * String Utils
 * @author ����ľ
 */
class StringUtils private constructor() {

    init {
        throw AssertionError()
    }

    companion object {

        /**
         * is null or its length is 0 or it is made by space

         * <pre>
         * isBlank(null) = true;
         * isBlank(&quot;&quot;) = true;
         * isBlank(&quot;  &quot;) = true;
         * isBlank(&quot;a&quot;) = false;
         * isBlank(&quot;a &quot;) = false;
         * isBlank(&quot; a&quot;) = false;
         * isBlank(&quot;a b&quot;) = false;
        </pre> *

         * @param str
         * *
         * @return if string is null or its size is 0 or it is made by space, return true, else return false.
         */
        fun isBlank(str: String?): Boolean {
            return str == null || str.trim { it <= ' ' }.length == 0
        }

        /**
         * is null or its length is 0

         * <pre>
         * isEmpty(null) = true;
         * isEmpty(&quot;&quot;) = true;
         * isEmpty(&quot;  &quot;) = false;
        </pre> *

         * @param str
         * *
         * @return if string is null or its size is 0, return true, else return false.
         */
        fun isEmpty(str: CharSequence?): Boolean {
            return str == null || str.length == 0
        }

        /**
         * compare two string

         * @param actual
         * *
         * @param expected
         * *
         * @return
         * *
         * @see ObjectUtils.isEquals
         */
        fun isEquals(actual: String?, expected: String?): Boolean {
            return actual === expected || if (actual == null) expected == null else actual == expected
        }

        /**
         * get length of CharSequence

         * <pre>
         * length(null) = 0;
         * length(\"\") = 0;
         * length(\"abc\") = 3;
        </pre> *

         * @param str
         * *
         * @return if str is null or empty, return 0, else return [CharSequence.length].
         */
        fun length(str: CharSequence?): Int {
            return str?.length ?: 0
        }

        /**
         * null Object to empty string

         * <pre>
         * nullStrToEmpty(null) = &quot;&quot;;
         * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
         * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
        </pre> *

         * @param str
         * *
         * @return
         */
        fun nullStrToEmpty(str: Any?): String {
            return if (str == null) "" else str as? String ?: str.toString()
        }

        /**
         * capitalize first letter

         * <pre>
         * capitalizeFirstLetter(null)     =   null;
         * capitalizeFirstLetter("")       =   "";
         * capitalizeFirstLetter("2ab")    =   "2ab"
         * capitalizeFirstLetter("a")      =   "A"
         * capitalizeFirstLetter("ab")     =   "Ab"
         * capitalizeFirstLetter("Abc")    =   "Abc"
        </pre> *

         * @param str
         * *
         * @return
         */
        fun capitalizeFirstLetter(str: String): String {
            if (isEmpty(str)) {
                return str
            }

            val c = str[0]
            return if (!Character.isLetter(c) || Character.isUpperCase(c))
                str
            else
                StringBuilder(str.length)
                        .append(Character.toUpperCase(c)).append(str.substring(1)).toString()
        }

        /**
         * encoded in utf-8

         * <pre>
         * utf8Encode(null)        =   null
         * utf8Encode("")          =   "";
         * utf8Encode("aa")        =   "aa";
         * utf8Encode("��������")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
        </pre> *

         * @param str
         * *
         * @return
         * *
         * @throws UnsupportedEncodingException if an error occurs
         */
        fun utf8Encode(str: String): String {
            if (!isEmpty(str) && str.toByteArray().size != str.length) {
                try {
                    return URLEncoder.encode(str, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    throw RuntimeException("UnsupportedEncodingException occurred. ", e)
                }

            }
            return str
        }

        /**
         * encoded in utf-8, if exception, return defultReturn

         * @param str
         * *
         * @param defultReturn
         * *
         * @return
         */
        fun utf8Encode(str: String, defultReturn: String): String {
            if (!isEmpty(str) && str.toByteArray().size != str.length) {
                try {
                    return URLEncoder.encode(str, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    return defultReturn
                }

            }
            return str
        }

        /**
         * get innerHtml from href

         * <pre>
         * getHrefInnerHtml(null)                                  = ""
         * getHrefInnerHtml("")                                    = ""
         * getHrefInnerHtml("mp3")                                 = "mp3";
         * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
         * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
         * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
         * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
         * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
         * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
         * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
         * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
         * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
        </pre> *

         * @param href
         * *
         * @return
         * *          * if href is null, return ""
         * *          * if not match regx, return source
         * *          * return the last string that match regx
         * *
         */
        fun getHrefInnerHtml(href: String): String {
            if (isEmpty(href)) {
                return ""
            }

            val hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*"
            val hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE)
            val hrefMatcher = hrefPattern.matcher(href)
            if (hrefMatcher.matches()) {
                return hrefMatcher.group(1)
            }
            return href
        }

        /**
         * process special char in html

         * <pre>
         * htmlEscapeCharsToString(null) = null;
         * htmlEscapeCharsToString("") = "";
         * htmlEscapeCharsToString("mp3") = "mp3";
         * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
         * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
         * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
         * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
         * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
        </pre> *

         * @param source
         * *
         * @return
         */
        fun htmlEscapeCharsToString(source: String): String {
            return if (StringUtils.isEmpty(source))
                source
            else
                source.replace("&lt;".toRegex(), "<").replace("&gt;".toRegex(), ">")
                        .replace("&amp;".toRegex(), "&").replace("&quot;".toRegex(), "\"")
        }

        /**
         * transform half width char to full width char

         * <pre>
         * fullWidthToHalfWidth(null) = null;
         * fullWidthToHalfWidth("") = "";
         * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
         * fullWidthToHalfWidth("�������磥��) = "!\"#$%&";
        </pre> *

         * @param s
         * *
         * @return
         */
        fun fullWidthToHalfWidth(s: String): String {
            if (isEmpty(s)) {
                return s
            }

            val source = s.toCharArray()
            for (i in source.indices) {
                if (source[i].toInt() == 12288) {
                    source[i] = ' '
                    // } else if (source[i] == 12290) {
                    // source[i] = '.';
                } else if (source[i].toInt() >= 65281 && source[i].toInt() <= 65374) {
                    source[i] = (source[i].toInt() - 65248).toChar()
                } else {
                    source[i] = source[i]
                }
            }
            return String(source)
        }

        /**
         * transform full width char to half width char

         * <pre>
         * halfWidthToFullWidth(null) = null;
         * halfWidthToFullWidth("") = "";
         * halfWidthToFullWidth(" ") = new String(new char[] {12288});
         * halfWidthToFullWidth("!\"#$%&) = "�������磥��";
        </pre> *

         * @param s
         * *
         * @return
         */
        fun halfWidthToFullWidth(s: String): String {
            if (isEmpty(s)) {
                return s
            }

            val source = s.toCharArray()
            for (i in source.indices) {
                if (source[i] == ' ') {
                    source[i] = 12288.toChar()
                    // } else if (source[i] == '.') {
                    // source[i] = (char)12290;
                } else if (source[i].toInt() >= 33 && source[i].toInt() <= 126) {
                    source[i] = (source[i].toInt() + 65248).toChar()
                } else {
                    source[i] = source[i]
                }
            }
            return String(source)
        }
    }
}