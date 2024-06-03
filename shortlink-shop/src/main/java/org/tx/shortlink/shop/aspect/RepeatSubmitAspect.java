package org.tx.shortlink.shop.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.tx.shortlink.shop.annotation.RepeatSubmit;
import org.tx.shortlink.shop.common.constant.RedisKeyConstant;

import java.util.Objects;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RepeatSubmitAspect {

    private final StringRedisTemplate stringRedisTemplate;
    @Pointcut("@annotation(repeatSubmit)")
    public void pointCutRepeatSubmit(RepeatSubmit repeatSubmit){

    }

    @Around(value = "pointCutRepeatSubmit(repeatSubmit)", argNames = "joinPoint,repeatSubmit")
    public Object around(ProceedingJoinPoint joinPoint,RepeatSubmit repeatSubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        boolean res = false;
        String type = repeatSubmit.limitType().name();
        if(type.equalsIgnoreCase(RepeatSubmit.Type.PARAM.name())){
            //参数形式防重提交
        }else{
            //令牌形式防重提交
            String token = request.getHeader("token");
            if(StringUtil.isBlank(token)){
                throw new RuntimeException("提交订单token为空");
            }
            String key = String.format(RedisKeyConstant.SUBMIT_ORDER_TOKEN_KEY,request.getHeader("userId"),token);

            res = Boolean.TRUE.equals(stringRedisTemplate.delete(key));
        }
        if (!res){
            throw new RuntimeException("重复提交订单");
        }
        return joinPoint.proceed();
    }
}
