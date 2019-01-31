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
package org.erola.spring.boot.netty.httprouter.util;

import org.erola.common.util.StringUtil;
import org.erola.spring.boot.netty.httprouter.router.PatternInfo;
import java.util.List;

/**
 *
 * @description: 路径匹配工具类
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-20
 *
 */
public class PathUtil {
    /**
     * 匹配多个字符
     */
    public static final char MULTI_WILDCARD = '*';
    /**
     * 匹配单个自字符
     */
    public static final char SINGLE_WILDCARD = '?';
    /**
     * 多路径通配符
     */
    public static final String MULTI_DIR_WILDCARD = "**";
    /**
     * 路径分隔符
     */
    public static final String DEFAULT_PATH_SEPARATOR = "/";

    /**
     * 判断是否包含通配符匹配
     * @param pattern
     * @return
     */
    public static boolean checkWildcardMatch(String pattern){
        if(StringUtil.isNullOrEmpty(pattern))
            return false;
        else
            return pattern.indexOf(MULTI_WILDCARD) >= 0 || pattern.indexOf(SINGLE_WILDCARD) >= 0;
    }

    /**
     * 判断是否包含多路径匹配
     * @param pattern
     * @return
     */
    public static boolean checkMultiDirWildcardMatch(String pattern){
        if(StringUtil.isNullOrEmpty(pattern))
            return false;
        else
            return pattern.indexOf(MULTI_DIR_WILDCARD) >= 0;
    }

    /**
     * 采用动态规划算法对单路径进行通配符匹配
     * @param dir
     * @param pattern
     * @return
     */
    private static boolean dirWildcardMatch(String dir, String pattern) {
        if (dir == null && pattern == null)
            return true;
        else if (dir == null || pattern == null)
            return false;
        else {
            int dirLength = dir.length();
            int patternLength = pattern.length();

            boolean[][] matchSignArray = new boolean[patternLength + 1][dirLength + 1];
            /**当 dir 和 pattern 都为空的情况*/
            matchSignArray[0][0] = true;
            /**在 pattern 为空情况下 dir 每个字符的匹配结果*/
            for (int dirIndex = 0; dirIndex < dirLength; dirIndex++) {
                matchSignArray[0][dirIndex + 1] = false;
            }
            /**在 dir 为空情况下 pattern 每个字符的匹配结果*/
            for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                matchSignArray[patternIndex + 1][0] = pattern.charAt(patternIndex) == '*' && matchSignArray[patternIndex][0];
            }

            for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                boolean isMultiWildcard = pattern.charAt(patternIndex) == MULTI_WILDCARD;
                boolean isSingleWildcard = pattern.charAt(patternIndex) == SINGLE_WILDCARD;
                for (int dirIndex = 0; dirIndex < dirLength; dirIndex++) {
                    if (isMultiWildcard) {
                        /**
                        * 如果 pattern 该位置的字符是 *
                        * 1、matchSignArray[patternIndex][dirIndex+1] 对应 * 匹配空字符串的情况
                        * 2、matchSignArray[patternIndex+1][dirIndex] 对应 * 匹配 1 个或者 多个字符的情况
                        */
                        matchSignArray[patternIndex + 1][dirIndex + 1] = matchSignArray[patternIndex + 1][dirIndex]
                                || matchSignArray[patternIndex][dirIndex + 1];
                    } else {
                        /**
                         * 如果 pattern 该位置的字符不是 *
                         * matchSignArray[patternIndex][dirIndex] 匹配 且 当前位置的字符相等 或者 为 ?
                         */
                        matchSignArray[patternIndex + 1][dirIndex + 1] = matchSignArray[patternIndex][dirIndex] &&
                                (isSingleWildcard || pattern.charAt(patternIndex) == dir.charAt(dirIndex));
                    }
                }
            }
            return matchSignArray[patternLength][dirLength];
        }
    }

    /**
     * 对路径进行匹配
     * @param path
     * @param patternInfo
     * @return
     */
    public static PatternInfo pathWildcardMatch(String path, PatternInfo patternInfo){
        PatternInfo ret = null;
        if(path!= null && patternInfo!=null){
            List<String> pathDirList = StringUtil.tokenize(path, DEFAULT_PATH_SEPARATOR, true);
            List<String> patternDirList = patternInfo.getPatternDirList();
            int pathDirListSize = pathDirList.size();
            int patternDirListSize = patternDirList.size();

            if(!patternInfo.getWildcardMatch()){
                /**如果不是通配符匹配*/
                if(pathDirListSize == patternDirListSize){
                    for (int index=0; index<pathDirListSize; index++){
                        if(!pathDirList.get(index).equals(patternDirList.get(index))){
                            return ret;
                        }
                    }
                    ret = patternInfo.setEntirelyMatch();
                }
            }else{
                /**如果包含通配符匹配*/
                if(!patternInfo.getMultiDirWildcardMatch()){
                    /**如果不包含多路径匹配*/
                    if(pathDirListSize == patternDirListSize){
                        for (int index=0; index<pathDirListSize; index++){
                            if(!dirWildcardMatch(pathDirList.get(index), patternDirList.get(index))){
                                return ret;
                            }
                        }
                        ret = patternInfo;
                    }
                }else{
                    /**
                     * 如果包含多路径匹配
                     * 则把每个路径看成一个整体，同样采用动态规划算法进行多路径匹配
                     */
                    boolean[][] matchSignArray = new boolean[patternDirListSize + 1][pathDirListSize + 1];
                    matchSignArray[0][0] = true;
                    for (int pathIndex = 0; pathIndex < pathDirListSize; pathIndex++) {
                        matchSignArray[0][pathIndex + 1] = false;
                    }
                    for (int patternIndex = 0; patternIndex < patternDirListSize; patternIndex++) {
                        matchSignArray[patternIndex + 1][0] = patternDirList.get(patternIndex).equals(MULTI_DIR_WILDCARD) && matchSignArray[patternIndex][0];
                    }
                    for (int patternIndex = 0; patternIndex < patternDirListSize; patternIndex++) {
                        boolean isMultiDirWildcard = patternDirList.get(patternIndex).equals(MULTI_DIR_WILDCARD);
                        for (int pathIndex = 0; pathIndex < pathDirListSize; pathIndex++) {
                            if (isMultiDirWildcard) {
                                matchSignArray[patternIndex + 1][pathIndex + 1] = matchSignArray[patternIndex + 1][pathIndex]
                                        || matchSignArray[patternIndex][pathIndex + 1];
                            } else {
                                matchSignArray[patternIndex + 1][pathIndex + 1] = matchSignArray[patternIndex][pathIndex] &&
                                        dirWildcardMatch(pathDirList.get(pathIndex), patternDirList.get(patternIndex));
                            }
                        }
                    }
                    if(matchSignArray[patternDirListSize][pathDirListSize]){
                        ret = patternInfo;
                    }
                }
                if(ret != null){
                    ret.calculateWildcardMatch(path);
                }
            }
        }
        return ret;
    }
}