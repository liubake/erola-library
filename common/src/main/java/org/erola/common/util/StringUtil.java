/*
 *                               .__
 *      ____   _______    ____   |  |   _____
 *    _/ __ \  \_  __ \  /  _ \  |  |   \__  \
 *    \  ___/   |  | \/ (  <_> ) |  |__  / __ \_
 *     \___  >  |__|     \____/  |____/ (____  /
 *         \/                                \/
 *
 *    - - - - - bake.liu@outlook.com - - - - -
 *
 */
package org.erola.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @description: 字符串操作辅助工具类
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public class StringUtil {
    /**
     * 安全的进行trim
     * @param value
     * @return
     */
    public static String safeTrim(String value){
        return value==null?value: value.trim();
    }

    /**
     * 判断字符串是否为 null 或 空
     * @param value
     * @return
     */
    public static boolean isNullOrEmpty(String value){
        return value==null || value.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为 null 且 不为空
     * @param value
     * @return
     */
    public static boolean isNotNullOrEmpty(String value){
        return !isNullOrEmpty(value);
    }

    /**
     * 按照指定的字符串对源字符串进行切割
     * @param source
     * @param delimiters
     * @param ignoreEmptyTokens
     * @return
     */
    public static List<String> tokenize(String source, String delimiters, boolean ignoreEmptyTokens){
        List<String> tokenList = new ArrayList<>();
        if (source != null){
            StringTokenizer st = new StringTokenizer(source, delimiters);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (!ignoreEmptyTokens || token.length() > 0) {
                    tokenList.add(token);
                }
            }
        }
        return tokenList;
    }

    /**
     * 返回目标字符串在源字符串中的位置【忽略大小写】
     * <br>
     * <pre>
     * StringUtil.indexOfIgnoreCase(null, *, *)          = -1   <br>
     * StringUtil.indexOfIgnoreCase(*, null, *)          = -1   <br>
     * StringUtil.indexOfIgnoreCase("", "", 0)           = 0   <br>
     * StringUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0   <br>
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2   <br>
     * StringUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1   <br>
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5   <br>
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1   <br>
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2   <br>
     * StringUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2   <br>
     * StringUtil.indexOfIgnoreCase("abc", "", 9)        = -1   <br>
     * </pre>
     * <br>
     * @param source 源字符串
     * @param target 目标字符串
     * @return 目标字符串在源字符串中的位置
     */
    public static int indexOfIgnoreCase(String source, String target){
        if(source==null || target==null){
            return -1;
        } else if(target.length() > source.length()){
            return -1;
        } else if(target=="") {
            return 0;
        } else{
            final int endLimit = source.length() - target.length() + 1;
            for (int i = 0; i < endLimit; i++) {
                if (source.regionMatches(true, i, target, 0, target.length())) {
                    return i;
                }
            }
            return -1;
        }
    }
}