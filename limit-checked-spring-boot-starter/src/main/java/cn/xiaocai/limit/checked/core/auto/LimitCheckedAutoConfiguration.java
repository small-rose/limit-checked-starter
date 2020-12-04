package cn.xiaocai.limit.checked.core.auto;


import cn.xiaocai.limit.checked.core.annotation.EnableLimitChecked;
import cn.xiaocai.limit.checked.core.annotation.LimitChecked;
import cn.xiaocai.limit.checked.core.aop.LimitCheckedAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Xiaocai.Zhang
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(LimitCheckedProperties.class)
public class LimitCheckedAutoConfiguration {

    @Resource
    private LimitCheckedProperties properties;

    @Bean
    @ConditionalOnClass(LimitCheckedProperties.class)
    @ConditionalOnProperty(prefix = LimitCheckedProperties.LIMIT_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
    public LimitCheckedProperties limitCheckedProperties(){
        log.info("Init limitCheckedProperties ... second = {}, totalCount = {} ", properties.getSecond(), properties.getTotalCount());
        return new LimitCheckedProperties(properties.getSecond(), properties.getTotalCount());
    }

    @Bean
    @ConditionalOnProperty(prefix = LimitCheckedProperties.LIMIT_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = false)
    public LimitCheckedAspect limitCheckedAspect(){
        log.info("Init limitCheckedAspect by properties ... ");
        return new LimitCheckedAspect(limitCheckedProperties());
    }

}
