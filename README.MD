## limit-checked-spring-boot-starter

easy to limit your api method  limit visit numbers in a period time  .

### quick start

the core annotation is  `@LimitChecked` :

```java
/**
 * @author Xiaocai.Zhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface LimitChecked {
    /**
     * global uniqueness cache key, you can user the signature ,do not repeat
     * 用作缓存key，建议应用内全局唯一
     * @return String
     */
    @NonNull
    String name() default  "default" ;

    /**
     * 方法限制标记，默认开启限制
     * the method limit flag
     * @return boolean
     */
    boolean limit() default true;

    /**
     * 方法限制一个时间窗口期，单位是秒
     * @return long
     */
    long second() default 0;
    /**
     * 方法限制一个时间窗口期内限定次数
     * @return long
     */
    long totalCount() default 0;
    /**
     * 是否开启限定后的等待延迟
     * @return boolean
     */
    boolean waitEnable() default false;
    /**
     * 启限定后的等待延迟时间，仅在 waitEnable = true 的时候生效
     * @return long
     */
    long limitWaiting() default 5;

    /**
     * 是否调用成功才计算次数
     * @return boolean
     */
    boolean onlySuccessCount() default false;
}
```
global properties :

```java
public class LimitCheckedProperties {

    public final static String LIMIT_PREFIX = "limit.checked";
    @Getter
    private boolean enabled = true;
    private long second = 60;
    private long totalCount = 100;
    // ...
}
```

### quick demo

### 1、pom.xml

```xml
       <dependency>
            <groupId>cn.xiaocai.limit</groupId>
            <artifactId>limit-checked-spring-boot-starter</artifactId>
            <version>version</version>
        </dependency>
```

### 2、add config to enabled

```properties
# limit check enabled
limit.checked.enabled=false
limit.checked.second=15
limit.checked.total-count=10

# debug log
logging.level.cn.xiaocai.limit.checked=debug
```
### 3、use annotation on method


```java
package cn.xiaocai.limit.checked.demo.controller;

import cn.xiaocai.limit.checked.core.annotation.LimitChecked;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xiaocai.Zhang
 */
@Slf4j
@RestController
public class DemoController {
    /**
     * use @LimitChecked
     * 访问该接口限制：10秒钟内只能访问2次
     * @return
     */
    @RequestMapping("/test")
    @LimitChecked(name = "test", second = 10, totalCount = 2)
    public String test(){
        return "ok";
    }

    /**
     * use @LimitChecked just limit on call success
     * when exception happened ,the limit numbers not count
     * 访问该接口限制：10秒内，限制成功访问2次
     * @return
     */
    @RequestMapping("/onSuccess")
    @LimitChecked(name = "success", second = 10, totalCount = 2, onlySuccessCount = true)
    public String success(){
        return "ok";
    }

    /**
     * when no params
     * use global application.properties
     * 该接口的限制参数都没有配置，则使用 application.properties 全局配置
     * @return
     */
    @RequestMapping("/test2")
    @LimitChecked(name = "global")
    public String global(){
        return "ok";
    }
}

```
 