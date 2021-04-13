package cn.xiaocai.limit.core.v3.annotation;


import cn.xiaocai.limit.core.v3.LimitCheckedScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Xiaocai.Zhang
 */
@Target(ElementType.TYPE)
@Retention( RetentionPolicy.RUNTIME)
@Import( LimitCheckedScannerRegistrar.class)
@Documented
@Inherited
public @interface LimitCheckedScans {
    String[] basePackage() default {};

}
