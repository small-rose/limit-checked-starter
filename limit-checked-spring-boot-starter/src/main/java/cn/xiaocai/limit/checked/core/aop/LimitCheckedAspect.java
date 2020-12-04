package cn.xiaocai.limit.checked.core.aop;


import cn.xiaocai.limit.checked.core.annotation.LimitChecked;
import cn.xiaocai.limit.checked.core.auto.LimitCheckedProperties;
import cn.xiaocai.limit.checked.core.cache.MyCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;


/**
 * @author Xiaocai.Zhang
 */
@Slf4j
@Aspect
public class LimitCheckedAspect {

    private final String DEFAULT_NAME = "default";
    private final String TIME_KEY = "limitTime";
    private final String TOTAL_KEY = "limitTotal";
    private final String WAIT_KEY = "limitWait";
    private static final HashMap<String, MyCacheManager> CACHE_LIMIT = new HashMap<>();

    /**
     * global properties fro all method
     */
    private LimitCheckedProperties properties ;

    public LimitCheckedAspect(){}

    public LimitCheckedAspect(LimitCheckedProperties limitCheckedProperties){
        this.properties = limitCheckedProperties;
    }

    /**
     * Order 代表优先级，数字越小优先级越高
     */
    @Order(100)
    @Pointcut("@annotation(cn.xiaocai.limit.checked.core.annotation.LimitChecked)")
    public void checkedPoint(){};

    @Before(value = "checkedPoint()")
    public void checkBefore(){
        // log.info("--checked before ");
    }

    @After(value = "checkedPoint()")
    public void checkAfter(){
        // log.info("--checked After ");

    }

    @Around(value = "checkedPoint() && @annotation(limitChecked)")
    public Object checkAround(ProceedingJoinPoint joinPoint, LimitChecked limitChecked){
        Object commonResult = null;
        // 获取方法名称
        String methodName = joinPoint.getSignature().getName();
        // 注解拦截处理
        String nameKey = limitChecked.name();
        nameKey = DEFAULT_NAME.equals(nameKey) ?  methodName : nameKey ;
        long sec = limitChecked.second();
        // 注解验证无限制直接放行
        if (limitChecked.limit()){
            MyCacheManager cacheManager = CACHE_LIMIT.get(nameKey) ;
            long total = limitChecked.totalCount();
            long wait = limitChecked.limitWaiting();

            if (sec==0 || total==0){
                sec = properties.getSecond();
                total = properties.getTotalCount();
                if ( log.isDebugEnabled()){
                    log.debug("Use global properties value !");
                }
            }
            if ( log.isDebugEnabled()) {
                log.debug("@LimitChecked params list: methodName = {} , total = {},  sec = {},  wait = {}", nameKey, total, sec, wait);
            }

            if ( null == cacheManager){
                cacheManager = new MyCacheManager(sec);
                CACHE_LIMIT.put(nameKey, cacheManager);
                log.info("Init cacheManager for api [ {} ], and the period is {} sec", nameKey, sec);
            }
            if (limitChecked.waitEnable() && null != CACHE_LIMIT.get(nameKey).get(WAIT_KEY)){
                log.info("Get wait value : {}", CACHE_LIMIT.get(nameKey).get(WAIT_KEY));
                return "the [ "+nameKey+" ] API limit, please call it after wait "+wait+" sec" ;
            }
            long expiryTime = sec * 1000 ;
            if (null == CACHE_LIMIT.get(nameKey).get(TIME_KEY)){
                LocalDateTime newLimit = LocalDateTime.now().plus(sec, ChronoUnit.SECONDS);
                CACHE_LIMIT.get(nameKey).put(TIME_KEY, newLimit, expiryTime);
                CACHE_LIMIT.get(nameKey).put(TOTAL_KEY, total, expiryTime);
                if ( log.isDebugEnabled()) {
                    log.debug("put value -- key : {} , value {} ", TIME_KEY, newLimit);
                    log.debug("put value -- key : {} , value {} ", TOTAL_KEY, total);
                }
            }

            LocalDateTime limitTime = (LocalDateTime) CACHE_LIMIT.get(nameKey).get(TIME_KEY);
            long newTotal = (long)CACHE_LIMIT.get(nameKey).get(TOTAL_KEY);

            log.info("Total value of the limit number of visits left  is  {}  times .", newTotal);

            long waitTime = wait * 1000 ;
            // 其实该种情况理论上不会出现，因为 limitTime 在 sec 之后就会过期，如果限制时间过期，其实不会比拿出比现在小的时间
            if (limitTime.isBefore(LocalDateTime.now()) ){
                Object nothing = limitChecked.waitEnable() ? CACHE_LIMIT.get(nameKey).put(WAIT_KEY, wait, waitTime) : null;
                return "The [ "+nameKey+" ] API limit, The time window has reached the limit time of expiry time" ;
            }
            if ( newTotal <= 0 ){
                if (limitChecked.waitEnable()){
                    CACHE_LIMIT.get(nameKey).put(WAIT_KEY, wait, waitTime);
                    log.debug("wait {} ", wait);
                }
                return "The [ "+nameKey+" ] API limit, The time window has reached the limit number of times ["+total+"]" ;
            }
            if (!limitChecked.onlySuccessCount()){
                newTotal = newTotal - 1 ;
                CACHE_LIMIT.get(nameKey).put(TOTAL_KEY, newTotal, expiryTime);
                log.debug("put new total value -- key : {} , value {}", TOTAL_KEY, newTotal);
            }
        }
        // 继续执行请求方法
        try {
            commonResult = joinPoint.proceed();
        }catch (Exception e) {
            return "api occurred exception ...";
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 注解限制生效前提下，开启接口调用成功才计数
        if (limitChecked.limit() && limitChecked.onlySuccessCount()){
            long newTotal = (long)CACHE_LIMIT.get(nameKey).get(TOTAL_KEY);
            newTotal = newTotal - 1 ;
            CACHE_LIMIT.get(nameKey).put(TOTAL_KEY, newTotal, sec * 1000);
            log.info("--put new total value -- key : {} , value {}", TOTAL_KEY, newTotal);
        }

        return  commonResult == null ? "API Error ": commonResult ;
    }

    @AfterReturning(value = "checkedPoint()", returning = "result")
    public void checkAfterReturn(Object result){
        // log.info("--checked after return   ---");

    }


    @AfterThrowing(value = "checkedPoint()", throwing="throwable")
    public void afterThrow(Throwable throwable){
        // log.info("--checked  afterThrow ");
    }

}
