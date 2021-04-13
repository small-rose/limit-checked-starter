package cn.xiaocai.limit.core.v2.annotation;


import cn.xiaocai.limit.core.v2.auto.LimitCheckedAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**
 * @author Xiaocai.Zhang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(LimitCheckedAutoConfiguration.class)
@Documented
@Inherited
public @interface EnableLimitChecked {
}
