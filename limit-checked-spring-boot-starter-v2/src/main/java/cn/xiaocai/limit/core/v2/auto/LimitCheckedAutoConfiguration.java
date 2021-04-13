package cn.xiaocai.limit.core.v2.auto;


import cn.xiaocai.limit.core.v2.aop.LimitCheckedAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
    private  LimitCheckedProperties properties;

    @Bean
    @ConditionalOnClass( LimitCheckedProperties.class)
    @ConditionalOnProperty(prefix =  LimitCheckedProperties.LIMIT_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
    public  LimitCheckedProperties limitCheckedProperties(){
        log.info("Init limitCheckedProperties ... second = {}, totalCount = {} ", properties.getSecond(), properties.getTotalCount());
        return new  LimitCheckedProperties(properties.getSecond(), properties.getTotalCount());
    }

    @Bean
    @ConditionalOnProperty(prefix =  LimitCheckedProperties.LIMIT_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = false)
    public LimitCheckedAspect limitCheckedAspect(){
        log.info("Init limitCheckedAspect by properties ... ");
        return new LimitCheckedAspect(limitCheckedProperties());
    }

}
