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

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.erola.common.localcache.LRUMemoryCache;
import org.erola.common.util.CollectionUtil;
import org.erola.common.util.StringUtil;
import org.erola.spring.boot.netty.httprouter.annotation.*;
import org.erola.spring.boot.netty.httprouter.interceptor.IInterceptor;
import org.erola.spring.boot.netty.httprouter.util.PathUtil;
import org.springframework.core.annotation.AnnotationUtils;
import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * @description: 映射关系存储
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public class RouterTable {
    /**
     * 是否启用路由缓存
     */
    private boolean usePathCache = true;
    /**
     * 路径匹配是否区分大小写
     */
    private boolean caseSensitive = false;
    /**
     * 路由缓存
     */
    private LRUMemoryCache pathCache = null;
    /**
     * 用于保存请求类型和对应的路由映射关系的字典
     * 由于只是在初始化的时候生成字典信息，之后只是查询，没有线程安全问题
     */
    private Map<String, List<PatternInfo>> methodPatternInfoListMap = new HashMap<>();

    /**
     * 根据设置处理大小写问题
     * @param param
     * @return
     */
    private String dealSensitive(String param){
        if(StringUtil.isNullOrEmpty(param))
            return param;
        else
            return caseSensitive ? param.toLowerCase() : param;
    }

    /**
     * 路由表构造函数
     */
    public RouterTable(boolean caseSensitive, boolean usePathCache, int pathCacheMaxCount){
        this.usePathCache = usePathCache;
        this.caseSensitive = caseSensitive;
        if(this.usePathCache){
            pathCache = new LRUMemoryCache(pathCacheMaxCount);
        }
    }

    /**
     * 添加单个拦截器
     * @param interceptorList
     * @param interceptor
     */
    private void add(List<Interceptor> interceptorList, Interceptor interceptor){
        if(interceptor != null){
            if(IInterceptor.class.isAssignableFrom(interceptor.value())) {
                interceptorList.add(interceptor);
            }
        }
    }

    /**
     * 添加多个拦截器
     * @param interceptorList
     * @param multiInterceptor
     */
    private void add(List<Interceptor> interceptorList, MultiInterceptor multiInterceptor){
        if(multiInterceptor != null){
            Arrays.stream(multiInterceptor.value()).forEach(interceptor->{
                if(IInterceptor.class.isAssignableFrom(interceptor.value())) {
                    interceptorList.add(interceptor);
                }
            });
        }
    }

    /**
     * 根据请求匹配对应的路由映射
     * @param request
     * @return
     */
    public PatternInfo getPatternInfo(FullHttpRequest request){
        PatternInfo ret = null;
        String uri = request.uri();
        String path = dealSensitive(new QueryStringDecoder(uri).path());
        if(usePathCache){
            ret = pathCache.get(path, PatternInfo.class);
        }
        if(ret == null){
            String requestMethodName = request.method().name().toUpperCase();
            List<PatternInfo> routeResolverList = methodPatternInfoListMap.get(requestMethodName);
            if(CollectionUtil.isNotNullOrEmpty(routeResolverList)){
                /**
                 * 如果多个匹配结果 '通配符匹配的字符数' 不相等，则优先选择 '通配符匹配的字符数' 少的
                 * 如果多个匹配结果 '通配符匹配的字符数' 相等，则比较 '通配符中 * 字符的数量' 中的数量, 优先选择 '通配符中 * 字符的数量' 少的，用于匹配类似问题：
                 *    /abc/d?
                 *    /abc/d*
                 *    路径 /abc/de 应该优先匹配 /abc/d?
                 */
                for (PatternInfo item :routeResolverList) {
                    PatternInfo matchResult = PathUtil.pathWildcardMatch(path, item);
                    if(matchResult!=null && matchResult.getMatchWithWildcardLength()>=0){
                        if(ret==null || ret.getMatchWithWildcardLength()<matchResult.getMatchWithWildcardLength() ||
                                (ret.getMatchWithWildcardLength()==matchResult.getMatchWithWildcardLength() &&
                                        ret.getMultiWildcardLength()>matchResult.getMultiWildcardLength())){
                            ret = matchResult;
                        }
                    }
                }
                if(usePathCache && ret!=null){
                    pathCache.set(path, ret);
                }
            }
        }
        return ret;
    }

    /**
     * 注册系统中的路由配置
     * @param controllerClass
     */
    public void register(Class<?> controllerClass){
        Controller controllerAnnotation  = AnnotationUtils.findAnnotation(controllerClass, Controller.class);
        if(controllerAnnotation != null) {
            //region 查找controller上定义的拦截器
            Class interceptorController = controllerClass;
            List<Interceptor> controllerInterceptorList = new ArrayList<>();
            while (interceptorController != null && interceptorController != Object.class){
                MultiInterceptor controllerMultiInterceptor = AnnotationUtils.findAnnotation(interceptorController, MultiInterceptor.class);
                if(controllerMultiInterceptor != null){
                    add(controllerInterceptorList, controllerMultiInterceptor);
                }else{
                    add(controllerInterceptorList, AnnotationUtils.findAnnotation(interceptorController, Interceptor.class));
                }
                interceptorController = interceptorController.getSuperclass();
            }
            controllerInterceptorList.sort(Comparator.comparingInt(Interceptor::order));
            //endregion
            String[] controllerPathArray = (controllerAnnotation.value() == null || controllerAnnotation.value().length == 0) ? new String[]{""} : controllerAnnotation.value();
            Method[] actionArray = controllerClass.getDeclaredMethods();
            Arrays.stream(actionArray).forEach(action -> {
                RequestMapping actionAnnotation = AnnotationUtils.findAnnotation(action, RequestMapping.class);
                if (actionAnnotation != null) {
                    String[] actionPathArray = actionAnnotation.value();
                    RequestMethod[] requestMethodArray = actionAnnotation.method();
                    if (actionPathArray != null && actionPathArray.length > 0 && requestMethodArray != null && requestMethodArray.length > 0) {
                        //region 查找action上定义的拦截器
                        List<Interceptor> actionInterceptorList = new ArrayList<>();
                        MultiInterceptor actionMultiInterceptor = AnnotationUtils.findAnnotation(action, MultiInterceptor.class);
                        if(actionMultiInterceptor != null){
                            add(actionInterceptorList, actionMultiInterceptor);
                        }else{
                            add(actionInterceptorList, AnnotationUtils.findAnnotation(action, Interceptor.class));
                        }
                        actionInterceptorList.sort(Comparator.comparingInt(Interceptor::order));
                        actionInterceptorList.addAll(0, controllerInterceptorList);
                        //endregion
                        Arrays.stream(controllerPathArray).forEach(controllerPath -> {
                            Arrays.stream(actionPathArray).forEach(actionPath -> {
                                Arrays.stream(requestMethodArray).forEach(requestMethod -> {
                                    String requestMethodName = requestMethod.name().toUpperCase();
                                    List<PatternInfo> patternInfoList = methodPatternInfoListMap.get(requestMethodName);
                                    if(patternInfoList == null){
                                        patternInfoList = new ArrayList<>();
                                        methodPatternInfoListMap.put(requestMethodName, patternInfoList);
                                    }
                                    patternInfoList.add(new PatternInfo(dealSensitive(controllerPath+actionPath), action, controllerClass, actionInterceptorList));
                                });
                            });
                        });
                    }
                }
            });
        }
    }
}