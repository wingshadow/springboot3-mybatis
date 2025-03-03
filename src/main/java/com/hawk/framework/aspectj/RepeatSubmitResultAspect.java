package com.hawk.framework.aspectj;

import cn.dev33.satoken.SaManager;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hawk.framework.annotation.common.RepeatSubmit;
import com.hawk.framework.common.constant.CacheConstants;
import com.hawk.framework.web.resp.R;
import com.hawk.utils.JsonUtils;
import com.hawk.utils.MessageUtils;
import com.hawk.utils.ServletUtils;
import com.hawk.utils.StringUtils;
import com.hawk.utils.redis.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;

/**
 * 防止重复提交(参考美团GTIS防重系统)
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
//@Component
public class RepeatSubmitResultAspect {

    @Pointcut("@annotation(com.ruoyi.common.annotation.RepeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);
        // 如果注解不为0 则使用注解数值
        long interval = 0;
        if (repeatSubmit.interval() > 0) {
            interval = repeatSubmit.timeUnit().toMillis(repeatSubmit.interval());
        }
        if (interval < 1000) {
            interval = 1000L;
        }
        HttpServletRequest request = ServletUtils.getRequest();
        String nowParams = argsArrayToString(joinPoint.getArgs());

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();

        // 唯一值（没有消息头则使用请求地址）
        String submitKey = StringUtils.trimToEmpty(request.getHeader(SaManager.getConfig().getTokenName()));

        submitKey = SecureUtil.md5(submitKey + ":" + nowParams);
        // 唯一标识（指定key + url + 消息头）
        String cacheRepeatKey = CacheConstants.REPEAT_SUBMIT_KEY + url + submitKey;
        String key = RedisUtils.getCacheObject(cacheRepeatKey);
        Object object;
        if (key == null) {
            RedisUtils.setCacheObject(cacheRepeatKey, "", Duration.ofMillis(interval));
            object = joinPoint.proceed();
            RedisUtils.setCacheObject(cacheRepeatKey, JSONUtil.toJsonStr(object), Duration.ofMillis(interval));
            return object;
        }
        if (StrUtil.isNotBlank(key)) {
            return JSONUtil.parseObj(key);
        }
        long num = 0;
        do {
            ThreadUtil.safeSleep(250L);
            key = RedisUtils.getCacheObject(cacheRepeatKey);
            if (StrUtil.isNotBlank(key)) {
                return JSONUtil.parseObj(key);
            }
            num += 100;
        } while (num <= interval && StrUtil.isBlank(key));
        String message = repeatSubmit.message();
        if (StringUtils.startsWith(message, "{") && StringUtils.endsWith(message, "}")) {
            message = MessageUtils.message(StringUtils.substring(message, 1, message.length() - 1));
        }
        return R.ok(message, new JSONObject());
    }


    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        params.append(JsonUtils.toJsonString(o)).append(" ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
            || o instanceof BindingResult;
    }

}
