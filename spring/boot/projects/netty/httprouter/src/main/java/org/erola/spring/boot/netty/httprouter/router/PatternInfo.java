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
package org.erola.spring.boot.netty.httprouter.router;

import org.erola.common.util.CollectionUtil;
import org.erola.common.util.StringUtil;
import org.erola.spring.boot.netty.httprouter.annotation.Interceptor;
import org.erola.spring.boot.netty.httprouter.util.PathUtil;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @description: 路径匹配对象模型
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public class PatternInfo {
    /**
     *
     */
    private String pattern;
    /**
     * 此路由映射的方法
     */
    private Method method;

    /**
     * 此路由映射的对象类型
     */
    private Class<?> controllerClass;
    /**
     *
     */
    private List<String> patternDirList;
    /**
     * 匹配中多字符匹配符的数量
     */
    private int multiWildcardLength = -1;
    /**
     * 通配符匹配的字符串长度
     */
    private int matchWithWildcardLength = -1;
    /**
     * 是否是通配符匹配
     */
    private boolean wildcardMatch = false;
    /**
     * 是否是多路径匹配
     */
    private boolean multiDirWildcardMatch = false;
    /**
     * 此路由上定义的拦截器列表
     */
    private List<Interceptor> interceptorList;

    /**
     *
     * @param pattern
     * @param method
     * @param controllerClass
     * @param interceptorList
     */
    public PatternInfo(String pattern, Method method, Class<?> controllerClass, List<Interceptor> interceptorList){
        this.pattern = pattern;
        this.method = method;
        this.controllerClass = controllerClass;
        this.interceptorList = interceptorList;
        this.wildcardMatch = PathUtil.checkWildcardMatch(pattern);
        this.multiDirWildcardMatch = PathUtil.checkMultiDirWildcardMatch(pattern);
        this.patternDirList = StringUtil.tokenize(pattern, PathUtil.DEFAULT_PATH_SEPARATOR, true);
    }

    public String getPattern() {
        return pattern;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public List<String> getPatternDirList() {
        return patternDirList;
    }

    public List<Interceptor> getInterceptorList() {
        return interceptorList;
    }

    public boolean getWildcardMatch() {
        return wildcardMatch;
    }

    public boolean getMultiDirWildcardMatch() {
        return multiDirWildcardMatch;
    }

    public int getMultiWildcardLength() {
        return multiWildcardLength;
    }

    public int getMatchWithWildcardLength() {
        return matchWithWildcardLength;
    }

    /**
     * 设置全匹配相关信息
     * @return
     */
    public PatternInfo setEntirelyMatch(){
        multiWildcardLength = 0;
        matchWithWildcardLength = 0;
        return this;
    }

    /**
     * 计算通配符匹配相关信息
     * @param path
     * @return
     */
    public PatternInfo calculateWildcardMatch(String path){
        if(CollectionUtil.isNotNullOrEmpty(patternDirList)){
            multiWildcardLength = 0;
            int noWildcardLength = 0;
            for(int x=0; x<patternDirList.size(); x++){
                String patternDir = patternDirList.get(x);
                searchNextChar:
                for(int y=0; y<patternDir.length(); y++){
                    char item = patternDir.charAt(y);
                    if(item == PathUtil.SINGLE_WILDCARD){
                        continue searchNextChar;
                    }else if(item == PathUtil.MULTI_WILDCARD){
                        multiWildcardLength++;
                        continue searchNextChar;
                    }
                    noWildcardLength++;
                }
            }
            int pathLength = StringUtil.isNullOrEmpty(path) ? 0 : path.length();
            matchWithWildcardLength = pathLength - noWildcardLength;
        }
        return this;
    }
}